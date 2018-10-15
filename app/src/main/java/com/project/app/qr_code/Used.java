package com.project.app.qr_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.app.qr_code.Utility.AppSharedPreferences;

public class Used extends AppCompatActivity {
    AppSharedPreferences app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used);
        app =  new AppSharedPreferences(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(app.pref.getString(app.USER_TYPE,"").equals("seller")) {
            Intent i = new Intent(Used.this, ScanSeller.class);
            startActivity(i);
            finish();
        }
        else  if(app.pref.getString(app.USER_TYPE,"").equals("checker"))
        {
            Intent i = new Intent(Used.this, ScanQr.class);
            startActivity(i);
            finish();
        }
    }
}
