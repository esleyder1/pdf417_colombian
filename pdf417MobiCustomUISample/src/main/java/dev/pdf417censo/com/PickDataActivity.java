package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

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

public class PickDataActivity extends AppCompatActivity {

    AutoCompleteTextView textViewCommunity, acRelationship;
    private NumberPicker picker1;
    private String[] pickerVals;

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
        acRelationship = findViewById(R.id.acRelationship);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayRelationshipM);
        acRelationship.setAdapter(adapter);*/

        //Campo: VEREDAS - BARRIO

        String[] arrayAddresses ={"La Mina","Guayope","El Epiro","Picacho", "La Palma", "Loma Redonda", "La Esperanza", "Barondillo", "Zolapa", "El Tablón" };
        ArrayAdapter<String> adapter;
        acRelationship = findViewById(R.id.acAddress);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayAddresses);
        acRelationship.setAdapter(adapter);


        //INTEGRANTES DE LA FAMILIA
        picker1 = findViewById(R.id.numberpicker_main_picker);
        picker1.setMaxValue(15);
        picker1.setMinValue(1);
        pickerVals  = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
        picker1.setDisplayedValues(pickerVals);

        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker1 = picker1.getValue();
                Log.d("picker value", pickerVals[valuePicker1]);
            }
        });


        Button button = (Button) findViewById(R.id.btnSaveForm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PickDataActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
