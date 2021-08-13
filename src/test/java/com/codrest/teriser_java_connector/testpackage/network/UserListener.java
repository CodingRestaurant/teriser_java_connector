package com.codrest.teriser_java_connector.testpackage.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class UserListener implements Runnable {

    private SocketChannel socket;

    public UserListener(SocketChannel socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
//        try {
//            while (socket.isConnected()) {
//                InputStream inputStream = socket.re();
//                byte[] buffer = new byte[195];
//                inputStream.read(buffer);
//                String dataString = new String(buffer, StandardCharsets.UTF_8);
//                System.out.println("data "+dataString);
//                JsonElement json = JsonParser.parseString(dataString);
//                System.out.println("Listen "+json);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
