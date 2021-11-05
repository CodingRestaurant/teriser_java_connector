/*
 * Author : Kasania
 * Filename : TestBot
 * Desc :
 */
package com.codrest.teriser_java_connector.testpackage;

import com.codrest.teriser_java_connector.User;
import com.codrest.teriser_java_connector.annotation.Api;

public class TestBot {


    @Api(name = "cube")
    public String cube(){
        System.out.println("cube!");
        return "cube!";
    }

    @Api
    public void cube2(User user){
        System.out.println(user);
    }


}
