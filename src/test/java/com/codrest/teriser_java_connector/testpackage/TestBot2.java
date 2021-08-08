/*
 * Author : Kasania
 * Filename : TestBot2
 * Desc :
 */
package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.annotation.Api;

public class TestBot2 {

    @Api(name="test")
    public String testMethod(CubeData cubeData){
        System.out.println(cubeData);
        return "";
    }

    @Api(name="test2")
    public String testMethod2(CubeData cubeData, User user){
        System.out.println(cubeData);
        System.out.println(user);
        return "";
    }



}
