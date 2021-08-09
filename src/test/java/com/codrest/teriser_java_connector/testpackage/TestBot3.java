package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.annotation.Api;

public class TestBot3 {

    @Api
    public void blackCube(CubeData cubeData, User user) {
        System.out.println("Cube Failed");
    }


    @Api
    public String helloWorld(String name, String old) {
        System.out.println("TestBot3 API method Called");
        return "Hello World!!";
    }
}
