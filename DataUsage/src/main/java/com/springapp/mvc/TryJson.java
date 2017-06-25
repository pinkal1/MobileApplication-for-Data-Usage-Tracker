package com.springapp.mvc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by PinkalJay on 5/7/15.
 */
public class TryJson {
    public static  void  main (String[] args) throws JSONException{
        String XYZ ="{\"result\":[{\"Data\":598906907,\"Phone No\":\"11111\"},{\"Data\":1197813814,\"Phone No\":\"11111\"},{\"Data\":1796720721,\"Phone No\":\"11111\"}],\"familyTotal\":-701525854,\"Quota\":4000,\"status\":true,\"Threshold\":4000}";

                JSONObject obj1 = new JSONObject(XYZ);
        // System.out.println("String = " + abc);

        String abc = "{\"totalData\":598906907,\"resultData\":{\"7\":598906907},\"usersQuota\":600,\"NoOfData\":1,\"usersThreshold\":1000,\"status\":true}";
        JSONObject jsonData = new JSONObject("{\"1\":14,\"2\":40,\"3\":6,\"4\":65857206,\"7\":0}");

        JSONObject obj = new JSONObject(XYZ);
        Iterator iter1 = obj.getJSONObject("resultData").keys();
        //obj.getJSONObject("resultData");

        String[] keyArr = null ;
        String[] valArr =null ;
        int count = 0;
       // System.out.println(jsonData.toString());

        Long[] Data = new Long[30];
        for(int i =0; i<30; i++){
            Data[i]=Long.valueOf(0);
        }

         for(Iterator iterator = jsonData.keys(); iterator.hasNext();) {

             String key = (String) iterator.next();
           int val = (Integer)jsonData.get(key+"");
            int k = Integer.parseInt(key);
           Data[k]= Long.valueOf(val);

             //System.out.println(key + "\t" + val );
        }

        for(int i =0; i<30; i++){
            System.out.println(i + "\t" + Data[i]);
        }
    }
}
