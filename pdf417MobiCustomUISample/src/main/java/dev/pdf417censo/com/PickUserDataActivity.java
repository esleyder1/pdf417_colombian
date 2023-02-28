package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class PickUserDataActivity extends AppCompatActivity {

    AutoCompleteTextView
            acRelationship,
            acScholarship,
            acProfession,
            acCivilState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_user_data);

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
        adapterRelationship = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayRelationshipM);
        acRelationship.setAdapter(adapterRelationship);

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
        String[] arrayProfession ={
                "Ninguno",
                "Agricultor",
                "Ama de casa",
                "Estudiante",
                "Comerciante",
                "Contrucción",
                "Docente",
                "Empleado"};

        ArrayAdapter<String> adapterProfession;
        acProfession = findViewById(R.id.acProfession);
        adapterProfession = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayProfession);
        acProfession.setAdapter(adapterProfession);

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
        adapterCivilState = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayCivilStateM);
        acCivilState.setAdapter(adapterCivilState);

    }
}