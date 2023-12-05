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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class sign_upowner extends AppCompatActivity {


    EditText email_owner,Fname_owner,Lname_owner,pass_owner,phone_owner,address_owner,pet_name,pettype,petage;
    Button bt1;

    RadioGroup radio_Group;
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
        setContentView(R.layout.activity_sign_upowner);

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        data=FirebaseDatabase.getInstance();
        email_owner=findViewById(R.id.email);
        Fname_owner=findViewById(R.id.name);
        Lname_owner=findViewById(R.id.seconedname);
        pass_owner=findViewById(R.id.password);
        phone_owner=findViewById(R.id.phone);
        address_owner=findViewById(R.id.address);
        pet_name=findViewById(R.id.petname);
        pettype=findViewById(R.id.Type);
        petage= findViewById(R.id.Age);
        bt1=findViewById(R.id.button2);
        radio_Group = findViewById(R.id.radioGroup);


        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemaps_);
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
                        mMap = googleMap;
                        if (location != null) {

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("currrent location").draggable(true);
                            Marker marker = googleMap.addMarker(markerOptions);

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
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





                            bt1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    mAuth = FirebaseAuth.getInstance();

                                    final String firstname1 = Fname_owner.getText().toString();
                                    final String seconedname1 = Lname_owner.getText().toString();
                                    final String email1 = email_owner.getText().toString();
                                    final String password = pass_owner.getText().toString();
                                    final String phonenumber = phone_owner.getText().toString();
                                    final String address = address_owner.getText().toString();
                                    final String pt_name = pet_name.getText().toString();
                                    final String pt_type = pettype.getText().toString();
                                    final String pt_age = petage.getText().toString();


                                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
                                    String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$";
                                    String namePattern = "([a-zA-Z]+|[a-zA-Z]+\\\\s[a-zA-Z]+)";
                                    String PhonePattern = "\"(05)\\\\d{8}\"";


                                    if (TextUtils.isEmpty(firstname1)) {
                                        Fname_owner.setError("First Name  Required");
                                        return;
                                    }

                                    if (!firstname1.matches(namePattern)) {
                                        Fname_owner.setError("The name must consist of letters");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(seconedname1)) {
                                        Lname_owner.setError("Last Name  Required");
                                        return;
                                    }


                                    if (!seconedname1.matches(namePattern)) {
                                        Lname_owner.setError("The name must consist of letters");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(email1)) {
                                        email_owner.setError("Email Required");

                                        return;
                                    }

                                    if (!email1.matches(emailPattern)) {
                                        email_owner.setError("Email Required With Right Format");
                                        return;
                                    }


                                    if (TextUtils.isEmpty(password)) {
                                        pass_owner.setError("Password  is Required ");
                                        return;
                                    }

                                    if (!password.matches(passwordPattern)) {
                                        pass_owner.setError("the password format is at least 6 characters long ,at least one letter and at least one digit");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(phonenumber)) {
                                        phone_owner.setError("Phone  is Required ");
                                        return;
                                    }

                /*if(!phonenumber.matches(PhonePattern)){
                    phone_owner.setError("Phone number must be valid phone number 10 digits only");
                    return;
                }*/


                                    if (TextUtils.isEmpty(address)) {
                                        address_owner.setError("Address  is Required ");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(pt_name)) {
                                        pet_name.setError("Pet Name  is Required ");
                                        return;
                                    }

                                    if (!pt_name.matches(namePattern)) {
                                        pet_name.setError("The pet name must consist of letters");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(pt_type)) {
                                        pettype.setError("Pet Type  is Required ");
                                        return;
                                    }

                                    if (!pt_type.matches(namePattern)) {
                                        pettype.setError("The pet type must consist of letters");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(pt_age)) {
                                        petage.setError("Pet Age  is Required ");
                                        return;
                                    }



                                    //add data to real time data

                                    String latitude = String.valueOf(location.getLatitude());
                                    String  longitude = String.valueOf(location.getLongitude());

                                    Owner petOwner = new Owner(firstname1, seconedname1, email1, password, phonenumber, address, pt_name, pt_type, pt_age, location.getLatitude(), location.getLongitude());

                                    DatabaseReference myRef = data.getReference("new data");
                                    myRef.push().setValue(petOwner);


                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // This method is called once with the initial value and again
                                            // whenever data at this location is updated.

                                            //Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                                            Owner petOwner = dataSnapshot.getValue(Owner.class);
                                            // Now you can use petOwner object


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Failed to read value

                                        }
                                    });


                                    //authinntcation

                                    mAuth.createUserWithEmailAndPassword(email1, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign up success, update UI with the signed-in user's information
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        Toast.makeText(sign_upowner.this, "Authentication success.",
                                                                Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        Toast.makeText(sign_upowner.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();


                                                    }
                                                }
                                            });


                                    startActivity(new Intent(sign_upowner.this, owner_home_page.class));
                                    finish();

                                }
                            });



                        }else{

                            Toast.makeText(sign_upowner.this, "pleas enter app permition", Toast.LENGTH_SHORT).show();



                        }
                    }
                });

            }
        });



    }
}