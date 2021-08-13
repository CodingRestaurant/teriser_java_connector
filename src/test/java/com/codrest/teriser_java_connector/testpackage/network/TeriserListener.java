package com.codrest.teriser_java_connector.testpackage.network;

import com.codrest.teriser_java_connector.core.DataPacketBuilder;
import com.codrest.teriser_java_connector.core.ResponseCode;
import com.codrest.teriser_java_connector.core.Teriser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TeriserListener implements Runnable{

    private Teriser teriser;
    private SocketChannel socket;

    public TeriserListener(Teriser teriser, SocketChannel socket) {
        this.teriser = teriser;
        this.socket = socket;
    }

    @Override
    public void run() {
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

                JsonObject json = JsonParser.parseString(data).getAsJsonObject();
                System.out.println("Json "+json);

                String result = teriser.request(data);

                String developerID = json.get("developerID").getAsString();
                String projectID = json.get("projectID").getAsString();
                int messageID = json.get("messageID").getAsInt();

                DataPacketBuilder builder = new DataPacketBuilder(developerID, projectID, messageID);
                String resultMsg = builder.setData(new String[]{result})
                        .buildServerOkMessage();

                byte[] bytes = resultMsg.getBytes(StandardCharsets.UTF_8);
                ByteBuffer answerBuffer = ByteBuffer.allocate(4+bytes.length);
                answerBuffer.putInt(4);
                answerBuffer.put(bytes);

                socket.write(answerBuffer);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
