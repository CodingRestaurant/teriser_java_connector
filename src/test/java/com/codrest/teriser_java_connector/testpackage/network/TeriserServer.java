package com.codrest.teriser_java_connector.testpackage.network;

import com.codrest.teriser_java_connector.core.DataPacketBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeriserServer {

    private ServerSocketChannel serverSocket;
    private ExecutorService executors;
    private ExecutorService userExecutors;

    private ArrayList<SocketChannel> users;
    private HashMap<String, SocketChannel> teriserClients;


    public TeriserServer() {
        executors = Executors.newCachedThreadPool();
        userExecutors = Executors.newCachedThreadPool();
        users = new ArrayList<>();
        teriserClients = new HashMap<>();
    }

    public void startServer() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(10101));

            if (!serverSocket.isOpen()) {
                System.out.println("Server is open " + serverSocket.getLocalAddress());
            }
            startAccept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAccept() {
        if (serverSocket.isOpen()) {
            executors.execute(this::acceptClient);
        }
    }

    /**
     * TODO
     * 프로토콜 설계 부족함
     * teriser클라이언트인지 일반 사용자인지 구별 해야 함
     */
    private void acceptClient() {
        while (serverSocket.isOpen()) {
            try {
                System.out.println("Waiting");
                SocketChannel client = serverSocket.accept();
                System.out.println("Client Accepted " + client.getLocalAddress());
                userExecutors.execute(new UserListener(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRequest(String method, JsonObject parameters) {
        String address = "";
        try {
            URL url = new URL(address);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");


            connection.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            DataPacketBuilder builder = new DataPacketBuilder("developerID", "projectID", 1);

            builder.setMethodName(method);
            builder.setMethodParameter(parameters);
            String msg = builder.buildClientMessage();

            bw.write(msg);
            bw.flush();
            bw.close();

            int code = connection.getResponseCode();

            if (code == HttpsURLConnection.HTTP_OK) {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
            }

            //TODO send result to client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
