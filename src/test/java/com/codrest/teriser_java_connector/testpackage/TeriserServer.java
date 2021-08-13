package com.codrest.teriser_java_connector.testpackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeriserServer {

    private ServerSocket serverSocket;
    private ExecutorService executors;
    private ExecutorService userExecutors;

    private ArrayList<Socket> users;
    private HashMap<String, Socket> teriserClients;


    public TeriserServer() {
        executors = Executors.newCachedThreadPool();
        userExecutors = Executors.newCachedThreadPool();
        users = new ArrayList<>();
        teriserClients = new HashMap<>();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(10101);
            if (!serverSocket.isClosed()) {
                System.out.println("Server is open "+serverSocket.getLocalPort());
            }
            startAccept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAccept() {
        if (!serverSocket.isClosed()) {
            executors.execute(this::acceptClient);
        }
    }

    /**
     * TODO
     * 프로토콜 설계 부족함
     * teriser클라이언트인지 일반 사용자인지 구별 해야 함
     */
    private void acceptClient() {
        try {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                System.out.println("Client Accepted "+client.getInetAddress());
                userExecutors.execute(new UserListener(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
