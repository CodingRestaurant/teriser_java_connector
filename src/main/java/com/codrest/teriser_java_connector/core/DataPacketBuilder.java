package com.codrest.teriser_java_connector.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class DataPacketBuilder {


    public static String clientMessageBuild(
            String developerID,
            String botID,
            int requestCode,
            String methodName,
            JsonObject data) {

        ClientMessage msg = new ClientMessage(developerID, botID, requestCode, methodName, data);

        Gson gson = new GsonBuilder().create();

        return gson.toJson(msg);
    }

    public static String serverMessageBuild(
            int requestCode,
            String statusCode,
            String[] data,
            String errorMessage
    ) {
        Gson gson = new GsonBuilder().create();
        ServerMessage serverMessage;
        if (data == null) {
            serverMessage = new ServerMessage(requestCode, statusCode, errorMessage);
        } else {
            serverMessage = new ServerMessage(requestCode, statusCode, data);
        }
        return gson.toJson(serverMessage);
    }

}
