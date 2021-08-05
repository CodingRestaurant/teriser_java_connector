/*
 * Author : Kasania
 * Filename : User
 * Desc :
 */
package com.codrest.teriser_java_connector.testpackage;

public class User {
    public long id;
    public String name;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
