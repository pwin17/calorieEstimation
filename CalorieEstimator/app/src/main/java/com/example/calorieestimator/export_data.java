package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class export_data extends AppCompatActivity {
    
    TextView value;
    EditText _txt_fruitName;
    Button _btn_next;
    double fr_density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        _txt_fruitName = (EditText)findViewById(R.id.txt_fruitName);
        //final String fruit = _txt_fruitName.getText().toString();
        //final String _fruit = fruit.substring(0, 1).toUpperCase() + fruit.substring(1);
        _btn_next = (Button)findViewById(R.id._btn_SideView);
        _btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fr_density = getCSVDATA(_txt_fruitName.getText().toString());
                if (fr_density != 0){
                    String fruit = _txt_fruitName.getText().toString();
                    fruit = fruit.substring(0, 1).toUpperCase() + fruit.substring(1);
                    Intent intent = new Intent(export_data.this, import_pictures.class);
                    intent.putExtra("density", fr_density);
                    intent.putExtra("Fruit", fruit);
                    startActivity(intent);
                } else {
                    //Toast.makeText(getApplicationContext(), _txt_fruitName.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Sorry, the fruit is not in our database yet. Please put another fruit.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private List<csvHelper> helperList = new ArrayList<>();
    private double getCSVDATA(String input){
        final InputStream inputStream = getResources().openRawResource(R.raw.density);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );
        String line = "";
        double densityVal = 0;

        try {
            reader.readLine();
            while ( (line = reader.readLine())!= null ){
                String[] values = line.split(",");
                String fruitName = values[0];
                if (fruitName.compareToIgnoreCase(input) == 0){
                    densityVal = Double.parseDouble(values[1]);
                }
            }
        } catch (IOException e){
            Log.wtf("Activity", "Error: "+line, e);
            e.printStackTrace();
        }
        return densityVal;
    };
}
