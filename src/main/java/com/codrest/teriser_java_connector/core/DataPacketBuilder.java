package com.codrest.teriser_java_connector.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class DataPacketBuilder {

    private final String developerID;
    private final String projectID;
    private final int messageID;
    private String methodName;
    private JsonObject methodParameter;
    private String responseCode;
    private String[] data;
    private String errorMessage;

    public DataPacketBuilder(String developerID, String projectID, int messageID) {
        this.developerID = developerID;
        this.projectID = projectID;
        this.messageID = messageID;
    }

    public DataPacketBuilder setMethodName(String name) {
        this.methodName = name;
        return this;
    }

    public DataPacketBuilder setMethodParameter(JsonObject parameter) {
        this.methodParameter = parameter;
        return this;
    }

    public DataPacketBuilder setResponseCode(String responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public DataPacketBuilder setData(String[] data) {
        this.data = data;
        return this;
    }

    public DataPacketBuilder setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Generate Client to Server Message
     * <p>
     * developerID  Identify hwo you are
     * projectID Identify project
     * requestCode Identify message
     * methodName request method name
     * data needed method parameters
     * @return formatted json String
     */
    public String buildClientMessage() {
        ClientMessage msg = new ClientMessage(developerID, projectID, messageID, methodName, methodParameter);

        Gson gson = new GsonBuilder().create();

        return gson.toJson(msg);
    }


    /**
     * Generate Server to Client Message
     *
     * requestCode  Identify message (use same requestCode in client message)
     * statusCode   result Code (follow http status code)
     * data         requested method result
     * errorMessage Error log or null
     * @return formatted json String
     */
    public String buildServerMessage() {
        Gson gson = new GsonBuilder().create();
        ServerMessage serverMessage;
        serverMessage = new ServerMessage(messageID, responseCode, data, errorMessage);
        return gson.toJson(serverMessage);
    }
}
