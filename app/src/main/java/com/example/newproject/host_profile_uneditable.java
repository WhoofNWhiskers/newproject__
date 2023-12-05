package com.example.newproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class host_profile_uneditable extends AppCompatActivity {

    private DatabaseReference hostRef;
    private ImageView profileImageView;
    private TextView name;
    private TextView dateOfBirthText;
    private TextView editTextStartDate;
    private TextView editTextEndDate;
    private TextView editTextNumber;
    private TextView editTextAddress;
    private TextView editTextGender;
    private TextView editTextDescription;
    private TextView editemail;
    private TextView priceTextView ;
    private TextView reciveremail1;

    private TextView counter;
    private TextView otherpetTextView ;
    private TextView OtherPetEditText ;
    private TextView RoomsNumberEditText;


    private CheckBox cat;
    private CheckBox dog;
    private CheckBox hamster;
    private CheckBox bird;
    private CheckBox Swimming_Pool;
    private CheckBox Backyard;
    private CheckBox Roof;
    private CheckBox Grass;

    private RadioGroup radioGroup;
    private RadioButton Apartment;
    private RadioButton Villa;
    private RadioButton Flat;
    private RadioButton BeachHouse;
    private RadioGroup YesNoRadioGroup;
    private RadioButton Yes;
    private RadioButton No;


    private String receiverEmail;
    private String senderEmail;
    private List<Request> requestList;


    private Button sendreq;
    String name2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_profile_uneditable);

        name = findViewById(R.id.show_Name);
        dateOfBirthText = findViewById(R.id.show_dateOfBirthText);
        editTextStartDate = findViewById(R.id.show_editTextStartDate);
        editTextEndDate = findViewById(R.id.show_editTextEndDate);
        editTextNumber = findViewById(R.id.show_editTextNumber);
        editTextAddress = findViewById(R.id.show_editTextAddress);
        editTextGender = findViewById(R.id.show_editTextGender);
        editTextDescription = findViewById(R.id.show_editTextDescription);
        editemail = findViewById(R.id.email_show);
        sendreq = findViewById(R.id.send_request1);
        cat=findViewById(R.id.Cat_uneditable);
        dog=findViewById(R.id.Dog_uneditable);
        bird=findViewById(R.id.Bird_uneditable);
        hamster=findViewById(R.id.Hamster_uneditable);
        priceTextView = findViewById(R.id.priceTextView_uneditable);
        counter = findViewById(R.id.hosted_counter_textView_);
        reciveremail1 = findViewById(R.id.show_reciveremail);
        profileImageView=findViewById(R.id.profileImageView1);
        RoomsNumberEditText=findViewById(R.id.RoomsNumberEditText_);
        otherpetTextView=findViewById(R.id.textView6_);
        Swimming_Pool=findViewById(R.id.Swimming_Pool_);
        Backyard=findViewById(R.id.Backyard_);
        Roof=findViewById(R.id.Roof_);
        Grass=findViewById(R.id.Grass_);
        radioGroup=findViewById(R.id.radioGroup_accommodatoin_);
        Apartment=findViewById(R.id.Apartment_);
        Flat=findViewById(R.id.Flat_);
        Villa=findViewById(R.id.Villa_);
        BeachHouse=findViewById(R.id.BeachHouse_);
        YesNoRadioGroup=findViewById(R.id.YesNoRadioGroup_);
        Yes=findViewById(R.id.YES_);
        No=findViewById(R.id.NO_);

        OtherPetEditText=findViewById(R.id.OtherPetEditText_);
        senderEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        RequestAdapter adapter = new RequestAdapter(requestList, receiverEmail,senderEmail);


        String hostId = getIntent().getStringExtra("hostId");
        if (hostId != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            hostRef = database.getReference("host data").child(hostId);

            hostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String hostName = dataSnapshot.child("first_name").getValue(String.class);
                        String hostEmail = dataSnapshot.child("email").getValue(String.class);
                        String hostDateOfBirth = dataSnapshot.child("dateOfBirth").getValue(String.class);
                        String hostStartDate = dataSnapshot.child("StartDate").getValue(String.class);
                        String hostEndDate = dataSnapshot.child("EndDate").getValue(String.class);
                        String hostNumber = dataSnapshot.child("phonenumber").getValue(String.class);
                        String hostAddress = dataSnapshot.child("address").getValue(String.class);
                        Integer hostCounter = dataSnapshot.child("counter").getValue(Integer.class);
                        //String hostCounter = dataSnapshot.child("counter").getValue(String.class);
                        String hostGender = dataSnapshot.child("gender").getValue(String.class);
                        String hostDescription = dataSnapshot.child("additional_info").getValue(String.class);
                        Integer price = dataSnapshot.child("price").getValue(Integer.class);
                        Integer RoomNumber = dataSnapshot.child("RoomNumber").getValue(int.class);
                        String other_pet_type = dataSnapshot.child("other_pet_type").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                        if (imageUrl != null) {
                            Glide.with(host_profile_uneditable.this /* context */)
                                    .load(imageUrl)
                                    .into(profileImageView);
                        }
                        if (price != null) {
                            priceTextView.setText(String.valueOf(price));
                        } else {
                            // Handle the case where price is not available
                            // You can set a default value or show a message to the user.
                            priceTextView.setText("Price not available");
                        }





                        name.setText(hostName);
                        dateOfBirthText.setText(hostDateOfBirth);
                        editTextStartDate.setText(hostStartDate);
                        editTextEndDate.setText(hostEndDate);
                        editTextNumber.setText(hostNumber);
                        editTextAddress.setText(hostAddress);
                        counter.setText(String.valueOf(hostCounter));
                        editTextGender.setText(hostGender);
                        editTextDescription.setText(hostDescription);
                        editemail.setText(hostEmail);
                        RoomsNumberEditText.setText(String.valueOf(RoomNumber));
                        OtherPetEditText.setText(other_pet_type);
                        priceTextView.setText(String.valueOf(price));

                        // Load and display selected pet names
                        String selectedPetNames = dataSnapshot.child("selected_pet_names").getValue(String.class);
                        // Update the checkboxes based on the selected pet names
                        if (selectedPetNames != null) {
                            if (selectedPetNames.contains("cat")) {
                                cat.setChecked(true);
                            }
                            if (selectedPetNames.contains("dog")) {
                                dog.setChecked(true);
                            }
                            if (selectedPetNames.contains("bird")) {
                                bird.setChecked(true);
                            }
                            if (selectedPetNames.contains("hamster")) {
                                hamster.setChecked(true);
                            }
                        }

                    } else {
                        Log.e("host_profile_uneditable", "Host data not found");
                    }
                    // Load and display selected property
                    String propertyType = dataSnapshot.child("Property Type").getValue(String.class);
                    if (propertyType != null) {
                        switch (propertyType) {
                            case "Apartment":
                                Apartment.setChecked(true);
                                break;
                            case "Villa":
                                Villa.setChecked(true);
                                break;
                            case "Flat":
                                Flat.setChecked(true);
                                break;
                            case "BeachHouse":
                                BeachHouse.setChecked(true);
                                break;
                            default:
                                // Do nothing or handle as necessary
                                break;
                        }
                    }
                    // Load and display other pet
                    String YesNoGroup = dataSnapshot.child("have other pet").getValue(String.class);
                    if (YesNoGroup != null) {
                        switch (YesNoGroup) {
                            case "Yes":
                                Yes.setChecked(true);
                                otherpetTextView.setVisibility(View.VISIBLE);
                                OtherPetEditText.setVisibility(View.VISIBLE);
                                break;
                            case "No":
                                No.setChecked(true);
                                otherpetTextView.setVisibility(View.GONE);
                                OtherPetEditText.setVisibility(View.GONE);
                                break;

                            default:
                                // Do nothing or handle as necessary
                                break;
                        }
                    }
                    // Load and display selected Amenities
                    String Amenities = dataSnapshot.child("Amenities").getValue(String.class);
                    // Update the checkboxes based on the selected pet names
                    if (Amenities != null) {
                        if (Amenities.contains("Swimming_Pool")) {
                            Swimming_Pool.setChecked(true);
                        }
                        if (Amenities.contains("Backyard")) {
                            Backyard.setChecked(true);
                        }
                        if (Amenities.contains("Roof")) {
                            Roof.setChecked(true);
                        }
                        if (Amenities.contains("Grass")) {
                            Grass.setChecked(true);
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("host_profile_uneditable", "Error getting host data: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("host_profile_uneditable", "No host ID provided");
        }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name.setText(bundle.getString("first_name"));
            dateOfBirthText.setText(bundle.getString("date of birth"));
            editTextStartDate.setText(bundle.getString("StartDate"));
            editTextEndDate.setText(bundle.getString("EndDate"));
            editTextNumber.setText(bundle.getString("phonenumber"));
            editTextGender.setText(bundle.getString("gender"));
            editTextDescription.setText(bundle.getString("additional info"));
            editTextAddress.setText(bundle.getString("address"));
            counter.setText(bundle.getString("counter"));
            editemail.setText(bundle.getString("email"));
            RoomsNumberEditText.setText(bundle.getString("RoomNumber"));
            OtherPetEditText.setText(bundle.getString("other pet type"));
            priceTextView.setText(bundle.getString("price"));
            receiverEmail = getIntent().getStringExtra("receiverEmail");
            reciveremail1.setText(receiverEmail);
            String selectedPetNames = bundle.getString("selected_pet_names");
            if (selectedPetNames != null) {
                if (selectedPetNames.contains("cat")) {
                    cat.setChecked(true);
                }
                if (selectedPetNames.contains("dog")) {
                    dog.setChecked(true);
                }
                if (selectedPetNames.contains("bird")) {
                    bird.setChecked(true);
                }
                if (selectedPetNames.contains("hamster")) {
                    hamster.setChecked(true);
                }
            }

            // Load and display selected property
            String propertyType = bundle.getString("Property Type");
            if (propertyType != null) {
                switch (propertyType) {
                    case "Apartment":
                        Apartment.setChecked(true);
                        break;
                    case "Villa":
                        Villa.setChecked(true);
                        break;
                    case "Flat":
                        Flat.setChecked(true);
                        break;
                    case "BeachHouse":
                        BeachHouse.setChecked(true);
                        break;
                    default:
                        // Do nothing or handle as necessary
                        break;
                }
            }
            // Load and display other pet
            String YesNoGroup = bundle.getString("have other pet");
            if (YesNoGroup != null) {
                switch (YesNoGroup) {
                    case "Yes":
                        Yes.setChecked(true);
                        otherpetTextView.setVisibility(View.VISIBLE);
                        OtherPetEditText.setVisibility(View.VISIBLE);
                        break;
                    case "No":
                        No.setChecked(true);
                        otherpetTextView.setVisibility(View.GONE);
                        OtherPetEditText.setVisibility(View.GONE);
                        break;

                    default:
                        // Do nothing or handle as necessary
                        break;
                }
            }
            // Load and display selected Amenities
            String Amenities = bundle.getString("Amenities");
            // Update the checkboxes based on the selected pet names
            if (Amenities != null) {
                if (Amenities.contains("Swimming_Pool")) {
                    Swimming_Pool.setChecked(true);
                }
                if (Amenities.contains("Backyard")) {
                    Backyard.setChecked(true);
                }
                if (Amenities.contains("Roof")) {
                    Roof.setChecked(true);
                }
                if (Amenities.contains("Grass")) {
                    Grass.setChecked(true);
                }
            }
            setEditTextFieldsEditable(false);


        }


        sendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                receiverEmail = editemail.getText().toString();
                String receiverName  = name.getText().toString();
                if (!receiverEmail.isEmpty()) {
                    String message = "Hello, I would like to connect with you."; // Replace with the actual message
                    DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
                    String requestId = requestsRef.push().getKey();
                    Request request = new Request(senderEmail, receiverEmail,message, receiverName,name2,System.currentTimeMillis(), "pending", requestId);
                    requestsRef.child(requestId).setValue(request);


                    Toast.makeText(host_profile_uneditable.this, "Request sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(host_profile_uneditable.this, "Receiver email is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }









    private void setEditTextFieldsEditable ( boolean editable){
        name.setEnabled(editable);
        editTextStartDate.setEnabled(editable);
        editTextEndDate.setEnabled(editable);
        editTextNumber.setEnabled(editable);
        editTextAddress.setEnabled(editable);
        editTextGender.setEnabled(editable);
        dateOfBirthText.setEnabled(editable);
        editTextDescription.setEnabled(editable);
        counter.setEnabled(editable);
        cat.setEnabled(editable);
        dog.setEnabled(editable);
        bird.setEnabled(editable);
        hamster.setEnabled(editable);
        Swimming_Pool.setEnabled(editable);
        Backyard.setEnabled(editable);
        Roof.setEnabled(editable);
        Grass.setEnabled(editable);
        Apartment.setEnabled(editable);
        Villa.setEnabled(editable);
        Flat.setEnabled(editable);
        BeachHouse.setEnabled(editable);
        Yes.setEnabled(editable);
        No.setEnabled(editable);
        RoomsNumberEditText.setEnabled(editable);
        OtherPetEditText.setEnabled(editable);

    }


}