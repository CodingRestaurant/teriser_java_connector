/*
 * Author : Kasania
 * Filename : Teriser
 * Desc :
 */
package com.codrest.teriser_java_connector.core;

import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        methods.forEach((name, method) -> System.out.println(name + ":" + method));
    }

    private String handleMessage(String formattedJson) {
        try {
            JsonObject jsonObject = JsonParser.parseString(formattedJson).getAsJsonObject();
            String methodName = jsonObject.get("method").getAsString();
            JsonElement data = jsonObject.get("data");
            JsonElement code = jsonObject.get("requestCode");

            Method targetMethod = methods.get(methodName);
            Object[] args = makeArgs(targetMethod, data.getAsJsonObject());

            if (code != null) {
                String msg = DataPacketBuilder.serverMessageBuild(
                        Integer.parseInt(code.getAsString()),
                        "200",
                        new String[]{
                                (String) targetMethod.invoke(instances.get(methodName), args)
                        },
                        "No Error"
                );

                System.out.println("Server Message "+msg);

                return msg;
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
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
