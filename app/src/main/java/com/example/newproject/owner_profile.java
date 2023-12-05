package com.example.newproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class owner_profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean isEditable = false;

    private  EditText petOwnerNameEditText;
    private  EditText ownerGenderEditText;
    private  EditText petOwnerAgeEditText;
    private  EditText ownerCityEditText;
    private  EditText petOwnerAdditionalInfoEditText;
    private  EditText petNameEditText;
    private  EditText petGenderEditText;
    private  EditText petAgeEditText;
    private  EditText petColorEditText;
    private  EditText petSpeciesEditText;
    private  EditText PetTypeEditText;
    private  EditText PetAdditionalInfoEditText;

    private ImageView profileImageView;
    private String imageUrl;


    private  Button savebutton;
    private  Button editButton;
    private  Button changeProfileButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);


        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());



        petOwnerNameEditText = findViewById(R.id.PetOwnerName);
        ownerGenderEditText = findViewById(R.id.OwnerGender);
        petOwnerAgeEditText = findViewById(R.id.PetOwnerAge);
        ownerCityEditText = findViewById(R.id.OwnerCity);
        petOwnerAdditionalInfoEditText = findViewById(R.id.PetOwnerAdditionalInfo);

        // Get references to the EditText fields pet info
        petNameEditText = findViewById(R.id.PetName);
        petGenderEditText = findViewById(R.id.PetGender);
        petAgeEditText = findViewById(R.id.PetAge);
        petColorEditText = findViewById(R.id.PetColor);
        petSpeciesEditText = findViewById(R.id.petSpecies);
        PetTypeEditText = findViewById(R.id.PetType);
        PetAdditionalInfoEditText = findViewById(R.id.PetAdditionalInfo);
        profileImageView=findViewById(R.id.imageView1);

         editButton = findViewById(R.id.EditButton);
         changeProfileButton = findViewById(R.id.changeProfileButton);
         savebutton=findViewById(R.id.saveButton1);
        Toolbar toolbar = findViewById(R.id.toolbar); // Use the id of your toolbar
        setSupportActionBar(toolbar);



        //Firebase initialization

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {

            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("new data");
            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                            Owner owner = userSnapshot.getValue(Owner.class);
                            // Use Glide to load the image into the ImageView
                            Glide.with(owner_profile.this /* context */)
                                    .load(imageUrl)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            Log.e("IMAGE_LOAD", "Load failed", e);
                                            // Important to return false so the error placeholder can be placed
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .into(profileImageView);


                            //owner
                            petOwnerNameEditText.setText(owner.getFirst_name());
                            ownerGenderEditText.setText(owner.gender);
                            petOwnerAgeEditText.setText(owner.age);
                            petOwnerAdditionalInfoEditText.setText(owner.additional_info);

                            // pet
                            petNameEditText.setText(owner.getPet_name());
                            petGenderEditText.setText(owner.pet_gender);
                            petAgeEditText.setText(owner.getPet_age());
                            petColorEditText.setText(owner.pet_color);
                            petSpeciesEditText.setText(owner.pet_species);
                            PetTypeEditText.setText(owner.getPet_type());
                            PetAdditionalInfoEditText.setText(owner.additional_info_pet);

                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }



            });




        }else {
            //
        }


        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProfileDataToFirebase();


            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {







                // Get references to the EditText fields pet owner info

                 petOwnerNameEditText = findViewById(R.id.PetOwnerName);
                 ownerGenderEditText = findViewById(R.id.OwnerGender);
                 petOwnerAgeEditText = findViewById(R.id.PetOwnerAge);
                 ownerCityEditText = findViewById(R.id.OwnerCity);
                 petOwnerAdditionalInfoEditText = findViewById(R.id.PetOwnerAdditionalInfo);
                 changeProfileButton.setVisibility(View.INVISIBLE);

                // Get references to the EditText fields pet info
                 petNameEditText = findViewById(R.id.PetName);
                 petGenderEditText = findViewById(R.id.PetGender);
                 petAgeEditText = findViewById(R.id.PetAge);
                 petColorEditText = findViewById(R.id.PetColor);
                 petSpeciesEditText = findViewById(R.id.petSpecies);
                 PetTypeEditText = findViewById(R.id.PetType);
                 PetAdditionalInfoEditText = findViewById(R.id.PetAdditionalInfo);



                // Toggle the edit state
                isEditable = !isEditable;

                // Set the editability of the EditText fields pet owner
                petOwnerNameEditText.setEnabled(isEditable);
                ownerGenderEditText.setEnabled(isEditable);
                petOwnerAgeEditText.setEnabled(isEditable);
                ownerCityEditText.setEnabled(isEditable);
                petOwnerAdditionalInfoEditText.setEnabled(isEditable);

                // Set the editability of the EditText fields pet
                petNameEditText.setEnabled(isEditable);
                petGenderEditText.setEnabled(isEditable);
                petAgeEditText.setEnabled(isEditable);
                petColorEditText.setEnabled(isEditable);
                petSpeciesEditText.setEnabled(isEditable);
                PetTypeEditText.setEnabled(isEditable);
                PetAdditionalInfoEditText.setEnabled(isEditable);


                // Optionally, change the appearance of the EditText fields based on the edit state
                int textColor = isEditable ? android.R.color.holo_blue_dark : android.R.color.black;

                //"pet owner"
                petOwnerNameEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
                ownerGenderEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
                petOwnerAgeEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
                ownerCityEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
                petOwnerAdditionalInfoEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));

        //"pet"
        petNameEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
        petGenderEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
        petAgeEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
        petColorEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
        petSpeciesEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
        PetTypeEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
        PetAdditionalInfoEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));

        // Toggle the visibility of the changeProfileButton
        if (isEditable) {
            changeProfileButton.setVisibility(View.VISIBLE);
            savebutton.setVisibility(View.VISIBLE);
        } else {
            changeProfileButton.setVisibility(View.INVISIBLE);
            savebutton.setVisibility(View.INVISIBLE);
        }


    }
});
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // Use the name of your menu resource file
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_button1) {
            Intent intent = new Intent(owner_profile.this, owner_home_page.class);
            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


private void openGallery() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);

                // Get the User ID
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Call the updateImage() method here with uid as an argument
                    updateImage(imageUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateImage(Uri imageUri) {
        // Check if the imageUri is valid
        if (imageUri == null) {
            Log.e("updateImage", "Image URI is null");
            return;
        }

        // Get the current user's email
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : null;

        // Check if the email is valid
        if (email == null || email.isEmpty()) {
            Log.e("updateImage", "Email is null or empty");
            return;
        }

        Log.d("updateImage", "Image URI: " + imageUri.toString());

        // Create a Firebase Storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // Generate a unique filename for the image
        String fileName = UUID.randomUUID().toString() + ".jpg";

        // Create a reference to the image file in Firebase Storage
        StorageReference imageRef = storageReference.child("images/" + fileName);

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Monitor the upload task to get the image URL after it's uploaded
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the image URL from the storageReference
            imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Perform the necessary tasks with the download URL
                String downloadURL = downloadUri.toString();

                // Get a Firebase Realtime Database instance
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("new data");

                // Query the Realtime Database for the current user
                Query query = usersRef.orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                // Update the image URL in the Realtime Database
                                DatabaseReference imageUrlRef = userSnapshot.getRef().child("imageUrl");
                                imageUrlRef.setValue(downloadURL)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("RealtimeDB", "Image URL successfully updated!");
                                            // Load the image using Glide after it's been uploaded
                                            if (!isFinishing() && !isDestroyed()) {
                                                Glide.with(owner_profile.this)
                                                        .load(downloadURL)
                                                        .into(profileImageView);
                                            } else {
                                                Log.e("updateImage", "Activity is either finishing or already destroyed.");
                                            }

                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("RealtimeDB", "Error updating image URL", e);
                                            // Log the error message
                                            Log.e("updateImage", "Error writing to database: " + e.getMessage());
                                        });
                            }
                        } else {
                            Log.e("updateImage", "User does not exist in the 'new data ' node");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur during data retrieval
                        Log.e("updateImage", "Data retrieval cancelled: " + databaseError.getMessage());
                    }
                });
            }).addOnFailureListener(e -> {
                // Handle the download URL failure
                Log.e("updateImage", "Error getting download URL: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            // Handle the upload failure
            if (e instanceof StorageException) {
                StorageException storageException = (StorageException) e;
                if (storageException.getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    // Handle object not found exception
                    Log.e("updateImage", "Object not found: " + e.getMessage());
                } else if (storageException.getErrorCode() == StorageException.ERROR_RETRY_LIMIT_EXCEEDED) {
                    // Handle retry limit exceeded exception
                    Log.e("updateImage", "Retry limit exceeded: " + e.getMessage());
                } else {
                    // Handle generic exceptions
                    Log.e("updateImage", "Upload failed: " + e.getMessage());
                }
            }
        });
    }

    private void saveProfileDataToFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("new data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data already exists, update it
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            updateUserProfile(userSnapshot.getKey());
                        }
                    } else {
                        // User data doesn't exist, add it
                        addUserProfile();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });



        }else {
            // User is not signed in
        }


    }
    private void addUserProfile() {
        // Fetch the new details from EditText fields
        String firstName = petOwnerNameEditText.getText().toString();
        String gender = ownerGenderEditText.getText().toString();
        String age = petOwnerAgeEditText.getText().toString();
        String additional_info = petOwnerAdditionalInfoEditText.getText().toString();

        //pet
        String petName = petNameEditText.getText().toString();
        String petGender = petGenderEditText.getText().toString();
        String petAge = petAgeEditText.getText().toString();
        String petColor = petColorEditText.getText().toString();
        String petSpecies =petSpeciesEditText.getText().toString();
        String petType =PetTypeEditText.getText().toString();
        String additional_info_pet = PetAdditionalInfoEditText.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : null;

        // Check if the email is valid
        if (email == null || email.isEmpty()) {
            Log.e("addUserProfile", "Email is null or empty");
            return;
        }

        // Query the database for the user with the specified email
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("new data");
        Query query = usersRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the key for the user with the specified email
                    String userId = snapshot.getKey();

                    getExistingOwner(userId, new OnOwnerReceived() {
                        @Override
                        public void onReceived(Owner existingOwner) {
                            if (existingOwner != null) {
                                // Create the Owner object using createOwner method

                                Owner owner = Owner.createOwner(firstName, existingOwner.getSeconed_name(), existingOwner.getEmail(),
                                        existingOwner.getPassword(), existingOwner.getPhonenumber(),
                                        existingOwner.getAddress(), petName, petType, petAge,
                                        existingOwner.getLatitude(), existingOwner.getLongitude(), gender,
                                        age, additional_info, petGender, petColor, petSpecies,
                                        additional_info_pet, existingOwner.getImageUrl());

                                // Assuming toMap method is available in Owner class
                                Map<String, Object> userValues = owner.toMap();

                                usersRef.child(userId).updateChildren(userValues)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data added successfully
                                                Toast.makeText(owner_profile.this, "Profile data updated.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to add data
                                                Toast.makeText(owner_profile.this, "Failed to update profile data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Handle the case where existingOwner is null
                                Log.e("addUserProfile", "existingOwner is null for user id: " + userId);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void getExistingOwner(String userId, OnOwnerReceived listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("new data").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Owner object
                Owner existingOwner = dataSnapshot.getValue(Owner.class);
                // Call the listener with the Owner object
                listener.onReceived(existingOwner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log a message
            }
        });
    }

    interface OnOwnerReceived {
        void onReceived(Owner owner);
    }
    private void updateUserProfile(String userId) {
        // Fetch the new details from EditText fields
        String firstName = petOwnerNameEditText.getText().toString();
        String gender = ownerGenderEditText.getText().toString();
        String age = petOwnerAgeEditText.getText().toString();
        String additional_info = petOwnerAdditionalInfoEditText.getText().toString();
        String address=ownerCityEditText.getText().toString();

        //pet
        String petName = petNameEditText.getText().toString();
        String petGender = petGenderEditText.getText().toString();
        String petAge = petAgeEditText.getText().toString();
        String petColor = petColorEditText.getText().toString();
        String petSpecies =petSpeciesEditText.getText().toString();
        String petType =PetTypeEditText.getText().toString();
        String additional_info_pet = PetAdditionalInfoEditText.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : null;

        // Check if the email is valid
        if (email == null || email.isEmpty()) {
            Log.e("addUserProfile", "Email is null or empty");
            return;
        }

        // Query the database for the user with the specified email
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("new data");
        Query query = usersRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the key for the user with the specified email
                    String userId = snapshot.getKey();

                    getExistingOwner(userId, new OnOwnerReceived() {
                        @Override
                        public void onReceived(Owner existingOwner) {
                            if (existingOwner != null) {
                                // Create the Owner object using createOwner method
                                Owner owner = Owner.createOwner(firstName, existingOwner.getSeconed_name(), existingOwner.getEmail(),
                                        existingOwner.getPassword(), existingOwner.getPhonenumber(),
                                        address, petName, petType, petAge,
                                        existingOwner.getLatitude(), existingOwner.getLongitude(), gender,
                                        age, additional_info, petGender, petColor, petSpecies,
                                        additional_info_pet, existingOwner.getImageUrl());

                                // Assuming toMap method is available in Owner class
                                Map<String, Object> userValues = owner.toMap();

                                usersRef.child(userId).updateChildren(userValues)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data added successfully
                                                Toast.makeText(owner_profile.this, "Profile data updated.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to add data
                                                Toast.makeText(owner_profile.this, "Failed to update profile data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Handle the case where existingOwner is null
                                Log.e("addUserProfile", "existingOwner is null for user id: " + userId);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

    }








}
