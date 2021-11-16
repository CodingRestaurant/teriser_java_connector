package com.codrest.teriser_java_connector.core.net;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/*
TODO Remove error stacktrace(time out), connection reset
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

        InetSocketAddress serverAddress = getProcessServerAddress(token);

        if (Objects.isNull(serverAddress)) {
            System.out.println("Server Address is null");
            return;
        }
        connect(serverAddress);

        checkToken();
        work();
    }

    private boolean checkToken() {
        try {
            JsonObject methodInfo = getMethodInfo.get();

            ByteBuffer initBuffer = ByteBuffer.allocate(4 + methodInfo.toString().getBytes(StandardCharsets.UTF_8).length);
            initBuffer.putInt(methodInfo.toString().getBytes(StandardCharsets.UTF_8).length);
            initBuffer.put(methodInfo.toString().getBytes(StandardCharsets.UTF_8));
            initBuffer.flip();

            processServer.write(initBuffer);

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
                .uri("http://teriser.codrest.com/connection/" + "address?token=" + token)
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

        String address = data.get("response").getAsString() + ".cs.teriser.codrest.com";

        return new InetSocketAddress(address, 25565);
    }


    private void connect(InetSocketAddress serverAddress) {
        try {
            processServer = SocketChannel.open();
            processServer.connect(serverAddress);
            isConnected.set(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Packet -> size(int) + data(String)
     */
    private void work() {
        System.out.println("Start Receiving method request");
        while (isConnected.get()) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                int res = processServer.read(buffer);

                if (res < 0) {
                    stop();
                    break;
                }

                buffer.flip();

                int size = buffer.getInt();

                buffer = ByteBuffer.allocate(size);

                res = processServer.read(buffer);

                if (res < 0) {
                    stop();
                    break;
                }

                String msg = new String(buffer.array());
                if (msg.startsWith("E01")) {
                    System.out.println(msg);
                    stop();
                    break;
                }
                if (msg.startsWith("E02")) {
                    System.out.println(msg);
                    stop();
                    break;
                }

                String result = requestQuery.apply(msg);

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
