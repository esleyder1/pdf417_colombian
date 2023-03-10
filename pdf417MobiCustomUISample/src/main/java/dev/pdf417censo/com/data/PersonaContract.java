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
        public static final String VALIDITY = "validity";
        public static final String GUARD = "guard";
        public static final String COMMUNITY = "community";
        public static final String SIDEWALK = "sideWalk";
        public static final String FAMILYRECORD = "familyRecord";
        public static final String MEMBERSFAMILY = "membersFamily";
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
        public static final String RELATIONSHIP ="relationship";
        public static final String SCHOLARSHIP ="scholarship";
        public static final String PROFESSION ="profession";
        public static final String CIVILSTATUS ="civilStatus";
        public static final String MUNICIPALITYCODE = "municipalityCode";
        public static final String DEPARTMENTCODE = "departmentCode";
        public static final String BLOODTYPE = "bloodType";
        public static final String PHONE = "phone";
        public static final String USER = "user";
        public static final String AGE = "age";

    }
}
