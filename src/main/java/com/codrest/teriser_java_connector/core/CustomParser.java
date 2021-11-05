package com.codrest.teriser_java_connector.core;

import com.codrest.teriser_java_connector.ClassScanner;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public class CustomParser {

//    "CubeData{" +
//              "id=" + id +
//              ", active=" + active +
//              ", prob=" + prob +
//              ", name='" + name + '\'' +
//              ", user=" + user +
//           '}';

    public static JsonObject parse(String data) {

        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        JsonArray array = new JsonArray();

        for (String key : json.keySet()) {
            if (key.equals("parameters")) {
                for (JsonElement e : json.get(key).getAsJsonArray()) {
                    if (e.isJsonObject()) {
                        JsonObject j = getParam(e.getAsJsonObject());
                        array.add(j);
                    } else {
                        array.add(e);
                    }
                }
            }

        }

        JsonObject root = new JsonObject();
        Iterator<String> i = json.keySet().iterator();
        root.addProperty("method", json.get(i.next()).getAsString());
        root.add("parameters", array);


        System.out.println("Before " + json);
        System.out.println("After " + root);

        return new JsonObject();
    }

    private static JsonObject getParam(JsonObject param) {

        String paramClassName = param.keySet().iterator().next();
        Class<?> targetClass = ClassScanner.classMap.get(paramClassName);
        Field[] fields = targetClass.getDeclaredFields();

        JsonArray paramArray = param.get(paramClassName).getAsJsonArray();

        System.out.println(targetClass.getName() + " fields " + Arrays.toString(fields));

        System.out.println("Param Array " + paramArray);

        if (paramArray.size() != fields.length) {
            System.out.println("Error : " + "Required Parameter size is difference");
            return null;
        }

        JsonObject paramJson = new JsonObject();

        for (int i = 0; i < paramArray.size(); i++) {
            if (paramArray.get(i).isJsonObject()) {
                paramJson.add(fields[i].getName(), getJsonParam(paramArray.get(i).getAsJsonObject()));
            } else {
                paramJson.add(fields[i].getName(), paramArray.get(i));
            }

        }
        JsonObject root = new JsonObject();
        root.add(paramClassName, paramJson);

        System.out.println("root " + root);

        return root;
    }

    private static JsonObject getJsonParam(JsonObject jsonObject) {
        String className = jsonObject.keySet().iterator().next();
        Class<?> targetClass = ClassScanner.classMap.get(className);


        JsonObject paramRoot = new JsonObject();

        JsonArray paramArray = jsonObject.get(className).getAsJsonArray();
        Field[] fields = targetClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
//            if (fields[i].getType().isPrimitive() || fields[i].getType()) {
//                paramRoot.add(fields[i].getName(), paramArray.get(i));
//            } else {
//                getJsonParam(paramArray.get(i).getAsJsonObject());
//            }
        }
        return paramRoot;
    }


    public static String parseToJsonString(Class<?> target, Object test) {


        StringBuilder builder = new StringBuilder();

        Field[] fields = target.getDeclaredFields();

        builder.append(target.getSimpleName());
        builder.append("{");
        for (Field field : fields) {
            builder.append("\"").append(field.getName()).append("\"");
            builder.append("=");
            field.setAccessible(true);
            try {
                Object value = field.get(test);
                builder.append(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");


        return builder.toString();
    }

}
