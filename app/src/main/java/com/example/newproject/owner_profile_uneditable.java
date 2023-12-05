package com.example.newproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class owner_profile_uneditable extends AppCompatActivity {


    private DatabaseReference hostRef;
    private TextView petOwnerNameEditText;
    private  TextView ownerGenderEditText;
    private  TextView petOwnerAgeEditText;
    private  TextView ownerCityEditText;
    private  TextView petOwnerAdditionalInfoEditText;
    private  TextView petNameEditText;
    private  TextView petGenderEditText;
    private  TextView petAgeEditText;
    private  TextView petColorEditText;
    private  TextView petSpeciesEditText;
    private  TextView PetTypeEditText;
    private  TextView PetAdditionalInfoEditText;
    private ImageView profileImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile_uneditable);

        petOwnerNameEditText = findViewById(R.id.PetOwnerName_unedit);
        ownerGenderEditText = findViewById(R.id.OwnerGender_unedit);
        petOwnerAgeEditText = findViewById(R.id.PetOwnerAge_unedit);
        ownerCityEditText = findViewById(R.id.OwnerCity_uneditable);
        petOwnerAdditionalInfoEditText = findViewById(R.id.PetOwnerAdditionalInfo_unedit);

        // Get references to the EditText fields pet info
        petNameEditText = findViewById(R.id.PetName_unedit);
        petGenderEditText = findViewById(R.id.PetGender_unedit);
        petAgeEditText = findViewById(R.id.PetAge_unedit);
        petColorEditText = findViewById(R.id.PetColor_unedit);
        petSpeciesEditText = findViewById(R.id.petSpecies_unedit);
        PetTypeEditText = findViewById(R.id.PetType_unedit);
        PetAdditionalInfoEditText = findViewById(R.id.PetAdditionalInfo_unedit);
        profileImageView=findViewById(R.id.imageView1_);

        String ownerId = getIntent().getStringExtra("ownerId");
        if (ownerId != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            hostRef = database.getReference("new data").child(ownerId);
            hostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //owner
                        String firstName = dataSnapshot.child("first_name").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String age = dataSnapshot.child("age").getValue(String.class);
                        String city = dataSnapshot.child("address").getValue(String.class);
                        String additional_info = dataSnapshot.child("additional_info").getValue(String.class);

                        //pet
                        String petName = dataSnapshot.child("pet_name").getValue(String.class);
                        String petGender = dataSnapshot.child("pet_gender").getValue(String.class);
                        String petAge = dataSnapshot.child("pet_age").getValue(String.class);
                        String petColor = dataSnapshot.child("pet_color").getValue(String.class);
                        String petSpecies = dataSnapshot.child("pet_species").getValue(String.class);
                        String petType = dataSnapshot.child("pet_type").getValue(String.class);
                        String additional_ifo_pet = dataSnapshot.child("additional_info_pet").getValue(String.class);

                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                        Log.i("owner_profile_uneditable", "Image URL: " + imageUrl); // log the image URL
                        if (imageUrl != null) {
                            Glide.with(owner_profile_uneditable.this /* context */)
                                    .load(imageUrl)
                                    .into(profileImageView);
                        }

                        //owner
                        petOwnerNameEditText.setText(firstName);
                        ownerGenderEditText.setText(gender);
                        petOwnerAgeEditText.setText(age);
                        ownerCityEditText.setText(city);
                        petOwnerAdditionalInfoEditText.setText(additional_info);

                        // pet
                        petNameEditText.setText(petName);
                        petGenderEditText.setText(petGender);
                        petAgeEditText.setText(petAge);
                        petColorEditText.setText(petColor);
                        petSpeciesEditText.setText(petSpecies);
                        PetTypeEditText.setText(petType);
                        PetAdditionalInfoEditText.setText(additional_ifo_pet);



                    }else {
                        Log.e("host_profile_uneditable", "Host data not found");
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("owner_profile_uneditable", "Error getting host data: " + databaseError.getMessage());
                }
            });

        } else {
            Log.e("host_profile_uneditable", "No host ID provided");
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            petOwnerNameEditText.setText(bundle.getString("first_name"));
            ownerGenderEditText.setText(bundle.getString("gender"));
            petOwnerAgeEditText.setText(bundle.getString("age"));
            ownerCityEditText.setText(bundle.getString("address"));
            petOwnerAdditionalInfoEditText.setText(bundle.getString("additional_info"));

            petNameEditText.setText(bundle.getString("pet_name"));
            petGenderEditText.setText(bundle.getString("pet_gender"));
            petAgeEditText.setText(bundle.getString("pet_age"));
            petColorEditText.setText(bundle.getString("pet_color"));
            petSpeciesEditText.setText(bundle.getString("pet_species"));
            PetTypeEditText.setText(bundle.getString("pet_type"));
            PetAdditionalInfoEditText.setText(bundle.getString("additional_info_pet"));
            String imageUrl = bundle.getString("imageUrl");
            if (imageUrl != null) {
                Glide.with(this /* context */)
                        .load(imageUrl)
                        .into(profileImageView);
            } else {
                Log.e("owner_profile_uneditable", "No image URL provided");
            }
            setEditTextFieldsEditable(false);




        }
    }
    private void setEditTextFieldsEditable ( boolean editable){

        petOwnerNameEditText.setEnabled(editable);
        ownerGenderEditText.setEnabled(editable);
        petOwnerAgeEditText.setEnabled(editable);
        ownerCityEditText.setEnabled(editable);
        petOwnerAdditionalInfoEditText.setEnabled(editable);
        petNameEditText.setEnabled(editable);
        petGenderEditText.setEnabled(editable);
        petAgeEditText.setEnabled(editable);
        petColorEditText.setEnabled(editable);
        petSpeciesEditText.setEnabled(editable);
        PetTypeEditText.setEnabled(editable);
        PetAdditionalInfoEditText.setEnabled(editable);



    }
}