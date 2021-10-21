package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.annotation.Api;

public class TestModule {

    @Api
    public String myMethod2(String value1){
        System.out.println("MyMethod in "+value1);
        return "MyMethod Called";
    }

    @Api
    public String myMethod3(String value1, int value2) {
        System.out.println("MyMethod3 in Stringvalue "+value1+" intvalue "+value2);
        return "MyMethod3 Called";
    }

}
