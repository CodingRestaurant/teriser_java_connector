/*
 * Author : Kasania
 * Filename : com.codrest.teriser.TeriserJavaConnectorTest
 * Desc :
 */
package com.codrest.teriser_java_connector;


import com.codrest.teriser_java_connector.core.ClientMessage;
import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.testpackage.TestBot4;
import com.codrest.teriser_java_connector.testpackage.TestBot5;
import com.codrest.teriser_java_connector.testpackage.test1.SameNameClass;
import com.google.gson.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeriserJavaConnectorTest {

    public static ArrayList<Class<?>> checkList = new ArrayList<>();
    MessageReceiver messageReceiver;
    Teriser teriser;

    @BeforeAll
    public void Init() {
        messageReceiver = new MessageReceiver();
        teriser = TeriserJavaConnector.Make("ABC");
        teriser.addModule(TestBot5.class);
        teriser.addModule(TestBot4.class);

        checkList.add(Integer.class);
        checkList.add(Double.class);
        checkList.add(String.class);
        checkList.add(Character.class);
        checkList.add(List.class);
        checkList.add(Array.class);
    }

    @Test
    @Order(1)
    @DisplayName("ModuleAddTest")
    public void ModuleAddTest() {
        Gson gson = new GsonBuilder().create();
        JsonArray array = new JsonArray();

        JsonObject p1 = new JsonObject();
        JsonArray testdata = new JsonArray();
        testdata.add("test1");
        testdata.add("test2");
        testdata.add("test3");

        p1.add("String[]", testdata);

        JsonArray testdata2 = new JsonArray();
        testdata2.add(1.0);
        testdata2.add(2.0);
        testdata2.add(3.0);

        JsonObject p2 = new JsonObject();
        p2.add("List<" + Double.class.getCanonicalName() + ">", testdata2);

        array.add(p1);
        array.add(p2);

        String msg = gson.toJson(new ClientMessage("myMethod5", array));

        System.out.println("Msg " + msg);

//        teriser.handleMessage(msg);
    }

    @Test
    @Order(2)
    @DisplayName("ModuleAddTest2")
    public void ModuleAddTest2() {
        Gson gson = new GsonBuilder().create();
        JsonArray array = new JsonArray();

        JsonObject p1 = new JsonObject();

        p1.addProperty("String", "test1");


        JsonObject p2 = new JsonObject();
        p2.addProperty("int", 1);

        JsonObject p3 = new JsonObject();
        p3.addProperty("double", 52.0);

        array.add(p1);
        array.add(p2);
        array.add(p3);

        String msg = gson.toJson(new ClientMessage("testMethod2", array));

        System.out.println("Msg " + msg);

//        teriser.handleMessage(msg);
    }

    @Test
    @Order(3)
    @DisplayName("ModuleAddTest3")
    public void ModuleAddTest3() {
        Gson gson = new GsonBuilder().create();
        JsonArray array = new JsonArray();

        JsonObject p1 = new JsonObject();
        JsonArray testdata = new JsonArray();
        testdata.add("test1");
        testdata.add("test2");
        testdata.add("test3");

        p1.add("String[]", testdata);

        JsonArray testdata2 = new JsonArray();
        testdata2.add(1);
        testdata2.add(2);
        testdata2.add(3);

        JsonObject p2 = new JsonObject();
        p2.add("Integer[]", testdata2);

        array.add(p1);
        array.add(p2);

        String msg = gson.toJson(new ClientMessage("arrayMethod", array));

        System.out.println("Msg " + msg);

//        teriser.handleMessage(msg);
    }

    @Test
    @Order(4)
    @DisplayName("ParameterOverlapTest")
    public void ParameterOverlapTest() {
        Gson gson = new GsonBuilder().create();
        JsonArray array = new JsonArray();

        JsonObject p1 = new JsonObject();
        JsonArray testdata = new JsonArray();
        testdata.add(JsonParser.parseString(gson.toJson(new SameNameClass("Name1"))));
        testdata.add(JsonParser.parseString(gson.toJson(new SameNameClass("Name2"))));
        testdata.add(JsonParser.parseString(gson.toJson(new SameNameClass("Name3"))));

        p1.add("SameNameClass[]", testdata);

        JsonArray testdata2 = new JsonArray();
        testdata2.add(JsonParser.parseString(gson.toJson(new com.codrest.teriser_java_connector.testpackage.test2.SameNameClass("name4"))));
        testdata2.add(JsonParser.parseString(gson.toJson(new com.codrest.teriser_java_connector.testpackage.test2.SameNameClass("name5"))));
        testdata2.add(JsonParser.parseString(gson.toJson(new com.codrest.teriser_java_connector.testpackage.test2.SameNameClass("name6"))));

        JsonObject p2 = new JsonObject();
        p2.add("SameNameClass[]", testdata2);

        array.add(p2);
        array.add(p1);

        String msg = gson.toJson(new ClientMessage("parameterArrayMethod", array));

        System.out.println("Msg " + msg);

//        teriser.handleMessage(msg);

    }

    @Test
    @Order(5)
    @DisplayName("message")
    public void message() {
        Gson gson = new GsonBuilder().create();

        JsonObject root = new JsonObject();


        root.addProperty("method", "myMethod2");

        JsonArray parameterArray = new JsonArray();

        JsonObject p1 = new JsonObject();
        p1.addProperty("String", "StringValue");

        parameterArray.add(p1);

        root.add("parameters", parameterArray);

        System.out.println("myMethod2 " + root);

        root = new JsonObject();
        root.addProperty("method", "myMethod3");

        parameterArray = new JsonArray();

        p1 = new JsonObject();
        p1.addProperty("String", "StringValue");
        JsonObject p2 = new JsonObject();
        p2.addProperty("int", 123);

        parameterArray.add(p1);
        parameterArray.add(p2);

        root.add("parameters", parameterArray);

        System.out.println("myMethod3 " + root);

        root = new JsonObject();
        root.addProperty("method", "myMethod5");

        parameterArray = new JsonArray();

        p1 = new JsonObject();
        JsonArray a1 = new JsonArray();
        a1.add("String Arr 1");
        a1.add("String Arr 2");
        p1.add("String[]", a1);
        p2 = new JsonObject();
        JsonArray a2 = new JsonArray();
        a2.add(4.0);
        a2.add(5.0);
        p2.add("List<" + Double.class.getCanonicalName() + ">", a2);

        parameterArray.add(p1);
        parameterArray.add(p2);

        root.add("parameters", parameterArray);

        System.out.println("myMethod5 " + root);

        //MyMethod6
        root = new JsonObject();
        root.addProperty("method", "myMethod6");

    }

    @Test
    @Order(6)
    @DisplayName("MessageReceiveTest1")
    public void MessageReceiveTest1() {
        messageReceiver.onMessageReceived("{\n" +
                "    \"method\":\"cube\",\n" +
                "    \"data\":{\n" +
                "        \"User\":{\n" +
                "        \"Id\":\"hello\",\n" +
                "        \"Name\":\"128\"\n" +
                "        }\n" +
                "    }\n" +
                "}");

    }

    @Test
    @Order(7)
    @DisplayName("MessageReceiveTest2")
    public void MessageReceiveTest2() {
        messageReceiver.onMessageReceived("{\n" +
                "    \"method\":\"cube2\",\n" +
                "    \"data\":{\n" +
                "        \"User\":{\n" +
                "        \"id\":128,\n" +
                "        \"name\":\"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}");


    }

    @Test
    @Order(8)
    @DisplayName("MessageReceiveTest3")
    public void MessageReceiveTest3() {
        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test\",\n" +
                "  \"data\": {\n" +
                "    \"CubeData\": {\n" +
                "      \"id\": 1234,\n" +
                "      \"active\": true,\n" +
                "      \"prob\": 10.1,\n" +
                "      \"name\": \"cube\",\n" +
                "      \"user\":{\n" +
                "        \"id\": 123,\n" +
                "        \"name\":\"name\"\n" +
                "      }\n" +
                "      \n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    @Order(9)
    @DisplayName("MessageReceiveTest4")
    public void MessageReceiveTest4() {
        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test\",\n" +
                "  \"data\": {\n" +
                "    \"CubeData\": {\n" +
                "      \"id\": 1234,\n" +
                "      \"active\": true,\n" +
                "      \"prob\": 10.1,\n" +
                "      \"name\": \"item\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    @Order(10)
    @DisplayName("MessageReceiveTest5")
    public void MessageReceiveTest5() {
        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test\",\n" +
                "  \"data\": {\n" +
                "    \"CubeData\": {\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    @Order(11)
    @DisplayName("MessageReceiveTest6")
    public void MessageReceiveTest6() {
        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test\",\n" +
                "  \"data\": {\n" +
                "  }\n" +
                "}");
    }

    @Test
    @Order(12)
    @DisplayName("MessageReceiveTest7")
    public void MessageReceiveTest7() {
        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test2\",\n" +
                "  \"data\": {\n" +
                "    \"CubeData\": {\n" +
                "      \"id\": 1234,\n" +
                "      \"active\": true,\n" +
                "      \"prob\": 10.1,\n" +
                "      \"name\": \"cube\",\n" +
                "      \"user\":{\n" +
                "        \"id\":123,\n" +
                "        \"name\":\"pl1\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"User\":{\n" +
                "        \"id\":128,\n" +
                "        \"name\":\"hello\"\n" +
                "        }\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    @Order(13)
    @DisplayName("MessageReceiveTest8")
    public void MessageReceiveTest8() {
        messageReceiver.onMessageReceived(
                "{\n" +
                        "  \"method\": \"blackCube\",\n" +
                        "  \"data\": {\n" +
                        "    \"CubeData\": {\n" +
                        "      \"id\": 1234,\n" +
                        "      \"active\": true,\n" +
                        "      \"prob\": 10.1,\n" +
                        "      \"name\": \"cube\",\n" +
                        "      \"user\":{\n" +
                        "        \"id\":123,\n" +
                        "        \"name\":\"pl1\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"User\":{\n" +
                        "        \"id\":128,\n" +
                        "        \"name\":\"hello\"\n" +
                        "        }\n" +
                        "  }\n" +
                        "}"
        );
    }

    @Test
    @Order(14)
    @DisplayName("EndPointTest")
    public void EndPointTest() {
        teriser.run();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
