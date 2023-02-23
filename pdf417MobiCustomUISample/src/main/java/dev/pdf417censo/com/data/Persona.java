package dev.pdf417censo.com.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Persona implements Serializable {
    private String id;
    String documentNumber;
    String lastName;
    String secondLastName;
    String fisrtName;
    String middleName;
    String gender;
    String birthdayYear;
    String birthdayMonth;
    String birthdayDay;
    String municipalityCode;
    String departmentCode;
    String bloodType;
    String documentType;
    String phone;
    String user;


    public Persona() {
    }

    public Persona(String documentType, String documentNumber, String lastName, String secondLastName, String fisrtName, String middleName, String gender, String birthdayYear, String birthdayMonth, String birthdayDay, String municipalityCode, String departmentCode, String bloodType, String phone, String user) {
        this.id = UUID.randomUUID().toString();
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.fisrtName = fisrtName;
        this.middleName = middleName;
        this.gender = gender;
        this.birthdayYear = birthdayYear;
        this.birthdayMonth = birthdayMonth;
        this.birthdayDay = birthdayDay;
        this.municipalityCode = municipalityCode;
        this.departmentCode = departmentCode;
        this.bloodType = bloodType;
        this.phone = phone;
        this.user = user;
    }

    public Persona(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.ID));
        documentType = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.DOCUMENTTYPE));
        documentNumber = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.DOCUMENTNUMBER));
        lastName = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.LASTNAME));
        secondLastName = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.SECONDNAME));
        fisrtName = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.FIRSTNAME));
        middleName = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.MIDDLENAME));
        gender = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.GENDER));
        birthdayYear = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.BIRTHDAYYEAR));
        birthdayMonth = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.BIRTHDAYMONTH));
        birthdayDay = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.BIRTHDAYDAY));
        municipalityCode = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.MUNICIPALITYCODE));
        departmentCode = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.DEPARTMENTCODE));
        bloodType = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.BLOODTYPE));
        phone = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.PHONE));
        user = cursor.getString(cursor.getColumnIndex(PersonaContract.PersonaEntry.USER));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PersonaContract.PersonaEntry.ID, id);
        values.put(PersonaContract.PersonaEntry.DOCUMENTTYPE, documentType);
        values.put(PersonaContract.PersonaEntry.DOCUMENTNUMBER, documentNumber);
        values.put(PersonaContract.PersonaEntry.LASTNAME, lastName);
        values.put(PersonaContract.PersonaEntry.SECONDNAME, secondLastName);
        values.put(PersonaContract.PersonaEntry.FIRSTNAME, fisrtName);
        values.put(PersonaContract.PersonaEntry.MIDDLENAME, middleName);
        values.put(PersonaContract.PersonaEntry.GENDER, gender);
        values.put(PersonaContract.PersonaEntry.BIRTHDAYYEAR, birthdayYear);
        values.put(PersonaContract.PersonaEntry.BIRTHDAYMONTH, birthdayMonth);
        values.put(PersonaContract.PersonaEntry.BIRTHDAYDAY, birthdayDay);
        values.put(PersonaContract.PersonaEntry.MUNICIPALITYCODE, municipalityCode);
        values.put(PersonaContract.PersonaEntry.DEPARTMENTCODE, departmentCode);
        values.put(PersonaContract.PersonaEntry.BLOODTYPE, bloodType);
        values.put(PersonaContract.PersonaEntry.PHONE, phone);
        values.put(PersonaContract.PersonaEntry.USER, user);

        return values;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(String birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(String birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calculateAge() {
        String age = "";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String birthdayFull = birthdayYear + "/" + birthdayMonth + "/" + birthdayDay;
        LocalDate birthDate = LocalDate.parse(birthdayFull, df);
        if ((birthDate != null) && (LocalDate.now() != null)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                age = String.valueOf(Period.between(birthDate, LocalDate.now()).getYears());
            }
        }
        return age;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String documentType() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String birthdayFull = birthdayYear + "/" + birthdayMonth + "/" + birthdayDay;
        LocalDate birthDate = LocalDate.parse(birthdayFull, df);
        if ((birthDate != null) && (LocalDate.now() != null)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                if(age > 18) {
                    this.setDocumentType("C.C");
                }else{
                    this.setDocumentType("T.I");
                }
            }
        }
        return this.getDocumentType();
    }

}