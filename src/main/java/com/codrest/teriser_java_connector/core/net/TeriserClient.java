package com.codrest.teriser_java_connector.core.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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
import java.util.function.Function;
import java.util.function.Supplier;


public class TeriserClient {

    private Function<String, String> requestQuery;
    private Supplier<Map<String, List<String>>> getMethodInfo;
    private HttpServer server;

    public TeriserClient(Function<String, String> requestQuery, Supplier<Map<String, List<String>>> getMethodInfo) {
        this.requestQuery = requestQuery;
        this.getMethodInfo = getMethodInfo;
        initClient();
    }

    private void initClient() {
        try {
            server = HttpServer.create(new InetSocketAddress(10101), 0);
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

                    System.out.println("Query " + exchange.getRequestURI().getQuery());
                    System.out.println("Path " + exchange.getRequestURI().getPath());


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

    private void sendMethodInfo() {
        String springServerAddress = "127.0.0.1";
        try {
            URL url = new URL(springServerAddress);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Gson gson = new GsonBuilder().create();

            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            JsonObject data = new JsonObject();
            data.addProperty("projectId", "ProjectId");
            data.addProperty("methods", gson.toJson(getMethodInfo.get()));

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
    }

    public void stopClient() {
        server.stop(2);
    }

}
