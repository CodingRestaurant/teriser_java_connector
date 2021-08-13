package com.codrest.teriser_java_connector.testpackage.network;

import com.codrest.teriser_java_connector.testpackage.CubeData;
import com.codrest.teriser_java_connector.testpackage.DataPacketBuilder;
import com.codrest.teriser_java_connector.testpackage.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserClient {

    private SocketChannel client;
    private ExecutorService executors;

    public UserClient() {
        executors = Executors.newCachedThreadPool();
        executors.execute(this::connectServer);
    }

    private void connectServer() {
        try {
            client = SocketChannel.open();
            client.connect(new InetSocketAddress("127.0.0.1", 10101));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData() {
        executors.execute(() -> {
            JsonObject test = new JsonObject();

            CubeData cubeData = new CubeData();
            cubeData.setName("cube");

            User user = new User();
            user.setName("User Name");

            Gson gson = new GsonBuilder().create();

            test.add("CubeData", JsonParser.parseString(gson.toJson(cubeData)));
            test.add("User", JsonParser.parseString(gson.toJson(user)));

            DataPacketBuilder builder = new DataPacketBuilder("developer ID", "Project ID", 1);

            String msg = builder
                    .setMethodName("helloWorld")
                    .setMethodParameter(test)
                    .buildClientMessage();

            try {
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                ByteBuffer buffer = ByteBuffer.allocate(data.length + 4);
                buffer.putInt(data.length);
                buffer.put(data);

                client.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
