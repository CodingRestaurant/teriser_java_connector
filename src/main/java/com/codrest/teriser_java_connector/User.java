package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.annotation.UserDefinedClass;

@UserDefinedClass
public class User {
    private long id;
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
