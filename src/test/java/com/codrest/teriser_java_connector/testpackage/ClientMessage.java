package com.codrest.teriser_java_connector.testpackage;

import java.util.Arrays;

public class ClientMessage {

    private String developerID;
    private String botID;
    private int requestCode;
    private String methodName;
    private String[] methodParameter;

    public ClientMessage(String developerID, String botID, int requestCode, String methodName, String[] methodParameter) {
        this.developerID = developerID;
        this.botID = botID;
        this.requestCode = requestCode;
        this.methodName = methodName;
        this.methodParameter = methodParameter;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "developerID='" + developerID + '\'' +
                ", botID='" + botID + '\'' +
                ", requestCode=" + requestCode +
                ", methodName='" + methodName + '\'' +
                ", methodParameter=" + Arrays.toString(methodParameter) +
                '}';
    }
}
