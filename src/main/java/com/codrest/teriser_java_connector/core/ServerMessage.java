package com.codrest.teriser_java_connector.core;

import java.util.Arrays;

/**
 * Server to Client Message
 *
 * Need 4 parameters
 * messageID -> Identify message (use same requestCode in client message)
 * responseCode -> result Code (follow http status code)
 * data -> requested method result
 * errorMessage -> Error log or null
 */
public class ServerMessage {

    private int messageID;
    private String responseCode;
    private String[] data;
    private String errorMessage;

    /**
     * Server Error Message
     * @param messageID Identify message (use same requestCode in client message)
     * @param responseCode result Code (follow http status code)
     * @param errorMessage Error log
     */
    public ServerMessage(int messageID, String responseCode, String errorMessage) {
        this.messageID = messageID;
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
        data = null;
    }

    /**
     * Server Success Message
     * @param messageID Identify message (use same requestCode in client message)
     * @param responseCode result Code (follow http status code)
     * @param data requested method result
     */
    public ServerMessage(int messageID, String responseCode, String[] data) {
        this.messageID = messageID;
        this.responseCode = responseCode;
        this.data = data;
        errorMessage = null;
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "messageID=" + messageID +
                ", responseCode='" + responseCode + '\'' +
                ", data=" + Arrays.toString(data) +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
