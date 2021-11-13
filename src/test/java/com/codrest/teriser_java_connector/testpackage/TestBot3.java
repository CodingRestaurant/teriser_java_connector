package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.CubeData;
import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.testpackage.test1.SameNameClass;

import java.util.Arrays;
import java.util.List;

public class TestBot3 {

    //    @Api
//    public String testMethod2(String string1, int int2, double double3) {
//        System.out.println("testMethod2 int2 " + int2);
//        System.out.println("testMethod2 double " + double3);
//        return "TestMethod2";
//    }
//
//    @Api
//    public String arrayMethod(List<String> stringArray) {
//        System.out.println("ArrayMethod in");
//        System.out.println("data " + stringArray);
//        return "arrayMethod";
//    }
//
//    @Api
//    public String testMethod(List<Integer> intList, String stringValue, double doubleValue, double[] doubleArray) {
//        System.out.println("testMethod in");
//        System.out.println("data " + intList+" string "+stringValue+" doubleValue "+doubleValue+" doubleArray "+Arrays.toString(doubleArray));
//        return "testMethod";
//    }
//
    @Api
    public String allMethod(CubeData cubeData, double doublevalue, String stringvalue, int intvalue,
                            List<CubeData> cubeDataList) {
        System.out.println("All Method in");
        System.out.println("Cubedata " + cubeData.getname() + cubeData.getid() + " doublevalue " + doublevalue + " string " + stringvalue + " int " + intvalue + " user " + cubeData.getuser());
        System.out.println("CubaList " + cubeDataList);
        return "";
    }

    //
//    @Api
    public String arrayMethod(String[] strings, Integer[] ints) {
        System.out.println("arrayMethod in ");
        System.out.println("strings : " + Arrays.toString(strings) + " ints " + Arrays.toString(ints));

        return "arrayMethod in";
    }

    @Api
    public String myMethod5(String[] stringList, List<Double> doubleArrayList) {
        System.out.println("myMethod5 " + Arrays.toString(stringList) + " " + doubleArrayList);

        return "MyMethod5 Called";
    }

    @Api
    public String customArrayMethod(CubeData[] cubeDatas, List<CubeData> cubeDataList) {
        System.out.println("customArray in");
        System.out.println("customs " + Arrays.toString(cubeDatas) + " list " + cubeDataList);
        return "CustomArray Called";
    }

    @Api
    public String cubeDataTest(CubeData cubeData) {
        System.out.println("CubeDataTest in");
        System.out.println("Cube " + cubeData.getid());
        return "";
    }



}
