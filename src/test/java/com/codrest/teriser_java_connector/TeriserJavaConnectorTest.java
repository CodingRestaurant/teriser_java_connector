/*
 * Author : Kasania
 * Filename : com.codrest.teriser.TeriserJavaConnectorTest
 * Desc :
 */
package com.codrest.teriser_java_connector;


import com.codrest.teriser_java_connector.core.ClientMessage;
import com.codrest.teriser_java_connector.core.CustomParser;
import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.testpackage.TestBot3;
import com.google.gson.*;
import org.junit.jupiter.api.*;

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
        teriser = TeriserJavaConnector.Make("ABC", messageReceiver);
        teriser.addModule(TestBot3.class);

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

        String msg = gson.toJson(new ClientMessage("customArrayMethod", array));

        System.out.println("Msg " + msg);

        teriser.handleMessage(msg);
    }

    @Test
    @Order(5)
    @DisplayName("customClassInfo")
    public void customClassInfo() {
        JsonArray methodInfo = teriser.checkCustom();

//        System.out.println("Method Info");
//        System.out.println(methodInfo);
    }


    @Test
    @Order(6)
    @DisplayName("MethodInfo")
    public void MethodInfo() {
        System.out.println(teriser.createMethodInfo());

    }

    @Test
    @Order(7)
    @DisplayName("makeArgs")
    public void makeArgs() {
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

        JsonObject p4 = new JsonObject();
        p4.addProperty("double", 2.0);
//
        JsonArray arr = new JsonArray();
        arr.add(gson.toJson(new CubeData("Cube Name1")));
        arr.add(gson.toJson(new CubeData("Cube Name2")));
        arr.add(gson.toJson(new CubeData("Cube Name3")));
        arr.add(gson.toJson(new CubeData("Cube Name4")));

        JsonObject p5 = new JsonObject();
        p5.add("List<" + CubeData.class.getCanonicalName() + ">", arr);

        JsonArray array = new JsonArray();

        array.add(p1);
        array.add(p2);
        array.add(p3);
        array.add(p4);
        array.add(p5);

        String msg = gson.toJson(new ClientMessage("allMethod", array));
        System.out.println("MSG " + msg);
        //{ "CubeData":[ 1,true,2.0,"",{"User":[1.0,"test"]},[1,2,3,4]]}
        teriser.handleMessage(msg);
    }


    @Test
    @Order(8)
    @DisplayName("CustomParserTest")
    public void CustomParserTest() {
        CubeData data = new CubeData("Name1");
        User user = new User();
        user.setid(123456);
        user.setname("UserName");
        data.setuser(user);

        JsonObject root = new JsonObject();

        JsonArray params = new JsonArray();
        params.add(1);
        params.add(true);
        params.add(2.0);

        JsonObject custom = new JsonObject();
        JsonArray customParams = new JsonArray();
        customParams.add(1.0);
        customParams.add("test");
        custom.add("User", customParams);

        params.add(custom);

        JsonArray arr = new JsonArray();
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.add(4);

        params.add(arr);

        root.add("CubeData", params);

        Gson gson = new GsonBuilder().create();

        System.out.println("CubeData " + gson.toJson(data));
        System.out.println("Data " + root);
        System.out.println(CustomParser.parse(root.toString()));
    }

    @Test
    @Order(9)
    @DisplayName("Test")
    public void Test() {
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
        params.add("StringVlaue");
        params.add(1);
        params.add(true);
        params.add(2.0);

        JsonObject custom = new JsonObject();
        JsonArray customParams = new JsonArray();
        customParams.add(1.0);
        customParams.add("test");
        custom.add("User", customParams);

        params.add(custom);

        JsonArray parr = new JsonArray();
        parr.add(1);
        parr.add(2);
        parr.add(3);
        parr.add(4);

        params.add(parr);

        test.add("CubeData", params);

        JsonObject root = new JsonObject();
        root.addProperty("method", "cubeDataTest");

        JsonArray array = new JsonArray();
        array.add(test);

        root.add("parameters", array);

        System.out.println("Msg " + msg);
        System.out.println("new MSG " + root);

        ClassScanner.scanAllClass();

        System.out.println(CustomParser.parse(root.toString()));


    }

    @Test
    @Order(10)
    @DisplayName("Test2")
    public void Test2() {
        Gson gson = new GsonBuilder().create();
        User test = gson.fromJson("{[1.0,\"test\"]}", User.class);
        System.out.println("Test " + test);
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
