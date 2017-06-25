package com.prgguru.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
 * 
 * Register Activity Class
 */
public class RegisterActivity extends Activity
{
	// Progress Dialog Object
	ProgressDialog prgDialog;
	// Error Msg TextView Object
	TextView errorMsg;
	// Name Edit View Object
	EditText nameET;
	// Email Edit View Object
	EditText emailET;
	// Password Edit View Object
	EditText pwdET;
    // Admin No view obj
    EditText adminET;
    //Phone No view obj
    EditText phnET;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

        phnET = (EditText) findViewById(R.id.register_phone);
        if(FetchPhoneNo() != null) {
            phnET.setText(FetchPhoneNo());
        }
		// Find Error Msg Text View control by ID
		errorMsg = (TextView)findViewById(R.id.register_error);
		// Find Name Edit View control by ID
		nameET = (EditText)findViewById(R.id.registerName);
		// Find Email Edit View control by ID
		emailET = (EditText)findViewById(R.id.registerEmail);
		// Find Password Edit View control by ID
		pwdET = (EditText)findViewById(R.id.registerPassword);
        //Find adminno Edit View control by ID
        adminET=(EditText)findViewById(R.id.register_admin);

		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
	}

    /*

    to fetch phone number

     */
    protected String FetchPhoneNo() {
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPN = tmgr.getLine1Number();
        //Log.d("phone number", mPN);

        return mPN;
    }
	/**
	 * Method gets triggered when Register button is clicked
	 *
	 * @param view
	 */
	public void registerUser(View view){
		// Get NAme ET control value
		String name = nameET.getText().toString();
		// Get Email ET control value
		String email = emailET.getText().toString();
		// Get Password ET control value
		String password = pwdET.getText().toString();
        //Get adminNo ET controll value
        String admin=adminET.getText().toString();
        //Get phone number ET control value
        String phn = phnET.getText().toString();
		// Instantiate Http Request Param Object
		 RequestParams params = new RequestParams();
		// When Name Edit View, Email Edit View and Password Edit View have values other than Null
		if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)){
			// When Email entered is Valid
			if(Utility.validate(email)){
				// Put Http parameter name with value of Name Edit View control
				params.put("username", name);
				// Put Http parameter username with value of Email Edit View control
				params.put("emailId", email);
				// Put Http parameter password with value of Password Edit View control
				params.put("password", password);
                // put Http parameter adminNo with value of admin Edit View control
                params.put("adminPhone",admin);
                // put Http parameter phoneNo
                params.put("phoneNo", phn);
				// Invoke RESTful Web Service with Http parameters
				invokeWS(params);
			}
			// When Email is invalid
			else{
				Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
			}
		} 
		// When any of the Edit View control left blank
		else{
			Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
		}
		
	}
	
	/**
	 * Method that performs RESTful webservice invocations
	 * 
	 * @param params
	 */
	public void invokeWS(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://a06cbb35.ngrok.io/user/add",params ,new AsyncHttpResponseHandler() {
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
                        	 // Set Default Values for Edit View controls
                        	 setDefaultValues();
                        	 // Display successfully registered message using Toast
                        	 //Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                             Intent intent=new Intent(getApplicationContext(),Tab_mainActivity.class);
                             startActivity(intent);
                        }
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
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
	
	/**
	 * Method which navigates from Register Activity to Login Activity
	 */
	public void navigatetoLoginActivity(View view){
		Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
		// Clears History of Activity
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);
	}
	
	/**
	 * Set degault values for Edit View controls
	 */
	public void setDefaultValues(){
		nameET.setText("");
		emailET.setText("");
		pwdET.setText("");
        adminET.setText("");

	}
	
}
