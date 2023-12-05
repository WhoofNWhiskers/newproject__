package com.example.newproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class viewmap extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mUsers;
    private ArrayList<LatLng> arrayList = new ArrayList<>();
    private static final int MAX_HOSTS =10;

    private static final double MAX_DISTANCE = 1.0;
    // Set your desired maximum distance in kilometers
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    // Fused Location Provider




    private List<DataSnapshot> hostList = new ArrayList<>();
    private Button gender_;
    private Button gender_male;
    private Button gender_female;
    private Spinner City;
    private  Spinner pets;
    private  Spinner price;
    private String userID;

    private boolean isEditable = false;





    private MapView mapView;


    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmap);


        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//to display the map*/





        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        gender_ = findViewById(R.id.btnGender);
        gender_female = findViewById(R.id.btnFemale);
        gender_male = findViewById(R.id.btnMale);
        City = findViewById(R.id.spinnerCity);
        pets = findViewById(R.id.petsSpinner);
        price=findViewById(R.id.priceSpinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pet_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pets.setAdapter(adapter);









        gender_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the edit state
                isEditable = !isEditable;
                // Toggle the visibility of the gender buttons and City button
                if (isEditable) {
                    gender_female.setVisibility(View.VISIBLE);
                    gender_male.setVisibility(View.VISIBLE);
                    City.setVisibility(View.INVISIBLE);
                    pets.setVisibility(View.INVISIBLE);
                    price.setVisibility(View.INVISIBLE);
                } else {
                    gender_female.setVisibility(View.INVISIBLE);
                    gender_male.setVisibility(View.INVISIBLE);
                    City.setVisibility(View.VISIBLE);
                    pets.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                }
            }
        });


        City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
                filterMarkersByCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMarkersByGender("Female");
            }
        });

        gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMarkersByGender("Male");
            }
        });

        pets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPet = parent.getItemAtPosition(position).toString();
                // Add a log statement or toast to check if this method is being called
                Log.d("PetSpinner", "Selected pet: " + selectedPet);
                filterMarkersByPet(selectedPet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when no pet is selected, if needed
            }
        });


         // Set a listener for price selection
        price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPriceRange = parent.getItemAtPosition(position).toString();
                filterMarkersByPrice(selectedPriceRange);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when no price range is selected, if needed
            }
        });

        //end



    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    //

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 0));
        addMarkers();


    }

    private void addMarkers() {
        // Clear existing markers from the map
        mMap.clear();



        // Add new markers to the map
        recommendation();


    }

    public void recommendation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("showHostsOnMap", "No authenticated user");
            return;
        }

        String email = currentUser.getEmail();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("new data");
        Query query = userRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("showHostsOnMap", "User data not found");
                    return;
                }

                Log.d("showHostsOnMap", "Snapshot: " + snapshot.toString());

                // Get the first (and possibly only) child of this snapshot
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                // Get user's pet type, latitude and longitude
                String userPetType = userSnapshot.child("pet type").getValue(String.class);
                Double userLatValue = userSnapshot.child("latitude").getValue(Double.class);
                Double userLngValue = userSnapshot.child("longitude").getValue(Double.class);

                if (userLatValue == null || userLngValue == null) {
                    Log.e("showHostsOnMap", "User latitude or longitude is null");
                    return;
                }

                double userLat = userLatValue;
                double userLng = userLngValue;

                DatabaseReference hostsRef = database.getReference("host data");
                hostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists() || dataSnapshot.getChildrenCount() == 0) {
                            Log.e("showHostsOnMap", "No hosts available");
                            return;
                        }

                        for (DataSnapshot hostSnapshot : dataSnapshot.getChildren()) {
                            // Get host's pet type, latitude and longitude
                            String hostPetTypes = hostSnapshot.child("selected_pet_names").getValue(String.class);
                            Double hostLatValue = hostSnapshot.child("latitude").getValue(Double.class);
                            Double hostLngValue = hostSnapshot.child("longitude").getValue(Double.class);
                            String hostName = hostSnapshot.child("first_name").getValue(String.class);

                            if (hostLatValue == null || hostLngValue == null) {
                                Log.e("showHostsOnMap", "Host latitude or longitude is null");
                                continue;
                            }

                            double hostLat = hostLatValue;
                            double hostLng = hostLngValue;

                            // Check if user pet type matches host pet type and if host is within certain radius
                            if (hostPetTypes != null && userPetType != null && hostPetTypes.contains(userPetType)) {
                                double distance = calculateDistance(userLat, userLng, hostLat, hostLng);
                                if (distance <= 10) {
                                    LatLng hostLocation = new LatLng(hostLat, hostLng);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(hostLocation)
                                            .title(hostName)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                }
                            }
                        }

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                // Get host's details and start new activity here
                                String hostName = marker.getTitle();
                                String hostId = getHostIdFromName(hostName, dataSnapshot);

                                if (hostId != null) {
                                    Intent intent = new Intent(viewmap.this, host_profile_uneditable.class);
                                    intent.putExtra("hostId", hostId);
                                    startActivity(intent);
                                    return true; // Indicate that we have handled the event
                                }

                                return false; // If hostId is null, indicate that we haven't handled the event
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        Log.e("showHostsOnMap", "Failed to get host data: ", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                Log.e("showHostsOnMap", "Failed to get user data: ", error.toException());
            }
        });
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

    private String getHostIdFromName(String hostName, DataSnapshot dataSnapshot) {
        for (DataSnapshot hostSnapshot : dataSnapshot.getChildren()) {
            String currentHostName = hostSnapshot.child("first_name").getValue(String.class);
            if (hostName != null && hostName.equals(currentHostName)) {
                return hostSnapshot.getKey(); // Return the ID of the matching host
            }
        }

        return null; // Return null if no matching host is found
    }





    private void filterMarkersByGender(String gender) {
        mMap.clear();
        String lowercaseGender = gender.toLowerCase().trim();
        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("host data");
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String gender1 = dataSnapshot.child("gender").getValue(String.class);

                    if (gender1 != null) {
                        // Split the selected pet names into an array
                        String[] petNames = gender1.split(", ");

                        for (String petName : petNames) {
                            // Trim and convert petName to lowercase for case-insensitive comparison
                            petName = petName.trim().toLowerCase();

                            if (petName.equals(lowercaseGender)) {
                                // If there's a match, add a marker for this host
                                String name = dataSnapshot.child("first_name").getValue(String.class);
                                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                LatLng markerLocation = new LatLng(latitude, longitude);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(markerLocation).title(name));
                                marker.setTag(dataSnapshot.getKey());

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewmap.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void filterMarkersByCity(String gender) {
        // Clear existing markers from the map
        mMap.clear();
        String lowercaseCity = gender.toLowerCase().trim();
        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("host data");
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String city = dataSnapshot.child("address").getValue(String.class);

                    if (city != null) {
                        // Split the selected pet names into an array
                        String[] petNames = city.split(", ");

                        for (String petName : petNames) {
                            // Trim and convert petName to lowercase for case-insensitive comparison
                            petName = petName.trim().toLowerCase();

                            if (petName.equals(lowercaseCity)) {
                                // If there's a match, add a marker for this host
                                String name = dataSnapshot.child("first_name").getValue(String.class);
                                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                LatLng markerLocation = new LatLng(latitude, longitude);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(markerLocation).title(name));
                                marker.setTag(dataSnapshot.getKey());

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewmap.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filterMarkersByPet(String selectedPet) {

        mMap.clear();

// Convert selectedPet to lowercase for case-insensitive comparison
        String selectedPetLower = selectedPet.toLowerCase().trim();

        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("host data");
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String selectedPetNames = dataSnapshot.child("selected_pet_names").getValue(String.class);

                    if (selectedPetNames != null) {
                        // Split the selected pet names into an array
                        String[] petNames = selectedPetNames.split(", ");

                        for (String petName : petNames) {
                            // Trim and convert petName to lowercase for case-insensitive comparison
                            petName = petName.trim().toLowerCase();

                            if (petName.equals(selectedPetLower)) {
                                // If there's a match, add a marker for this host
                                String name = dataSnapshot.child("first_name").getValue(String.class);
                                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                LatLng markerLocation = new LatLng(latitude, longitude);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(markerLocation).title(name));
                                marker.setTag(dataSnapshot.getKey());

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewmap.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void filterMarkersByPrice(String selectedPriceRange) {
        // Clear existing markers from the map
        mMap.clear();
        Log.d("Price Range", selectedPriceRange);

        // Implement filtering logic based on the selected price range
        // You can use a switch statement or if-else statements to handle different price ranges

        switch (selectedPriceRange) {
            case "0-30":
                filterMarkersInRange(0, 30);
                break;
            case "31-50":
                filterMarkersInRange(31, 50);
                break;
            case "51-100":
                filterMarkersInRange(51, 100);
                break;
            case "101+":
                filterMarkersAbove(100);
                break;
            // Handle "Any" or other cases as needed
            // For "Any," you may choose to show all markers
            default:
                addMarkers(); // Show all markers
                break;
        }
    }
    private void filterMarkersInRange(int minPrice, int maxPrice) {
        mMap.clear();
        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("host data");
        Query query = mUsers.orderByChild("price").startAt(minPrice).endAt(maxPrice);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Log.d("Data from Firebase", snapshot.getValue().toString());
                } else {
                    Log.d("Data from Firebase", "Data is null");
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Parse the price
                    int price = Integer.parseInt(dataSnapshot.child("price").getValue().toString());

                    // Check if the price falls within the filter range
                    if (price >= minPrice && price <= maxPrice) {
                        // If it does, add a marker to the map
                        String name = dataSnapshot.child("first_name").getValue(String.class);
                        double latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                        double longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                        LatLng location = new LatLng(latitude, longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(name));
                        marker.setTag(dataSnapshot.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewmap.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filterMarkersAbove(int minPrice) {
        DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("host data");
        Query query = mUsers.orderByChild("price").startAt(minPrice);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Log.d("Data from Firebase", snapshot.getValue().toString());
                } else {
                    Log.d("Data from Firebase", "Data is null");
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                    LatLng markerLocation = new LatLng(latitude, longitude);

                    // Convert latitude and longitude to string and combine them
                    String locationString = latitude + "," + longitude;

                    // Only create markers for locations in arrayList
                    if (arrayList.contains(locationString)) {
                        String name = dataSnapshot.child("first_name").getValue(String.class);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(markerLocation).title(name));
                        marker.setTag(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewmap.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }






}












