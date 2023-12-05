package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_page_host extends AppCompatActivity {
    Button signupfromlogin, afterlogin;
    private FirebaseAuth auth1;
    EditText email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_host);
        auth1 = FirebaseAuth.getInstance();
        signupfromlogin = findViewById(R.id.sing_up_host);//button
        afterlogin = findViewById(R.id.login_button_host);//button
        email = findViewById(R.id.email_login_host);//text field
        password = findViewById(R.id.password_login_host);//text field

        signupfromlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_page_host.this, sign_uphost.class));
                finish();
            }
        });

        afterlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //one line to check if the email has been registred
                final String email_login = email.getText().toString();
                final String pass_login = password.getText().toString();



                if (TextUtils.isEmpty(email_login)) {
                    email.setError("Email is required");
                    return;
                }

                if (!email_login.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+")) {
                    email.setError("Invalid email format");
                    return;
                }

                if (TextUtils.isEmpty(pass_login)) {
                    password.setError("Password is required");
                    return;
                }


                auth1.signInWithEmailAndPassword(email_login, pass_login)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {



                                startActivity(new Intent(login_page_host.this, host_home_page.class));
                                finish();



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {

                                email.setError("this email is not registered");

                                return;
                            }
                        });


            }


        });


    }


}