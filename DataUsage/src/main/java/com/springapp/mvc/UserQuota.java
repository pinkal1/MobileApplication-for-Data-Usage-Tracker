package com.springapp.mvc;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by scsakhar on 4/24/2015.
 */
@Entity(name = "userquota")
public class UserQuota {
    @Id
    private String phoneNo;
    private int quotaMB;
    private int threshhold;

    public UserQuota(){

    }
    public UserQuota(String phoneNo, int quotaMB, int threshhold) {
        super();
        this.phoneNo = phoneNo;
        this.quotaMB = quotaMB;
        this.threshhold = threshhold;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getQuotaMB() {
        return quotaMB;
    }

    public void setQuotaMB(int quotaMB) {
        this.quotaMB = quotaMB;
    }

    public int getThreshhold() {
        return threshhold;
    }

    public void setThreshhold(int threshhold) {
        this.threshhold = threshhold;
    }
}
