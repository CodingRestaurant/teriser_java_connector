package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;
import com.codrest.teriser_java_connector.core.net.TeriserServerConnector;

public class Main {

    public static void main(String[] args) {
        Teriser teriser = TeriserJavaConnector.Make("TestToken");
        teriser.addModule(TestModule.class);
        teriser.run();

    }

}
