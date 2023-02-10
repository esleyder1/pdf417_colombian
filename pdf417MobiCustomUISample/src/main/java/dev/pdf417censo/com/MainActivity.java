package dev.pdf417censo.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private BasicOverlayController mScanOverlay = createRecognizerAndOverlay();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mRecognizerRunnerFragment = (RecognizerRunnerFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.recognizer_runner_view_container);
        }
    }

    public String eliminarceros(String cadena)
    {
        boolean salida=true;
        int j=0;
        String resultado=cadena;
        while (salida)
        {
            if (Integer.parseInt(""+resultado.charAt(j))> 0 )
            {
                salida= false;
            }else
            {
                resultado= resultado.substring(1,resultado.length())  ;
            }
        }
        return resultado;
    }

    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnDefaultActivity: {
                startDefaultScanActivity();
                break;
            }
            case R.id.btnDefaultOverlay: {
                showScanFragment();
                break;
            }
            case R.id.btnCustomUI: {
                startCustomUiActivity();
                break;
            }
            case R.id.btnCustomUIROI: {
                startCustomUiActivityWithCustomScanRegion();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // updates bundled recognizers with results that have arrived
            mRecognizerBundle.loadFromIntent(data);
            handleScanResult();
        }
    }

    private void handleScanResult() {
        BarcodeRecognizer.Result result = mBarcodeRecognizer.getResult();

        shareScanResult(result);
    }

    public static boolean isValidIndex(String[] arr, int index) {
        return index >= 0 && index < arr.length;
    }

    private void shareScanResult(BarcodeRecognizer.Result result) {

        StringBuilder sb = new StringBuilder(result.getBarcodeType().name());
        sb.append("\n\n");

        if (result.isUncertain()) {
            sb.append("\nThis scan data is uncertain!\n\nString data:\n");
        }
        sb.append(result.getStringData());

        byte[] rawDataBuffer = result.getRawData();

        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(RESULT_TYPE, "Barcode result");
            resultObject.put(TYPE, "PDF417");
            resultObject.put(DATA, result.getStringData());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean datosGuadados = false;

        //Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();

        Log.d("CEDULA", result.getStringData());

        String res = null;
        try {
            res = resultObject.getString("data").replaceAll("\u0000", " ");
            res = res.replaceAll("\\s+", " ");
            String[] elements = res.split(" ");

            Persona persona = new Persona();
            //LINA
            if(elements[2].matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$") && elements[2].length() > 6){

                persona.identificacion = elements[2].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
                String primerApellido = elements[2].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];


                //si contiene RH + || -
                if(isValidIndex(elements,7) && elements[7].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 7", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[4]+" "+elements[5]+" "+elements[6];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[7].split("");
                    persona.genero = generoAndRH[1];
                    persona.rh = generoAndRH[16]+generoAndRH[17];

                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;

                }else if(isValidIndex(elements,6) && elements[6].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 6", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[4]+" "+elements[5];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[6].split("");
                    persona.rh = generoAndRH[16]+generoAndRH[17];
                    persona.genero = generoAndRH[1];
                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isValidIndex(elements,5) && elements[5].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 5", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[4];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[5].split("");
                    persona.rh = generoAndRH[16]+generoAndRH[17];
                    persona.genero = generoAndRH[1];
                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //HUBER
            if(elements[3].matches("\\d+") && elements[3].length() > 6){
                persona.identificacion = elements[3];
                String primerApellido = elements[4];

                //si contiene RH + || -
                if(isValidIndex(elements,8) && elements[8].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 7", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[5]+" "+elements[6]+" "+elements[7];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[8].split("");
                    persona.genero = generoAndRH[1];
                    persona.rh = generoAndRH[16]+generoAndRH[17];

                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();

                    return;
                }
                else if(isValidIndex(elements,7) && elements[7].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 7", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[5]+" "+elements[6];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[7].split("");
                    persona.genero = generoAndRH[1];
                    persona.rh = generoAndRH[16]+generoAndRH[17];

                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;

                }else if(isValidIndex(elements,6) && elements[6].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 6", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[5];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[6].split("");
                    persona.rh = generoAndRH[16]+generoAndRH[17];
                    persona.genero = generoAndRH[1];
                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;
                }



            }
            if(elements[3].matches("\\d+") && elements[3].length() > 6){
                persona.identificacion = elements[3];
                String primerApellido = elements[4];

                //si contiene RH + || -
                if(isValidIndex(elements,7) && elements[7].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 7", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[5]+" "+elements[6];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[7].split("");
                    persona.genero = generoAndRH[1];
                    persona.rh = generoAndRH[16]+generoAndRH[17];

                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;

                }else if(isValidIndex(elements,6) && elements[6].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 6", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[5];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[6].split("");
                    persona.rh = generoAndRH[16]+generoAndRH[17];
                    persona.genero = generoAndRH[1];
                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;
                }



            }
            // ESLEYDER
            if(elements[3].matches(".*[a-zA-Z].*") && elements[3].length() > 6){
                persona.identificacion = elements[3].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
                String primerApellido = elements[3].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];


                //si contiene RH + || -
                if(isValidIndex(elements,7) && elements[7].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 7", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[4]+" "+elements[5]+" "+elements[6];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[7].split("");
                    persona.genero = generoAndRH[1];
                    persona.rh = generoAndRH[16]+generoAndRH[17];

                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;

                }else if(isValidIndex(elements,6) && elements[6].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 6", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[4]+" "+elements[5];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[6].split("");
                    persona.rh = generoAndRH[16]+generoAndRH[17];
                    persona.genero = generoAndRH[1];
                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isValidIndex(elements,5) && elements[5].matches(".*[+-].*")){
                    Toast.makeText((this), "aqui: 5", Toast.LENGTH_SHORT).show();
                    persona.nombreCompleto = primerApellido+" "+elements[4];
                    Toast.makeText(this, persona.nombreCompleto, Toast.LENGTH_SHORT).show();
                    String[] generoAndRH = elements[5].split("");
                    persona.rh = generoAndRH[16]+generoAndRH[17];
                    persona.genero = generoAndRH[1];
                    StringBuilder fechaNac = new StringBuilder();
                    for(int i = 2; i< 10; i++){
                        fechaNac.append(generoAndRH[i]);
                        if(i == 5 || i == 7){
                            fechaNac.append("/");
                        }
                    }
                    persona.fechaNacimiento = fechaNac.toString();
                    Toast.makeText(this, persona.fechaNacimiento, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.genero, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, persona.rh, Toast.LENGTH_SHORT).show();
                    return;
                }

            }






        } catch (JSONException e) {
            e.printStackTrace();
        }




        String Nombre="",Apellido="", cedula="",fecha="", dia, mes, ano;
        /*String[] auxliarArray=arrayElements[7].split("decoded\\):");

        String strDatos =auxliarArray[1];
        char[] ca = strDatos.toCharArray();
        for(int i=0; i<strDatos.length();i++)
        {
            if(Character.isLetter(ca[i]))       //Si es letra
                Apellido+=ca[i];    //Salto de línea e imprimimos el carácter
            else                                //Si no es letra
                cedula+=ca[i];           //Imprimimos el carácter
        }
        Apellido=Apellido.trim();
        cedula=(cedula.replaceAll("\n", "")).trim();
        if (cedula.length()== 0 ) {
            auxliarArray = arrayElements[5].split("decoded\\):");
            strDatos = auxliarArray[1];
            ca = strDatos.toCharArray();
            Apellido = "";
            for (int i = 0; i < strDatos.length(); i++) {
                if (Character.isLetter(ca[i]))       //Si es letra
                    Apellido += ca[i];    //Salto de línea e imprimimos el carácter
                else                                //Si no es letra
                    cedula += ca[i];           //Imprimimos el carácter
            }
            Apellido = Apellido.trim();
            cedula = (cedula.replaceAll("\n", "")).trim();
            cedula = cedula.substring(cedula.length() - 10, cedula.length());
            cedula = eliminarceros(cedula);
            auxliarArray = arrayElements[9].split("decoded\\):");
            Nombre = (auxliarArray[1].replaceAll("\n", "")).trim();
            Toast.makeText(this, Nombre, Toast.LENGTH_SHORT).show();*/

    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    private String byteArrayToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
