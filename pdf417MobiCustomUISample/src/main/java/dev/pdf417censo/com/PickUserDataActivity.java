package dev.pdf417censo.com;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kelin.translucentbar.library.TranslucentBarManager;
import com.liyu.sqlitetoexcel.SQLiteToExcel;

import org.apache.poi.ss.formula.functions.T;

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
            acCivilState,
            acDocumentType;

    private Persona objPersona;
    private Toolbar toolbar;
    AlertDialog dialogPermission;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_user_data);

        // If you have access to the external storage, do whatever you need
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                dialogStoragePermission();
            }
        }

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

        //Campo: TIPO DOCUMENTO
        String[] arrayDocumentTypes ={"C.C", "T.I", "R.C"};

        ArrayAdapter<String> adapterDocumentType;
        acDocumentType = findViewById(R.id.acDocumentType);



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
                "N/A",
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
                "N/A",
                "Agricultor",
                "Ama de casa",
                "Estudiante",
                "Comerciante",
                "Contrucción",
                "Docente",
                "Empleado"};

        String[] arrayProfessionM ={
                "N/A",
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
                "N/A",
                "Soltero",
                "Casado",
                "Unión libre",
                "Divorciado",
                "Viudo"};

        String[] arrayCivilStateF ={
                "N/A",
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


        if(objPersona != null){
            adapterDocumentType = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayDocumentTypes);
            acDocumentType.setAdapter(adapterDocumentType);

            int age = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                age = objPersona.calculateAgeYear();

                if(age >= 18){
                    acDocumentType.setText(acDocumentType.getAdapter().getItem(0).toString(), false);
                }
                if(age >= 7 && age < 18){
                    acDocumentType.setText(acDocumentType.getAdapter().getItem(1).toString(), false);
                }
                if(age < 7){
                    acDocumentType.setText(acDocumentType.getAdapter().getItem(2).toString(), false);

                    acCivilState.setText(acCivilState.getAdapter().getItem(0).toString(), false);
                    acScholarship.setText(acScholarship.getAdapter().getItem(0).toString(), false);
                    acProfession.setText(acProfession.getAdapter().getItem(0).toString(), false);
                    acCivilState.setText(acCivilState.getAdapter().getItem(0).toString(), false);

                }
            }
        }

        Button button = (Button) findViewById(R.id.btnSaveUserData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String relationShip = acRelationship.getText().toString();
                String  scholarShip = acScholarship.getText().toString();
                String  profession = acProfession.getText().toString();
                String  civilState = acCivilState.getText().toString();

                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()){
                        dialogStoragePermission();
                    }else{
                        if(relationShip.isEmpty() || scholarShip.isEmpty() ||
                                profession.isEmpty() || civilState.isEmpty()){
                            emptyFields();
                        }else{
                            confirmationDialog();

                        }
                    }
                }


            }
        });

    }

    public void confirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogTitle);

        //Datos ingresados
        String documentType = acDocumentType.getText().toString();
        String relationShip = acRelationship.getText().toString();
        String scholarShip = acScholarship.getText().toString();
        String profession = acProfession.getText().toString();
        String civilState = acCivilState.getText().toString();

        String message =
                "Tipo documento: "+ documentType + "\n" +
                "Parentesco: " + relationShip + "\n" +
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
                            if (SDK_INT >= Build.VERSION_CODES.O) {
                                saveInfo();
                            }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveInfo() throws IOException {

        SharedPreferences prefe=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=prefe.edit();

        objPersona.setValidity(currentYearDate());
        objPersona.setGuard("1131");

        if(prefe.contains("community")){
            objPersona.setCommunity(prefe.getString("community", ""));
        }
        if(prefe.contains("sidewalk")){
            objPersona.setSidewalk(prefe.getString("sidewalk", ""));
        }
        if(prefe.contains("familyRecord")){
            objPersona.setFamilyRecord(String.valueOf(prefe.getInt("familyRecord", 0)));
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


        objPersona.getDocumentNumber().replaceFirst("^0*", "");

        if (objPersona.getMiddleName() == null || objPersona.getMiddleName().isEmpty()) {
            objPersona.setNames(objPersona.getFirstName());
        } else {
            objPersona.setNames(objPersona.getFirstName() + " " + objPersona.getMiddleName());
        }

        if (objPersona.getSecondLastName() == null || objPersona.getSecondLastName().isEmpty()) {
            objPersona.setSurnames(objPersona.getLastName());
        } else {
            objPersona.setSurnames(objPersona.getLastName() + " " + objPersona.getSecondLastName());
        }

        objPersona.setBirthdayFull(objPersona.getBirthdayDay() + "/" + objPersona.getBirthdayMonth() + "/" + objPersona.getBirthdayYear());

        //Campos personales
        String documentType = acDocumentType.getText().toString();
        String relationShip = acRelationship.getText().toString();
        String scholarShip = acScholarship.getText().toString();
        String profession = acProfession.getText().toString();
        String civilState = acCivilState.getText().toString();

        objPersona.setDocumentType(documentType);
        objPersona.setProfession(profession);
        objPersona.setScholarship(scholarShip);
        objPersona.setCivilStatus(civilState);
        objPersona.setRelationship(relationShip);
        if (SDK_INT >= Build.VERSION_CODES.O) {
            objPersona.setAge(objPersona.calculateAge());
        }

        //Guardar objeto persona en base de datos
        PersonasDbHelper conn = new PersonasDbHelper(this);
        long idRes = conn.savePersona(objPersona);
        conn.close();

        if(idRes > 0) {
            Toast.makeText(this, "Se guardó correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }

        File localStorage = getExternalFilesDir(null);
        if (localStorage == null) {
            return;
        }
        String storagePath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));

        new SQLiteToExcel
                .Builder(this)
                .setDataBase(this.getDatabasePath("Encuestados.db").getAbsolutePath())
                .setSQL("select " +
                        "validity as 'VIGENCIA'," +
                        "guard as 'RESGUARDO INDIGENA'," +
                        "community as 'COMUNIDAD INDIGENA'," +
                        "familyRecord as 'FICHA FAMILIAR'," +
                        "documentType as 'TIPO DOCUMENTO'," +
                        "documentNumber as 'NUMERO DE DOCUMENTO'," +
                        "names as 'NOMBRES'," +
                        "surnames as 'APELLIDOS'," +
                        "birthdayFull as 'FECHA DE NACIMIENTO'," +
                        "relationship as 'PARENTESCO'," +
                        "gender as 'SEXO'," +
                        "civilStatus as 'ESTADO CIVIL'," +
                        "profession as 'PROFESION'," +
                        "scholarship as 'ESCOLARIDAD'," +
                        "membersFamily as 'INTEGRANTES'," +
                        "sideWalk as 'DIRECCIÓN'," +
                        "phone as 'TELEFONO'," +
                        "user as 'USUARIO'," +
                        "age as 'EDAD'" +
                        " from persona")

                .setOutputPath(storagePath)
                .setOutputFileName("CensoJambalo2023.xls")
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
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void requestStoragePermission () {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        dialogPermission.dismiss();
    }

    public void dialogStoragePermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiso de almacenamiento");

        builder.setMessage("Es necesario habilitar el permiso de almacenamiento para poder guardar el Documento Excel.");
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (SDK_INT >= Build.VERSION_CODES.R) {
                            requestStoragePermission ();
                        }
                    }
                });
        dialogPermission = builder.create();
        dialogPermission.show();
    }


    @Override
    public void onBackPressed() {}
}