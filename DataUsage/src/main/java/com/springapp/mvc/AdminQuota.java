package com.springapp.mvc;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import java.sql.Date;

/**
 * Created by scsakhar on 4/28/2015.
 */
@Entity
public class AdminQuota {
    @javax.persistence.Id
    private String adminNo;
    private int quotaMB;
    private Date billingStartDate;

    public AdminQuota(String adminNo, int quotaMB, Date billingStartDate) {
        this.adminNo = adminNo;
        this.quotaMB = quotaMB;
        this.billingStartDate = billingStartDate;
    }
    public AdminQuota(){

    }
    public String getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(String adminNo) {
        this.adminNo = adminNo;
    }

    public int getQuotaMB() {
        return quotaMB;
    }

    public void setQuotaMB(int quotaMB) {
        this.quotaMB = quotaMB;
    }

    public Date getBillingStartDate() {
        return billingStartDate;
    }

    public void setBillingStartDate(Date billingStartDate) {
        this.billingStartDate = billingStartDate;
    }
}
