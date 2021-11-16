package com.codrest.teriser_java_connector.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DataPacketBuilder {

    private String token;
    private String methodName;
    private JsonArray methodParameter;
    private String responseCode;
    private String[] data;
    private String errorMessage;

    private Gson gson;

    public DataPacketBuilder() {
        gson = new GsonBuilder().create();
    }

    public DataPacketBuilder setMessageID() {
        return this;
    }

    public DataPacketBuilder setMethodName(String name) {
        this.methodName = name;
        return this;
    }

    public DataPacketBuilder setMethodParameter(JsonArray parameter) {
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
     * developerID  Identify who you are
     * projectID Identify project
     * requestCode Identify message
     * methodName request method name
     * data needed method parameters
     * @return formatted json String
     */
    public String buildClientMessage() {
        ClientMessage msg = new ClientMessage( methodName, methodParameter);
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
    public String buildServerMessage(String token) {
        ServerMessage serverMessage = new ServerMessage(token, responseCode, data, errorMessage);
        return gson.toJson(serverMessage);
    }

    public String buildServerOkMessage(String token){
        ServerMessage serverMessage = new ServerMessage(token, ResponseCode.OK, data, "success");
        return gson.toJson(serverMessage);
    }
}
