package com.mycompany.core.service;

public class Option {
    private String name;
    private String value;

    public Option(){

    }

    public Option(String value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
