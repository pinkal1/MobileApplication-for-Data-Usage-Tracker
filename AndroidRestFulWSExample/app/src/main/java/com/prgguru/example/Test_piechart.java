package com.prgguru.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PinkalJay on 5/9/15.
 */
public class Test_piechart extends Activity
{
    private ProgressBar pb;
    private static String PHONE_NUM = "123";

String phoneno;
    ArrayList<HashMap<String, String>> dataMap;
    JSONArray data;
    float dataUsed;
    TextView optionalTextBox;


    private User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_layout);
//String phno = "7543";
        String STARTDATE="2015-04-23";
        String ENDDATE="2015-05-08";



        RequestParams params = new RequestParams();
        params.put("phoneNo",PHONE_NUM);
        params.put("startDate",STARTDATE );
        params.put("endDate", ENDDATE);

        invokeWS(params);
    }

    public void invokeWS(RequestParams params){

        // Show Progress Dialog
       // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient objectvk
        AsyncHttpClient client = new AsyncHttpClient();
        //http://localhost:8055/login/doLogin?phoneNo=123&password=axc
        client.get("http://a06cbb35.ngrok.io/dataUsage/familyData",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    // When the JSON response has status boolean value assigned with true
                   if(obj.getBoolean("status")){
                    //Toast.makeText(getApplicationContext(), "data displayed...!"+obj.toString(), Toast.LENGTH_LONG).show();


                       JSONArray arr = new JSONArray(obj.getString("result"));
                       int Data[] = new int[arr.length()];
                       String phone[] = new String[arr.length()];
                       Double threshold = obj.getDouble("Threshold");
                       Double quota = obj.getDouble("Quota");
                       int familyTotal = obj.getInt("familyTotal");
                       System.out.println("threshold - " + threshold  + "\nquota -" + quota + "\nfamilyTotal = "+familyTotal);
                       if(arr.length()!=0) {


                           for (int i = 0; i < arr.length(); i++) {
                               JSONObject o = arr.getJSONObject(i);
                               System.out.println("phone - " + arr.getJSONObject(i).getString("Phone No"));
                               System.out.println("Data - " + arr.getJSONObject(i).getString("Data"));
                               phone[i] = o.getString("Phone No");
                               Data[i] = Integer.parseInt(o.getString("Data"));
                           }
                       }

                        Toast.makeText(getApplicationContext(), "data displayed...!"+ phone[0]+":"+Data[0]+" || "+phone[1]+":"+Data[1]+" || "+phone[2]+":"+Data[2],Toast.LENGTH_LONG).show();

                        //TextView usage = (TextView)findViewById(R.id.txt1usage);
                       // usage.setText((obj.getString("totalData")));

                    }
                    // Else display error message
                    else{
                     //   errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
               // prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}


