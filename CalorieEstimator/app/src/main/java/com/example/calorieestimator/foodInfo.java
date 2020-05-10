package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class foodInfo extends AppCompatActivity {

    TextView _api_result, _title;
    EditText _food_name;
    Button _btn_calc, _btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        final String fruit = getIntent().getExtras().getString("Fruit");
        final Double mass = getIntent().getExtras().getDouble("mass");
        _api_result = (TextView)findViewById(R.id.api_view);
        _title = (TextView)findViewById(R.id.textView_title);
        _btn_start = (Button)findViewById(R.id.btn_start);
        _btn_calc = (Button)findViewById(R.id.btn_calc);
        _title.setText("Total Calories of "+ fruit + " is:");
        _btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String food = fruit;
            String[] t = food.split(" ", 0);

            String api_helper = "";
            int len = t.length;
            for (int i=0; i<len; i++){
                api_helper = api_helper + t[i]+ "%20";
            }
            api_helper = api_helper + "Raw";
            //Log.d("Result: ", api_helper);

            String URL1 = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=KEVJZOV5r0tWduJkb2efmIX5hsp78tjCggR4jdBY&query=";
            String URL = URL1 + api_helper;
            RequestQueue requestQueue = Volley.newRequestQueue(foodInfo.this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONArray jsonArray = response.getJSONArray("foods");
                                JSONObject food = jsonArray.getJSONObject(0);
                                String description = food.getString("description");
                                JSONArray foodNutrients = food.getJSONArray("foodNutrients");
                                JSONObject nutrients = foodNutrients.getJSONObject(3);
                                String amount = nutrients.getString("nutrientNumber");
                                Double _amount = Double.parseDouble(amount);
                                _amount = _amount/100 * mass;
                                BigDecimal bd = BigDecimal.valueOf(_amount);
                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                _amount = bd.doubleValue();
                                amount = _amount.toString();
                                String kcal = nutrients.getString("unitName");
                                String final_str = amount + kcal;

                                _api_result.setText(final_str.toString());
                                //Log.e("Rest Response", response.toString());
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error Response", error.toString());
                        }
                    }
            );
            requestQueue.add(objectRequest);
            }
        });

        _btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(foodInfo.this, export_data.class);
                startActivity(intent);
            }
        });

    }
}
