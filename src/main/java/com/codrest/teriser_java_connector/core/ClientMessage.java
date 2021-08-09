package com.codrest.teriser_java_connector.core;

import com.google.gson.JsonObject;

public class ClientMessage {

    private String developerID;
    private String botID;
    private int requestCode;
    private String method;
    private JsonObject data;

    public ClientMessage(String developerID, String botID, int requestCode, String methodName, JsonObject data) {
        this.developerID = developerID;
        this.botID = botID;
        this.requestCode = requestCode;
        this.method = methodName;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "developerID='" + developerID + '\'' +
                ", botID='" + botID + '\'' +
                ", requestCode=" + requestCode +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
