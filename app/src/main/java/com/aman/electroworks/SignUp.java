package com.aman.electroworks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    EditText editTextFullname, editTextEmail, editTextUsername, editTextPassword;
    Button buttonSignUp;
    TextView textViewlogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Getting values from fields
        editTextFullname = findViewById(R.id.fullnameSignUp);
        editTextEmail = findViewById(R.id.emailSignUp);
        editTextUsername = findViewById(R.id.usernameSignUp);
        editTextPassword = findViewById(R.id.passwordSignUp);
        buttonSignUp = findViewById(R.id.btnSignUp);
        textViewlogin = findViewById(R.id.signuphere);
        progressBar = findViewById(R.id.progress);
        //Open Login Page from signUp page
        textViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //storing field values to upload to server
                String fullname, email, username, password;
                fullname = String.valueOf(editTextFullname.getText());
                email = String.valueOf(editTextEmail.getText());
                username = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                //Condition Check for Signing Up
                if (!fullname.equals("") && !email.equals("") && !username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "email";
                            field[2] = "username";
                            field[3] = "password";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullname;
                            data[1] = email;
                            data[2] = username;
                            data[3] = password;

                            PutData putData = new PutData("http://192.168.0.108/projectData/signup.php", "POST", field, data);//Location Of php file

                            //Checking and Showing Result
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success"))
                                    {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Login.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),"All Fiels Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}