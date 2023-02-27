package dev.pdf417censo.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class PickDataActivity extends AppCompatActivity {

    AutoCompleteTextView textViewCommunity, acSidewalk;
    private NumberPicker npFamilyIntegrants;
    private String[] npMembersFamily;
    String[] arrayAddresses ={"La Mina","Guayope","El Epiro","Picacho", "La Palma", "Loma Redonda", "La Esperanza", "Barondillo", "Zolapa", "El Tablón" };
    boolean sidewalkError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_pick_data);

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

        ArrayAdapter<String> adapter;
        acSidewalk = findViewById(R.id.acAddress);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayAddresses);
        acSidewalk.setAdapter(adapter);

        //INTEGRANTES DE LA FAMILIA
        npFamilyIntegrants = findViewById(R.id.npFamilyIntegrants);
        npFamilyIntegrants.setMaxValue(15);
        npFamilyIntegrants.setMinValue(1);
        npMembersFamily  = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
        npFamilyIntegrants.setDisplayedValues(npMembersFamily);

        npFamilyIntegrants.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuenpFamilyIntegrants = npFamilyIntegrants.getValue();
                Log.d("picker value", npMembersFamily[valuenpFamilyIntegrants]);
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

                if(community.isEmpty() || sidewalk.isEmpty()){
                    Toast.makeText(PickDataActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
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
        String membersFamily = String.valueOf(npFamilyIntegrants.getValue());

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

}
