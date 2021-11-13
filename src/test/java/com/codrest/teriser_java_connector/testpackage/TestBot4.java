package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.CubeData;
import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.testpackage.test1.SameNameClass;

import java.util.Arrays;
import java.util.List;

public class TestBot4 {

    @Api
    public String parameterTestMethod(SameNameClass test1, com.codrest.teriser_java_connector.testpackage.test2.SameNameClass test2) {
        return "";
    }

    @Api
    public String parameterArrayMethod(SameNameClass[] test1, com.codrest.teriser_java_connector.testpackage.test2.SameNameClass[] test2) {
        System.out.println("ParameterArrayMethod in");
        System.out.println("Test1 ");
        Arrays.stream(test1).forEach(e -> System.out.println(e.getName()));
        System.out.println("Test2 ");
        Arrays.stream(test2).forEach(e -> System.out.println(e.getName()));
        return "";
    }

    @Api
    public String parameterListMethod(List<SameNameClass> test1, List<com.codrest.teriser_java_connector.testpackage.test2.SameNameClass> test2) {
        return "";
    }

    @Api
    public String primitiveArrayOverlapMethod(String[] string1, String[] string2,
                                              int[] int1, int[] int2,
                                              char[] char1, char[] char2) {
        return "";
    }

    @Api
    public String sameTypeArrayMethod(CubeData[] cube1, CubeData[] cube2) {
        return "";
    }

}
