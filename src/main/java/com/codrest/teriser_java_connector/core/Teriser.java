/*
 * Author : Kasania
 * Filename : Teriser
 * Desc :
 */
package com.codrest.teriser_java_connector.core;

import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.core.net.TeriserClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Teriser {
    private String token;
    private MessageReceiver messageReceiver;

    Set<Class<?>> classes = new HashSet<>();
    Map<String, Method> methods = new HashMap<>();
    Map<String, Object> instances = new HashMap<>();

    public Teriser(String token, MessageReceiver messageReceiver) {
        this.token = token;
        this.messageReceiver = messageReceiver;
        messageReceiver.setMessageExecutor(this::handleMessage);
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
        TeriserClient teriserClient = new TeriserClient(this::request);
        teriserClient.startClient();
        System.out.println("Teriser client is Running");
        methods.forEach((name, method) -> System.out.println(name + ":" + method));
    }

    public String request(String formattedJson) {
        return messageReceiver.onMessageReceived(formattedJson);
    }

    private String handleMessage(String formattedJson) {
        JsonObject jsonObject = JsonParser.parseString(formattedJson).getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        JsonElement parameters = jsonObject.get("parameters");
//        JsonElement code = jsonObject.get("messageID");

        Method targetMethod = methods.get(methodName);
        Object[] args = makeArgs(targetMethod, parameters.getAsJsonObject());

//        DataPacketBuilder builder = new DataPacketBuilder(jsonObject.get("developerID").getAsString(), jsonObject.get("projectID").getAsString(), jsonObject.get("messageID").getAsInt());
        DataPacketBuilder builder = new DataPacketBuilder();

        String msg = "";

        try {
            builder.setData(
                    new String[]{(String) targetMethod.invoke(instances.get(methodName), args)}
            );
        } catch (IllegalAccessException e) {
            builder.setErrorMessage("No Parameter");
            msg = builder.buildServerMessage();
            return msg;
        } catch (InvocationTargetException e1){
            e1.printStackTrace();
        }

        msg = builder.buildServerOkMessage();

        return msg;
    }

    public Object[] makeArgs(Method targetMethod, JsonObject data) {
        Object[] args = new Object[targetMethod.getParameterCount()];

        int i = 0;
        for (Class<?> parameterType : targetMethod.getParameterTypes()) {
            Gson gson = new Gson();
            for (Map.Entry<String, JsonElement> namedJsonElement : data.entrySet()) {
                if (parameterType.getName().endsWith(namedJsonElement.getKey())) {
                    args[i++] = gson.fromJson(namedJsonElement.getValue(), parameterType);
                }
            }

        }
        return args;
    }

}
