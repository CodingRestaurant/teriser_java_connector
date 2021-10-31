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

public class TeriserClient {

    private Function<String, String> requestQuery;
    private Supplier<Map<String, List<String>>> getMethodInfo;
    private SocketChannel processServer;
    private AtomicBoolean isConnected = new AtomicBoolean(false);

    public TeriserClient(Function<String, String> requestQuery, Supplier<Map<String, List<String>>> getMethodInfo) {
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
        InetSocketAddress serverAddress = new InetSocketAddress("cs0.teriser.codrest.com", 25565);

        connect(serverAddress);

        checkToken(token);
        work();

//        if (checkToken(token)) {
//            work();
//        }
    }

    private boolean checkToken(String token) {
        try {
            JsonObject tokenJson = createMethodInfo();
            tokenJson.addProperty("Token", token);

            ByteBuffer initBuffer = ByteBuffer.allocate(4 + tokenJson.toString().getBytes(StandardCharsets.UTF_8).length);
            initBuffer.putInt(tokenJson.toString().getBytes(StandardCharsets.UTF_8).length);
            initBuffer.put(tokenJson.toString().getBytes(StandardCharsets.UTF_8));
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

    public JsonObject createMethodInfo() {
        Map<String, List<String>> methodMap = getMethodInfo.get();

        JsonObject list = new JsonObject();

        for (String key : methodMap.keySet()) {
            JsonArray parameterArray = new JsonArray();
            List<String> parameters = methodMap.get(key);
            for (String p : parameters) {
                parameterArray.add(p);
            }
            JsonObject data = new JsonObject();
            data.add("parameters", parameterArray);
            list.add(key, data);
        }

        return list;
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
        while (isConnected.get()) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                processServer.read(buffer);

                buffer.flip();

                int size = buffer.getInt();

                System.out.println("Size : "+size);

                buffer = ByteBuffer.allocate(size);

                processServer.read(buffer);

                System.out.println("Data : "+ new String(buffer.array()));

                String error = new String(buffer.array());
                if (error.startsWith("E01")) {
                    System.out.println("Server throw error");
                    stop();
                }

                String result = requestQuery.apply(new String(buffer.array()));

                System.out.println("method request result : "+result);

                byte[] arr = result.getBytes(StandardCharsets.UTF_8);

                ByteBuffer resultBuffer = ByteBuffer.allocate(4 + arr.length);
                resultBuffer.putInt(arr.length);
                resultBuffer.put(arr);
                resultBuffer.flip();

                processServer.write(resultBuffer);

                System.out.println("After write");
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
