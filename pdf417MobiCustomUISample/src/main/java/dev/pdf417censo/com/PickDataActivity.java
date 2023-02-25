package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.pdf417censo.com.databinding.ActivityMainBinding;
import kotlin.LateinitKt;

public class PickDataActivity extends AppCompatActivity {

    AutoCompleteTextView textViewCommunity, acRelationship;


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
        String[] arrayRelationshipF ={"Madre cabeza de hogar","Abuela","Hija","Prima","Tía", "Nieta", "Sobrina" };
        String[] arrayRelationshipM ={"Padre cabeza de hogar","Abuelo","Hijo","Primo","Tío", "Nieto", "Sobrino" };

        ArrayAdapter<String> adapter;
        acRelationship = findViewById(R.id.acRelationship);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayRelationshipM);
        acRelationship.setAdapter(adapter);


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
