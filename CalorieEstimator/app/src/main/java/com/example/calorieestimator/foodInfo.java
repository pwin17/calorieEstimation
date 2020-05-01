package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Arrays;

public class foodInfo extends AppCompatActivity {

    TextView _api_result, _textv2_density;
    EditText _food_name;
    Button _btn_calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        Double density = getIntent().getExtras().getDouble("density");
        _api_result = (TextView)findViewById(R.id.api_view);
        _textv2_density = (TextView)findViewById(R.id.textView_density);
        _food_name = (EditText)findViewById(R.id.food_name);
        _btn_calc = (Button)findViewById(R.id.btn_calc);
        _textv2_density.setText(density.toString());
        _btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String food = _food_name.getText().toString();
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
                                    String kcal = nutrients.getString("unitName");
                                    String final_str = description + ": " + amount + kcal;

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


    }
}
