package com.example.calorieestimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

public class import_pictures extends AppCompatActivity {

    Button _get_side, _btn_next;
    ImageView _side_view, _ruler;
    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    String focal_length;
    String pathStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_pictures);
        final Double density = getIntent().getExtras().getDouble("density");
        _get_side = (Button)findViewById(R.id.btn_getSide);
        _btn_next = (Button)findViewById(R.id.btn_next);
        _side_view = (ImageView)findViewById(R.id.imageView_side);
        _ruler = (ImageView)findViewById(R.id.imageView_ruler);
        final String ruler_URL = "https://lh3.googleusercontent.com/proxy/8DtFyFkBz3Dzyt8cr8x7JGh2X5WAgQGC3rXn9gLwO0tDKGFBM7TXAoFZXiM0YrcAd4iHcEagGZU0q0iX7DW93i5OndoN-66v";

        _get_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(ruler_URL).into(_ruler);
                openGallery();
            }
        });
        _btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), focal_length, Toast.LENGTH_SHORT).show();
                if (pathStr == ""){
                    Toast.makeText(getApplicationContext(), "Please select a picture", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(import_pictures.this, side_ruler.class);
                    intent.getIntExtra("Focal_length", Integer.valueOf(focal_length));
                    intent.getDoubleExtra("density", density);
                    startActivity(intent);
                }

                //Toast.makeText(getApplicationContext(), side_path, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void openGallery(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICK_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery();
                } else {
                    Toast.makeText(this, "Permission Denied. Required to grant access.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //String path = "";
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Uri path = data.getData();
            pathStr = data.getData().getPath();
            _side_view.setImageURI(data.getData());
            try{
                InputStream in = getContentResolver().openInputStream(path);
                ExifInterface exif = new ExifInterface(in);
                focal_length= exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
                focal_length = focal_length.split("/", 2)[0];
            } catch (IOException e){
                Log.e("ERRORRRRR", e.toString());
                e.printStackTrace();
            }


        }
    }

}
