package com.codrest.teriser_java_connector.core.net;

import com.codrest.teriser_java_connector.core.Teriser;
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
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class TeriserClient {

    private Function<String, String> requestQuery;
    private HttpServer server;

    public TeriserClient(Function<String, String> requestQuery) {
        this.requestQuery = requestQuery;
        initClient();
    }

    private void initClient(){
        try {
            server = HttpServer.create(new InetSocketAddress(10101), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
                    if (!exchange.getRequestMethod().equals("GET")){
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

    public void startClient() {
        server.start();
    }

    public void stopClient() {
        server.stop(2);
    }

}
