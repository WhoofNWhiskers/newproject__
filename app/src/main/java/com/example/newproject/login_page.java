package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_page extends AppCompatActivity {

    Button signupfromlogin, afterlogin;
    private FirebaseAuth auth1;
    EditText log, sign;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        auth1 = FirebaseAuth.getInstance();
        signupfromlogin = findViewById(R.id.sing_up);//button
        afterlogin = findViewById(R.id.login_button);//button
        log = findViewById(R.id.email_login);//text field
        sign = findViewById(R.id.password_login);//text field


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usertype, com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item);



        signupfromlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_page.this, sign_upowner.class));
                finish();
            }
        });

        afterlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //one line to check if the email has been registred
                final String email_login = log.getText().toString();
                final String pass_login = sign.getText().toString();



                if (TextUtils.isEmpty(email_login)) {
                    log.setError("Email is required");
                    return;
                }

                if (!email_login.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+")) {
                    log.setError("Invalid email format");
                    return;
                }

                if (TextUtils.isEmpty(pass_login)) {
                    sign.setError("Password is required");
                    return;
                }


                auth1.signInWithEmailAndPassword(email_login, pass_login)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {




                                    startActivity(new Intent(login_page.this, owner_home_page.class));
                                    finish();
                                }






                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {

                                log.setError("this email is not registered");

                                return;
                            }
                        });

            }


        });
    }


}









