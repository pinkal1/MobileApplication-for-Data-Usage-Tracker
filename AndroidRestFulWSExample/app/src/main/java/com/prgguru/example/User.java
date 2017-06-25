package com.prgguru.example;

import java.io.Serializable;

/**
 * Created by PinkalJay on 4/21/15.
 */
public class User implements Serializable {

    User(String name, String number){
        username = name;
        phoneNum = number;
    }

    User(){

    }

    String username;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    String phoneNum;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return getPhoneNum()+":"+getUsername();
    }
}
