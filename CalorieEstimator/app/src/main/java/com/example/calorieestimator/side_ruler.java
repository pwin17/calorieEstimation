package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class side_ruler extends AppCompatActivity {

    EditText width, height;
    Button to_topView;
    Double _height = 0.0;
    Double _width = 0.0;
    Double real_width = 0.0;
    Double real_height = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_ruler);

        final Integer focal_length = getIntent().getExtras().getInt("Focal_length");
        final Double density = getIntent().getExtras().getDouble("density");
        final String fruit = getIntent().getExtras().getString("Fruit");
        width = (EditText)findViewById(R.id.width_text);
        height = (EditText)findViewById(R.id.height_text);
        to_topView = (Button)findViewById(R.id._btn_TopView);

        to_topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(height.getText().toString())){
                    _height = Double.parseDouble(height.getText().toString());
                    if (_height > 0.0){
                        real_height = getValues(focal_length, _height);
                    }
                }
                if (!"".equals(width.getText().toString())){
                    _width = Double.parseDouble(width.getText().toString());
                    if (_width > 0.0){
                        real_width = getValues(focal_length, _width);
                    }
                }
                //Toast.makeText(getApplicationContext(), real_height.toString() + " " + real_width.toString(), Toast.LENGTH_SHORT).show();
                if (real_width > 0.0 && real_height > 0.0){
                    Intent intent = new Intent(side_ruler.this, import_picture2.class);
                    intent.putExtra("Density", density);
                    intent.putExtra("Fruit", fruit);
                    intent.putExtra("RealWidth", real_width);
                    intent.putExtra("RealHeight", real_height);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please input both values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Double getValues(Integer focalLength, Double sizeInImg) {
        Double value;
        value = (sizeInImg * 150) / ((focalLength/100)*2);
        return value;
    }

}
