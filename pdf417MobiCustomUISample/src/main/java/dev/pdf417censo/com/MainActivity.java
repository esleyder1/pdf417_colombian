package dev.pdf417censo.com;

import static com.liyu.sqlitetoexcel.SQLiteToExcel.*;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liyu.sqlitetoexcel.ExcelToSQLite;
import com.liyu.sqlitetoexcel.SQLiteToExcel;
import com.microblink.blinkbarcode.entities.recognizers.RecognizerBundle;
import com.microblink.blinkbarcode.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.blinkbarcode.fragment.RecognizerRunnerFragment;
import com.microblink.blinkbarcode.fragment.overlay.ScanningOverlay;
import com.microblink.blinkbarcode.fragment.overlay.basic.BasicOverlayController;
import com.microblink.blinkbarcode.recognition.RecognitionSuccessType;
import com.microblink.blinkbarcode.uisettings.ActivityRunner;
import com.microblink.blinkbarcode.uisettings.BarcodeUISettings;
import com.microblink.blinkbarcode.view.recognition.ScanResultListener;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import static java.nio.charset.StandardCharsets.*;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import dev.pdf417censo.com.data.Persona;
import dev.pdf417censo.com.data.PersonasDbHelper;

// ScanningOverlayBinder must be implemented for case when RecognizerRunnerFragment is used
public class MainActivity extends AppCompatActivity implements RecognizerRunnerFragment.ScanningOverlayBinder {

    private static final int MY_REQUEST_CODE = 1337;

    /**
     * Barcode recognizer that will perform recognition of images
     */
    private BarcodeRecognizer mBarcodeRecognizer;

    /**
     * Recognizer bundle that will wrap the barcode recognizer in order for recognition to be performed
     */
    private RecognizerBundle mRecognizerBundle;

    /**
     * Recognizer runner fragment will be shown on top of layout view with BarcodeOverlayController.
     */
    private RecognizerRunnerFragment mRecognizerRunnerFragment;

    /**
     * BasicOverlayController displays same UI as BarcodeScanActivity, but over given RecognizerRunnerFragment.
     * Association is done via {@link #getScanningOverlay()} method in fragment's {@link RecognizerRunnerFragment#onAttach(Activity)}
     * lifecycle event, so you must ensure that mScanOverlay exists at this time.
     */
    private final BasicOverlayController mScanOverlay = createRecognizerAndOverlay();

    private BasicOverlayController createRecognizerAndOverlay() {
        // You have to enable recognizers and barcode types you want to support
        // Don't enable what you don't need, it will significantly decrease scanning performance
        mBarcodeRecognizer = new BarcodeRecognizer();
        mBarcodeRecognizer.setScanPdf417(true);
        mBarcodeRecognizer.setScanQrCode(true);

        mRecognizerBundle = new RecognizerBundle(mBarcodeRecognizer);

        BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
        return uiSettings.createOverlayController(this, new ScanResultListener() {
            // called when RecognizerRunnerFragment finishes recognition
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            @WorkerThread
            public void onScanningDone(@NonNull RecognitionSuccessType recognitionSuccessType) {
                // pause scanning to prevent further scanning and mutating of mBarcodeRecognizer's result
                // while fragment is being removed

                mRecognizerRunnerFragment.getRecognizerRunnerView().pauseScanning();
                removeFragment();
                try {
                    handleScanResult();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onUnrecoverableError(@NonNull Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        if (savedInstanceState != null) {
            mRecognizerRunnerFragment = (RecognizerRunnerFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.recognizer_runner_view_container);
        }
    }

    public void scanDocument(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnDefaultActivity: {
                startScanDocument();
                break;
            }

        }
    }

    private void startScanDocument() {
        BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, uiSettings);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // updates bundled recognizers with results that have arrived
            mRecognizerBundle.loadFromIntent(data);
            try {
                handleScanResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleScanResult() throws IOException {
        BarcodeRecognizer.Result result = mBarcodeRecognizer.getResult();

        shareScanResult(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void shareScanResult(BarcodeRecognizer.Result result) throws IOException {

        StringBuilder sb = new StringBuilder(result.getBarcodeType().name());
        sb.append("\n\n");

        if (result.isUncertain()) {
            sb.append("\nThis scan data is uncertain!\n\nString data:\n");
        }
        sb.append(result.getStringData());

        byte[] rawDataBuffer = result.getRawData();

        String lastName = "";
        String documentNumber = "";
        String gender = "";
        String birthdayYear = "";
        String birthdayMonth = "";
        String birthdayDay = "";
        String municipalityCode = "";
        String departmentCode = "";
        String bloodType = "";

        String tentativeLastName = convertByteToArray(rawDataBuffer, 58, 80);
        String tentativeDocumentNumber = convertByteToArray(rawDataBuffer, 48, 58);
        if (Pattern.matches("[a-zA-Z\\u00f1\\u00d1]+",tentativeLastName) && Pattern.matches("[0-9]+",tentativeDocumentNumber)) {
            documentNumber = convertByteToArray(rawDataBuffer, 48, 58);
            lastName = convertByteToArray(rawDataBuffer, 58, 80);
        }else{
            documentNumber = convertByteToArray(rawDataBuffer, 48, 59);
            lastName = convertByteToArray(rawDataBuffer, 59, 80);
        }
        String secondLastName = convertByteToArray(rawDataBuffer, 81, 104);
        String firstName = convertByteToArray(rawDataBuffer, 104, 127);
        String middleName = convertByteToArray(rawDataBuffer, 127, 150);


        if (Pattern.matches("[a-zA-Z]+",convertByteToArray(rawDataBuffer, 151, 152))) {
            gender = convertByteToArray(rawDataBuffer, 151, 152);
            birthdayYear = convertByteToArray(rawDataBuffer, 152, 156);
            birthdayMonth = convertByteToArray(rawDataBuffer, 156, 158);
            birthdayDay = convertByteToArray(rawDataBuffer, 158, 160);
            municipalityCode = convertByteToArray(rawDataBuffer, 160, 162);
            departmentCode = convertByteToArray(rawDataBuffer, 162, 165);
            bloodType = convertByteToArray(rawDataBuffer, 166, 168);
        }else{
            gender = convertByteToArray(rawDataBuffer, 152, 153);
            birthdayYear = convertByteToArray(rawDataBuffer, 153, 157);
            birthdayMonth = convertByteToArray(rawDataBuffer, 157, 159);
            birthdayDay = convertByteToArray(rawDataBuffer, 159, 161);
            municipalityCode = convertByteToArray(rawDataBuffer, 161, 163);
            departmentCode = convertByteToArray(rawDataBuffer, 163, 166);
            bloodType = convertByteToArray(rawDataBuffer, 167, 169);
        }



        Persona p = new Persona("",documentNumber, lastName, secondLastName, firstName
                , middleName, gender, birthdayYear, birthdayMonth, birthdayDay,
                municipalityCode, departmentCode, bloodType,"","");

        createExcel(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String convertByteToArray(byte[] rawDataBuffer, int from, int to){
        byte[] bytes = Arrays.copyOfRange(rawDataBuffer, from, to);
        String dec = new String(bytes, UTF_8).trim();
        return dec.replaceAll("\uFFFD", "Ñ");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createExcel(Persona p) throws IOException {

        PersonasDbHelper conn = new PersonasDbHelper(this);

        long idRes = conn.savePersona(p);
        conn.close();
/*
        Toast.makeText(this, "ID registro " + idRes, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Hola: " + p.getFisrtName(), Toast.LENGTH_SHORT).show();
*/

        SharedPreferences prefe = getSharedPreferences("user_data", Context.MODE_PRIVATE);


        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("CENSO RESGUARDO JAMBALO " + currentYearDate());
        HSSFRow headerRow = hssfSheet.createRow(0);


        ArrayList<String> header = new ArrayList<>();
        header.add("VIGENCIA");
        header.add("RESGUARDO INDIGENA");
        header.add("COMUNIDAD INDIGENA MISAK (290) NASA (500)");
        header.add("FICHA FAMILIAR");
        header.add("TIPO DOCUMENTO");
        header.add("NUMERO DE DOCUMENTO");
        header.add("NOMBRES");
        header.add("APELLIDOS");
        header.add("FECHA DE NACIMIENTO");
        header.add("PARENTESCO");
        header.add("SEXO");
        header.add("ESTADO CIVIL");
        header.add("PROFESION");
        header.add("ESCOLARIDAD");
        header.add("INTEGRANTES");
        header.add("DIRECCION");
        header.add("TELEFONO");
        header.add("USUARIO");
        header.add("EDAD");

        for (int i = 0; i < header.size(); i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(header.get(i));
        }

        HSSFRow dataRow = hssfSheet.createRow(getCountRowsExcel() + 1);

        ArrayList<String> row = new ArrayList<>();
        row.add(currentYearDate());
        row.add("1131");
        row.add("500");
        row.add("1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            row.add(p.documentType());
        }
        row.add(p.getDocumentNumber().replaceFirst("^0*", ""));

        if (p.getMiddleName().isEmpty()) {
            row.add(p.getFisrtName());
        } else {
            row.add(p.getFisrtName() + " " + p.getMiddleName());
        }

        if (p.getSecondLastName().isEmpty()) {
            row.add(p.getLastName());
        } else {
            row.add(p.getLastName() + " " + p.getSecondLastName());
        }


        row.add(p.getBirthdayDay() + "/" + p.getBirthdayMonth() + "/" + p.getBirthdayYear());
        row.add("HI");
        row.add(p.getGender());
        if (Objects.equals(p.getDocumentType(), "T.I")) {
            row.add("S");
        } else {
            row.add("S");
        }

        row.add("AGRICULTOR");
        row.add("SE");
        row.add("1");
        row.add("VITOYO");
        row.add(prefe.getString("phone", ""));
        row.add(prefe.getString("user", ""));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            row.add(p.calculateAge());
        }

        for (int i = 0; i < row.size(); i++) {
            HSSFCell cell = dataRow.createCell(i);
            cell.setCellValue(row.get(i));
        }


        File localStorage = getExternalFilesDir(null);
        if (localStorage == null) {
            return;
        }
        String storagePath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
        String rootPath = storagePath + "/CensoJambalo";
        String fileName = "/CENSO_JAMBALO_" + currentYearDate() + ".xls";

        File root = new File(rootPath);
        if (!root.mkdirs()) {
            Log.i("Test", "This path is already exist: " + root.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.deleteIfExists(Paths.get(root.getAbsolutePath() +fileName));
            }
        }


        File file = new File(rootPath + fileName);



        new SQLiteToExcel
                .Builder(this)
                .setDataBase(this.getDatabasePath("Encuestados.db").getAbsolutePath())
                .setOutputPath(rootPath)
                .setOutputFileName("Encuestados.xls")
                .setTables("persona")
                .start(new ExportListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(MainActivity.this, "iniciado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        Toast.makeText(MainActivity.this, "completado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        try {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!file.createNewFile()) {
                    Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
                    file.createNewFile();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String makeLog(String log) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return log + " " + sdf.format(now);
    }

    private int getCountRowsExcel() {
        SharedPreferences prefe = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return prefe.getInt("countRows",1);
    }

    public String currentYearDate(){
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
    }


    @NonNull
    @Override
    public ScanningOverlay getScanningOverlay() {
        return mScanOverlay;
    }

    @WorkerThread
    private void removeFragment() {
        getFragmentManager().popBackStack();
        mRecognizerRunnerFragment = null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View scanLayout = findViewById(R.id.recognizer_runner_view_container);
                scanLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}