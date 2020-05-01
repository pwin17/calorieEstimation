package com.example.calorieestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Button _btn_login, _btn_skip;
    EditText _user_email, _user_pswd;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openHelper = new database_helper(this);
        db = openHelper.getReadableDatabase();
        _btn_login = (Button)findViewById(R.id.btn_login);
        _btn_skip = (Button)findViewById(R.id.btn_skip);
        _user_email = (EditText)findViewById(R.id.user_email);
        _user_pswd = (EditText)findViewById(R.id.user_pswd);
        _btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = _user_email.getText().toString();
                String pass = _user_pswd.getText().toString();
                cursor = db.rawQuery("SELECT * FROM "+ database_helper.TABLE_NAME+ "WHERE "+ database_helper.COL_4+"=? AND"+ database_helper.COL_5+ "=?", new String[] {email,pass});
                if (cursor != null){
                    if (cursor.getCount() > 0){
                        Toast.makeText(getApplicationContext(), "login successfully", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        _btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, export_data.class);
                startActivity(intent);
            }
        });
    }
    /*private void validate(String username, String password){
        if ((username == "Admin") && (password=="123abc")){
            Intent
        }
    }*/
}
