package com.example.newproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


    public class sign_uphost extends AppCompatActivity {
    
        EditText email_host,Fname_host,Lname_host,pass_host,phone_host,address_host,additoonal;
        Button bt2;
        FirebaseFirestore db;
        FirebaseAuth mAuth;
        FirebaseDatabase data;
        SupportMapFragment mapFragment;
    
        private GoogleMap mMap;
    
    
        FusedLocationProviderClient fusedLocationProviderClient;
        LocationCallback locationCallback;
    
    
    
    
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_uphost);
    
    
            mAuth = FirebaseAuth.getInstance();
            db=FirebaseFirestore.getInstance();
            data=FirebaseDatabase.getInstance();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemaps);
            fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);
            Fname_host=findViewById(R.id.first_name_edit_text);
            Lname_host=findViewById(R.id.last_name_edit_text);
            email_host=findViewById(R.id.email_edit_text);
            pass_host=findViewById(R.id.password_edit_text);
            phone_host=findViewById(R.id.phone_edit_text);
            address_host=findViewById(R.id.address_edit_text);
            additoonal=findViewById(R.id.info_edit_text);
            bt2=findViewById(R.id.signup_button_host);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    
    
    
    
    
    
    
    
            Dexter.withContext(getApplicationContext())
                            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    getcurrentLocation();
    
                                }
    
                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
    
                                }
    
                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
    
                                }
                            }).check();
    
    
    
    
                }
    
    
    
        @SuppressLint("MissingPermission")
        private void getcurrentLocation() {
    
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
    
    
    
    
    
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                                //
                                mMap = googleMap;
                                //
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("currrent location").draggable(true);
                                Marker marker = googleMap.addMarker(markerOptions);
    
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(Marker draggedMarker) {
                                        // Called when the marker drag starts
                                    }
    
                                    @Override
                                    public void onMarkerDrag(Marker draggedMarker) {
                                        // Called repeatedly as the marker is being dragged
                                    }
    
                                    @Override
                                    public void onMarkerDragEnd(Marker draggedMarker) {
                                        // Called when the marker drag ends
                                        LatLng updatedLatLng = draggedMarker.getPosition();
                                        // You can access the updated latitude and longitude using updatedLatLng.latitude and updatedLatLng.longitude
                                    }
                                });
                                bt2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final String firstname1_host= Fname_host.getText().toString();
                                        final String seconedname1_host= Lname_host.getText().toString();
                                        final String email1_host= email_host.getText().toString();
                                        final String password_host=pass_host.getText().toString();
                                        final String phonenumber_host=phone_host.getText().toString();
                                        final String addresshost=address_host.getText().toString();
                                        final String addti=additoonal.getText().toString();
    
    
                                        String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
                                        String passwordPattern1 = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";
                                        String namePattern1="([a-zA-Z]+|[a-zA-Z]+\\\\s[a-zA-Z]+)";
                                        //String PhonePattern="\"(05)\\\\d{8}\"";
    
    
    
    
                                        if(TextUtils.isEmpty(firstname1_host)){
                                            Fname_host.setError("First Name  Required");
                                            return;
                                        }
                                        if(!firstname1_host.matches(namePattern1)){
                                            Fname_host.setError("The name must consist of letters");
                                            return;
                                        }
    
                                        if(TextUtils.isEmpty(seconedname1_host)){
                                            Lname_host.setError("Last Name  Required");
                                            return;
                                        }
    
    
                                        if(!seconedname1_host.matches(namePattern1)){
                                            Lname_host.setError("The name must consist of letters");
                                            return;
                                        }
    
                                        if(TextUtils.isEmpty(email1_host)){
                                            email_host.setError("Email Required");
    
                                            return;
                                        }
    
                                        if (!email1_host.matches(emailPattern1)) {
                                            email_host.setError("Email Required With Right Format");
                                            return;
                                        }
    
    
                                        if(TextUtils.isEmpty(password_host)){
                                            pass_host.setError("Password  is Required ");
                                            return;
                                        }
    
                                        if(!password_host.matches(passwordPattern1)){
                                            pass_host.setError("the password format is at least 6 characters long ,at least one letter and at least one digit");
                                            return;
                                        }
    
                                        if(TextUtils.isEmpty(phonenumber_host)){
                                            phone_host.setError("Phone  is Required ");
                                            return;
                                        }






    
    
                                        //add data to real time data
                                        Host host = new Host(firstname1_host, seconedname1_host, email1_host, password_host, phonenumber_host, addresshost,addti, location.getLatitude(), location.getLongitude());

                                        DatabaseReference myRef = data.getReference("host data");
                                        myRef.push().setValue(host);


                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // This method is called once with the initial value and again
                                                // whenever data at this location is updated.

                                                //Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                                                Host host = dataSnapshot.getValue(Host.class);
                                                // Now you can use petOwner object


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                // Failed to read value

                                            }
                                        });


                                        //authinntcation
    
                                        mAuth.createUserWithEmailAndPassword(email1_host, password_host)
                                                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Sign up success, update UI with the signed-in user's information
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            Toast.makeText(sign_uphost.this, "Authentication success.",
                                                                    Toast.LENGTH_SHORT).show();
    
                                                        } else {
                                                            Toast.makeText(sign_uphost.this, "Authentication failed.",
                                                                    Toast.LENGTH_SHORT).show();
    
    
                                                        }
                                                    }
                                                });

                                        //savelocation(location.getLatitude(), location.getLongitude());
                                        Toast.makeText(sign_uphost.this, "location is saved", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(sign_uphost.this, host_home_page.class));
                                        finish();






                                    }
    
                                });
    
                            }else {
                                Toast.makeText(sign_uphost.this, "pleas enter app permition", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    

    }