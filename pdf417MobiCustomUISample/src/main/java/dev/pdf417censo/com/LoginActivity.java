package dev.pdf417censo.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kelin.translucentbar.library.TranslucentBarManager;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private TextInputEditText edUser;
    private TextInputEditText edPhone;
    private Button btnSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        TranslucentBarManager translucentBarManager = new TranslucentBarManager(this);
        translucentBarManager.transparent(this);

        edUser= (TextInputEditText) findViewById(R.id.edUser);
        edPhone= (TextInputEditText) findViewById(R.id.edPhone);

        btnSaveData = findViewById(R.id.btnSaveData);


        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edUser.getText().toString().isEmpty() && !edPhone.getText().toString().isEmpty()){

                    confirmationDialog();
                }else{
                    Toast.makeText(LoginActivity.this, "Ingrese los datos correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void confirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogTitle);

        //Datos ingresados
        String user = Objects.requireNonNull(edUser.getText()).toString();
        String  phone = Objects.requireNonNull(edPhone.getText()).toString();

        String message = "Usuario: "+ user + "\n" +
                "Celular: " + phone;
        builder.setMessage(getString(R.string.dialogDescription));
        builder.setMessage(getString(R.string.dialogDescription));
        builder.setMessage(message);
        String positiveText = getString(R.string.btn_continue);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss alert dialog, update preferences with game score and restart play fragment
                        dialog.dismiss();
                        saveInfo();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialog, start counter again
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
// display dialog
        dialog.show();
    }

    private void saveInfo() {
        SharedPreferences prefe = getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("user", Objects.requireNonNull(edUser.getText()).toString().toUpperCase());
        editor.putString("phone", Objects.requireNonNull(edPhone.getText()).toString());
        editor.apply();



        Intent i = new Intent(LoginActivity.this, PickDataActivity.class);
        startActivity(i);
        finish();
    }
}