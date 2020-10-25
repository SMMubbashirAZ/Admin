package com.mart.admin.model;

/**
 * Created by WeMartDevelopers .
 */
public class UserModel {
    private String Number,email,name;

    public UserModel(String number, String email, String name) {
        Number = number;
        this.email = email;
        this.name = name;
    }

    public UserModel() {
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
