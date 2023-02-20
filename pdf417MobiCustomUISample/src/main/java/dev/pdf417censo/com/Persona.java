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

    String identificacion;
    String nombreCompleto;
    String fechaNacimiento;
    String genero;
    String rh;
    String tipoDocumento;

    public Persona() {
    }

    public Persona(String identificacion, String nombreCompleto, String fechaNacimiento, String genero, String rh) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.rh = rh;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calculateAge() {
        String age = "";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate birthDate = LocalDate.parse(this.fechaNacimiento, df);
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
        LocalDate birthDate = LocalDate.parse(this.fechaNacimiento, df);
        if ((birthDate != null) && (LocalDate.now() != null)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                if(age > 18) {
                    this.tipoDocumento = "C.C";
                }else{
                    this.tipoDocumento = "T.I";
                }
            }
        }
        return this.tipoDocumento;
    }

}
