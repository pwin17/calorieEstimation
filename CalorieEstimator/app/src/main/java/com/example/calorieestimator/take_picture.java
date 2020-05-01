package com.example.calorieestimator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.hardware.camera2.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class take_picture extends AppCompatActivity {

    Button _btn_cam;
    private TextureView _imageTView;
    Button _btn_next;

    //Checking state orientation of output image
    private static final SparseIntArray orientation = new SparseIntArray();
    static {
        orientation.append(Surface.ROTATION_0,90);
        orientation.append(Surface.ROTATION_90,0);
        orientation.append(Surface.ROTATION_180,270);
        orientation.append(Surface.ROTATION_270,180);
    }

    private String cameraID;
    private CameraDevice my_camera;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder cameraRequestBuilder;
    private Size imageDimensions;
    private ImageReader imageReader;

    //Save to FILE
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            my_camera = cameraDevice;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    int focal_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        _imageTView = findViewById(R.id.img_texture_view);
        assert _imageTView != null;
        _imageTView.setSurfaceTextureListener(textureListener);
        _btn_cam = (Button)findViewById(R.id.btn_cam);
        _btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        _btn_next = (Button)findViewById(R.id.btn_next);
        _btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(take_picture.this, foodInfo.class);
                startActivity(intent);
            }
        });
    }

    private void openCamera() {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraID = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraID, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    };
    private void takePicture(){
        if (my_camera == null)
            return;
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        /*try {
            for (String camera_id : manager.getCameraIdList()) {
                CameraCharacteristics chars = manager.getCameraCharacteristics(camera_id);
                Size[] jpegTN = chars.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES);
                for (int i = 0; i<jpegTN.length; i++) {
                    Log.d("CHARSSSSSSSSSS: ", String.valueOf(jpegTN[i]));
                }
            }
        }catch (CameraAccessException e){
            e.printStackTrace();
        }*/
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(my_camera.getId());
            Size[] jpegSizes = null;
            if (characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }
            Log.e("Sizesssss: ",characteristics.toString());
            int width = 320;
            int height = 240;
            if (jpegSizes != null && jpegSizes.length > 0){
                width = jpegSizes[0].getWidth();
                height= jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width, height,ImageFormat.RAW_SENSOR, 1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(_imageTView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = my_camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, orientation.get(rotation));

            file = new File(Environment.getExternalStorageDirectory()+"/"+ UUID.randomUUID().toString() +".jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    } finally {
                        if (image != null)
                            image.close();
                    }

                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream outputStream = null;
                    try{
                        outputStream = new FileOutputStream(file);
                        outputStream.write(bytes);
                    } finally {
                        if (outputStream != null)
                            outputStream.close();
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(take_picture.this, "Saved "+file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };
            my_camera.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void createCameraPreview() {
        try {
            SurfaceTexture texture = _imageTView.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
            Surface surface = new Surface(texture);
            cameraRequestBuilder = my_camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            cameraRequestBuilder.addTarget(surface);
            my_camera.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (my_camera == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(take_picture.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch ( CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private  void updatePreview() {
        if (my_camera == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        cameraRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(cameraRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You cannot use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (_imageTView.isAvailable())
            openCamera();
        else
            _imageTView.setSurfaceTextureListener(textureListener);
    }

    @Override
    protected void onPause() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera background: ");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
}
