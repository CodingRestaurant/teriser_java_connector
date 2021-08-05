/*
 * Author : Kasania
 * Filename : CubeData
 * Desc :
 */
package com.codrest.teriser_java_connector.testpackage;

public class CubeData {

    public long id;
    public boolean active;
    public double prob;
    public String name;

    @Override
    public String toString() {
        return "CubeData{" +
                "id=" + id +
                ", active=" + active +
                ", prob=" + prob +
                ", name='" + name + '\'' +
                '}';
    }
}
