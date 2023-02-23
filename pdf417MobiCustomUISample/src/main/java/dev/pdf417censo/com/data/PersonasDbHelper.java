package dev.pdf417censo.com.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PersonasDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Encuestados.db";

    public PersonasDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + PersonaContract.PersonaEntry.TABLE_NAME + " ("
                + PersonaContract.PersonaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PersonaContract.PersonaEntry.ID + " TEXT NOT NULL,"
                + PersonaContract.PersonaEntry.DOCUMENTTYPE + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.DOCUMENTNUMBER + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.LASTNAME + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.SECONDNAME + " VARCHAR,"
                + PersonaContract.PersonaEntry.FIRSTNAME + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.MIDDLENAME + " VARCHAR,"
                + PersonaContract.PersonaEntry.GENDER + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.BIRTHDAYYEAR + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.BIRTHDAYMONTH + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.BIRTHDAYDAY + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.MUNICIPALITYCODE + " VARCHAR,"
                + PersonaContract.PersonaEntry.DEPARTMENTCODE + " VARCHAR,"
                + PersonaContract.PersonaEntry.BLOODTYPE + " VARCHAR NOT NULL,"
                + PersonaContract.PersonaEntry.PHONE + " TEXT,"
                + PersonaContract.PersonaEntry.USER + " TEXT,"

                + "UNIQUE (" + PersonaContract.PersonaEntry.ID + "))");

        mockData(sqLiteDatabase);
    }

    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockPersona(sqLiteDatabase, new Persona("C.c", "1061775307",
                "ordoñez", "",
                "Esleyder", "", "M","1994","11","25","","","0-", "",""));

    }

    public long mockPersona(SQLiteDatabase db, Persona persona) {
        return db.insert(
                PersonaContract.PersonaEntry.TABLE_NAME,
                null,
                persona.toContentValues());
    }

    public long savePersona(Persona Persona) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                PersonaContract.PersonaEntry.TABLE_NAME,
                null,
                Persona.toContentValues());

    }

    public Cursor getAllPersonas() {
        return getReadableDatabase()
                .query(
                        PersonaContract.PersonaEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getPersonaById(String PersonaId) {
        Cursor c = getReadableDatabase().query(
                PersonaContract.PersonaEntry.TABLE_NAME,
                null,
                PersonaContract.PersonaEntry.ID + " LIKE ?",
                new String[]{PersonaId},
                null,
                null,
                null);
        return c;
    }

    public int deletePersona(String PersonaId) {
        return getWritableDatabase().delete(
                PersonaContract.PersonaEntry.TABLE_NAME,
                PersonaContract.PersonaEntry.ID + " LIKE ?",
                new String[]{PersonaId});
    }

    public int updatePersona(Persona Persona, String PersonaId) {
        return getWritableDatabase().update(
                PersonaContract.PersonaEntry.TABLE_NAME,
                Persona.toContentValues(),
                PersonaContract.PersonaEntry.ID + " LIKE ?",
                new String[]{PersonaId}
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
