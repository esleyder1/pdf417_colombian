package dev.pdf417censo.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.NumberPicker;

public class PickDataActivity extends AppCompatActivity {

    private NumberPicker picker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_data);

        picker1 = findViewById(R.id.numberpicker_main_picker);
        picker1.setMaxValue(4);
        picker1.setMinValue(0);
    }
}