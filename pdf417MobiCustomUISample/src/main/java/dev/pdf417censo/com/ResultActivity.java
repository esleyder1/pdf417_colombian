package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView tvProgress;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvProgress = findViewById(R.id.tvProgress);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences prefe = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        int familyNucleusScanned = prefe.getInt("familyNucleusScanned", 0);
        int membersFamilyCount = prefe.getInt("membersFamilyCount", 0);
        if(membersFamilyCount > 0){
            tvProgress.setText(familyNucleusScanned +"/"+ membersFamilyCount);
        }else{
            tvProgress.setText("0");
        }



        Button button = (Button) findViewById(R.id.btnContinious);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(membersFamilyCount == familyNucleusScanned) {
                    SharedPreferences.Editor editor = prefe.edit();
                    editor.remove("membersFamilyCount");
                    editor.remove("familyNucleusScanned");
                    editor.apply();
                }
                if(familyNucleusScanned < membersFamilyCount) {
                    Intent i = new Intent(ResultActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(ResultActivity.this, PickDataActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        });
    }
}
