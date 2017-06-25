package com.prgguru.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
 * Created by PinkalJay on 4/18/15.
 */
public class User_List_activity extends Activity {
    ListView mListView;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;
    java.util.List<User> userlist = new ArrayList<User>();
    private UserListAdapter userListAdpater;
    //JSON Node Names
    private static final String TAG_OS = "android";
    private static final String TAG_VER = "ver";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "api";
    JSONArray android = null;
    String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(User_List_activity.class.getName(), "onCreate called ");
        setContentView(R.layout.list_main);
        oslist = new ArrayList<HashMap<String, String>>();
        //i Btngetdata = (Button)findViewById(R.id.getdata);
        mListView = (ListView) findViewById(R.id.userListView);
        userListAdpater = new UserListAdapter(userlist, getApplicationContext());
        mListView.setAdapter(userListAdpater);
        errorMsg = (TextView) findViewById(R.id.txtMsg);
        userlist.clear();
        RequestParams params = new RequestParams();
        params.put("phoneNo", "9162345555");
        invokeWS(params);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = (User) parent.getItemAtPosition(position);
                //getting values from selected listitem
                // String val= ((TextViewaaaaaa)view.findViewById(R.id.name)).getText().toString();
                Intent intent=new Intent(view.getContext(), Edit_activity.class);

                intent.putExtra("USEROBJECT",selectedUser);
                // getIntent().getSerializableExtra(selectedUser);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void invokeWS(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://a06cbb35.ngrok.io/list/familyUsers", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    UserResponse userResponse = new UserResponse();
                    JSONArray arr = obj.getJSONArray("result");
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObject = arr.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        String phonenumber = jsonObject.getString("Phone No");
                        User user = new User(username, phonenumber);
                        userlist.add(user);
                        //Display list item statilcally
                        //strBuilder.append(user.getUsername() +  " " + user.getPhoneNum()).append("\n");
                    }
                    userListAdpater.updateUserList(userlist);
                    errorMsg.setText(strBuilder.toString());
                    if (obj.getBoolean("status")) {
                        Log.i("User list size :", "" + userlist.size());
                        //Toast.makeText(getApplicationContext(), "Json String fetched!" + obj.getString("result"), Toast.LENGTH_LONG).show();
                    }
                    /* Else display error message */

                    else {
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
                //prgDialog.hide();
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
}
