package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_animated);

        SharedPreferences prefe = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        if(!prefe.getString("user","").isEmpty() && !prefe.getString("phone","").isEmpty()){
            Intent i = new Intent(SplashActivity.this, PickDataActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
        }
        finish();
    }
}