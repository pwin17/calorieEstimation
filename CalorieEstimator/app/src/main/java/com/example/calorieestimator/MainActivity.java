package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btnregister, _btn_to_login, _btn_skip;
    EditText _fname, _lname, _user_email, _user_pswd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = new database_helper(this);
        _btnregister = (Button)findViewById(R.id.btnregister);
        _fname = (EditText)findViewById(R.id.fname);
        _lname = (EditText)findViewById(R.id.lname);
        _user_email = (EditText)findViewById(R.id.user_email);
        _user_pswd = (EditText)findViewById(R.id.user_pswd);
        _btn_to_login = (Button)findViewById(R.id.btn_to_login);
        _btn_skip = (Button)findViewById((R.id.btn_skip));
        _btnregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                db = openHelper.getWritableDatabase();
                String fname = _fname.getText().toString();
                String lname = _lname.getText().toString();
                String email = _user_email.getText().toString();
                String pswd = _user_pswd.getText().toString();
                insertdata(fname, lname, email, pswd);
                Toast.makeText(getApplicationContext(), "register successfully", Toast.LENGTH_LONG).show();

            }

        });
        _btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
        _btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, export_data.class);
                startActivity(intent);
            }
        });
    }
    public void insertdata(String fname, String lname, String email, String pswd){
        ContentValues contentValues = new ContentValues();
        contentValues.put(database_helper.COL_2, fname);
        contentValues.put(database_helper.COL_3, lname);
        contentValues.put(database_helper.COL_4, email);
        contentValues.put(database_helper.COL_5, pswd);
        long id = db.insert(database_helper.TABLE_NAME, null, contentValues);
    }
}
