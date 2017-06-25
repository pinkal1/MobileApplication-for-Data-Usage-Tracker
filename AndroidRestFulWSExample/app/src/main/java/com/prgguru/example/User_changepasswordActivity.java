package com.prgguru.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
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
 * Created by PinkalJay on 4/6/15.
 */
public class User_changepasswordActivity extends Activity
{
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errormsg;
    //phoneno EditView object
    EditText phonenoET;
    //oldpassword EditView object
    EditText oldpwdET;
    //newpassword EditView object
    EditText newpwdET;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        //find errormessage TextView control by ID
        errormsg=(TextView)findViewById(R.id.changepwd_error);
        //find phoneno EditVew control by ID
        phonenoET = (EditText) findViewById(R.id.edit_phone_no);
        //find oldpassword EditView control by ID
        oldpwdET = (EditText) findViewById(R.id.edit_oldpassword);
        //find newpassword EditView control by ID
        newpwdET = (EditText) findViewById(R.id.edit_newpassword);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    //method gets triggered when confirm button is clicked
    public void confirmUser(View view) {
        //get phonenoET control value
        String phoneno = phonenoET.getText().toString();

        //get oldpwdET control value
        String oldpwd = oldpwdET.getText().toString();

        //get newpwdET control value
        String newpwd = newpwdET.getText().toString();
        //instantiate Http Request param object
        RequestParams params = new RequestParams();
        //When phoneno EditText, oldpwd EditTExt and newpwd Edittext  view have values other than null
        if (Utility.isNotNull(phoneno) && Utility.isNotNull(oldpwd) && Utility.isNotNull(newpwd))
        {
            //put Http parameter name with value of phoneNo editview conrol
            params.put("phoneNo", phoneno);
            params.put("password", oldpwd);
            params.add("newval",newpwd);

            invokeWS(params);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "please fill up the form,don't leave fill blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS(RequestParams params)
    {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //http://a06cbb35.ngrok.io/login/changePwd?phoneNo=7543&password=aaa&newval=asa
        client.get("http://a06cbb35.ngrok.io/login/changePwd",params,new AsyncHttpResponseHandler() {
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
                        Toast.makeText(getApplicationContext(), "Password successfully changed!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Tab_mainActivity.class);
                        startActivity(intent);
                    }
                    // Else display error message
                    else {
                        errormsg.setText(obj.getString("error_msg"));
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


    //to fetch phone number
    protected String FetchPhoneNo() {
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPN = tmgr.getLine1Number();
        Log.d("phone number", mPN);

        return mPN;
    }
}