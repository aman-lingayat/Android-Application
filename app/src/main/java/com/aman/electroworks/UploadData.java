    package com.aman.electroworks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UploadData extends AppCompatActivity {

    EditText editTextDescription;
    ImageView imageView;
    Button btnOpenGallary,btnOpenCamera,btnUploadData;
    Bitmap bitmap;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);

        // Getting values from fields
        editTextDescription = findViewById(R.id.editDescription);
        imageView = findViewById(R.id.imgView);
        btnOpenCamera = findViewById(R.id.openCamera);
        btnOpenGallary = findViewById(R.id.openGallary);
        btnUploadData = findViewById(R.id.uploadData);

        //Open Gallry Activity
        btnOpenGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(UploadData.this)//Dexter Library to ask permission to the user
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);//For image pick activity
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        //Open Camera Activity
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
            }
        });

        //Upload Button
        btnUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = editTextDescription.getText().toString().trim();

                StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.108/projectdata/uploadData.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(UploadData.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadData.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("description",description);
                        params.put("image",encodedImage);

                        return params;
                        // Getting Encoded Image and description as a parameter
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(UploadData.this); //Volly Library use for establishing HTTP connection.
                requestQueue.add(request);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // IF image comes from gallary
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                imageStore(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if(requestCode == 2 && resultCode == RESULT_OK && data != null){ //If image comes form camera
            bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            imageStore(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //Converting image into form of bytes
    private void imageStore(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[] imageByte = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imageByte, android.util.Base64.DEFAULT);
    }
}