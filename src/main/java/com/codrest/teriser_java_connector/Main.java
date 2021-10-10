package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;

public class Main {

    public static void main(String[] args) {
        Teriser teriser = TeriserJavaConnector.Make("test", "test");
        teriser.addModule(TestModule.class);
        teriser.run();
    }

}
