package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.annotation.Api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestModule {

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
    public String myMethod6(CubeData[] cubeDatas, List<CubeData> cubeDataList) {
        System.out.println("customArray in");
        System.out.println("customs "+Arrays.toString(cubeDatas) +" list "+cubeDataList);
        return "CustomArray Called";
    }

}
