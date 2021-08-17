package com.codrest.teriser_java_connector.testpackage.network;

import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.testpackage.TestBot;
import com.codrest.teriser_java_connector.testpackage.TestBot2;
import com.codrest.teriser_java_connector.testpackage.TestBot3;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeriserClient {

    private ServerSocketChannel serverSocket;
    private ExecutorService executors;
    private ExecutorService clientExecutors;
    private Teriser teriser;

    public TeriserClient() {
        executors = Executors.newCachedThreadPool();
        clientExecutors = Executors.newCachedThreadPool();
        initTeriser();
    }


    public void test() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(10101), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
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

                    String result = teriser.request(builder.toString());

                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Content-Type", "application/json");
                    headers.add("Content-Length", String.valueOf(result));

                    OutputStream stream = exchange.getResponseBody();
                    try {
                        exchange.sendResponseHeaders(200, result.length());
                        stream.write(result.getBytes(StandardCharsets.UTF_8));
                        stream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initTeriser() {
        teriser = TeriserJavaConnector.Make("ABC", new MessageReceiver());
        //TODO component scan 만들기
        teriser.addModule(TestBot.class);
        teriser.addModule(TestBot2.class);
        teriser.addModule(TestBot3.class);
        teriser.run();
    }

    public void startServer() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(10102));
            startAccept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAccept() {
        try {
            SocketChannel client = serverSocket.accept();
            clientExecutors.execute(new TeriserListener(teriser, client));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}