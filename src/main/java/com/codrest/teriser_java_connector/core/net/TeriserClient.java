package com.codrest.teriser_java_connector.core.net;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;


public class TeriserClient {

    private Function<String, String> requestQuery;
    private Supplier<Map<String, List<String>>> getMethodInfo;
    private HttpServer server;
    private ScheduledExecutorService executor;
    private String token;

    public TeriserClient(Function<String, String> requestQuery, Supplier<Map<String, List<String>>> getMethodInfo, String token) {
        this.requestQuery = requestQuery;
        this.getMethodInfo = getMethodInfo;
        executor = Executors.newSingleThreadScheduledExecutor();
        this.token = token;
        initClient();
    }

    private void initClient() {
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/api/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
                    if (exchange.getRequestMethod().equals("PUT")) {
                        return;
                    }
                    if (exchange.getRequestMethod().equals("DELETE")) {
                        return;
                    }
                    if (exchange.getRequestMethod().equals("PATCH")) {
                        return;
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

                    StringBuilder builder = new StringBuilder();

                    try {
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    String result = requestQuery.apply(builder.toString());

                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Content-Type", "application/json");
                    headers.add("Content-Length", String.valueOf(result));

                    JsonObject msg = JsonParser.parseString(result).getAsJsonObject();
                    int code = msg.get("responseCode").getAsInt();

                    OutputStream stream = exchange.getResponseBody();
                    try {
                        exchange.sendResponseHeaders(code, result.length());
                        stream.write(result.getBytes(StandardCharsets.UTF_8));
                        stream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSendingAlive() {
        executor.scheduleWithFixedDelay(this::sendAlive, 0, 30, TimeUnit.SECONDS);
    }

    public void stopSendingAlive() {
        executor.shutdownNow();
    }

    private void sendAlive() {
        try {
            URL url = new URL("teriser.codrest.com/projects/alive");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            JsonObject data = new JsonObject();
            data.addProperty("token", token);

            OutputStream os = connection.getOutputStream();
            os.write(data.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMethodInfo() {
        Map<String, List<String>> methodMap = getMethodInfo.get();

        for (String key : methodMap.keySet()){
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
        String springServerAddress = "127.0.0.1";
        try {
            URL url = new URL(springServerAddress);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startClient() {
        server.start();
        startSendingAlive();
    }

    public void stopClient() {
        server.stop(2);
        stopSendingAlive();
    }

}
