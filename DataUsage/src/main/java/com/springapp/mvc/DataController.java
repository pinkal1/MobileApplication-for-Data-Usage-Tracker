package com.springapp.mvc;

import antlr.collections.List;
import org.hibernate.criterion.CriteriaQuery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.crypto.Data;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by scsakhar on 4/22/2015.
 */
@Controller

//http://92aecc3d.ngrok.io/dataUsage/dummyUsage?phoneNo=3829&startDate=2015-04-28&endDate=2015-04-30
@RequestMapping(value = "/dataUsage")
public class DataController {

    private EntityManager em;


    @Autowired
    private AdminQuotaRepository adminQuotaRepository;


    @Autowired
    private UserQuotaRepository userQuotaRepository;

    @Autowired
    private DataUsageDumyRepository dumyRepo;
    @Autowired
    private UserRepository userRepository;


    // http://localhost:8055/dataUsage/addData?phoneNo=3829&usageMb=2
    @RequestMapping(value = "/addData")
    public
    @ResponseBody
    String addData(@RequestParam(value = "phoneNo") String phno, @RequestParam(value = "usageMb") String usageMb) throws JSONException {
        JSONObject status = new JSONObject();
        DataUsageDumy datausage = new DataUsageDumy(phno, usageMb);
        System.out.print("Data =" + datausage.getPhoneNo() + "    bytes = " + datausage.getUsageMb());
        if (datausage != null) {
            dumyRepo.save(datausage);
            status.put("status", new Boolean(true));
            status.put("msg", "data successfully added");

        } else {
            status.put("status", new Boolean(false));
            status.put("msg", "Error while adding data");
        }
        return status.toString();
    }

    //total, threshhold, number of days and data.
    //http://92aecc3d.ngrok.io/dataUsage/dataRange?phoneNo=7543&startDate=2015-04-23&endDate=2015-05-02
    @RequestMapping(value = "/dataRange")
    public
    @ResponseBody

    String dataRange(@RequestParam(value = "phoneNo") String phoneNo, @RequestParam(value = "startDate") String date1, @RequestParam(value = "endDate") String date2) throws JSONException {
        JSONObject status = new JSONObject();
        JSONArray userArray = new JSONArray();
        HashMap<Integer, Integer> dayTotal = new HashMap<Integer, Integer>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d1=null, d2=null;
        int totalData = 0;
        try {
            d1 = df.parse(date1);
            d2 = df.parse(date2);
        }catch(ParseException e){e.printStackTrace();}
        for (DataUsageDumy dataUsage : dumyRepo.findByPhoneNoAndUsageDateBetween(phoneNo, d1, d2)) {
            JSONObject userJSON = new JSONObject();
            int usageMB =  Integer.parseInt(dataUsage.getUsageMb());
            Timestamp time = dataUsage.getUsageDate();
            userJSON.put("usageMb", usageMB);
            userJSON.put("Time", time);
            int day = time.getDate();
            totalData+= usageMB;
            if (dayTotal.containsKey(day)) {
                usageMB += dayTotal.get(day);
            }
            dayTotal.put(day, usageMB);
            userArray.put(userJSON);
        }
        if (userArray.length() == 0) {
            status.put("status", new Boolean(false));
            status.put("error_msg", "No Usage for This number");
        } else {
            //status.put("result", userArray.toString());

            status.put("status", new Boolean(true));
            status.put("resultData", new JSONObject(dayTotal.toString()));
            status.put("totalData",totalData);
            status.put("usersThreshold",userQuotaRepository.findByPhoneNo(phoneNo).getThreshhold());
            status.put("usersQuota",userQuotaRepository.findByPhoneNo(phoneNo).getQuotaMB());
            status.put("NoOfData",dayTotal.size());

        }

        return status.toString();
    }

    //http://localhost:8055/dataUsage/allData
    @RequestMapping(value = "/allData", method = RequestMethod.GET)
    public
    @ResponseBody
    String listData(ModelMap model) throws JSONException {
        JSONArray userArray = new JSONArray();
        for (DataUsageDumy dataUsage : dumyRepo.findAll()) {
            JSONObject userJSON = new JSONObject();
            userJSON.put("username ", dataUsage.getPhoneNo());
            userJSON.put("usage(MB)", dataUsage.getUsageMb());
            userJSON.put("date", dataUsage.getUsageDate());
            userArray.put(userJSON);
        }
        return userArray.toString();
    }

   // http://localhost:8055/dataUsage/familyData?phoneNo=123&startDate=2015-04-23&endDate=2015-05-30
    @RequestMapping("/familyData")
    public
    @ResponseBody
    String findFamily(@RequestParam(value = "phoneNo") String phoneNo, @RequestParam(value = "startDate") String date1, @RequestParam(value = "endDate") String date2) throws JSONException {
        JSONObject status = new JSONObject();
        JSONArray familyArray = new JSONArray();
        JSONArray userArray = new JSONArray();
        HashMap<Integer, Integer> dayTotal = new HashMap<Integer, Integer>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d1 = null, d2 = null;
        int totalData = 0;
        int familyTotal = 0;
        try {
            d1 = df.parse(date1);
            d2 = df.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User u = userRepository.findByPhoneNo(phoneNo);
        if (u != null) {

            for (User user : userRepository.findByAdminNo(u.getAdminNo())) {
                JSONObject familyJSON = new JSONObject();
                familyJSON.put("Phone No", user.getPhoneNo());
                for (DataUsageDumy dataUsage : dumyRepo.findByPhoneNoAndUsageDateBetween(user.getPhoneNo(), d1, d2)) {
                    JSONObject userJSON = new JSONObject();
                    int usageMB = Integer.parseInt(dataUsage.getUsageMb());
                    Timestamp time = dataUsage.getUsageDate();
                    int day = time.getDate();
                    totalData += usageMB;
                    if (dayTotal.containsKey(day)) {
                        usageMB += dayTotal.get(day);
                    }
                    dayTotal.put(day, usageMB);
                    userArray.put(userJSON);

                }
                familyTotal+=totalData;
                familyJSON.put("Data", totalData);

                //userJSON.put("Admin No",user.getAdminNo());
                familyArray.put(familyJSON);
            }if (familyArray.length() == 0) {
                status.put("status", new Boolean(false));
                status.put("error_msg", "Admin number does not have users");
            } else {
                status.put("status", new Boolean(true));
                status.put("result", familyArray);
                status.put("familyTotal",familyTotal);
                status.put("Threshold", adminQuotaRepository.findByAdminNo(phoneNo).getQuotaMB());
                status.put("Quota", adminQuotaRepository.findByAdminNo(phoneNo).getQuotaMB());


                //	status.put("result", "Login successful");
            }

        } else {
            status.put("status", new Boolean(false));
            status.put("error_msg", "Phone number does not exists");
        }


        return status.toString();
    }

    //http://bf97e17f.ngrok.io/dataUsage/thresholdNotify?phoneNo=7543&startDate=2015-04-23&endDate=2015-05-05


    @RequestMapping("/thresholdNotify")
    public
    @ResponseBody
    String thresholdNotify(@RequestParam(value = "phoneNo") String phoneNo,@RequestParam(value = "startDate") String date1, @RequestParam(value = "endDate") String date2) throws JSONException{
        JSONArray userArray = new JSONArray();
        JSONObject status = new JSONObject();
        HashMap<Integer, Integer> dayTotal = new HashMap<Integer, Integer>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d1 = null, d2 = null;
        int totalData = 0;
        try {
            d1 = df.parse(date1);
            d2 = df.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (DataUsageDumy dataUsage : dumyRepo.findByPhoneNoAndUsageDateBetween(phoneNo, d1, d2)) {

            JSONObject userJSON = new JSONObject();
            int usageMB = Integer.parseInt(dataUsage.getUsageMb());
            Timestamp time = dataUsage.getUsageDate();
            userJSON.put("usageMb", usageMB);
            //   userJSON.put("Time", time);
            int day = time.getDate();
            totalData += usageMB;
            if (dayTotal.containsKey(day)) {
                usageMB += dayTotal.get(day);
            }
            dayTotal.put(day, usageMB);
            userArray.put(userJSON);

        }

        if (userArray.length() == 0) {
            status.put("status", new Boolean(false));
            status.put("error_msg", "No Usage for This number");
        } else {
            int threshold = userQuotaRepository.findByPhoneNo(phoneNo).getThreshhold();
            if(totalData >= threshold)
                status.put("status", new Boolean(true));
            status.put("threshold", threshold);
            status.put("totalData", totalData);
            // status.put("NoOfData", dayTotal.size());
            //status.put("result", userArray.toString());
        }
        return status.toString();
    }

}