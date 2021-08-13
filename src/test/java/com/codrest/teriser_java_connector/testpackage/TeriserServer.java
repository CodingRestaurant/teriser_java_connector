package com.codrest.teriser_java_connector.testpackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeriserServer {

    private ServerSocket serverSocket;
    private ExecutorService executors;

    private ArrayList<Socket> clients;

    public TeriserServer() {
        executors = Executors.newCachedThreadPool();
        clients = new ArrayList<>();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(10101);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAccept() {
        if (!serverSocket.isClosed()) {
            executors.execute(this::acceptClient);
        }
    }

    private void acceptClient() {
        try {
            while (!serverSocket.isClosed()) {
                clients.add(serverSocket.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
