package com.springapp.mvc;

/**
 * Created by PinkalJay on 4/6/15.
 */
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by scsakhar on 4/1/2015.

 */
@Entity(name = "userlogin")
public class User {
    @Id
    private String emailId;
    private String username;
    private String phoneNo;
    private String password;
    private java.sql.Timestamp registerDate;
    private String adminNo;

    public User(){
    }
    public User(String emailId, String username, String phoneNo, String adminNo, String password) {
        this.emailId = emailId;
        this.username = username;
        this.phoneNo = phoneNo;
        //   this.registerDate = registerDate;
        this.adminNo = adminNo;
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public java.sql.Timestamp getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(java.sql.Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    public String getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(String adminString) {
        this.adminNo = adminString;
    }
}
