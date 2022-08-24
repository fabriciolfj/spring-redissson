package com.github.fariciolfj.redisspring.entity;

public class Customer {

    private String name;

    private String status;

    public Customer(final String name) {
        this.name = name;
    }

    public Customer(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                '}';
    }
}
