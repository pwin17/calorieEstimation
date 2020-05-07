package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class side_ruler extends AppCompatActivity {

    EditText width, height;
    Button _btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_ruler);
        Integer focal_length = getIntent().getExtras().getInt("Focal_length");
        Double density = getIntent().getExtras().getDouble("density");
        width = (EditText)findViewById(R.id.width_text);
        height = (EditText)findViewById(R.id.height_text);
        Double real_width;
        Double real_height;



    }
}
