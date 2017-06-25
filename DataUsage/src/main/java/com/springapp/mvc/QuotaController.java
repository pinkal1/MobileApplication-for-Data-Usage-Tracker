package com.springapp.mvc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by scsakhar on 4/25/2015.
 */
@Controller


@RequestMapping(value = "/manageQuota")
public class QuotaController {
    @Autowired
    private UserQuotaRepository userQuotaRepository;

   @Autowired
    private AdminQuotaRepository adminQuotaRepository;

    //http://0adbeee9.ngrok.io/manageQuota/addUserData?phoneNo=11111&quotaMb=222&threshhold=23
    @RequestMapping(value = "/addUserData")
    public
    @ResponseBody
    String addUserData(@RequestParam(value = "phoneNo") String phno, @RequestParam(value = "quotaMb") int quotaMb, @RequestParam(value = "threshhold") int threshhold) throws JSONException {
        JSONObject status = new JSONObject();
        UserQuota userQuota = new UserQuota(phno, quotaMb, threshhold);
        System.out.print("Data =" + userQuota.getPhoneNo() + "\tthreshhold: " + userQuota.getThreshhold() + "\tbytes = " + userQuota.getQuotaMB());
        if (userQuota != null) {
            userQuotaRepository.save(userQuota);
            status.put("status", new Boolean(true));
            status.put("msg", "data successfully added");
        } else {
            status.put("status", new Boolean(false));
            status.put("msg", "Error while adding data");
        }
        return status.toString();
    }

    @RequestMapping(value = "/userQuota", method = RequestMethod.GET)
    public
    @ResponseBody
    String listUserData(@RequestParam(value = "phoneNo") String phno) throws JSONException {

        UserQuota userQuota =userQuotaRepository.findByPhoneNo(phno);
            JSONObject userJSON = new JSONObject();
        if(userQuota != null) {
            userJSON.put("User Phone No", userQuota.getPhoneNo());
            userJSON.put("Quota(MB)", userQuota.getQuotaMB());
            userJSON.put("Threshhold", userQuota.getThreshhold());

            }
            else{
                userJSON.put("status", new Boolean(false));
                userJSON.put("err_msg", "phone number not found");
            }

        return userJSON.toString();
    }
    //http://localhost:8055/manageQuota/addAdminData?adminNo=3829101&quotaMb=4739&billingDate=2015-01-01
    @RequestMapping(value = "/addAdminData")
    public
    @ResponseBody
    String addAdminData(@RequestParam(value = "adminNo") String phno, @RequestParam(value = "quotaMb") int quotaMb, @RequestParam(value = "billingDate") String date) throws JSONException {
        JSONObject status = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date billingDate = null;
        try {
             billingDate = (Date) dateFormat.parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        AdminQuota adminQuota = new AdminQuota(phno,quotaMb,billingDate );
        System.out.print("Data =" + adminQuota.getAdminNo()+ "\tBilling DAte: "+ adminQuota.getBillingStartDate()+"\tbytes = "+adminQuota.getQuotaMB());
        if(adminQuota!=null ) {
            adminQuotaRepository.save(adminQuota);
            status.put("status", new Boolean(true));
            status.put("msg", "data successfully added");
        }
        else{
            status.put("status", new Boolean(false));
            status.put("msg", "Error while adding data");
        }
        return status.toString();
    }


    @RequestMapping(value = "/adminQuota", method = RequestMethod.GET)
    public
    @ResponseBody
    String listAdminData(@RequestParam(value = "adminNo") String phno ) throws JSONException {

        AdminQuota adminUsage = adminQuotaRepository.findByAdminNo(phno);
        JSONObject userJSON = new JSONObject();
        if(adminUsage != null) {

            userJSON.put("Admin No ", adminUsage.getAdminNo());
            userJSON.put("Quota(MB)", adminUsage.getQuotaMB());
            userJSON.put("billing date", adminUsage.getBillingStartDate());
            userJSON.put("status", new Boolean(true));
        }
        else{
            userJSON.put("status", new Boolean(false));
            userJSON.put("err_msg", "phone number not found");
        }
        return userJSON.toString();
    }

    //http://92aecc3d.ngrok.io/manageQuota/getBillingCycleDate?adminNo=12313
    @RequestMapping(value = "/getBillingCycleDate", method = RequestMethod.GET)
    public
    @ResponseBody
    String getBillingCycle(@RequestParam(value = "adminNo") String adminPh) throws JSONException {
        JSONObject status = new JSONObject();
        AdminQuota admin = adminQuotaRepository.findByAdminNo(adminPh);
        //UserController uc = new UserController();

        if(admin != null){
            status.put("billing date", admin.getBillingStartDate().toString());
            status.put("status",new Boolean(true));
            //status.put("isadmin",uc.isAdmin(adminPh));
        }
        else{
            status.put("err_msg", "Phone number not found in database");
            status.put("status",new Boolean(false));
        }
        return status.toString();
    }



}