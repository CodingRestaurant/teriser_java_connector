package com.codrest.teriser_java_connector.core.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TeriserServerConnector {

    private SocketChannel socketChannel;
    private Supplier<Map<String, List<String>>> getMethodInfo;

    public TeriserServerConnector(Supplier<Map<String, List<String>>> getMethodInfo) {
        this.getMethodInfo = getMethodInfo;
    }

    public void start() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMethodInfo() {
        Map<String, List<String>> methodMap = getMethodInfo.get();

        for (String key : methodMap.keySet()) {
            JsonArray parameterArray = new JsonArray();
            List<String> parameters = methodMap.get(key);
            for (String p : parameters) {
                parameterArray.add(p);
            }
            JsonObject data = new JsonObject();
            data.add("parameters", parameterArray);
            requestCommand("POST", key, data);
        }
    }

//    public void deleteMethodInfo(JsonObject data) {
//        requestCommand("DELETE", data);
//    }
//
//    public void patchMethodInfo(JsonObject data) {
//        requestCommand("PATCH", data);
//    }

    private void requestCommand(String requestMethod, String methodName, JsonObject data) {
//        String springServerAddress = "http://120.142.140.116:18089/projects/fishfish/" + methodName;
////        String springServerAddress = "http://localhost:8080/projects/fishfish/"+methodName;
//        try {
//            URL url = new URL(springServerAddress);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setRequestProperty("Content-Type", "application/json; utf-8");
//            connection.setRequestMethod(requestMethod);
//            connection.setDoOutput(true);
//
//            try (OutputStream os = connection.getOutputStream()) {
//                os.write(data.toString().getBytes(StandardCharsets.UTF_8));
//                os.flush();
//            }
//            connection.getInputStream().readAllBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            socketChannel.write(ByteBuffer.wrap(data.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
