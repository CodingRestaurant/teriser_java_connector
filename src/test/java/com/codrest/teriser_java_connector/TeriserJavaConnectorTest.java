/*
 * Author : Kasania
 * Filename : com.codrest.teriser.TeriserJavaConnectorTest
 * Desc :
*/
package com.codrest.teriser_java_connector;


import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;
import com.codrest.teriser_java_connector.core.net.MessageReceiver;
import com.codrest.teriser_java_connector.testpackage.TestBot;
import com.codrest.teriser_java_connector.testpackage.TestBot2;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeriserJavaConnectorTest {

    MessageReceiver messageReceiver;
    Teriser teriser;

    @BeforeAll
    public void Init(){
        messageReceiver = new MessageReceiver();
        teriser = TeriserJavaConnector.Make("ABC", messageReceiver);

    }

    @Test
    @Order(1)
    @DisplayName("ModuleAddTest")
    public void ModuleAddTest(){
        teriser.addModule(TestBot.class);
        teriser.addModule(TestBot2.class);
        teriser.run();
    }

    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
    @Order(7)
    @DisplayName("MessageReceiveTest6")
    public void MessageReceiveTest6() {
        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test\",\n" +
                "  \"data\": {\n" +
                "  }\n" +
                "}");
    }

}
