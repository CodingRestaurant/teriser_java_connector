package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.core.Teriser;
import com.codrest.teriser_java_connector.core.TeriserJavaConnector;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {

    static int tokenIndex = 0;
    static Executor executor = Executors.newFixedThreadPool(30);

    public static void main(String[] args) {
        test();
//        for (int i = 0; i < 30; i++) {
//            executor.execute(Main::test);
//        }
    }

    public static void test() {
        Teriser teriser = TeriserJavaConnector.Make("TestToken");
//        Teriser teriser = TeriserJavaConnector.Make("TestToken"+tokenIndex++);
        teriser.addModule(TestModule.class);
        teriser.run();
    }

}
