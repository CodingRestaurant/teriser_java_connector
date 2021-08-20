package com.codrest.teriser_java_connector.core;

import com.google.gson.JsonObject;

/**
 * Client to Server message
 *
 * This need 5 parameters
 * developerID -> Identify hwo you are
 * projectID -> Identify project
 * messageID -> Identify message
 * method -> request method name
 * data -> needed method parameters
 */
public class ClientMessage {

    private int messageID;
    private String method;
    private JsonObject data;

    public ClientMessage(int messageID, String methodName, JsonObject data) {
        this.messageID = messageID;
        this.method = methodName;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                ", messageID=" + messageID +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
