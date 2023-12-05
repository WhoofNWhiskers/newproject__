package com.example.newproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Random;
public class owner_home_page extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button btn;
    private DatabaseReference hostsRef;
    private Double userLat;
    private Double userLng;
    private String userPetType = null;

    private Button button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home_page);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btn=findViewById(R.id.logout_button);
        button=findViewById(R.id.button);


        recommendHost();


        //logout
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(owner_home_page.this, choice_page.class);
                startActivity(intent);
            }
        });








        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_Find_Host)
                {
                    Intent intent = new Intent(owner_home_page.this, viewmap.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.navigation_Location) {
                    // Handle menu item 2
                    Intent intent = new Intent(owner_home_page.this, petTracker.class);
                    startActivity(intent);
                    return true;
                   
                } else if (item.getItemId() == R.id.navigation_owner_profile) {
                    Intent intent = new Intent(owner_home_page.this, owner_profile.class);
                    startActivity(intent);
                    return true;


                } else if (item.getItemId() == R.id.navigation_live_chat) {
                    Intent intent = new Intent(owner_home_page.this, owner_list_Activity.class);
                    startActivity(intent);


                    // Handle menu item 3
                    return true;

                }


                else {
                    return false;
                }
            }
        });
    }
    private void recommendHost() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("recommendHost", "No authenticated user");

            return;
        }


        String email = currentUser.getEmail();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("new data");
        Query query = userRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("recommendHost", "User data not found1");
                    return;
                }

                Log.d("recommendHost", "Snapshot: " + snapshot.toString());



                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    userPetType = userSnapshot.child("pet type").getValue(String.class);
                    userLat = userSnapshot.child("latitude").getValue(Double.class);
                    userLng = userSnapshot.child("longitude").getValue(Double.class);
                    if (userPetType != null && userLat != null && userLng != null) break;
                }


                Log.d("recommendHost", "User pet type: " + userPetType);

                if (userPetType == null) {
                    Log.e("recommendHost", "User pet type not found2");
                    return;
                }

                DatabaseReference hostsRef = database.getReference("host data");
                hostsRef.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists() || dataSnapshot.getChildrenCount() == 0) {
                            Log.e("recommendHost", "No hosts available for the pet type: " );
                            return;
                        }
                        List<DataSnapshot> hostSnapshots = new ArrayList<>();
                        final double radius = 10.0;  // Radius in kilometers. Adjust as needed.
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String hostPetTypes = snapshot.child("selected_pet_names").getValue(String.class);
                            Double hostLat = snapshot.child("latitude").getValue(Double.class);
                            Double hostLng = snapshot.child("longitude").getValue(Double.class);
                            if (hostPetTypes != null && hostLat != null && hostLng != null) {
                                String[] hostPetTypesArray = hostPetTypes.split(", ");
                                for (String hostPetType : hostPetTypesArray) {
                                    double distance = calculateDistance(userLat, userLng, hostLat, hostLng);
                                    Log.d("recommendHost", "Comparing user pet type " + userPetType + " with host pet type " + hostPetType + " and comparing distance " + distance);
                                    if (userPetType.equalsIgnoreCase(hostPetType.trim()) && distance <= radius) {
                                        hostSnapshots.add(snapshot);
                                        Log.d("recommendHost", "Found match. Host: " + snapshot.getKey() + " added to list.");
                                        continue;
                                    }
                                }
                            }
                        }
                        if (hostSnapshots.isEmpty()) {
                            Log.e("recommendHost", "No hosts available for the pet type: " + userPetType);
                            return;
                        }

                        Random random = new Random();
                        DataSnapshot randomSnapshot = hostSnapshots.get(random.nextInt(hostSnapshots.size()));

                        String hostName = randomSnapshot.child("first_name").getValue(String.class);
                        Long priceLong = randomSnapshot.child("price").getValue(Long.class);
                        String petTypee = randomSnapshot.child("selected_pet_names").getValue(String.class);
                        String price = priceLong != null ? String.valueOf(priceLong) : "Price not available";
                        String petType_ = petTypee != null ? String.valueOf(petTypee) : "Pet type not available";

                        TextView hostNameTextView = findViewById(R.id.host_name);
                        TextView PriceInRecommendation = findViewById(R.id.PriceInRecommendation);
                        TextView petType=findViewById(R.id.pettype);
                        hostNameTextView.setText(hostName);
                        PriceInRecommendation.setText(price);
                        petType.setText( petType_);

                        Button button = findViewById(R.id.button);  // Replace with the actual button ID

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String hostId = getHostIdFromName(hostName, dataSnapshot);
                                if (hostId != null) {
                                    Intent intent = new Intent(owner_home_page.this, host_profile_uneditable.class);
                                    intent.putExtra("hostId", hostId);
                                    startActivity(intent);
                                } else {
                                    Log.e("recommendHost", "Host ID not found");
                                }
                            }
                        });

                        // Set click listener for hostNameTextView
                        hostNameTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String hostId = getHostIdFromName(hostName, dataSnapshot);
                                if (hostId != null) {
                                    Intent intent = new Intent(owner_home_page.this, host_profile_uneditable.class);
                                    intent.putExtra("hostId", hostId);
                                    startActivity(intent);
                                } else {
                                    Log.e("recommendHost", "Host ID not found");
                                }
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {


            }
        });



    }

    private String getHostIdFromName(String hostName, DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            String firstName = snapshot.child("first_name").getValue(String.class);
            if (firstName != null && firstName.equals(hostName)) {
                return snapshot.getKey(); // Return the host ID
            }
        }
        return null;
    }

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371;  // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;  // Convert to distance
    }



}
