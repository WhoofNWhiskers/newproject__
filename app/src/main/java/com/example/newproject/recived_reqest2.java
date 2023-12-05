package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class recived_reqest2 extends AppCompatActivity {
    RecyclerView requestListRecyclerView;
    RequestAdapter requestAdapter;
    DatabaseReference databaseReference1;
    private String senderEmail;
    private String receiverEmail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recived_reqest2);
        requestListRecyclerView = findViewById(R.id.recaycle1);
        requestAdapter = new RequestAdapter(new ArrayList<>(),receiverEmail,senderEmail);
        requestListRecyclerView.setAdapter(requestAdapter);
        databaseReference1= FirebaseDatabase.getInstance().getReference("requests");
        requestListRecyclerView.setHasFixedSize(true);
        requestListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String receiverEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        databaseReference1.orderByChild("receiveremail").equalTo(receiverEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Request> requestList = new ArrayList<>();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Request request = requestSnapshot.getValue(Request.class);
                    if (request != null  && "pending".equals(request.getStatus())){
                        requestList.add(request);
                    }
                }
                requestAdapter.setRequestList(requestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG, "Failed to query requests", error.toException());

            }
        });




    }



}