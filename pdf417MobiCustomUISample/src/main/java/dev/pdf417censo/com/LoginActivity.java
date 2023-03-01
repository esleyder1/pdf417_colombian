package dev.pdf417censo.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {


    private TextInputEditText edUser;
    private TextInputEditText edPhone;
    private Button btnSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edUser= (TextInputEditText) findViewById(R.id.edUser);
        edPhone= (TextInputEditText) findViewById(R.id.edPhone);

        btnSaveData = findViewById(R.id.btnSaveData);






        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edUser.getText().toString().isEmpty() && !edPhone.getText().toString().isEmpty()){
                    saveInfo();
                }else{
                    Toast.makeText(LoginActivity.this, "Ingrese los datos correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void saveInfo() {
        SharedPreferences preferencias=getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("user", edUser.getText().toString().toUpperCase());
        editor.putString("phone", edPhone.getText().toString());
        editor.apply();

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
}