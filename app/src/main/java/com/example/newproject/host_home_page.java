package com.example.newproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class host_home_page extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button btn1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_home_page);
        bottomNavigationView = findViewById(R.id.bottom_navigation1);
        btn1=findViewById(R.id.logout_button1);

        //logout
        // logout
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(host_home_page.this, choice_page.class);
                startActivity(intent);
            }
        });
       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               if (item.getItemId() == R.id.Notifcation){
                   Intent intent = new Intent(host_home_page.this, recived_reqest2.class);
                   startActivity(intent);
                   return true;

               }
               else if(item.getItemId() == R.id.Profile){
                   Intent intent = new Intent(host_home_page.this, host_profile.class);
                   startActivity(intent);
                   return true;

               }
               else if(item.getItemId() == R.id.Chat){
                   Intent intent = new Intent(host_home_page.this, host_list_Activity.class);
                   startActivity(intent);
                   return true;

               }
               else{
                   return false;
               }

           }
       });
    }
}