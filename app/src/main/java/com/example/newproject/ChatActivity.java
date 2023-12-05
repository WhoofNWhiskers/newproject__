package com.example.newproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity {
    private String receiverEmail, senderEmail;
    private EditText editTextMessage;
    private FrameLayout buttonSend;
    private FrameLayout buttonSelectFile;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private String chatRoomId;
    private TextView textViewNames;

    private String otherUserId;

    private Button payment;


    private DatabaseReference senderUserStatusRef, receiverUserStatusRef;
    private ValueEventListener senderStatusListener, receiverStatusListener;

    TextView textViewSenderStatus, textViewReceiverStatus;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_view);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        buttonSelectFile = findViewById(R.id.button_select_file);
        textViewNames = findViewById(R.id.tv_sender_name);
        TextView textViewSenderStatus, textViewReceiverStatus;
        payment=findViewById(R.id.pay);

        buttonSelectFile.setOnClickListener(view -> selectFile());

        messages = new ArrayList<>();














        // Step 1: Get sender and receiver emails from the intent
        /*Intent intent = getIntent();
        if (intent != null) {
            senderEmail = intent.getStringExtra("senderEmail");
            receiverEmail = intent.getStringExtra("receiverEmail");
            //String senderName = intent.getStringExtra("senderName");
            //String receiverName = intent.getStringExtra("receiverName");
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(senderEmail)) {
                textViewNames.setText(receiverEmail);
                otherUserId = receiverEmail;
            } else {
                textViewNames.setText(senderEmail);
                otherUserId = senderEmail;
            }


            chatRoomId = generateChatRoomId(senderEmail, receiverEmail); // Generate a unique chat room ID
        }*/


        Intent intent = getIntent();
        if (intent != null) {
            senderEmail = intent.getStringExtra("senderEmail");
            receiverEmail = intent.getStringExtra("receiverEmail");

            // Initialize the DatabaseReference
            DatabaseReference newDataRef = FirebaseDatabase.getInstance().getReference("new_data");
            DatabaseReference hostDataRef = FirebaseDatabase.getInstance().getReference("host_data");

            // Fetch owner name and set it in textViewNames
            newDataRef.child(receiverEmail.replace(".",",")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Owner owner = dataSnapshot.getValue(Owner.class);
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(senderEmail)) {
                        textViewNames.setText(owner.getFirst_name());
                        otherUserId = receiverEmail;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });

            // Fetch host name and set it in textViewNames
            hostDataRef.child(senderEmail.replace(".",",")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Host host = dataSnapshot.getValue(Host.class);
                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(senderEmail)) {
                        textViewNames.setText(host.getFirst_name());
                        otherUserId = senderEmail;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });

            chatRoomId = generateChatRoomId(senderEmail, receiverEmail); // Generate a unique chat room ID
        }





        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        buttonSend.setOnClickListener(view -> {
            String messageText = editTextMessage.getText().toString();
            if (!messageText.trim().isEmpty()) {
                sendMessage(messageText);
                editTextMessage.setText(""); // Clear the text field after sending the message
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        readMessages();
        setupMessageSending();
        setupMessageReading();


        //payment section





        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, payment.class);
                startActivity(intent);


            }
        });




    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();

            // Upload the file to Firebase Storage and get the download URL
            // Then, call sendMessage(fileUrl)
            uploadFile(fileUri);



        }






    }

    private void sendMessage(String messageText) {
        if (messageText == null || messageText.trim().isEmpty()) {
            return;
        }

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Assume you're using FirebaseAuth

        // Determine the sender and receiver based on the current user
        String senderId, receiverId;
        if (currentUserId.equals(senderEmail)) {
            senderId = senderEmail;
            receiverId = receiverEmail;

        } else {
            senderId = receiverEmail;
            receiverId = senderEmail;

        }

        Message newMessage = new Message(senderId, receiverId, messageText);
        databaseReference.child("Chats").child(encodeForFirebaseKey(chatRoomId)).push().setValue(newMessage);

        Log.d("ChatActivity", "Sent message: " + messageText + ", Sender: " + senderId + ", Receiver: " + receiverId);

        editTextMessage.setText("");
    }

    private void readMessages() {
        databaseReference.child("Chats").child(encodeForFirebaseKey(chatRoomId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);

                    if (message.getSenderId() != null && message.getReceiverId() != null) {
                        if (!message.getSenderId().equalsIgnoreCase(message.getReceiverId()) &&
                                ((message.getSenderId().equalsIgnoreCase(senderEmail) && message.getReceiverId().equalsIgnoreCase(receiverEmail)) ||
                                        (message.getSenderId().equalsIgnoreCase(receiverEmail) && message.getReceiverId().equalsIgnoreCase(senderEmail)))) {
                            messages.add(message);
                        }
                    }
                }

                messageAdapter = new MessageAdapter(ChatActivity.this, messages, senderEmail);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ChatActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }





    // Function to generate a unique chat room ID based on user emails
    private String generateChatRoomId(String userEmail1, String userEmail2) {
        // You can use a combination of user emails, for example, for simplicity
        return userEmail1 + "_" + userEmail2;
    }
    // Function to encode a string for use as a Firebase key
    private String encodeForFirebaseKey(String s) {
        if (s == null) {
            return "";
        }
        Pattern p = Pattern.compile("[^a-z0-9A-Z]");
        Matcher m = p.matcher(s);
        return m.replaceAll("_");
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadFile(Uri fileUri) {
        if (fileUri == null) {
            // Handle the case where the fileUri is null or invalid
            Log.e("ChatActivity", "Invalid file URI");
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String uniqueFileName = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child("uploads/" + uniqueFileName);

        UploadTask uploadTask = fileReference.putFile(fileUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // File successfully uploaded, you can now get the download URL and send your message
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                sendMessage(downloadUrl);
            }).addOnFailureListener(e -> {
                // Handle any errors when retrieving the download URL
                Log.e("ChatActivity", "Error getting download URL: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            // Handle the upload failure
            if (e instanceof StorageException) {
                StorageException storageException = (StorageException) e;
                if (storageException.getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    // Handle object not found exception
                    Log.e("ChatActivity", "Object not found: " + e.getMessage());
                } else if (storageException.getErrorCode() == StorageException.ERROR_RETRY_LIMIT_EXCEEDED) {
                    // Handle retry limit exceeded exception
                    Log.e("ChatActivity", "Retry limit exceeded: " + e.getMessage());
                } else {
                    // Handle generic exceptions
                    Log.e("ChatActivity", "Upload failed: " + e.getMessage());
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (senderUserStatusRef != null && senderStatusListener != null) {
            senderUserStatusRef.removeEventListener(senderStatusListener);
        }

        if (receiverUserStatusRef != null && receiverStatusListener != null) {
            receiverUserStatusRef.removeEventListener(receiverStatusListener);
        }



        super.onDestroy();
    }
    private void setupMessageSending() {
        buttonSend.setOnClickListener(view -> {
            String messageText = editTextMessage.getText().toString();
            if (!messageText.trim().isEmpty()) {
                sendMessage(messageText);
                clearMessageField(); // Clear the text field after sending the message
            }
        });
    }

    private void setupMessageReading() {
        readMessages();
    }










    private void clearMessageField() {
        editTextMessage.setText("");
    }








}