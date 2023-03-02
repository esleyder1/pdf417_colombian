package dev.pdf417censo.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import dev.pdf417censo.com.data.Persona;

public class PickDataActivity extends AppCompatActivity {

    AutoCompleteTextView textViewCommunity, acSidewalk;
    private NumberPicker npFamilyIntegrants;
    private int numberMembersFamily = 0;
    private String wordSidewalk = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_pick_data);



        Slider sliderMembersFamily = findViewById(R.id.sliderMembersFamily);

        //Campo: COMUNIDAD INDÍGENA
        String[] arrayCommunity={"Misak","Nasa"};
        ArrayAdapter<String> adapterCommunity;
        textViewCommunity = findViewById(R.id.acCommunity);
        adapterCommunity = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayCommunity);
        textViewCommunity.setAdapter(adapterCommunity);

        //Campo: VEREDAS - BARRIO

        String[] arrayAddresses ={
                "La Mina",
                "Guayope",
                "El Epiro",
                "Picacho",
                "La Palma",
                "Loma Redonda",
                "La Esperanza",
                "Barondillo",
                "Zolapa",
                "El Tablón" };
        ArrayAdapter<String> adapter;
        acSidewalk = findViewById(R.id.acAddress);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayAddresses);
        acSidewalk.setAdapter(adapter);

        acSidewalk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordSidewalk = acSidewalk.getText().toString();
                boolean found = Arrays.asList(arrayAddresses).contains(wordSidewalk);
                if(found){
                    View view = PickDataActivity.this.getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //INTEGRANTES DE LA FAMILIA
        sliderMembersFamily.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                numberMembersFamily = (int) value;

            }
        });


        Button button = (Button) findViewById(R.id.btnSaveForm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String community = textViewCommunity.getText().toString();
                String  sidewalk = acSidewalk.getText().toString();

                boolean found = Arrays.asList(arrayAddresses).contains(wordSidewalk);

                if(community.isEmpty() || sidewalk.isEmpty() || numberMembersFamily <= 0){
                    emptyFields();
                }else{
                    if(found){
                        confirmationDialog();
                    }else{
                        infoDialog();
                    }

                }


            }
        });

    }

    public void confirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogTitle);

        //Datos ingresados
        String community = textViewCommunity.getText().toString();
        String  sidewalk = acSidewalk.getText().toString();
        String membersFamily = String.valueOf(numberMembersFamily);

        String message = "Comunidad indígena: "+ community + "\n" +
                "Vereda / Barrio: " + sidewalk + "\n" +
                "Integrantes de la familia: "+ membersFamily;
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

                        Intent i = new Intent(PickDataActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
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

    public void infoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vereda / Barrio");

        //Datos ingresados

        String message = wordSidewalk + " no es el nombre de una vereda o barrio, verifique e intente nuevamente.";
        builder.setMessage(message);
        String negativeText = getString(android.R.string.ok);
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

    public void emptyFields() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error al guardar");

        //Datos ingresados

        String message = "Algunos campos están vacios, verifique e intente nuevamente.";
        builder.setMessage(message);
        String negativeText = getString(android.R.string.ok);
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

        String community = textViewCommunity.getText().toString();
        String  sidewalk = acSidewalk.getText().toString();

        SharedPreferences preferencias=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("community", community.toUpperCase());
        editor.putString("sidewalk", sidewalk);
        editor.putInt("membersFamilyCount", numberMembersFamily);
        editor.apply();

        Intent i = new Intent(PickDataActivity.this, MainActivity.class);
        startActivity(i);
    }

}
