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
import com.codrest.teriser_java_connector.testpackage.CubeData;
import com.codrest.teriser_java_connector.testpackage.TestBot3;
import com.google.gson.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeriserJavaConnectorTest {

    MessageReceiver messageReceiver;
    Teriser teriser;

    @BeforeAll
    public void Init() {
        messageReceiver = new MessageReceiver();
        teriser = TeriserJavaConnector.Make("ABC", messageReceiver);
        teriser.addModule(TestBot3.class);

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

        teriser.handleMessage(msg);
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

        teriser.handleMessage(msg);
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

        teriser.handleMessage(msg);
    }

    @Test
    @Order(4)
    @DisplayName("customArray")
    public void customArray() {
        Gson gson = new GsonBuilder().create();
        JsonArray array = new JsonArray();

        List<CubeData> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new CubeData("name" + i));
        }

        JsonObject p1 = new JsonObject();
        JsonArray testdata = new JsonArray();
//        JsonObject test = JsonParser.parseString(gson.toJson(new CubeData("name 1"))).getAsJsonObject();
//        testdata.add(test);
        list.forEach(e -> testdata.add(JsonParser.parseString(gson.toJson(e))));


        p1.add("CubeData[]", testdata);

        JsonArray testdata2 = new JsonArray();
        testdata2.add(new CubeData("name4").toString());
        testdata2.add(new CubeData("name5").toString());

        JsonObject p2 = new JsonObject();
        p2.add("List<" + CubeData.class.getCanonicalName() + ">", testdata2);

        array.add(p1);
        array.add(p2);

        String msg = gson.toJson(new ClientMessage("customArray", array));

        System.out.println("Msg " + msg);

        teriser.handleMessage(msg);
    }

    @Test
    @Order(5)
    @DisplayName("customClassInfo")
    public void customClassInfo() {
        teriser.getMethodInfo();
    }

//    @Test
//    @Order(2)
//    @DisplayName("ListTest")
//    public void ListTest() {
//        Gson gson = new Gson();
//        String json = "[ \"Adam\", \"John\", \"Mary\" ]";
//
//        Type type = new TypeToken<List<String>>(){}.getType();
//        List<String> members = gson.fromJson(json, type);
//        System.out.println("Members "+members);
//    }

    //
//    @Test
//    @Order(2)
//    @DisplayName("MessageReceiveTest1")
//    public void MessageReceiveTest1() {
//        messageReceiver.onMessageReceived("{\n" +
//                "    \"method\":\"cube\",\n" +
//                "    \"data\":{\n" +
//                "        \"User\":{\n" +
//                "        \"Id\":\"hello\",\n" +
//                "        \"Name\":\"128\"\n" +
//                "        }\n" +
//                "    }\n" +
//                "}");
//
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("MessageReceiveTest2")
//    public void MessageReceiveTest2() {
//        messageReceiver.onMessageReceived("{\n" +
//                "    \"method\":\"cube2\",\n" +
//                "    \"data\":{\n" +
//                "        \"User\":{\n" +
//                "        \"id\":128,\n" +
//                "        \"name\":\"hello\"\n" +
//                "        }\n" +
//                "    }\n" +
//                "}");
//
//
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("MessageReceiveTest3")
//    public void MessageReceiveTest3() {
//        messageReceiver.onMessageReceived("{\n" +
//                "  \"method\": \"test\",\n" +
//                "  \"data\": {\n" +
//                "    \"CubeData\": {\n" +
//                "      \"id\": 1234,\n" +
//                "      \"active\": true,\n" +
//                "      \"prob\": 10.1,\n" +
//                "      \"name\": \"cube\",\n" +
//                "      \"user\":{\n" +
//                "        \"id\": 123,\n" +
//                "        \"name\":\"name\"\n" +
//                "      }\n" +
//                "      \n" +
//                "    }\n" +
//                "  }\n" +
//                "}");
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("MessageReceiveTest4")
//    public void MessageReceiveTest4() {
//        messageReceiver.onMessageReceived("{\n" +
//                "  \"method\": \"test\",\n" +
//                "  \"data\": {\n" +
//                "    \"CubeData\": {\n" +
//                "      \"id\": 1234,\n" +
//                "      \"active\": true,\n" +
//                "      \"prob\": 10.1,\n" +
//                "      \"name\": \"item\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("MessageReceiveTest5")
//    public void MessageReceiveTest5() {
//        messageReceiver.onMessageReceived("{\n" +
//                "  \"method\": \"test\",\n" +
//                "  \"data\": {\n" +
//                "    \"CubeData\": {\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("MessageReceiveTest6")
//    public void MessageReceiveTest6() {
//        messageReceiver.onMessageReceived("{\n" +
//                "  \"method\": \"test\",\n" +
//                "  \"data\": {\n" +
//                "  }\n" +
//                "}");
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("MessageReceiveTest7")
//    public void MessageReceiveTest7() {
//        messageReceiver.onMessageReceived("{\n" +
//                "  \"method\": \"test2\",\n" +
//                "  \"data\": {\n" +
//                "    \"CubeData\": {\n" +
//                "      \"id\": 1234,\n" +
//                "      \"active\": true,\n" +
//                "      \"prob\": 10.1,\n" +
//                "      \"name\": \"cube\",\n" +
//                "      \"user\":{\n" +
//                "        \"id\":123,\n" +
//                "        \"name\":\"pl1\"\n" +
//                "      }\n" +
//                "    },\n" +
//                "    \"User\":{\n" +
//                "        \"id\":128,\n" +
//                "        \"name\":\"hello\"\n" +
//                "        }\n" +
//                "  }\n" +
//                "}\n");
//    }
//
//    @Test
//    @Order(9)
//    @DisplayName("MessageReceiveTest8")
//    public void MessageReceiveTest8() {
//        messageReceiver.onMessageReceived(
//                "{\n" +
//                        "  \"method\": \"blackCube\",\n" +
//                        "  \"data\": {\n" +
//                        "    \"CubeData\": {\n" +
//                        "      \"id\": 1234,\n" +
//                        "      \"active\": true,\n" +
//                        "      \"prob\": 10.1,\n" +
//                        "      \"name\": \"cube\",\n" +
//                        "      \"user\":{\n" +
//                        "        \"id\":123,\n" +
//                        "        \"name\":\"pl1\"\n" +
//                        "      }\n" +
//                        "    },\n" +
//                        "    \"User\":{\n" +
//                        "        \"id\":128,\n" +
//                        "        \"name\":\"hello\"\n" +
//                        "        }\n" +
//                        "  }\n" +
//                        "}"
//        );
//    }
//
//
//    @Test
//    @Order(2)
//    @DisplayName("ClientMessageBuilderTest")
//    public void ClientMessageBuilderTest() {
//        JsonObject test = new JsonObject();
//
//        JsonArray array = new JsonArray();
//
//        JsonObject parameter1 = new JsonObject();
//        parameter1.addProperty("String", "string1");
//
//        JsonObject parameter2 = new JsonObject();
//        parameter2.addProperty("String", "string2");
//
//        JsonObject parameter3 = new JsonObject();
//        parameter3.addProperty("int", "22222");
//
//        array.add(parameter1);
//        array.add(parameter2);
//        array.add(parameter3);
//
//        test.add("data", array);
//
//        DataPacketBuilder builder = new DataPacketBuilder();
//
//        String data = builder
//                .setMethodName("testMethod")
//                .setMethodParameter(test)
//                .buildClientMessage();
//
//        System.out.println("Client message "+data);
//
//        messageReceiver.onMessageReceived(data);
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("ParameterTest_StringIntDouble")
//    public void ParameterTest_StringIntDouble() {
//        JsonObject test = new JsonObject();
//
//        JsonArray array = new JsonArray();
//
//        JsonObject parameter1 = new JsonObject();
//        parameter1.addProperty("String", "string1");
//
//        JsonObject parameter2 = new JsonObject();
//        parameter2.addProperty("int", "123456789");
//
//        JsonObject parameter3 = new JsonObject();
//        parameter3.addProperty("double", "14.123");
//
//        array.add(parameter1);
//        array.add(parameter2);
//        array.add(parameter3);
//
//        test.add("data", array);
//
//        DataPacketBuilder builder = new com.codrest.teriser_java_connector.core.DataPacketBuilder(1);
//
//        String data = builder
//                .setMethodName("testMethod2")
//                .setMethodParameter(test)
//                .buildClientMessage();
//
//        System.out.println("Client message "+data);
//
//        messageReceiver.onMessageReceived(data);
//    }

//    @Test
//    @Order(4)
//    @DisplayName("EndPointTest")
//    public void EndPointTest() {
//        teriser.run();
//
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
