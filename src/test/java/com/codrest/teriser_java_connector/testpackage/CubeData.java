/*
 * Author : Kasania
 * Filename : CubeData
 * Desc :
 */
package com.codrest.teriser_java_connector.testpackage;

import java.util.List;

public class CubeData {

    private long id;
    private boolean active;
    private double prob;
    private String name;
    private User user;
    private List<Integer> testList;

    public CubeData(String name) {
        this.name = name;
    }

    public CubeData() {
    }

//    @Override
//    public String toString() {
//        return "CubeData{" +
//                "id=" + id +
//                ", active=" + active +
//                ", prob=" + prob +
//                ", name='" + name + '\'' +
//                ", user=" + user +
//                '}';
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
