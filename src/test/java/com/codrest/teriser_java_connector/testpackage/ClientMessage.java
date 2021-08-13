package com.codrest.teriser_java_connector.testpackage;

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

    private String developerID;
    private String projectID;
    private int messageID;
    private String method;
    private JsonObject data;

    public ClientMessage(String developerID, String projectID, int messageID, String methodName, JsonObject data) {
        this.developerID = developerID;
        this.projectID = projectID;
        this.messageID = messageID;
        this.method = methodName;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "developerID='" + developerID + '\'' +
                ", projectID='" + projectID + '\'' +
                ", messageID=" + messageID +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
