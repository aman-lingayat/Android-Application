    package com.aman.electroworks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aman.electroworks.constants.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadData extends AppCompatActivity implements View.OnClickListener {

    SessionManager sessionManager;

    private Button uploadBtn,chooseBtn, logOutBtn, openCameraBtn;
    private EditText name;
    private ImageView imageView;
    private final int IMG_REQUEST =1;
    private final int IMG_REQUEST2 =2;
    private Bitmap bitmap;
    private String URL = "http://192.168.0.108/projectData/mobile/uploadinfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        //
        sessionManager=new SessionManager(UploadData.this);
        uploadBtn=(Button)findViewById(R.id.uploadData);
        chooseBtn=(Button)findViewById(R.id.openGallary);
        name=(EditText)findViewById(R.id.editDescription);
        imageView=(ImageView)findViewById(R.id.imgView);
        logOutBtn =(Button)findViewById(R.id.logOut);
        openCameraBtn = (Button)findViewById(R.id.openCamera);

        chooseBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        logOutBtn.setOnClickListener(this);
        openCameraBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openGallary:
                    selectImg();
                break;
            case R.id.uploadData:
                    uploadImage();
                break;
            case R.id.logOut:
                    logOutUser();
                break;
            case R.id.openCamera:
                openCamera();
                break;
        }
    }

    private void openCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,IMG_REQUEST2);
    }

    private void logOutUser() {
        sessionManager.removeUser();
        Intent i=new Intent(UploadData.this,Login.class);
        startActivity(i);
        finish();
    }

    private void selectImg(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode==IMG_REQUEST2 && resultCode==RESULT_OK&&data!=null){
            bitmap=(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void uploadImage(){
        StringRequest request=new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(UploadData.this, Response, Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(0);
                            name.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",name.getText().toString().trim());
                params.put("image",imageToString(bitmap));
                return params;
            }
        };

        MySingleTone.getInstance(UploadData.this).addToRequestQue(request);

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}

class MySingleTone{
    private static MySingleTone mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private MySingleTone(Context context){
        mContext=context;
        requestQueue= getRequestQue();
    }

    private RequestQueue getRequestQue() {
        if(requestQueue==null){
            requestQueue =  Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleTone getInstance(Context context){
        if (mInstance==null){
            mInstance= new MySingleTone(context);
        }
        return mInstance;
    }
    public<T> void addToRequestQue(Request<T> request){
        getRequestQue().add(request);
    }
}