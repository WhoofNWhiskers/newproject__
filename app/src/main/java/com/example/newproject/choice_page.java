package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class choice_page extends AppCompatActivity {

    Button buttonsignhost,buttonsignowner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_page);

        buttonsignhost=findViewById(R.id.pet_host_button);
        buttonsignowner=findViewById(R.id.pet_owner_button);

        buttonsignhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(choice_page.this, login_page_host.class));

            }
        });







        buttonsignowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(choice_page.this, login_page.class));

            }
        });
    }
}