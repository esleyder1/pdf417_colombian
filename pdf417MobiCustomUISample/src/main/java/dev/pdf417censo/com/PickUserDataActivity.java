package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import dev.pdf417censo.com.data.Persona;

public class PickUserDataActivity extends AppCompatActivity {

    AutoCompleteTextView
            acRelationship,
            acScholarship,
            acProfession,
            acCivilState;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_user_data);

        TextView tvInfoPerson = findViewById(R.id.tvInfoPerson);

        Persona objPersona = (Persona) getIntent().getSerializableExtra("objPersona");
        Toast.makeText(this, objPersona.getFirstName(), Toast.LENGTH_SHORT).show();

        String fullName = objPersona.getFirstName() + " " + objPersona.getLastName();
        tvInfoPerson.setText(fullName);

        //Campo: PARENTESCO
        String[] arrayRelationshipF ={
                "Madre cabeza de hogar",
                "Abuela",
                "Hija",
                "Prima",
                "Tía",
                "Nieta",
                "Sobrina"
        };

        String[] arrayRelationshipM ={
                "Padre cabeza de hogar",
                "Abuelo",
                "Hijo",
                "Primo",
                "Tío",
                "Nieto",
                "Sobrino"
        };

        ArrayAdapter<String> adapterRelationship;
        acRelationship = findViewById(R.id.acRelationship);

        if(!objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "M")){
            adapterRelationship = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayRelationshipM);
            acRelationship.setAdapter(adapterRelationship);
        }
        if(!objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "F")){
            adapterRelationship = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayRelationshipF);
            acRelationship.setAdapter(adapterRelationship);
        }





        //Campo: ESCOLARIDAD

        String[] arrayScholarship ={
                "Ninguno",
                "Primaria",
                "Secundaria",
                "Técnico",
                "Tecnologo",
                "Profesional",
                "Licenciado" };

        ArrayAdapter<String> adapterScholarship;
        acScholarship = findViewById(R.id.acScholarship);
        adapterScholarship = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayScholarship);
        acScholarship.setAdapter(adapterScholarship);

        //Campo: PROFESIÓN
        String[] arrayProfessionF ={
                "Ninguno",
                "Agricultor",
                "Ama de casa",
                "Estudiante",
                "Comerciante",
                "Contrucción",
                "Docente",
                "Empleado"};

        String[] arrayProfessionM ={
                "Ninguno",
                "Agricultor",
                "Estudiante",
                "Comerciante",
                "Contrucción",
                "Docente",
                "Empleado"};



        ArrayAdapter<String> adapterProfession;
        acProfession = findViewById(R.id.acProfession);


        if(!objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "M")){
            adapterProfession = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayProfessionM);
            acProfession.setAdapter(adapterProfession);
        }

        if(!objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "F")){
            adapterProfession = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayProfessionF);
            acProfession.setAdapter(adapterProfession);
        }

        //Campo: ESTADO CIVIL
        String[] arrayCivilStateM ={
                "Soltero",
                "Casado",
                "Unión libre",
                "Divorciado",
                "Viudo"};

        String[] arrayCivilStateF ={
                "Soltera",
                "Casada",
                "Unión libre",
                "Divorciada",
                "Viuda"};

        ArrayAdapter<String> adapterCivilState;
        acCivilState = findViewById(R.id.acCivilState);

        if(!objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "M")){
            adapterCivilState = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayCivilStateM);
            acCivilState.setAdapter(adapterCivilState);
        }

        if(!objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "F")) {
            adapterCivilState = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayCivilStateF);
            acCivilState.setAdapter(adapterCivilState);
        }

    }

    @Override
    public void onBackPressed() {}
}