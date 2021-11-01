package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.annotation.Api;

import java.util.ArrayList;
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
//    @Api
//    public String cubeData(CubeData cubeData) {
//        return"";
//    }

    @Api
    public String arrayMethod(String[] strings, Integer[] ints) {
        System.out.println("arrayMethod in ");
        System.out.println("strings : "+Arrays.toString(strings)+" ints "+Arrays.toString(ints));

        return "arrayMethod in";
    }

    @Api
    public String myMethod5(String[] stringList, List<Double> doubleArrayList) {
        System.out.println("myMethod5 "+ Arrays.toString(stringList)+" "+doubleArrayList);

        return "MyMethod5 Called";
    }

    @Api
    public String customArray(CubeData[] cubeDatas, List<CubeData> cubeDataList) {
        System.out.println("customArray in");
        System.out.println("customs "+Arrays.toString(cubeDatas) +" list "+cubeDataList);
        return "CustomArray Called";
    }
}
