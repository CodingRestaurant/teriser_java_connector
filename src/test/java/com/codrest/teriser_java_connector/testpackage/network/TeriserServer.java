package com.codrest.teriser_java_connector.testpackage.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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


}
