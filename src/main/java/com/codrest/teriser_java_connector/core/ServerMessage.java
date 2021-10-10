package com.codrest.teriser_java_connector.core;

import java.util.Arrays;

/**
 * Server to Client Message
 * <p>
 * Need 4 parameters
 * messageID -> Identify message (use same requestCode in client message)
 * responseCode -> result Code (follow http status code)
 * data -> requested method result
 * errorMessage -> Error log or null
 */
public class ServerMessage {

    private String token;
    private String responseCode;
    private String[] data;
    private String errorMessage;

    public ServerMessage(String token, String responseCode, String[] data, String errorMessage) {
        this.token = token;
        this.responseCode = responseCode;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    /**
     * Server Error Message
     *
     * @param responseCode result Code (follow http status code)
     * @param errorMessage Error log
     */
    public ServerMessage(String token, String responseCode, String errorMessage) {
        this.token = token;
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
        data = null;
    }

    /**
     * Server Success Message
     *
     * @param responseCode result Code (follow http status code)
     * @param data         requested method result
     */
    public ServerMessage(String token, String responseCode, String[] data) {
        this.token = token;
        this.responseCode = responseCode;
        this.data = data;
        errorMessage = null;
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "token=" + token +
                ", responseCode='" + responseCode + '\'' +
                ", data=" + Arrays.toString(data) +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
