package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.annotation.Api;
import com.codrest.teriser_java_connector.annotation.UserDefinedClass;

@UserDefinedClass
public class CubeData {
    private long id;
    private boolean active;
    private double prob;
    private String name;
    private User user;

    public CubeData(String name) {
        this.name = name;
    }

    public CubeData() {
    }


    @Override
    public String toString() {
        return "CubeData{" +
                "id=" + id +
                ", active=" + active +
                ", prob=" + prob +
                ", name='" + name + '\'' +
                ", user=" + user +
                '}';
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public boolean isactive() {
        return active;
    }

    public void setactive(boolean active) {
        this.active = active;
    }

    public double getprob() {
        return prob;
    }

    public void setprob(double prob) {
        this.prob = prob;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }

}
