package dev.pdf417censo.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class PickDataActivity extends AppCompatActivity {

    AutoCompleteTextView textViewCommunity, acSidewalk;
    private NumberPicker npFamilyIntegrants;
    private int numberMembersFamily = 0;

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

        //Campo: PARENTESCO
/*        String[] arrayRelationshipF ={"Madre cabeza de hogar","Abuela","Hija","Prima","Tía", "Nieta", "Sobrina" };
        String[] arrayRelationshipM ={"Padre cabeza de hogar","Abuelo","Hijo","Primo","Tío", "Nieto", "Sobrino" };
        ArrayAdapter<String> adapter;
        acSidewalk = findViewById(R.id.acSidewalk);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayRelationshipM);
        acSidewalk.setAdapter(adapter);*/

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

                String word = acSidewalk.getText().toString();

                boolean found = Arrays.asList(arrayAddresses).contains(word);

                if(community.isEmpty() || sidewalk.isEmpty() || numberMembersFamily <= 0){
                    Toast.makeText(PickDataActivity.this, "Es necesario llenar todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    if(found){
                        Toast.makeText(PickDataActivity.this, "Encontrado", Toast.LENGTH_SHORT).show();
                        confirmationDialog();
                    }else{
                        Toast.makeText(PickDataActivity.this, "NO ncontrado", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PickDataActivity.this, "Continuar", Toast.LENGTH_SHORT).show();
                        Log.d("myTag", "positive button clicked");



                        dialog.dismiss();
                        Intent i = new Intent(PickDataActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialog, start counter again
                        dialog.dismiss();
                        Log.d("myTag", "negative button clicked");
                        Toast.makeText(PickDataActivity.this, "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
// display dialog
        dialog.show();
    }

    private void saveInfo() {
        SharedPreferences preferencias=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
/*        editor.putString("user", edUser.getText().toString().toUpperCase());
        editor.putString("phone", edPhone.getText().toString());
        editor.apply();*/

        Intent i = new Intent(PickDataActivity.this, MainActivity.class);
        startActivity(i);
    }

}
