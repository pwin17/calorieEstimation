package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.core.Mat;

public class top_ruler extends AppCompatActivity {

    EditText heightText, widthText;
    Button to_final;
    Double height = 0.0;
    Double width = 0.0;
    Double real_width = 0.0;
    Double real_height = 0.0;
    Double mass = 0.0;
    //Double sphereMass = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ruler);
        final Double density = getIntent().getExtras().getDouble("Density");
        final String fruit = getIntent().getExtras().getString("Fruit");
        final Double _real_width = getIntent().getExtras().getDouble("RealWidth");
        final Double _real_height = getIntent().getExtras().getDouble("RealHeight");
        final Integer focal_length = getIntent().getExtras().getInt("Focal_length2");

        widthText = (EditText)findViewById(R.id.top_width);
        heightText= (EditText)findViewById(R.id.top_height);
        to_final = (Button) findViewById(R.id.btn_toFinal);

        to_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(heightText.getText().toString())){
                    height = Double.parseDouble(heightText.getText().toString());
                    if (height > 0.0){
                        real_height = getValues(focal_length, height);
                    }
                }
                if (!"".equals(widthText.getText().toString())){
                    width = Double.parseDouble(widthText.getText().toString());
                    if (width > 0.0){
                        real_width = getValues(focal_length, height);
                    }
                }
                Double length;
                if (real_height == real_width){
                    length = real_height;
                } else if (real_height == _real_height || real_height == _real_width ) {
                    length = real_width;
                } else if (real_width == _real_width || real_width == _real_height) {
                    length = real_height;
                } else {
                    double[] all = {real_width,real_height,_real_height,_real_width};
                    length = findClosest(all);
                }
                mass = getMass(length,_real_width,_real_height, density);
                //sphereMass = getSphereMass(length, real_width,_real_height, density);
                //Toast.makeText(getApplicationContext(), mass.toString(), Toast.LENGTH_SHORT).show();
                if (mass != 0.0){
                    Intent intent = new Intent(top_ruler.this, foodInfo.class);
                    intent.putExtra("mass", mass);
                    //intent.putExtra("sphere mass", sphereMass);
                    intent.putExtra("Fruit", fruit);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "Please input both values", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Double findClosest(double[] a){
        Double minAbs=100.0;
        Double closest = a[0];
        double[] abs;

        for (int i = 2; i <a.length; i++){
            Double x = a[0]-a[i];
            Double y = a[1]-a[i];
            if (Math.abs(x)>Math.abs(y)){
                if (Math.abs(y)<minAbs){
                    minAbs = Math.abs(y);
                    closest = a[1];
                }
            } else {
                if (Math.abs(x)<minAbs) {
                    minAbs = Math.abs(x);
                    closest = a[0];
                }
            }
        }
        return closest;
    }
    /*
    private Double getSphereMass(Double length, Double width, Double height, Double density){
        Double average = (length+width+height)/3;
        Double volume = (4/3) * Math.PI * Math.pow(average/2, 3);
        Double mass = volume * density;
        return mass;
    }
    */


    private Double getMass(Double length, Double width, Double height, Double density){
        Double volume, mass;
        volume = length*width*height;
        mass = volume * density;
        return mass;
    }

    private Double getValues(Integer focalLength, Double sizeInImg) {
        Double value;
        value = (sizeInImg * 150) / ((focalLength/100)*2);
        return value;
    }
}
