package com.example.newproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class host_profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private Button setLocationButton;
    private Button changeProfileButton;
    private Button editButton;
    private Button savebutton;
    private EditText name;
    private EditText dateOfBirthText;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private EditText editTextNumber;
    private EditText editTextAddress;
    private EditText editTextGender;
    private EditText editTextDescription;
    private EditText RoomsNumberEditText;
    private EditText OtherPetEditText;

    private TextView counter1;
    private TextView priceTextView ;
    private TextView otherpetTextView ;


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

    private SeekBar priceSeekBar ;

    private String email;
    private String imageUrl;
    private int counter=0;

    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    private boolean isEditModeEnabled = false;
    int price;
    String selectedPetNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_profile);
        profileImageView = findViewById(R.id.profileImageView);
        changeProfileButton = findViewById(R.id.changeProfileButton);

        name = findViewById(R.id.Name);
        dateOfBirthText = findViewById(R.id.dateOfBirthText);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextNumber = findViewById(R.id.editTextNumber);


        editTextAddress = findViewById(R.id.editTextAddress);
        setLocationButton = findViewById(R.id.setLocationButton);

        editTextGender = findViewById(R.id.editTextGender);
        editTextDescription = findViewById(R.id.editTextDescription);
        RoomsNumberEditText=findViewById(R.id.RoomsNumberEditText);
        otherpetTextView=findViewById(R.id.textView6);
        OtherPetEditText=findViewById(R.id.OtherPetEditText);


        cat=findViewById(R.id.Cat);
        dog=findViewById(R.id.Dog);
        bird=findViewById(R.id.Bird);
        hamster=findViewById(R.id.Hamster);
        Swimming_Pool=findViewById(R.id.Swimming_Pool);
        Backyard=findViewById(R.id.Backyard);
        Roof=findViewById(R.id.Roof);
        Grass=findViewById(R.id.Grass);
        radioGroup=findViewById(R.id.radioGroup_accommodatoin);
        Apartment=findViewById(R.id.Apartment);
        Flat=findViewById(R.id.Flat);
        Villa=findViewById(R.id.Villa);
        BeachHouse=findViewById(R.id.BeachHouse);
        YesNoRadioGroup=findViewById(R.id.YesNoRadioGroup);
        Yes=findViewById(R.id.YES);
        No=findViewById(R.id.NO);


        counter1=findViewById(R.id.hosted_counter_textView_2);

        editButton = findViewById(R.id.EditButton);
        savebutton=findViewById(R.id.saveButton);
        priceSeekBar = findViewById(R.id.priceSeekBar);
        priceTextView = findViewById(R.id.priceTextView);
        changeProfileButton.setVisibility(View.INVISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar2); // Use the id of your toolbar
        setSupportActionBar(toolbar);



        // Assuming you have a reference to your RadioGroup

        YesNoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.YES) {
                    otherpetTextView.setVisibility(View.VISIBLE);
                    OtherPetEditText.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.NO) {
                    otherpetTextView.setVisibility(View.GONE);
                    OtherPetEditText.setVisibility(View.GONE);
                }
            }
        });




        // Firebase initialization realtime data
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
             email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                           Host host = userSnapshot.getValue(Host.class);
                            String imageUrl = host.getImageUrl(); // Make sure Host class has this method

                            Log.d("IMAGE_LOAD", "imageUrl: " + imageUrl);

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(host_profile.this /* context */)
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
                            } else {
                                // Your code to handle null or empty imageUrl
                                Log.e("IMAGE_LOAD", "imageUrl is null or empty");
                            }


                            //Retrieve the "price" value from Firebase

                            if (userSnapshot.hasChild("price")) {
                                price = userSnapshot.child("price").getValue(Integer.class);

                            } else {
                                Log.e("IMAGE_LOAD", "No user data found for this email");
                            }

                            priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    // Calculate the actual price based on the progress (adding 50 to it)
                                   /*int selectedPrice = progress + 50;
                                    priceTextView.setText("Price: " + selectedPrice);*/
                                    int selectedPrice = progress + 50;

                                    // Set the price in the Host object
                                    host.setPrice(selectedPrice);

                                    // Update the price in Firebase
                                    usersRef.child(userId).child("price").setValue(selectedPrice);

                                    priceTextView.setText("Price: " + selectedPrice);
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    // Handle touch start if needed
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    // Handle touch stop if needed
                                }
                            });



                            name.setText(host.getFirst_name());
                            editTextNumber.setText(host.getPhonenumber());
                            editTextAddress.setText(host.getAddress());
                            editTextDescription.setText(host.getAdditional_info());
                            editTextGender.setText(host.gender);
                            RoomsNumberEditText.setText(String.valueOf(host.getRoomNumber()));
                            editTextStartDate.setText(host.StartDate);
                            editTextEndDate.setText(host.EndDate);
                            OtherPetEditText.setText(host.other_pet_type);
                            priceTextView.setText(String.valueOf(host.getPrice()));
                            dateOfBirthText.setText(host.dateOfBirth);
                            counter1.setText(String.valueOf(host.getCounter()));


                            // Display the retrieved price

                            // Load and display selected pet names
                             selectedPetNames = userSnapshot.child("selected_pet_names").getValue(String.class);
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
                            // Load and display selected property
                            String propertyType = userSnapshot.child("Property Type").getValue(String.class);
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
                            String YesNoGroup = userSnapshot.child("have other pet").getValue(String.class);
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
                            String Amenities = userSnapshot.child("Amenities").getValue(String.class);
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
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                    Log.e("IMAGE_LOAD", "Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
           //
        }




        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                List<String> selectedPetNames = new ArrayList<>();
                if (cat.isChecked()) {
                    selectedPetNames.add("cat");
                }
                if (dog.isChecked()) {
                    selectedPetNames.add("dog");
                }
                if (bird.isChecked()) {
                    selectedPetNames.add("bird");
                }
                if (hamster.isChecked()) {
                    selectedPetNames.add("hamster");
                }

                List<String> selectedAmenities = new ArrayList<>();
                if (Swimming_Pool.isChecked()) {
                    selectedAmenities.add("Swimming_Pool");
                }
                if (Backyard.isChecked()) {
                    selectedAmenities.add("Backyard");
                }
                if (Roof.isChecked()) {
                    selectedAmenities.add("Roof");
                }
                if (Grass.isChecked()) {
                    selectedAmenities.add("Grass");
                }

                String other_pet_type = "";
                int checkedId1 = YesNoRadioGroup.getCheckedRadioButtonId();
                if (checkedId1 == R.id.YES) {
                    other_pet_type="Yes";
                }
                if (checkedId1 == R.id.NO) {
                    other_pet_type="No";
                }

                String selectedProperty = "";
                int checkedId = radioGroup.getCheckedRadioButtonId();
                if (checkedId == R.id.Apartment) {
                    selectedProperty = "Apartment";
                } else if (checkedId == R.id.Villa) {
                    selectedProperty = "Villa";
                } else if (checkedId == R.id.Flat) {
                    selectedProperty = "Flat";
                } else if (checkedId == R.id.BeachHouse) {
                    selectedProperty = "BeachHouse";
                }
                // save the pet name in the firebase
                saveSelectedPetNamesToFirebase(selectedPetNames);
                // save the selectedAmenities in the firebase
                saveselectedAmenitiesToFirebase(selectedAmenities);
                // Save the accommodation type to Firebase
                saveSelectedPropertyToFirebase(selectedProperty);
                //save the choise have other pet
                saveSelectedOtherpetToFirebase(other_pet_type);

                // save the profile data
                saveProfileDataToFirebase();




            }
        });

        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        dateOfBirthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(dateOfBirthText, null);
            }
        });

        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextStartDate, endDateSetListener);
            }
        });
        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextEndDate, startDateSetListener);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMode();
            }
        });

        // Prevent the EditText fields from gaining focus and opening the keyboard

        dateOfBirthText.setFocusable(false);
        dateOfBirthText.setFocusableInTouchMode(false);
        editTextStartDate.setFocusable(false);
        editTextStartDate.setFocusableInTouchMode(false);
        editTextEndDate.setFocusable(false);
        editTextEndDate.setFocusableInTouchMode(false);

        // Initially, make the EditText fields uneditable
        setEditTextFieldsEditable(false);
        priceSeekBar.setFocusable(false);


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
            Intent intent = new Intent(host_profile.this, host_home_page.class);
            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private void openImagePicker() {
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
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

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
                                                Glide.with(host_profile.this)
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


    private void showDatePicker(final EditText editText, final DatePickerDialog.OnDateSetListener dateListener) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(host_profile.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener != null ? dateListener : new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    private void toggleEditMode() {
        isEditModeEnabled = !isEditModeEnabled;
        setEditTextFieldsEditable(isEditModeEnabled);


        if (isEditModeEnabled) {
            setLocationButton.setVisibility(View.VISIBLE);
            changeProfileButton.setVisibility(View.VISIBLE);


            // Show the checkboxes and hide the TextView when in edit mode
            cat.setVisibility(View.VISIBLE);
            dog.setVisibility(View.VISIBLE);
            bird.setVisibility(View.VISIBLE);
            hamster.setVisibility(View.VISIBLE);


            Swimming_Pool.setVisibility(View.VISIBLE);
            Backyard.setVisibility(View.VISIBLE);
            Roof.setVisibility(View.VISIBLE);
            Grass.setVisibility(View.VISIBLE);

            Apartment.setVisibility(View.VISIBLE);
            Villa.setVisibility(View.VISIBLE);
            Flat.setVisibility(View.VISIBLE);
            BeachHouse.setVisibility(View.VISIBLE);

            Yes.setVisibility(View.VISIBLE);
            No.setVisibility(View.VISIBLE);


            //name edit text
            name.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));



            //edit Text Start Date
            editTextStartDate.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

            //edit Text end Date
            editTextEndDate.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));



            //mobile in edit mood
            editTextNumber.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));


            //address in edit mood
            editTextAddress.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            //date of birth in edit mood
            dateOfBirthText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));


            //description in edit mood
            editTextDescription.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            RoomsNumberEditText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));



            //gender
            editTextGender.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            savebutton.setVisibility(View.VISIBLE);
            changeProfileButton.setVisibility(View.VISIBLE);
            //
            OtherPetEditText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            savebutton.setVisibility(View.VISIBLE);
            changeProfileButton.setVisibility(View.VISIBLE);





        } else {
            setLocationButton.setVisibility(View.INVISIBLE);
            changeProfileButton.setVisibility(View.INVISIBLE);
            // Restore the default style of the EditText when not in edit mode
            int styleResId = R.style.EditTextAsTextView; // Replace with your style resource ID
            TypedArray typedArray = obtainStyledAttributes(new int[]{styleResId});
            int backgroundResId = typedArray.getResourceId(0, 0);
            typedArray.recycle();



            //name
            name.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            name.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            name.setBackgroundResource(backgroundResId);


            //edit Text Start Date
            editTextStartDate.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextStartDate.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextStartDate.setBackgroundResource(backgroundResId);
            //edit Text end Date
            editTextEndDate.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextEndDate.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextEndDate.setBackgroundResource(backgroundResId);
            //mobile in edit mood
            editTextNumber.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextNumber.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextNumber.setBackgroundResource(backgroundResId);

            //address in edit mood
            editTextAddress.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextAddress.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextAddress.setBackgroundResource(backgroundResId);

            //date of birth in edit mood
            dateOfBirthText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            dateOfBirthText.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            dateOfBirthText.setBackgroundResource(backgroundResId);

            //description in edit mood
            editTextDescription.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextDescription.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextDescription.setBackgroundResource(backgroundResId);

            //Room number in edit mood
            RoomsNumberEditText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            RoomsNumberEditText.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            RoomsNumberEditText.setBackgroundResource(backgroundResId);



            //gender
            editTextGender.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextGender.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextGender.setBackgroundResource(backgroundResId);

            OtherPetEditText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            editTextGender.setTextAppearance(this, R.style.EditTextAsTextView); // Replace with your style resource ID
            editTextGender.setBackgroundResource(backgroundResId);




            displaySelectedPetNames();
            displaySelectedAmenities();
            displaySelectedPropertyFromFirebase();
            displayOtherPetFromFirebase();
            savebutton.setVisibility(View.INVISIBLE);
            changeProfileButton.setVisibility(View.INVISIBLE);



        }
    }
    private void setEditTextFieldsEditable(boolean editable) {
        name.setEnabled(editable);
        editTextStartDate.setEnabled(editable);
        editTextEndDate.setEnabled(editable);
        editTextNumber.setEnabled(editable);
        editTextAddress.setEnabled(editable);
        editTextGender.setEnabled(editable);
        editTextDescription.setEnabled(editable);
        RoomsNumberEditText.setEnabled(editable);
        OtherPetEditText.setEnabled(editable);
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
        priceSeekBar.setEnabled(editable);
        changeProfileButton.setEnabled(editable);

    }


    private void saveProfileDataToFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Users data already exists, update it
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            updateUserProfile(userSnapshot.getKey());
                        }
                    } else {
                        // Users data doesn't exist, add it
                        addUserProfile();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }




    }
    private void addUserProfile() {


        String firstName = name.getText().toString();
        String gender= editTextGender.getText().toString();
        String StartDate=editTextStartDate.getText().toString();
        String EndDate=editTextEndDate.getText().toString();
        int RoomNumber= Integer.parseInt(RoomsNumberEditText.getText().toString());
        int price= Integer.parseInt(priceTextView.getText().toString());
        String dateOfBirth=dateOfBirthText.getText().toString();
        int counter= Integer.parseInt(counter1.getText().toString());
        String other_pet_type=OtherPetEditText.getText().toString();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");
        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    // Get the key for the user with the specified email
                    String userId = datasnapshot.getKey();






                    getExistingHost(userId, new host_profile.OnHostReceived() {
                        @Override
                        public void onReceived(Host existingHost) {
                            if (existingHost != null) {

                                Host host=Host.createHost(firstName,existingHost.getLast_name(),existingHost.getEmail(),existingHost.getPassword()
                                ,existingHost.getPhonenumber(),existingHost.getAddress(),existingHost.getAdditional_info(),
                                        existingHost.getLatitude(),existingHost.getLongitude(),gender,RoomNumber,
                                       existingHost.getImageUrl(),StartDate,EndDate,price,dateOfBirth,counter,other_pet_type);

                                // Assuming toMap method is available in Owner class
                                Map<String, Object> userValues = host.toMap();
                                usersRef.child(userId).updateChildren(userValues)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data added successfully
                                                Toast.makeText(host_profile.this, "Profile data updated.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to add data
                                                Toast.makeText(host_profile.this, "Failed to update profile data.", Toast.LENGTH_SHORT).show();
                                                Log.e("addUserProfile", "Error updating profile data", e);
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
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });







    }

    private void getExistingHost(String userId, host_profile.OnHostReceived listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Owner object
                Host existingHost = dataSnapshot.getValue(Host.class);
                // Call the listener with the Owner object
                listener.onReceived(existingHost);
                Log.d("getExistingHost", "DataSnapshot: " + dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("getExistingHost", "Error reading host data", databaseError.toException());
                // Log a message
            }
        });
    }

    interface OnHostReceived {
        void onReceived(Host host);
    }




    private void updateUserProfile(String userId) {
       /* DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

        String firstName = name.getText().toString();
        String phoneNumber = editTextNumber.getText().toString();
        String address = editTextAddress.getText().toString();
        String description = editTextDescription.getText().toString();
        String gender= editTextGender.getText().toString();
        String date_of_birth=dateOfBirthText.getText().toString();
        String StartDate=editTextStartDate.getText().toString();
        String EndDate=editTextEndDate.getText().toString();
        String RoomNumber = RoomsNumberEditText.getText().toString();
        String other_pet_type = OtherPetEditText.getText().toString();
        String counter_firebase=counter1.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String email = currentUser != null ? currentUser.getEmail() : null;

        // Check if the email is valid
        if (email == null || email.isEmpty()) {
            Log.e("addUserProfile", "Email is null or empty");
            return;
        }*/
        String firstName = name.getText().toString();
        String phoneNumber = editTextNumber.getText().toString();
        String address = editTextAddress.getText().toString();
        String description = editTextDescription.getText().toString();
        String gender= editTextGender.getText().toString();
        int RoomNumber = Integer.parseInt(RoomsNumberEditText.getText().toString());
        int price= Integer.parseInt(priceTextView.getText().toString());
        String StartDate=editTextStartDate.getText().toString();
        String EndDate=editTextEndDate.getText().toString();
        String dateOfBirth=dateOfBirthText.getText().toString();
        int counter= Integer.parseInt(counter1.getText().toString());
        String other_pet_type=OtherPetEditText.getText().toString();




        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");
        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    // Get the key for the user with the specified email
                    String userId = datasnapshot.getKey();


                    getExistingHost(userId, new host_profile.OnHostReceived() {
                        @Override
                        public void onReceived(Host existingHost) {
                            if (existingHost != null) {

                                Host host=Host.createHost(firstName,existingHost.getLast_name(),existingHost.getEmail(),existingHost.getPassword()
                                        ,phoneNumber,address,description,
                                        existingHost.getLatitude(),existingHost.getLongitude(),gender,RoomNumber,existingHost.getImageUrl(),StartDate,EndDate,price,dateOfBirth,counter
                                ,other_pet_type);

                                // Assuming toMap method is available in Owner class
                                Map<String, Object> userValues = host.toMap();
                                usersRef.child(userId).updateChildren(userValues)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data added successfully
                                                Toast.makeText(host_profile.this, "Profile data updated.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to add data
                                                Toast.makeText(host_profile.this, "Failed to update profile data.", Toast.LENGTH_SHORT).show();
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
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });











        /*Map<String, Object> userValues = new HashMap<>();
        userValues.put("first_name", firstName);
        userValues.put("phonenumber", phoneNumber);
        userValues.put("address", address);
        userValues.put("additional info", description);
        userValues.put("gender",gender);
        userValues.put("date of birth",date_of_birth);
        userValues.put("StartDate",StartDate);
        userValues.put("EndDate",EndDate);
        userValues.put("RoomNumber",RoomNumber);
        userValues.put("other pet type",other_pet_type);
        userValues.put("counter",counter_firebase);
        userValues.put("counter",counter);

        usersRef.child(userId).updateChildren(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data updated successfully
                        Toast.makeText(host_profile.this, "Profile data updated.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update data
                        Toast.makeText(host_profile.this, "Failed to update profile data.", Toast.LENGTH_SHORT).show();
                    }
                });*/











    }



        // save the pet type by calling another method
        private void saveSelectedPetNamesToFirebase(List<String> selectedPetNames) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                String email = currentUser.getEmail();
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

                Query query = usersRef.orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                updateUserPetNames(userSnapshot.getKey(), selectedPetNames);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur during data retrieval
                        System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                    }
                });
            } else {
                // Users is not signed in
            }


        }





   // save the selected pet type in the firebase
    private void updateUserPetNames(String userId, List<String> selectedPetNames) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

        // Convert the list of selected pet names to a comma-separated string
        String petNamesString = TextUtils.join(", ", selectedPetNames);

        Map<String, Object> userValues = new HashMap<>();
        userValues.put("selected_pet_names", petNamesString); // Update the database field with the selected pet names

        usersRef.child(userId).updateChildren(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data updated successfully
                        Toast.makeText(host_profile.this, "Selected pet names saved.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update data
                        Toast.makeText(host_profile.this, "Failed to save selected pet names.", Toast.LENGTH_SHORT).show();
                    }
                });




    }



    //i did not do anything here
    private void displaySelectedPetNames() {
        // Retrieve and display selected pet names from Firebase in the petNamesTextView
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String selectedPetNames = userSnapshot.child("selected_pet_names").getValue(String.class);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }
    }

    //save the selected Amenities To the Firebase
    private void saveselectedAmenitiesToFirebase(List<String> selectedAmenities){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            updateUserAmenities(userSnapshot.getKey(), selectedAmenities);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }


    }

    private void updateUserAmenities(String userId, List<String> selectedAmenities){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

        // Convert the list of selected pet names to a comma-separated string
        String petNamesString = TextUtils.join(", ", selectedAmenities);

        Map<String, Object> userValues = new HashMap<>();
        userValues.put("Amenities", petNamesString); // Update the database field with the selected pet names

        usersRef.child(userId).updateChildren(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data updated successfully
                        Toast.makeText(host_profile.this, "Selected Amenities  .", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update data
                        Toast.makeText(host_profile.this, "Failed to save selected Amenities.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void displaySelectedAmenities() {
        // Retrieve and display selected pet names from Firebase in the petNamesTextView
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String selectedPetNames = userSnapshot.child("Amenities").getValue(String.class);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }
    }



    // here to save the price to the firebase
    private void savePriceToFirebase(int price) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Update the "price" field for this user
                            userSnapshot.getRef().child("price").setValue(price);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }



    }

     // save the proprety tn firebase
    public void saveSelectedPropertyToFirebase(String selectedProperty) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                           updateUserProperty(userSnapshot.getKey(), selectedProperty);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }
    }


    private void updateUserProperty(String userId, String selectedProperty) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");



        Map<String, Object> userValues = new HashMap<>();
        userValues.put("Property Type", selectedProperty); // Update the database field with the selected pet names

        usersRef.child(userId).updateChildren(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data updated successfully
                        Toast.makeText(host_profile.this, "Selected proprtey  saved.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update data
                        Toast.makeText(host_profile.this, "Failed to save selected propraty.", Toast.LENGTH_SHORT).show();
                    }
                });




    }




    public void displaySelectedPropertyFromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String property = (String) userSnapshot.child("Property Type").getValue();
                            if (property != null) {
                                switch (property) {
                                    case "Apartment":
                                        radioGroup.check(R.id.Apartment);
                                        break;
                                    case "Villa":
                                        radioGroup.check(R.id.Villa);
                                        break;
                                    case "Flat":
                                        radioGroup.check(R.id.Flat);
                                        break;
                                    case "BeachHouse":
                                        radioGroup.check(R.id.BeachHouse);
                                        break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // User is not signed in
        }
    }

    private void saveSelectedOtherpetToFirebase(String OtherPetType) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            updateUserOtherPetType(userSnapshot.getKey(), OtherPetType);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // Users is not signed in
        }


    }


    private void updateUserOtherPetType(String userId, String selectedpetType){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

        // Convert the list of selected pet names to a comma-separated string


        Map<String, Object> userValues = new HashMap<>();
        userValues.put("have other pet", selectedpetType); // Update the database field with the selected pet names

        usersRef.child(userId).updateChildren(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data updated successfully
                        Toast.makeText(host_profile.this, "Selected Amenities  .", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update data
                        Toast.makeText(host_profile.this, "Failed to save selected Amenities.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void displayOtherPetFromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("host data");

            Query query = usersRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String have_other_pet = (String) userSnapshot.child("have other pet").getValue();
                            if (have_other_pet != null) {
                                switch (have_other_pet) {
                                    case "Yes":
                                        radioGroup.check(R.id.YES);
                                        break;
                                    case "No":
                                        radioGroup.check(R.id.NO);
                                        break;

                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    System.out.println("Data retrieval cancelled: " + databaseError.getMessage());
                }
            });
        } else {
            // User is not signed in
        }
    }











}