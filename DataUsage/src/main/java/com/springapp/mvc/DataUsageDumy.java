package com.springapp.mvc;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by PinkalJay on 5/5/15.
 */
@Entity(name = "dataUsageDumy")
public class DataUsageDumy {
    @Id
    private int id;
    public String phoneNo;
    public  String usageMb;
    public Timestamp usageDate;

    public DataUsageDumy(){

    }
    public DataUsageDumy(String phoneNo, String usageMb) {
        super();
        this.phoneNo = phoneNo;
        this.usageMb = usageMb;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsageMb() {
        return usageMb;
    }

    public void setUsageMb(String usageMb) {
        this.usageMb = usageMb;
    }

    public Timestamp getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Timestamp usageDate) {
        this.usageDate = usageDate;
    }
}
