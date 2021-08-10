package com.codrest.teriser_java_connector.core;

import java.util.Arrays;

/**
 * Server to Client Message
 *
 * Need 4 parameters
 * requestCode -> Identify message (use same requestCode in client message)
 * statusCode -> result Code (follow http status code)
 * data -> requested method result
 * errorMessage -> Error log or null
 */
public class ServerMessage {

    private int requestCode;
    private String statusCode;
    private String[] data;
    private String errorMessage;

    /**
     * Server Error Message
     * @param requestCode Identify message (use same requestCode in client message)
     * @param statusCode result Code (follow http status code)
     * @param errorMessage Error log
     */
    public ServerMessage(int requestCode, String statusCode, String errorMessage) {
        this.requestCode = requestCode;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        data = null;
    }

    /**
     * Server Success Message
     * @param requestCode Identify message (use same requestCode in client message)
     * @param statusCode result Code (follow http status code)
     * @param data requested method result
     */
    public ServerMessage(int requestCode, String statusCode, String[] data) {
        this.requestCode = requestCode;
        this.statusCode = statusCode;
        this.data = data;
        errorMessage = null;
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "requestCode=" + requestCode +
                ", statusCode='" + statusCode + '\'' +
                ", data=" + Arrays.toString(data) +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
