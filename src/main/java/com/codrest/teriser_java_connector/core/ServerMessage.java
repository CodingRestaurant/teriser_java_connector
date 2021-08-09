package com.codrest.teriser_java_connector.core;

import java.util.Arrays;

public class ServerMessage {

    private int requestCode;
    private String statusCode;
    private String[] data;
    private String errorMessage;

    /**
     * Server Error Message
     * @param requestCode
     * @param statusCode
     * @param errorMessage
     */
    public ServerMessage(int requestCode, String statusCode, String errorMessage) {
        this.requestCode = requestCode;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        data = null;
    }

    /**
     * Server Success Message
     * @param requestCode
     * @param statusCode
     * @param data
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
