/*
 * Author : Kasania
 * Filename : Teriser
 * Desc :
 */
package com.codrest.teriser_java_connector.core;

import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.core.net.TeriserClient;
import com.codrest.teriser_java_connector.core.net.TeriserServerConnector;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Teriser {

    private String token;
    private MessageReceiver messageReceiver;
    private TeriserServerConnector teriserServerConnector;
    Set<Class<?>> classes = new HashSet<>();
    Map<String, Method> methods = new HashMap<>();
    Map<String, Object> instances = new HashMap<>();
    public TeriserClient socketTest;

    public Teriser(String token, MessageReceiver messageReceiver) {
        this.token = token;
        this.messageReceiver = messageReceiver;
        messageReceiver.setMessageExecutor(this::handleMessage);
        teriserServerConnector = new TeriserServerConnector(this::getMethodInfo, token);
        socketTest = new TeriserClient(this::request, this::getMethodInfo);
    }


    /**
     * Add Module to Teriser.
     * <p>
     * usage : teriser.addModule(Module.class)
     *
     * @param clazz class of Module
     * @param <K>   Type of Module
     * @see Api
     * @see MessageReceiver
     */

    public <K> void addModule(Class<K> clazz) {
        classes.add(clazz);
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            if (declaredConstructor.getParameterCount() == 0) {
                try {

                    @SuppressWarnings("unchecked")
                    K object = (K) declaredConstructor.newInstance();

                    for (Method method : object.getClass().getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Api.class)) {
                            Api named = method.getAnnotation(Api.class);
                            if (named.name().equals("")) {
                                methods.put(method.getName(), method);
                                instances.put(method.getName(), object);
                            } else {
                                methods.put(named.name(), method);
                                instances.put(named.name(), object);
                            }
                        }

                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void run() {

        System.out.println("Teriser client is Running");
        methods.forEach((name, method) -> System.out.println(name + ":" + method));
        socketTest.initConnection(token);
//        teriserServerConnector.connectToProcessServer();
    }

    public String request(String formattedJson) {
        return messageReceiver.onMessageReceived(formattedJson);
    }

    public String handleMessage(String formattedJson) {
        JsonObject jsonObject = JsonParser.parseString(formattedJson).getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        JsonArray parameters = jsonObject.get("parameters").getAsJsonArray();

        Method targetMethod = methods.get(methodName);
        Object[] args = makeArgs(targetMethod, parameters);

        DataPacketBuilder builder = new DataPacketBuilder();

        String msg = "";

        try {
            builder.setData(
                    new String[]{(String) targetMethod.invoke(instances.get(methodName), args)}
            );
        } catch (IllegalAccessException e) {
            builder.setErrorMessage("Not Enough Parameter");
            builder.setResponseCode(ResponseCode.NullValue);
            msg = builder.buildServerMessage(token);
            return msg;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        msg = builder.buildServerOkMessage(token);

        return msg;
    }

    public Object[] makeArgs(Method targetMethod, JsonArray data) {
        Object[] args = new Object[targetMethod.getParameterCount()];

        int i = 0;
        for (Class<?> parameterType : targetMethod.getParameterTypes()) {
            Gson gson = new Gson();
            for (JsonElement parameter : data) {
                JsonObject json = parameter.getAsJsonObject();
                String type = json.keySet().iterator().next();
                if (parameterType.getName().contains("List")){
                    args[i++] = gson.fromJson(json.get(type).getAsString().replace("\\",""), TypeToken.get(parameterType).getType());
                    data.remove(json);
                    break;
                }
                else if (parameterType.getName().endsWith(type)) {
                    args[i++] = gson.fromJson(json.get(type), parameterType);
                    data.remove(json);
                    break;
                }
            }

        }
        return args;
    }

    public Map<String, List<String>> getMethodInfo() {
        Map<String, List<String>> methodMap = new HashMap<>();

        for (String key : methods.keySet()) {
            List<String> parameters = new ArrayList<>();
            Method method = methods.get(key);
            for (Class<?> parameterType : method.getParameterTypes()) {
                String[] tokens = parameterType.getName().split("\\.");
                parameters.add(tokens[tokens.length - 1]);
            }
            methodMap.put(method.getName(), parameters);
        }

        System.out.println(methodMap);

        return methodMap;
    }
}
