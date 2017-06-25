package com.prgguru.example;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PinkalJay on 4/21/15.
 */
public class UserResponse implements Serializable {

    String status;
    java.util.List<User> result;

    public List<User> getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(List<User> result) {
        this.result = result;

    }
}
