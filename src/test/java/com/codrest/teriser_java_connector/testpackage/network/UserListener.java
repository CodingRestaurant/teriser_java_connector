package com.codrest.teriser_java_connector.testpackage.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class UserListener implements Runnable {

    private SocketChannel socket;

    public UserListener(SocketChannel socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("User Listener Start");
        while (socket.isConnected()) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            try {
                int rect = socket.read(buffer);
                if (rect <= 1){
                    return;
                }
                buffer.flip();
                int size = buffer.getInt();
                System.out.println("Data size "+size);


                ByteBuffer dataBuffer = ByteBuffer.allocate(size);

                while (dataBuffer.hasRemaining()) {
                    socket.read(dataBuffer);
                }

                String data = new String(dataBuffer.array(), StandardCharsets.UTF_8);
                System.out.println("Data "+data);

                JsonElement json = JsonParser.parseString(data);
                System.out.println("Json "+json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
