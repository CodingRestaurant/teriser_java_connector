package com.codrest.teriser_java_connector.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class DataPacketBuilder {

    /**
     * Generate Client to Server Message
     *
     * @param developerID  Identify hwo you are
     * @param botID Identify project
     * @param requestCode Identify message
     * @param methodName request method name
     * @param data needed method parameters
     * @return formatted json String
     */
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

    /**
     * Generate Server to Client Message
     *
     * @param requestCode Identify message (use same requestCode in client message)
     * @param statusCode  result Code (follow http status code)
     * @param data requested method result
     * @param errorMessage Error log or null
     * @return formatted json String
     */
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
