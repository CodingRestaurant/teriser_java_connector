/*
 * Author : Kasania
 * Filename : Teriser
 * Desc :
 */
package com.codrest.teriser_java_connector.core;

import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.core.net.TeriserClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.*;
import java.util.*;

public class Teriser {

    private ArrayList<Class<?>> checkList = new ArrayList<>();
    private String token;
    private MessageReceiver messageReceiver;
    Set<Class<?>> classes = new HashSet<>();
    Map<String, Method> methods = new HashMap<>();
    Map<String, Object> instances = new HashMap<>();
    public TeriserClient socketTest;


    public Teriser(String token, MessageReceiver messageReceiver) {
        this.token = token;
        this.messageReceiver = messageReceiver;
        messageReceiver.setMessageExecutor(this::handleMessage);
        initCheckList();
        socketTest = new TeriserClient(this::request, this::createMethodInfo);
    }

    private void initCheckList() {
        checkList.add(Integer.class);
        checkList.add(Double.class);
        checkList.add(String.class);
        checkList.add(Character.class);
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
            String parameterName = parameterType.getName();
            Gson gson = new Gson();
            for (JsonElement parameter : data) {
                JsonObject json = parameter.getAsJsonObject();
                String type = json.keySet().iterator().next();

                if (parameterName.contains("List") && type.startsWith("List<")) {
                    args[i++] = gson.fromJson(json.get(type), TypeToken.get(parameterType).getType());
                    data.remove(json);
                    break;
                } else if (parameterName.startsWith("[L") && parameterName.endsWith(";") && parameterName.contains(type.split("\\[")[0])) {
                    args[i++] = gson.fromJson(json.get(type), parameterType);
                    data.remove(json);
                    break;
                } else if (parameterName.endsWith(type)) {
                    args[i++] = gson.fromJson(json.get(type).getAsString(), parameterType);
                    data.remove(json);
                    break;
                }
            }

        }

        return args;
    }

    public JsonObject createMethodInfo() {
        Map<String, List<String>> methodMap = getMethodInfo();

        JsonObject root = new JsonObject();

        root.addProperty("Token", token);

        JsonArray method_parameterArray = new JsonArray();
        for (String key : methodMap.keySet()) {
            JsonArray parameterArray = new JsonArray();
            List<String> parameters = methodMap.get(key);
            for (String p : parameters) {
                parameterArray.add(p);
            }
            JsonObject data = new JsonObject();
            data.add("parameters", parameterArray);
            JsonObject method_parameterJson = new JsonObject();
            method_parameterJson.add(key, data);
            method_parameterArray.add(method_parameterJson);
        }
        root.add("apis", method_parameterArray);

        root.add("types", checkCustom());

        return root;
    }


    public JsonArray checkCustom() {
        Set<Class<?>> customClassSet = new HashSet<>();
        JsonArray list = new JsonArray();
        for (String key : methods.keySet()) {
            Method method = methods.get(key);
            for (Class<?> type : method.getParameterTypes()) {
                isCustom(type, customClassSet);
            }
            for (Type test : method.getGenericParameterTypes()) {
                if (test.getTypeName().contains("List")) {
                    ParameterizedType t = ((ParameterizedType) test);
                    Type listType = t.getActualTypeArguments()[0];
                    try {
                        Class<?> c = Class.forName(listType.getTypeName());
                        if (!checkList.contains(c)) {
                            customClassSet.add(c);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (Class<?> type : customClassSet) {
            checkField(type, customClassSet);
        }

        for (Class<?> type : customClassSet) {
            list.add(customFieldInfo(type));
        }

        System.out.println(list);

        return list;
    }

    public JsonObject customFieldInfo(Class<?> targetClass) {
        JsonArray arr = new JsonArray();
        JsonObject json = new JsonObject();
        for (Field field : targetClass.getDeclaredFields()) {
//            arr.add(field.getGenericType().toString());
            json.addProperty(field.getName(), field.getGenericType().toString());
        }
        JsonObject res = new JsonObject();
        res.add(targetClass.getSimpleName(), json);
        return res;
    }

    private void isCustom(Class<?> targetClass, Set<Class<?>> customClassSet) {
        if (!targetClass.isPrimitive() && !checkList.contains(targetClass)) {
            addCustomInfo(targetClass, customClassSet);
        }
    }

    private void checkField(Class<?> targetClass, Set<Class<?>> customClassSet) {

        Field[] test = targetClass.getDeclaredFields();
        for (Field field : test) {
            Class<?> type = field.getType();
            if (!type.isPrimitive()) {
                if (!checkList.contains(type)) {
                    if (type.getSimpleName().equals("List")) {
                        return;
                    }
                    customClassSet.add(type);
                    checkField(type, customClassSet);
                }
            }
        }
    }

    private void addCustomInfo(Class<?> custom, Set<Class<?>> customClassSet) {

        if (custom.getSimpleName().equals("List")) {
            return;
        }

        String name = custom.getName();
        Class<?> type = custom;

        if (name.startsWith("[L") && name.endsWith(";")) {
            if (checkList.contains(custom.getComponentType())) {
                return;
            } else {
                type = custom.getComponentType();
            }
        }
        customClassSet.add(type);
//        checkField(custom, customClassSet);


//        } else {
//            for (Field field : custom.getDeclaredFields()) {
//                customFields.addProperty(field.getName(), field.getType().toString());
//            }
//        }
    }

    public Map<String, List<String>> getMethodInfo() {
        Map<String, List<String>> methodMap = new HashMap<>();

        for (String key : methods.keySet()) {
            List<String> parameters = new ArrayList<>();
            Method method = methods.get(key);

            for (Type type : method.getGenericParameterTypes()) {
                String typeName = type.getTypeName();
                String[] tokens = typeName.split("\\.");
                String name = tokens[tokens.length - 1];
                if (name.contains(";")) {
                    name = name.replace(";", "[]");
                }
                if (type.getTypeName().contains("List")) {
                    String[] test = type.getTypeName().split("List");
                    name = "List" + test[test.length - 1];
                }
                parameters.add(name);
            }

//            for (Class<?> parameterType : method.getParameterTypes()) {
//                String[] tokens = parameterType.getName().split("\\.");
//
//                System.out.println(method.getName() + " " + parameterType.getCanonicalName() + " " + tokens[tokens.length - 1]);
//                String name = tokens[tokens.length - 1];
//                if (name.contains(";")) {
//                    name = name.replace(";", "[]");
//                }
//                parameters.add(name);
//            }
            methodMap.put(method.getName(), parameters);
        }

        return methodMap;
    }
}
