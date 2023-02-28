package dev.pdf417censo.com.data;

import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PersonaContract {

    public static abstract class PersonaEntry implements BaseColumns{

        public static final String TABLE_NAME ="persona";

        public static final String ID = "id";
        public static final String DOCUMENTTYPE = "documentType";
        public static final String DOCUMENTNUMBER = "documentNumber";
        public static final String LASTNAME = "lastName";
        public static final String SECONDNAME = "secondLastName";
        public static final String SURNAMES = "surnames";
        public static final String FIRSTNAME = "fisrtName";
        public static final String MIDDLENAME = "middleName";
        public static final String NAMES = "names";
        public static final String GENDER = "gender";
        public static final String BIRTHDAYYEAR ="birthdayYear";
        public static final String BIRTHDAYMONTH = "birthdayMonth";
        public static final String BIRTHDAYDAY = "birthdayDay";
        public static final String BIRTHDAYFULL ="birthdayFull";
        public static final String MUNICIPALITYCODE = "municipalityCode";
        public static final String DEPARTMENTCODE = "departmentCode";
        public static final String BLOODTYPE = "bloodType";
        public static final String PHONE = "phone";
        public static final String USER = "user";

    }
}
