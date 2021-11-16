package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.annotation.Api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestBot5 {
    @Api
    public String myMethod2(String value1){
        System.out.println("MyMethod in "+value1);
        return "MyMethod2 Called";
    }

    @Api
    public String myMethod3(String value1, int value2) {
        System.out.println("MyMethod3 in Stringvalue "+value1+" intvalue "+value2);
        return "MyMethod3 Called";
    }

    @Api
    public String myMethod4(String value1, int value2, char charValue, double doubleValue) {
        System.out.println("MyMethod3 in Stringvalue "+value1+" intvalue "+value2);
        return "MyMethod4 Called";
    }

    @Api
    public String myMethod5(String[] stringList, ArrayList<Double> doubleArrayList) {
        System.out.println("myMethod5 "+ Arrays.toString(stringList)+" "+doubleArrayList);

        return "MyMethod5 Called";
    }



    @Api
    public String myMethod7(String stringValue) {
        System.out.println("myMethod7 called");
        System.out.println("Start sleep");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End sleep");
        System.out.println("stringValue "+stringValue);
        return stringValue;
    }
}
