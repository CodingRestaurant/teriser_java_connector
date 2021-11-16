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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
TODO public methods must be change to private
 with out addModule and run
 */

public class Teriser {

    /* key -> 클래스 네임 type -> class object */
    private ArrayList<Class<?>> checkList = new ArrayList<>();
    private String token;
    private MessageReceiver messageReceiver;
    Set<Class<?>> classes = new HashSet<>();
    Map<String, Method> methods = new HashMap<>();
    Map<String, Object> instances = new HashMap<>();
    public TeriserClient socketTest;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private LinkedList<Future<String>> workList = new LinkedList<>();


    public Teriser(String token, MessageReceiver messageReceiver) {
        initCheckList();
        this.token = token;
        this.messageReceiver = messageReceiver;
        messageReceiver.setMessageExecutor(this::handleMessage);
        socketTest = new TeriserClient(this::request, this::createMethodInfo);
    }

    private void initCheckList() {
        checkList.add(Integer.class);
        checkList.add(Double.class);
        checkList.add(String.class);
        checkList.add(Character.class);
        checkList.add(ArrayList.class);
        checkList.add(Object.class);
        checkList.add(Array.class);
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
        return buildMessage(jsonObject);
    }


    private String buildMessage(JsonObject jsonObject) {
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
                    System.out.println("Data " + json.get(type).toString());
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
            Class<?>[] typeClasses = method.getParameterTypes();
            Type[] types = method.getGenericParameterTypes();

            for (int i = 0; i < typeClasses.length; i++) {
                Class<?> typeClass = typeClasses[i];
                Type type = types[i];
                addCustom(typeClass, customClassSet);
                if (type.getTypeName().contains("List")) {
                    ParameterizedType t = ((ParameterizedType) type);
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

        HashSet<Class<?>> test = new HashSet<>(customClassSet);
        for (Class<?> type : test) {
            checkField(type, customClassSet);
        }

        for (Class<?> type : customClassSet) {
            list.add(customFieldInfo(type));
        }

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

    private void addCustom(Class<?> targetClass, Set<Class<?>> customClassSet) {
        if (!targetClass.isPrimitive() && !checkList.contains(targetClass)) {
            if (!targetClass.getCanonicalName().contains("[]")) {
                addCustomInfo(targetClass, customClassSet);
            }
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

    private List<String> checkHasOverlap(Method method) {

        List<String> names = new ArrayList<>();

        for (Type type : method.getGenericParameterTypes()) {
            String[] tokens = type.getTypeName().split("\\.");
            if (!tokens[tokens.length - 1].contains(">")) {
                names.add(tokens[tokens.length - 1]);
            }
        }

        List<String> overlapClassNames = new ArrayList<>();

        for (String name : names) {
            if (Collections.frequency(names, name) > 1) {
                overlapClassNames.add(name);
            }
        }

        return overlapClassNames;
    }

    public Map<String, List<String>> getMethodInfo() {
        Map<String, List<String>> methodMap = new HashMap<>();

        for (String methodKey : methods.keySet()) {

            List<String> parameters = new ArrayList<>();
            Method method = methods.get(methodKey);

            List<String> overlapClassNames = checkHasOverlap(method);

            for (Type type : method.getGenericParameterTypes()) {
                String typeName = type.getTypeName();
                String[] tokens = typeName.split("\\.");
                String name = tokens[tokens.length - 1];

                if (overlapClassNames.contains(name)) { // 중복된 타입에 대한 것 풀네임 넣기
                    name = typeName;
                }

                //리스트의 경우 타입값은 풀네임으로 넘어오고있음
                if (typeName.contains("List")) {
                    String[] test = typeName.split("List");
                    name = "List" + test[test.length - 1];
                }

                parameters.add(name);
            }
            methodMap.put(method.getName(), parameters);
        }

        return methodMap;
    }
}
