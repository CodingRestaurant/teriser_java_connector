package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.annotation.Api;

public class TestModule {

    @Api
    public String myMethod2(String value1){
        System.out.println("MyMethod in "+value1);
        return "MyMethod Called";
    }

}
