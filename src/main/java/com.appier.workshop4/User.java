package com.appier.workshop4;

public class User {
    public String id = null;
    public String name = null;
    public Integer age = null;
    public Long timestamp = null;

    User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.timestamp = System.currentTimeMillis();
    }
}