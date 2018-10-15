package com.project.app.qr_code;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.project.app.qr_code.Utility.API;
import com.project.app.qr_code.Utility.AppSharedPreferences;
import com.project.app.qr_code.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanQr extends AppCompatActivity  implements ZXingScannerView.ResultHandler{
    private Button button;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private   ZXingScannerView zXingScannerView;
    AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        appSharedPreferences =  new AppSharedPreferences(this);
        button = (Button) findViewById(R.id.button_start_scan);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                zXingScannerView =new ZXingScannerView(getApplicationContext());
                setContentView(zXingScannerView);

                zXingScannerView.setMinimumWidth(20);
                zXingScannerView.setResultHandler((ZXingScannerView.ResultHandler) ScanQr.this);


                zXingScannerView.startCamera();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.startCamera();

    }

    @Override
    public void handleResult(Result result) {

        zXingScannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) ScanQr.this);
        zXingScannerView.stopCamera();
        scancode(result.getText());

    }

  public void scancode(final String text)
  {
      final ProgressDialog pDialog = new ProgressDialog(ScanQr.this);

      pDialog.setMessage("Checking in ...");
      pDialog.show();

      StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SCAN,
              new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                      Log.i("detail", String.valueOf(response));
                      try {
                          JSONObject obj = new JSONObject(response);


                          if(obj.getString("status").equals("200")) {

                              //Toasty.success(getApplicationContext(), "Login Successful ", Toast.LENGTH_SHORT, true).show();


                              JSONArray jsonArray = obj.getJSONArray("user_data");
                              JSONObject jsonArraycust = obj.getJSONObject("customer");
                             /// Toast.makeText(ScanQr.this, "Something Went Wrong"+jsonArray, Toast.LENGTH_SHORT).show();
                              pDialog.hide();
                              Intent i  =  new Intent(ScanQr.this,Success.class);
                              Bundle bundle =  new Bundle();
                              bundle.putString("name",jsonArraycust.getString("name"));
                              bundle.putString("code_type",jsonArray.getJSONObject(0).getString("qr_type"));
                              i.putExtras(bundle);
                              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                              startActivity(i);



                          }else if(obj.getString("status").equals("300")) {

                              Intent i  =  new Intent(ScanQr.this,Used.class);
                              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                              startActivity(i);

                          }


                          else
                          {
                              Toast.makeText(ScanQr.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

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
              params.put("user_id", String.valueOf(appSharedPreferences.pref.getString("mast_id","")));
              params.put("code", text);
              return params;
          }
      };
      VolllyRequest.getInstance(ScanQr.this).addToRequestQueue(stringRequest);
  }


    public void logout(View view) {
        appSharedPreferences.editor.putString(AppSharedPreferences.mast_id,"");
        appSharedPreferences.editor.putString(AppSharedPreferences.USER_TYPE,"");
        appSharedPreferences.editor.putString(AppSharedPreferences.FirstName,"");
        appSharedPreferences.editor.commit();
        Intent i  =  new Intent(ScanQr.this,LoginScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(appSharedPreferences.pref.getString(appSharedPreferences.USER_TYPE,"").equals("seller")) {
            Intent i = new Intent(ScanQr.this, ScanSeller.class);
            startActivity(i);
            finish();
        }
        else  if(appSharedPreferences.pref.getString(appSharedPreferences.USER_TYPE,"").equals("checker"))
        {
            Intent i = new Intent(ScanQr.this, ScanQr.class);
            startActivity(i);
            finish();
        }
    }
}


