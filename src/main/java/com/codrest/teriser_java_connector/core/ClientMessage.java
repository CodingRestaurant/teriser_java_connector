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

    private String method;
    private JsonObject parameters;

    public ClientMessage(String methodName, JsonObject parameters) {
        this.method = methodName;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                ", method='" + method + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
