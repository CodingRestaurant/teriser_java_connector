package com.codrest.teriser_java_connector.core.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class TeriserServerConnector {

    private SocketChannel socketChannel;
    private Supplier<Map<String, List<String>>> getMethodInfo;
    private String token;
    private SSLSocket processServer;

    private String serverAddress = "http://teriser.codrest.com/connection/";

    public TeriserServerConnector(Supplier<Map<String, List<String>>> getMethodInfo, String token) {
        this.getMethodInfo = getMethodInfo;
        this.token = token;
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

//    public void deleteMethodInfo(JsonObject data) {
//        requestCommand("DELETE", data);
//    }
//
//    public void patchMethodInfo(JsonObject data) {
//        requestCommand("PATCH", data);
//    }

    private void requestCommand(String requestMethod, String methodName, JsonObject data) {
//        String springServerAddress = "http://120.142.140.116:18089/projects/fishfish/" + methodName;
////        String springServerAddress = "http://localhost:8080/projects/fishfish/"+methodName;
//        try {
//            URL url = new URL(springServerAddress);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setRequestProperty("Content-Type", "application/json; utf-8");
//            connection.setRequestMethod(requestMethod);
//            connection.setDoOutput(true);
//
//            try (OutputStream os = connection.getOutputStream()) {
//                os.write(data.toString().getBytes(StandardCharsets.UTF_8));
//                os.flush();
//            }
//            connection.getInputStream().readAllBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            socketChannel.write(ByteBuffer.wrap(data.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToTeriserMainServer() {
        HttpClient client = HttpClient.create();

        String res = client.get()
                .uri("http://teriser.codrest.com/connection/" + "address?TOKEN=" + token)
                .responseContent()
                .aggregate()
                .asString().block();

        if (Objects.isNull(res)) {
            return;
        }
        JsonObject data = (JsonObject) JsonParser.parseString(res);

        connectToProcessServer();
    }

    public void connectToProcessServer() {

        Connection connection = TcpClient.create()
                .host("cs0.teriser.codrest.com")
                .port(25565)//Fixed port
//                .secure(spec -> spec.sslContext(TcpSslContextSpec.forClient()))
                .handle((inbound, outbound) ->

                        outbound.sendString(Mono.just(token))
                                .then(
                                        inbound.receive().flatMap(byteBuf -> {
                                            System.out.println("Test " + byteBuf.toString(StandardCharsets.UTF_8));
                                            return Mono.empty();
                                        })
                                )
                )
                .connectNow();


        connection.onDispose().block();
    }

    private Publisher<Void> handler(NettyInbound inbound, NettyOutbound outbound) {
        System.out.println("handler in");

        outbound.sendString(Mono.just("Data"));
        System.out.println("send block");


//        return outbound.send(ByteBufFlux.fromString(Mono.just("DataDataDataDataDataDataData")));
        System.out.println("data " + inbound.receive().aggregate().asString());

        System.out.println("Receive block");
        return s -> {
        };
    }
}
