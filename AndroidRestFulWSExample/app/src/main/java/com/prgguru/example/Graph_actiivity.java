package com.prgguru.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PinkalJay on 5/6/15.
 */
public class Graph_actiivity extends Activity {

    //for billing date
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    TextView billingdate;
    String adminno="12313";

    //for graph
    String phoneno="";
    String startdate="";
    String endDate="";

    private ProgressBar pb;
    int billing_cycle = 30;
    int data_usage = 2;
    int threshold = 9;
    int max = 10;
    private static String URL = "http://a06cbb35.ngrok.io/dataUsage/dataRange?phoneNo=";
    private static String URL1="&startDate=";
    private static String URL2="&endDate=";
    private static String PHONE_NUM = "";

    //{"totalData":65857252,"resultData":{"2":40,"3":6,"4":65857206,"7":0},"usersQuota":32141,"NoOfData":4,"usersThreshold":323423,"status":true}
    private static final String STARTDATE="2015-05-02";
    private static final String ENDDATE="2015-05-30";

    private static final String DATA_USED = "totalData";
    private static final String RESULT = "resultData";
    private static final String STATUS = "status";
    ArrayList<HashMap<String, String>> dataMap;
    JSONArray data;
    float dataUsed;
    TextView optionalTextBox;

    //Display username and phoneNo
    private User user = null;
    private TextView userName, userPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        //for billing cycle;
        billingdate=(TextView)findViewById(R.id.txtbillingdate);

        optionalTextBox = (TextView)findViewById(R.id.optionalTextView);
        user = (User) getIntent().getSerializableExtra("USEROBJECT");
        PHONE_NUM = user.getPhoneNum();

        //pi
        String STARTDATE="2015-04-23";
        String ENDDATE="2015-05-02";

        userName = (TextView) findViewById(R.id.txtunm);
        userPhoneNum = (TextView) findViewById(R.id.txtphno);



        userName.setText(user.getUsername());
        userPhoneNum.setText(user.getPhoneNum());
        Log.i("before","it's working");
        Async thread = new Async();
        thread.execute();

        //for billing date
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Put Http parameter username with value of Email Edit View control
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        params.put("adminNo", adminno);
        // Invoke RESTful Web Service with Http parameters
        invokeWS(params);
    }

    public class Async extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void...params){
            try {
                //pi changed +start and end date
                String data = getData(Graph_actiivity.URL+Graph_actiivity.PHONE_NUM+Graph_actiivity.URL1+Graph_actiivity.STARTDATE+Graph_actiivity.URL2+Graph_actiivity.ENDDATE);
                parseData(data);
                Log.i("dataUsed",String.valueOf(dataUsed));
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //For grapgh
            Long[] Data = new Long[billing_cycle];

            for (int i = 0; i < billing_cycle; i++) {
                Data[i] = Long.valueOf(i * 2);

            }

            //plotting graph
            GraphView graph = (GraphView) findViewById(R.id.graph);
            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(0, Data[0]),

                    new DataPoint(1,dataUsed)
            });
            graph.addSeries(series);
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return Color.rgb(0, 50, 50);
                }
            });

            series.setSpacing(50);

            // draw values on top
            series.setDrawValuesOnTop(false);
            //series.setValuesOnTopColor(Color.RED);

            TextView view_usage_disp = (TextView)findViewById(R.id.Txt_view_usage_disp);
            view_usage_disp.setText(Double.toString(data_usage));
            //Displaying usage on progress bar
            pb=(ProgressBar)findViewById(R.id.progress_bar);

            pb.setProgress(data_usage);pb.setMax(max);
            pb.setSecondaryProgress(threshold);
        }


    }

    private String getData(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500000;

        try {

            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Response", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void parseData(String jsonStr){
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Log.i("jsonObj",String.valueOf(jsonObj));
                String status = jsonObj.getString(STATUS);
                if(status.equals("true")) {

                    data = jsonObj.getJSONArray(RESULT);
                    Log.i("data", String.valueOf(data));
                    // Getting JSON Array node
                    JSONObject obj = data.getJSONObject(0);
                    Log.i("obj", String.valueOf(obj));
                    String dataUsage = obj.getString(DATA_USED);
                    dataUsed = Float.parseFloat(dataUsage);
                }
                else if(status.equals("false")){

                    optionalTextBox.setVisibility(TextView.VISIBLE);


                }
                // looping through All Contacts
               /* for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String email = c.getString(TAG_EMAIL);
                    String address = c.getString(TAG_ADDRESS);
                    String gender = c.getString(TAG_GENDER);

                    // Phone node is JSON Object
                    JSONObject phone = c.getJSONObject(TAG_PHONE);
                    String mobile = phone.getString(TAG_PHONE_MOBILE);
                    String home = phone.getString(TAG_PHONE_HOME);
                    String office = phone.getString(TAG_PHONE_OFFICE);

                    // tmp hashmap for single contact
                    HashMap<String, String> contact = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    contact.put(TAG_ID, id);
                    contact.put(TAG_NAME, name);
                    contact.put(TAG_EMAIL, email);
                    contact.put(TAG_PHONE_MOBILE, mobile);

                    // adding contact to contact list
                    contactList.add(contact);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }


    }
    public void invokeWS(RequestParams params){

        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient objectvk
        AsyncHttpClient client = new AsyncHttpClient();
        //http://localhost:8055/login/doLogin?phoneNo=123&password=axc
        client.get("http://7f67eb70.ngrok.io/manageQuota/getBillingCycleDate",params ,new AsyncHttpResponseHandler() {
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
                        //for billing date...printing it
                        billingdate.setText(obj.toString());
                       // Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                       // Intent intent=new Intent(getApplicationContext(),Tab_mainActivity.class);
                       // startActivity(intent);
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

}