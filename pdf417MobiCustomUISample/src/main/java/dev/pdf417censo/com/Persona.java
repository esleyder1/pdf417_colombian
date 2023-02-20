package dev.pdf417censo.com;

import android.annotation.SuppressLint;
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

public class Persona implements Serializable {

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


    public Persona() {
    }

    public Persona(String documentNumber, String lastName, String secondLastName, String fisrtName, String middleName, String gender, String birthdayYear, String birthdayMonth, String birthdayDay, String municipalityCode, String departmentCode, String bloodType) {
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
                    this.documentType = "C.C";
                }else{
                    this.documentType = "T.I";
                }
            }
        }
        return this.documentType;
    }

}
