package com.example.calorieestimator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class take_picture extends AppCompatActivity {

    //Button _btn_cam;
    private ImageView _imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        _imageView = findViewById(R.id.imageView);
        //_btn_cam = (Button)findViewById(R.id.btn_cam);


            //Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //if (imageTakeIntent.resolveActivity(getPackageManager())!=null){
            //    startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);



        /*_btnregister.setOnClickListener(new View.OnClickListener(){
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

        });*/
    }

    public void btn_cam(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        _imageView.setImageBitmap(imageBitmap);
    }
}
