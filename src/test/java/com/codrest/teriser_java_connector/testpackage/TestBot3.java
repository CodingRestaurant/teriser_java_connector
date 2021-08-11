package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.annotation.Api;

public class TestBot3 {

    @Api
    public void blackCube(CubeData cubeData, User user) {
        System.out.println("Cube Failed");
    }


    @Api
    public String helloWorld(CubeData cube, User user) {
        System.out.println("TestBot3 API method Called");
        System.out.println("CubeData "+cube+" User "+user);
        return "Hello World!!";
    }
}
