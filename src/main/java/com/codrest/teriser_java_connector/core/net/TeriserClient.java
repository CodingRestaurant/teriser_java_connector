package com.codrest.teriser_java_connector.core.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/*
TODO Remove error stacktrace(time out)
 */

public class TeriserClient {

    private Function<String, String> requestQuery;
    private Supplier<JsonObject> getMethodInfo;
    private SocketChannel processServer;
    private AtomicBoolean isConnected = new AtomicBoolean(false);

    public TeriserClient(Function<String, String> requestQuery, Supplier<JsonObject> getMethodInfo) {
        this.requestQuery = requestQuery;
        this.getMethodInfo = getMethodInfo;
    }

    public void initConnection(String token) {

//        InetSocketAddress serverAddress = getProcessServerAddress(token);
//
//        if (Objects.isNull(serverAddress)) {
//            System.out.println("Server Address is null");
//            return;
//        }
        InetSocketAddress serverAddress = new InetSocketAddress("alice.cs.teriser.codrest.com", 25565);

        connect(serverAddress);

        checkToken();
        work();

//        if (checkToken()) {
//            work();
//        }
    }

    private boolean checkToken() {
        try {
            JsonObject methodInfo = getMethodInfo.get();

            ByteBuffer initBuffer = ByteBuffer.allocate(4 + methodInfo.toString().getBytes(StandardCharsets.UTF_8).length);
            initBuffer.putInt(methodInfo.toString().getBytes(StandardCharsets.UTF_8).length);
            initBuffer.put(methodInfo.toString().getBytes(StandardCharsets.UTF_8));
            initBuffer.flip();

            processServer.write(initBuffer);
//            initBuffer = ByteBuffer.allocate(4);
//
//            processServer.read(initBuffer);
//
//            int size = initBuffer.getInt();
//
//            initBuffer = ByteBuffer.allocate(size);
//
//            processServer.read(initBuffer);
//
//            StringBuilder builder = new StringBuilder();
//            builder.append(initBuffer.getChar());
//            builder.append(initBuffer.getChar());
//
//            if (builder.toString().equals("OK")) {
//                System.out.println("Token verify success");
//                return true;
//            }

        } catch (Exception e) {
            e.printStackTrace();
            stop();
            return false;
        }
        return false;
    }

    private InetSocketAddress getProcessServerAddress(String token) {
        HttpClient client = HttpClient.create();

        String res = client.get()
                .uri("http://teriser.codrest.com/connection/" + "address?TOKEN=" + token)
                .responseContent()
                .aggregate()
                .asString().block();

        if (Objects.isNull(res)) {
            return null;
        }
        JsonObject data = (JsonObject) JsonParser.parseString(res);

        if (Objects.isNull(data)) {
            return null;
        }

        return new InetSocketAddress(data.get("address").getAsString(), 25565);
    }



    private void connect(InetSocketAddress serverAddress) {
        try {
            processServer = SocketChannel.open();
            processServer.connect(serverAddress);
            isConnected.set(true);
            System.out.println("Connect " + processServer.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Packet -> size(int) + data(String)
     *
     *
     * what if
     *  요청많으면 문제없이 소화가 가능한가?
     *  작업 큐를 만들고 꺼내서 써야하나?
     */
    private void work() {
        System.out.println("Start Receiving method request");
        System.out.println("Connection state "+isConnected.get());
        while (isConnected.get()) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                int res = processServer.read(buffer);

                if (res < 0) {
                    System.out.println("Res "+res);
                    System.out.println("Size read error");
                    stop();
                    break;
                }

                buffer.flip();

                int size = buffer.getInt();

                System.out.println("Size : "+size);

                buffer = ByteBuffer.allocate(size);

                res = processServer.read(buffer);

                if (res < 0) {
                    System.out.println("Res "+res);
                    System.out.println("Read data error");
                    stop();
                    break;
                }

                String error = new String(buffer.array());
                if (error.startsWith("E01")) {
                    System.out.println("Server throw error");
                    stop();
                    break;
                }

                String result = requestQuery.apply(new String(buffer.array()));

                byte[] arr = result.getBytes(StandardCharsets.UTF_8);

                ByteBuffer resultBuffer = ByteBuffer.allocate(4 + arr.length);
                resultBuffer.putInt(arr.length);
                resultBuffer.put(arr);
                resultBuffer.flip();

                processServer.write(resultBuffer);
            } catch (Exception e) {
                e.printStackTrace();
                stop();
                return;
            }
        }
    }

    public void stop() {
        try {
            isConnected.set(false);
            processServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
