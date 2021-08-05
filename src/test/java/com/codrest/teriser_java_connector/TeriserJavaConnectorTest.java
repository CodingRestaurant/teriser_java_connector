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
import org.junit.jupiter.api.Test;

public class TeriserJavaConnectorTest {

    @Test
    public void AnnotationScanTest() {

        MessageReceiver messageReceiver = new MessageReceiver();
        Teriser teriser = TeriserJavaConnector.Make("ABC",messageReceiver);
        teriser.addModule(TestBot.class);
        teriser.addModule(TestBot2.class);
        teriser.run();

        messageReceiver.onMessageReceived("{\n" +
                "    \"method\":\"cube\",\n" +
                "    \"data\":{\n" +
                "        \"User\":{\n" +
                "        \"Id\":\"hello\",\n" +
                "        \"Name\":\"128\"\n" +
                "        }\n" +
                "    }\n" +
                "}");

        messageReceiver.onMessageReceived("{\n" +
                "    \"method\":\"cube2\",\n" +
                "    \"data\":{\n" +
                "        \"User\":{\n" +
                    "        \"id\":128,\n" +
                    "        \"name\":\"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}");

        messageReceiver.onMessageReceived("{\n" +
                "  \"method\": \"test\",\n" +
                "  \"data\": {\n" +
                "    \"CubeData\": {\n" +
                "      \"id\": 1234,\n" +
                "      \"active\": true,\n" +
                "      \"prob\": 10.1,\n" +
                "      \"name\": \"cube\"\n" +
                "    }\n" +
                "  }\n" +
                "}");


    }


}
