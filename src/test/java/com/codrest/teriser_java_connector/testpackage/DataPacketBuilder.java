package com.codrest.teriser_java_connector.testpackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class DataPacketBuilder {


    public static String clientMessageBuild(
            String developerID,
            String botID,
            int requestCode,
            String methodName,
            String[] methodParameter) {

        ClientMessage msg = new ClientMessage(developerID, botID, requestCode, methodName, methodParameter);

        Gson gson = new GsonBuilder().create();

        return gson.toJson(msg);
    }

}
