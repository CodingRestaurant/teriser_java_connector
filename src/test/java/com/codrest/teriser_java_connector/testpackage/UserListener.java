package com.codrest.teriser_java_connector.testpackage;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class UserListener implements Runnable{

    private Socket socket;

    public UserListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while(socket.isConnected()){
                InputStream inputStream = socket.getInputStream();
                inputStream.reset();
                byte[] data = inputStream.readAllBytes();
                String dataString = new String(data, StandardCharsets.UTF_8);
                System.out.println("Listen String "+dataString);
                JsonElement json = JsonParser.parseString(dataString);
                System.out.println("Listen Json "+json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
