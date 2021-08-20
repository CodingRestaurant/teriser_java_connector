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

    @Api
    public String testMethod(String string1, String string2, int int3){
        System.out.println("testMethod string1 "+string1);
        System.out.println("testMethod string2 "+string2);
        System.out.println("testMethod int "+int3);
        return "TestMethod";
    }
}
