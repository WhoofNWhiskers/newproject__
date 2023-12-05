package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.newproject.SwipeToDeleteCallback; // Update with the correct package name
import com.example.newproject.hosts_list_Adapter; // Update with the correct package name


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class host_list_Activity extends AppCompatActivity implements SwipeToDeleteCallback.SwipeToDeleteListener    {

    private RecyclerView recyclerView;
    private hosts_list_Adapter adapter;
    FirebaseUser currentUser;

    private String currentUserID; // Set this to the currently logged-in user's ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_list);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser != null) {
            currentUserID = currentUser.getUid();

            recyclerView = findViewById(R.id.recyclerViewHost);
            adapter = new hosts_list_Adapter(new ArrayList<>(), this, this); // Initialize with an empty list
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Set up ItemTouchHelper
            SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

            getHosts(); // Call the method to populate the adapter with host data
        }
        else {
            // Handle the case when the user is not authenticated
            Log.e("HostListActivity", "User is not authenticated.");
        }

    }

    // Modify this method to retrieve the list of hosts (contacts) from Firebase
    private void getHosts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("host_contacts");
        Query query = databaseReference.orderByChild("receiverEmail").equalTo(currentUser.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatMessage> hosts = new ArrayList<>(); // Create a new list for hosts

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage host = snapshot.getValue(ChatMessage.class);
                    hosts.add(host);
                }

                // Set the retrieved data in the adapter
                adapter.setContacts(hosts);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors or exceptions
                Log.e("HostListActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }


    @Override
    public void onSwipe(int position) {
        // Handle the swipe action and remove the item from the list
        if (position >= 0 && position < adapter.getItemCount()) {
            adapter.removeContact(position);
        }
    }



}