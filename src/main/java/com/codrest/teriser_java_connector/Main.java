package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.core.ClientMessage;
import com.codrest.teriser_java_connector.core.CustomParser;
import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Main {


    public static void main(String[] args) {
        test2();
    }

    public static void test2() {
        JsonObject test = new JsonObject();

        Gson gson = new GsonBuilder().create();
        CubeData data = new CubeData("Name1");
        User user = new User();
        user.setid(123456);
        user.setname("UserName");
        data.setuser(user);

        JsonObject p1 = new JsonObject();
        p1.addProperty("CubeData", gson.toJson(data));
        JsonObject p2 = new JsonObject();
        p2.addProperty("String", "StringValue");
        JsonObject p3 = new JsonObject();
        p3.addProperty("int", 1);


        JsonArray arr = new JsonArray();
        arr.add(p1);
        arr.add(p2);
        arr.add(p3);


        String msg = gson.toJson(new ClientMessage("allMethod", arr));


        JsonArray params = new JsonArray();
        params.add(1);
        params.add(true);
        params.add(2.0);
        params.add("Cube Name");

        JsonObject custom = new JsonObject();
        JsonArray customParams = new JsonArray();
        customParams.add(1.0);
        customParams.add("test");
        custom.add("User", customParams);

        params.add(custom);

        test.add("CubeData", params);

        JsonObject root = new JsonObject();
        root.addProperty("method", "cubeDataTest");

        JsonArray array = new JsonArray();
        array.add(test);

        root.add("parameters", array);

        System.out.println("Msg "+msg);
        System.out.println("new MSG "+root);

        ClassScanner.scanAllClass();

        System.out.println(CustomParser.parse(root.toString()));
    }


    public static void test() {
        Teriser teriser = TeriserJavaConnector.Make("TestToken");
//        Teriser teriser = TeriserJavaConnector.Make("TestToken"+tokenIndex++);
        teriser.addModule(TestModule.class);
        teriser.run();
    }

}
