package dev.pdf417censo.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kelin.translucentbar.library.TranslucentBarManager;
import com.liyu.sqlitetoexcel.SQLiteToExcel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import dev.pdf417censo.com.data.Persona;
import dev.pdf417censo.com.data.PersonasDbHelper;
import dev.pdf417censo.com.data.TinyDB;

public class PickUserDataActivity extends AppCompatActivity {

    AutoCompleteTextView
            acRelationship,
            acScholarship,
            acProfession,
            acCivilState;

    private Persona objPersona;
    private Toolbar toolbar;
    List<ArrayList<String>> listRows = new ArrayList<ArrayList<String>>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_user_data);

        TranslucentBarManager translucentBarManager = new TranslucentBarManager(this);
        translucentBarManager.transparent(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvInfoPerson = findViewById(R.id.tvInfoPerson);

        if(getIntent().getSerializableExtra("objPersona") != null){
            objPersona = (Persona) getIntent().getSerializableExtra("objPersona");
            String fullName = objPersona.getFirstName() + " " + objPersona.getLastName();
            tvInfoPerson.setText(fullName);

        }

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


        if(objPersona != null && !objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "M")){
            adapterRelationship = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayRelationshipM);
            acRelationship.setAdapter(adapterRelationship);
        }
        if(objPersona != null && !objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "F")){
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

        if(objPersona != null && !objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "M")){
            adapterProfession = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayProfessionM);
            acProfession.setAdapter(adapterProfession);
        }
        if(objPersona != null && !objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "F")){
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

        if(objPersona != null && !objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "M")){
            adapterCivilState = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayCivilStateM);
            acCivilState.setAdapter(adapterCivilState);
        }

        if(objPersona != null && !objPersona.getGender().isEmpty() && Objects.equals(objPersona.getGender(), "F")) {
            adapterCivilState = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayCivilStateF);
            acCivilState.setAdapter(adapterCivilState);
        }

        Button button = (Button) findViewById(R.id.btnSaveUserData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String relationShip = acRelationship.getText().toString();
                String  scholarShip = acScholarship.getText().toString();
                String  profession = acProfession.getText().toString();
                String  civilState = acCivilState.getText().toString();

                if(relationShip.isEmpty() || scholarShip.isEmpty() ||
                        profession.isEmpty() || civilState.isEmpty()){
                    emptyFields();
                }else{
                    confirmationDialog();

                }
            }
        });

    }

    public void confirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogTitle);

        //Datos ingresados
        String relationShip = acRelationship.getText().toString();
        String  scholarShip = acScholarship.getText().toString();
        String  profession = acProfession.getText().toString();
        String  civilState = acCivilState.getText().toString();

        String message = "Parentesco: "+ relationShip + "\n" +
                "Escolaridad: " + scholarShip + "\n" +
                "Profesión: "+ profession + "\n" +
                "Estado civíl: "+ civilState + "\n";
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
                        try {
                            saveInfo();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    private void saveInfo() throws IOException {

        SharedPreferences prefe=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=prefe.edit();

        objPersona.setValidity(currentYearDate());

        if(prefe.contains("community")){
            objPersona.setCommunity(prefe.getString("community", ""));
        }
        if(prefe.contains("sidewalk")){
            objPersona.setSidewalk(prefe.getString("sidewalk", ""));
        }
        if(prefe.contains("membersFamilyCount")){
            objPersona.setMembersFamily(String.valueOf(prefe.getInt("membersFamilyCount", 0)));
        }
        if(prefe.contains("user")){
            objPersona.setUser(prefe.getString("user", ""));
        }
        if(prefe.contains("phone")){
            objPersona.setPhone(prefe.getString("phone", ""));
        }
        objPersona.setDocumentType("C.C");
        ArrayList<String> row = new ArrayList<>();

        objPersona.getDocumentNumber().replaceFirst("^0*", "");

        if (objPersona.getMiddleName().isEmpty()) {
            objPersona.setNames(objPersona.getFirstName());
        } else {
            objPersona.setNames(objPersona.getFirstName() + " " + objPersona.getMiddleName());
        }

        if (objPersona.getSecondLastName().isEmpty()) {
            objPersona.setSurnames(objPersona.getLastName());
        } else {
            objPersona.setSurnames(objPersona.getLastName() + " " + objPersona.getSecondLastName());
        }


        objPersona.setBirthdayFull(objPersona.getBirthdayDay() + "/" + objPersona.getBirthdayMonth() + "/" + objPersona.getBirthdayYear());
        row.add("HI");
        row.add(objPersona.getGender());

        row.add("AGRICULTOR");
        row.add("SE");
        row.add("1");
        row.add("VITOYO");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            row.add(objPersona.calculateAge());
        }

        listRows.add(row);




        //Guardar objeto persona en base de datos
        PersonasDbHelper conn = new PersonasDbHelper(this);
        long idRes = conn.savePersona(objPersona);
        conn.close();





        File localStorage = getExternalFilesDir(null);
        if (localStorage == null) {
            return;
        }
        String storagePath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
        String rootPath = storagePath + "/CensoJambalo2023";

        new SQLiteToExcel
                .Builder(this)
                .setDataBase(this.getDatabasePath("Encuestados.db").getAbsolutePath())
                .setSQL("select " +
                        "validity as 'VIGENCIA'," +
                        "community as 'COMUNIDAD'," +
                        "sideWalk as 'COMUNIDAD'," +
                        "documentType as 'TIPO DOCUMENTO'," +
                        " documentNumber as 'DOCUMENTO'," +
                        " surnames as 'APELLIDOS'," +
                        " names as 'NOMBRES'," +
                        " birthdayFull as 'FECHA DE NACIMIENTO'," +
                        " phone as 'TELEFONO'," +
                        " user as 'USUARIO'" +
                        " from persona")
                .setOutputPath(storagePath)
                .setOutputFileName("Encuestados.xls")
                .setTables("persona")
                .start(new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(PickUserDataActivity.this, "Exportando", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        Toast.makeText(PickUserDataActivity.this, "completado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(PickUserDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        int familyNucleusScanned = 0;

        if(prefe.contains("familyNucleusScanned")){
            familyNucleusScanned = prefe.getInt("familyNucleusScanned", 0) + 1;
            editor.putInt("familyNucleusScanned", familyNucleusScanned) ;
        }else{
            editor.putInt("familyNucleusScanned", familyNucleusScanned + 1) ;
        }
        editor.apply();

        Intent i = new Intent(PickUserDataActivity.this, ResultActivity.class);
        startActivity(i);
    }

    public String currentYearDate(){
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    @Override
    public void onBackPressed() {}
}