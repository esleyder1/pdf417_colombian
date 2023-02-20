package dev.pdf417censo.com;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.microblink.blinkbarcode.entities.recognizers.RecognizerBundle;
import com.microblink.blinkbarcode.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.blinkbarcode.fragment.RecognizerRunnerFragment;
import com.microblink.blinkbarcode.fragment.overlay.ScanningOverlay;
import com.microblink.blinkbarcode.fragment.overlay.basic.BasicOverlayController;
import com.microblink.blinkbarcode.geometry.Rectangle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

// ScanningOverlayBinder must be implemented for case when RecognizerRunnerFragment is used
public class MainActivity extends AppCompatActivity implements RecognizerRunnerFragment.ScanningOverlayBinder {

    private static final int MY_REQUEST_CODE = 1337;

    private static final String RESULT_LIST = "resultList";
    private static final String RESULT_TYPE = "resultType";
    private static final String TYPE = "type";
    private static final String DATA = "data";
    private static final String FIELDS = "fields";
    private static final String RAW_DATA = "raw";

    Persona persona = new Persona();


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
                handleScanResult();
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


        //startDefaultScanActivity();
    }

    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnDefaultActivity: {
                startDefaultScanActivity();
                break;
            }

        }
    }

    private void startDefaultScanActivity() {
        BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, uiSettings);
    }

    private void showScanFragment() {
        if (mRecognizerRunnerFragment == null) {
            View scanLayout = findViewById(R.id.recognizer_runner_view_container);
            scanLayout.setVisibility(View.VISIBLE);

            mRecognizerRunnerFragment = new RecognizerRunnerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recognizer_runner_view_container, mRecognizerRunnerFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void startCustomUiActivity() {
        Intent intent = new Intent(this, CustomUIScanActivity.class);
        // add RecognizerBundle to intent

        mRecognizerBundle.saveToIntent(intent);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    private void startCustomUiActivityWithCustomScanRegion() {
        Intent intent = new Intent(this, CustomUIScanActivity.class);

        // add RecognizerBundle to intent
        mRecognizerBundle.saveToIntent(intent);

        // define scanning region
        // first parameter of rectangle is x-coordinate represented as percentage
        // of view width*, second parameter is y-coordinate represented as percentage
        // of view height*, third parameter is region width represented as percentage
        // of view width* and fourth parameter is region height represented as percentage
        // of view heigth*
        //
        // * view width and height are defined in current context, i.e. they depend on
        // screen orientation. If you allow your scan region view to be rotated, then in portrait
        // view width will be smaller than height, whilst in landscape orientation width
        // will be larger than height. This complies with view designer preview in eclipse ADT.
        // If you choose not to rotate your ROI view, then your ROI view will be layout either
        // in portrait or landscape, depending on setting for your camera activity in AndroidManifest.xml
        Rectangle scanRegion = new Rectangle(0.2f, 0.1f, 0.5f, 0.4f);
        intent.putExtra(CustomUIScanActivity.EXTRAS_SCAN_REGION, scanRegion);
        // if you intent to rotate your ROI view, you should set the EXTRAS_ROTATE_SCAN_REGION extra to true
        // so that PDF417.mobi can adjust ROI coordinates for native library when device orientation
        // change event occurs
        intent.putExtra(CustomUIScanActivity.EXTRAS_ROTATE_SCAN_REGION, true);

        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // updates bundled recognizers with results that have arrived
            mRecognizerBundle.loadFromIntent(data);
            handleScanResult();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleScanResult() {
        BarcodeRecognizer.Result result = mBarcodeRecognizer.getResult();

        shareScanResult(result);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //createExcel();
        }
    }

    public static boolean isValidIndex(String[] arr, int index) {
        return index >= 0 && index < arr.length;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void shareScanResult(BarcodeRecognizer.Result result) {

        StringBuilder sb = new StringBuilder(result.getBarcodeType().name());
        sb.append("\n\n");

        if (result.isUncertain()) {
            sb.append("\nThis scan data is uncertain!\n\nString data:\n");
        }
        sb.append(result.getStringData());

        byte[] rawDataBuffer = result.getRawData();

        String documentNumber = convertByteToArray(rawDataBuffer, 48, 58);
        String lastName = convertByteToArray(rawDataBuffer, 58, 80);
        String secondLastName = convertByteToArray(rawDataBuffer, 81, 104);
        String fisrtName = convertByteToArray(rawDataBuffer, 104, 127);
        String middleName = convertByteToArray(rawDataBuffer, 127, 150);
        String gender = convertByteToArray(rawDataBuffer, 151, 152);
        String birthdayYear = convertByteToArray(rawDataBuffer, 152, 156);
        String birthdayMonth = convertByteToArray(rawDataBuffer, 156, 158);
        String birthdayDay = convertByteToArray(rawDataBuffer, 158, 160);
        String municipalityCode = convertByteToArray(rawDataBuffer, 160, 162);
        String departmentCode = convertByteToArray(rawDataBuffer, 162, 165);
        String bloodType = convertByteToArray(rawDataBuffer, 166, 168);

        Persona p = new Persona(documentNumber, lastName, secondLastName, fisrtName
                , middleName, gender, birthdayYear, birthdayMonth, birthdayDay,
                municipalityCode, departmentCode, bloodType);

        createExcel(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String convertByteToArray(byte[] rawDataBuffer, int from, int to){
        byte[] bytes = Arrays.copyOfRange(rawDataBuffer, from, to);
        String dec = new String(bytes, UTF_8).trim();
        return dec.replaceAll("\uFFFD", "Ñ");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createExcel(Persona p) {

        if (p != null) {
            Toast.makeText(this, "Hola: " + p.getFisrtName(), Toast.LENGTH_SHORT).show();


            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            HSSFSheet hssfSheet = hssfWorkbook.createSheet("CENSO RESGUARDO JAMBALO 2023");

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

            HSSFRow dataRow = hssfSheet.createRow(1);

            ArrayList<String> row = new ArrayList<>();
            row.add(currentYearDate());
            row.add("1131");
            row.add("500");
            row.add("1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                row.add(p.documentType());
            }
            row.add(p.getDocumentNumber());
            row.add(p.getFisrtName() + " "+ p.getMiddleName());
            row.add(p.getLastName() + " "+ p.getSecondLastName());
            row.add(p.getBirthdayYear() + "/" + p.getBirthdayMonth() + "/" + p.getBirthdayDay());
            row.add("HI");
            row.add(p.getGender());
            row.add("S");
            row.add("AGRICULTOR");
            row.add("SE");
            row.add("1");
            row.add("VITOYO");
            row.add("3215980548");
            row.add("BRICEYDA PAZU SECUE");
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
            String rootPath = storagePath + "/censo";
            String fileName = "/listado.xlsx";

            File root = new File(rootPath);
            if (!root.mkdirs()) {
                Log.i("Test", "This path is already exist: " + root.getAbsolutePath());
            }

            File file = new File(rootPath + fileName);
            try {
                int permissionCheck = ContextCompat.checkSelfPermission(
                        MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    if (!file.createNewFile()) {
                        Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
                    }
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                hssfWorkbook.write(fileOutputStream);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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