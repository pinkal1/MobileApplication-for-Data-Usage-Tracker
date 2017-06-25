package com.prgguru.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PinkalJay on 5/8/15.
 */
public class Edit_activity extends Activity {

    //for saving data
    // Progress Dialog Object
    ProgressDialog prgDialog;
    //quota EditView object
    EditText quotaET;
    //threshholdEditView object
    EditText threshholdET;

    //for fetching list phoneno
    private static String PHONE_NUM = "";
    private User user = null;
    private TextView userName, userPhoneNum;

    //for remove user
    String adminno="12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        //for fetching list phoneno
        user = (User) getIntent().getSerializableExtra("USEROBJECT");
        PHONE_NUM = user.getPhoneNum();
        userName = (TextView) findViewById(R.id.txtunm);
        userPhoneNum = (TextView) findViewById(R.id.txtphno);
        userName.setText(user.getUsername());
        userPhoneNum.setText(user.getPhoneNum());

        //for saving data
        //find quota EditVew control by ID
        quotaET = (EditText) findViewById(R.id.edit_quota);
        //find oldpassword EditView control by ID
        threshholdET = (EditText) findViewById(R.id.edit_threshold);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    //for saving data...onclick button
    public void savedata(View view)
    {
        //get phonenoET control value
        String quota = quotaET.getText().toString();
        //get oldpwdET control value
        String threshhold = threshholdET.getText().toString();
        //instantiate Http Request param object
        RequestParams params = new RequestParams();
        //When phoneno EditText, oldpwd EditTExt and newpwd Edittext  view have values other than null
        if (Utility.isNotNull(quota) && Utility.isNotNull(threshhold))
        {
            //put Http parameter name with value of phoneNo editview conrol
            params.put("phoneNo", PHONE_NUM);
            params.put("quotaMb", quota);
            params.add("threshhold",threshhold);

            invokeWS(params);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "please fill up the form,don't leave fill blank", Toast.LENGTH_LONG).show();
        }
    }
    //ws for saving data
    public void invokeWS(RequestParams params)
    {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://a06cbb35.ngrok.io/manageQuota/addUserData",params,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        // Set Default Values for Edit View controls
                        //setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Data added..", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                       // errormsg.setText(obj.getString("error_msg"));
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
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //for remove user...onclick button
    public void removeuser(View view)
    {
// Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Put Http parameter username with value of Email Edit View control
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        params.put("phoneNo",PHONE_NUM);
        params.put("adminNo", adminno);
        // Invoke RESTful Web Service with Http parameters
        invokeWS1(params);
    }
    private void finishActivity(){
        Intent intent=new Intent(this, User_List_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void invokeWS1(RequestParams params){

        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient objectvk
        AsyncHttpClient client = new AsyncHttpClient();
        //http://localhost:8055/login/doLogin?phoneNo=123&password=axc
        client.get("http://a06cbb35.ngrok.io/login/removeUser",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "Selected user has been removed..", Toast.LENGTH_LONG).show();
                        finishActivity();

                    }
                    // Else display error message
                    else{
                       // errorMsg.setText(obj.getString("error_msg"));
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
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found2", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end2", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running2]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
