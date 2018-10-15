package com.project.app.qr_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.app.qr_code.R;
import com.project.app.qr_code.Utility.AppSharedPreferences;

public class Success extends AppCompatActivity {
    AppSharedPreferences app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        app =  new AppSharedPreferences(this);
        TextView type = (TextView)findViewById(R.id.type);
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(getIntent().getExtras().getString("name"));
        type.setText("PASS TYPE "+getIntent().getExtras().getString("code_type"));

    }
    public void back(View view) {


        if(app.pref.getString(app.USER_TYPE,"").equals("seller")) {
            Intent i = new Intent(Success.this, ScanSeller.class);
            startActivity(i);
            finish();
        }
        else  if(app.pref.getString(app.USER_TYPE,"").equals("checker"))
        {
            Intent i = new Intent(Success.this, ScanQr.class);
            startActivity(i);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(app.pref.getString(app.USER_TYPE,"").equals("seller")) {
            Intent i = new Intent(Success.this, ScanSeller.class);
            startActivity(i);
            finish();
        }
        else  if(app.pref.getString(app.USER_TYPE,"").equals("checker"))
        {
            Intent i = new Intent(Success.this, ScanQr.class);
            startActivity(i);
            finish();
        }
    }
}
