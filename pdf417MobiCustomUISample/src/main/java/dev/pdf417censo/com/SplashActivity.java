package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kelin.translucentbar.library.TranslucentBarManager;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_animated);

        TranslucentBarManager translucentBarManager = new TranslucentBarManager(this);
        translucentBarManager.transparent(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

            SharedPreferences prefe = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefe.edit();

            int familyNucleusScanned = prefe.getInt("familyNucleusScanned", 0);
            int membersFamilyCount = prefe.getInt("membersFamilyCount", 0);
            int familyRecord = prefe.getInt("familyRecord", 0);

            if(prefe.contains("membersFamilyCount") && prefe.contains("familyNucleusScanned") && familyNucleusScanned >= membersFamilyCount){
                editor.remove("membersFamilyCount");
                editor.remove("familyNucleusScanned");

                if(prefe.contains("familyRecord") && familyRecord > 1) {
                    editor.putInt("familyRecord", familyRecord + 1);
                    editor.apply();
                }
            }


        if(prefe.contains("user") && prefe.contains("phone") && !prefe.contains("membersFamilyCount")){
            Intent i = new Intent(SplashActivity.this, PickDataActivity.class);
            startActivity(i);
        }
        if(prefe.contains("user") && prefe.contains("phone") && prefe.contains("membersFamilyCount")){
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
        if(!prefe.contains("user") && !prefe.contains("phone")){
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
        }
        finish();
    }
        }, 3000);
    }
}