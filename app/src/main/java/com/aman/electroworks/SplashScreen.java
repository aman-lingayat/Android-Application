package com.aman.electroworks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aman.electroworks.constants.SessionManager;

public class SplashScreen extends AppCompatActivity {
    int SPLASH_TIME_OUT = 3000;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sessionManager = new SessionManager(getApplicationContext());

        new Handler().postDelayed(() -> {
//                Intent i=new Intent(SplashScreen.this,Login.class);
//                startActivity(i);
//                finish();
            if(sessionManager.isLoggedIn()){
                Intent i=new Intent(SplashScreen.this,UploadData.class);
                startActivity(i);
                finish();
            }
            else{
                Intent i=new Intent(SplashScreen.this,Login.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}