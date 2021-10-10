package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.annotation.Api;

import java.util.List;

public class TestBot3 {

    @Api
    public void blackCube(CubeData cubeData, User user) {
        System.out.println("Cube Failed");
    }


    @Api
    public String helloWorld(CubeData cube, User user) {
        System.out.println("TestBot3 API method Called");
        System.out.println("CubeData " + cube + " User " + user);
        return "Hello World!!";
    }

    @Api
    public String testMethod(String string1, String string2, int int3) {
        System.out.println("test method in");
        System.out.println("testMethod string1 " + string1);
        System.out.println("testMethod string2 " + string2);
        System.out.println("testMethod int " + int3);
        return "TestMethod";
    }

    @Api
    public String testMethod2(String string1, int int2, double double3) {
        System.out.println("testMethod2 string1 " + string1);
        System.out.println("testMethod2 int2 " + int2);
        System.out.println("testMethod2 double " + double3);
        return "TestMethod2";
    }

    @Api
    public String arrayMethod(List<String> stringArray) {
        System.out.println("ArrayMethod in");
        System.out.println("data " + stringArray);
        return "arrayMethod";
    }

    @Api
    public String arrayMethod2(List<CubeData> intArray, String stringValue, double doubleValue) {
        System.out.println("ArrayMethod2 in");
        System.out.println("data " + intArray+" string "+stringValue+" doubleValue "+doubleValue);

        return "arrayMethod";
    }
}
