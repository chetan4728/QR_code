package com.project.app.qr_code;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.app.qr_code.Utility.API;
import com.project.app.qr_code.Utility.AppSharedPreferences;
import com.project.app.qr_code.Utility.VolllyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    EditText username,password;
    ImageView login;
    AppSharedPreferences app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (ImageView)findViewById(R.id.login);
        app =  new AppSharedPreferences(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(LoginScreen.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                }
                else
                if(password.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(LoginScreen.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final ProgressDialog pDialog = new ProgressDialog(LoginScreen.this);

                    pDialog.setMessage("Logging in ...");
                    pDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGIN,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("detail", String.valueOf(response));
                                    try {
                                        JSONObject obj = new JSONObject(response);


                                        if(obj.getString("status").equals("200")) {

                                            //Toasty.success(getApplicationContext(), "Login Successful ", Toast.LENGTH_SHORT, true).show();


                                            JSONObject jsonArray = obj.getJSONObject("user_data");


                                            pDialog.hide();
                                            Log.i("detail", String.valueOf(jsonArray.getString("username")));
                                            app.editor.putString(AppSharedPreferences.FirstName,jsonArray.getString("username"));
                                            app.editor.putString(AppSharedPreferences.mast_id,jsonArray.getString("qr_user_id"));
                                            app.editor.putString(AppSharedPreferences.USER_TYPE,jsonArray.getString("qr_scanner_type"));


                                            app.editor.commit();



                                           if(jsonArray.getString("qr_scanner_type").equals("seller")) {
                                               Intent i = new Intent(LoginScreen.this, ScanSeller.class);
                                               i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(i);
                                               finish();
                                           }
                                           else  if(jsonArray.getString("qr_scanner_type").equals("checker"))
                                           {
                                               Intent i = new Intent(LoginScreen.this, ScanQr.class);
                                               i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(i);
                                               finish();
                                           }
                                        }


                                        else
                                        {
                                            Toast.makeText(LoginScreen.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                                        }

                                        pDialog.hide();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    //  Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            params.put("username", String.valueOf(username.getText()));
                            params.put("password", String.valueOf(password.getText()));
                            return params;
                        }
                    };
                    VolllyRequest.getInstance(LoginScreen.this).addToRequestQueue(stringRequest);
                }

            }
        });
    }
}
