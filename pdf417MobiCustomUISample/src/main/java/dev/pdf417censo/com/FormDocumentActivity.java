package dev.pdf417censo.com;

import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kelin.translucentbar.library.TranslucentBarManager;
import com.liyu.sqlitetoexcel.SQLiteToExcel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import dev.pdf417censo.com.data.Persona;
import dev.pdf417censo.com.data.PersonasDbHelper;

public class FormDocumentActivity extends AppCompatActivity {


    private Persona objPersona;
    private Toolbar toolbar;
    AlertDialog dialogPermission;
    private RadioGroup rgSex;
    private TextInputEditText edDocument, edNames, edSurnames, edBirthday;
    final Calendar myCalendar= Calendar.getInstance();

    private Button btnSaveFormDocument;

    private String strSex;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_document);

        TranslucentBarManager translucentBarManager = new TranslucentBarManager(this);
        translucentBarManager.transparent(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        rgSex = findViewById(R.id.rgSex);

        edDocument = findViewById(R.id.edDocument);
        edNames = findViewById(R.id.edNames);
        edSurnames = findViewById(R.id.edSurnames);
        edBirthday= findViewById(R.id.edBirthday);

        btnSaveFormDocument = findViewById(R.id.btnSaveFormDocument);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        edBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FormDocumentActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //radio button birthday
        btnSaveFormDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String document = Objects.requireNonNull(edDocument.getText().toString().trim());
                String  names = Objects.requireNonNull(edNames.getText()).toString().trim();
                String  surnames = Objects.requireNonNull(edSurnames.getText()).toString().trim();
                String  birthday = Objects.requireNonNull(edBirthday.getText()).toString().trim();

                if(document.isEmpty() || names.isEmpty() || surnames.isEmpty() || birthday.isEmpty()){
                    emptyFields();
                }else{
                    saveInfo();
                }

            }
        });

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

    @SuppressLint("SetTextI18n")
    private void saveInfo() {
        String document = Objects.requireNonNull(edDocument.getText().toString().trim());
        String  names = Objects.requireNonNull(edNames.getText()).toString().toUpperCase().trim();
        String  surnames = Objects.requireNonNull(edSurnames.getText()).toString().toUpperCase().trim();
        String  birthday = Objects.requireNonNull(edBirthday.getText()).toString().trim();


        int selectedBtnId =
                rgSex.getCheckedRadioButtonId();

        if (selectedBtnId == -1) {

        }
        else{
            MaterialRadioButton selectedRadioBtn =
                    findViewById(selectedBtnId);

            if(selectedRadioBtn.getText().equals("Masculino")){
                strSex = "M";
            }else{
                strSex = "F";
            }
        }

        String lastName, secondLastName = null, firstName, middleName = null;

        //apellidos
        String[] surnamesSplited = surnames.split("\\s+");
        lastName = surnamesSplited[0];
        if(surnamesSplited.length > 1){
            secondLastName = surnamesSplited[1];
        }
        //nombres
        String[] namesSplited = names.split("\\s+");
        firstName = namesSplited[0];
        if(namesSplited.length > 1){
            middleName = namesSplited[1];
        }

        String[] birthdaySplited = birthday.split("/");
        String birthdayYear, birthdayMonth, birthdayDay;
        birthdayYear = birthdaySplited[2];
        birthdayMonth = birthdaySplited[1];
        birthdayDay = birthdaySplited[0];

        Persona p = new Persona("",document, lastName, secondLastName, firstName
                , middleName, strSex, birthdayYear, birthdayMonth, birthdayDay,
                "", "", "","","");

        //Guardo el objeto persona en un arreglo que persistirá en tiny

        Intent i = new Intent(FormDocumentActivity.this, PickUserDataActivity.class);
        i.putExtra("objPersona", p);
        startActivity(i);
        finish();
        //createExcel(
    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edBirthday.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}