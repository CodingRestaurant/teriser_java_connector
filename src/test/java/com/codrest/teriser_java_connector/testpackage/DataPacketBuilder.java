package com.codrest.teriser_java_connector.testpackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataPacketBuilder {


    public static String clientMessageBuild(
            String developerID,
            String botID,
            int requestCode,
            String methodName,
            String[] methodParameter) {

        ClientMessage msg = new ClientMessage(developerID, botID, requestCode, methodName, methodParameter);

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
