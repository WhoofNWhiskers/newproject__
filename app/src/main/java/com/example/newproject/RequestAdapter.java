    package com.example.newproject;

    import android.content.Intent;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.cardview.widget.CardView;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.MutableData;
    import com.google.firebase.database.Transaction;
    import com.google.firebase.database.ValueEventListener;

    import org.checkerframework.checker.nullness.qual.NonNull;
    import org.checkerframework.checker.nullness.qual.Nullable;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

        private List<Request> requestList;
        private String receiverEmail;
        private String senderEmail; // Add this field






        public RequestAdapter(List<Request> requestList, String receiverEmail,String senderEmail) {

            this.requestList = requestList;
            this.receiverEmail = receiverEmail;
            this.senderEmail = senderEmail; // Initialize senderEmail

            // Create an instance of ChatActivity and pass senderEmail


            // Initialize the ChatActivity instance as needed





        }




        @NonNull
        @Override
        public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newitem, parent, false);
            return new RequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {

            Request request = requestList.get(position);


            //holder.senderNameTextView.setText(request.getSenderemail());


            String senderEmail = request.getSenderemail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("new data");
            usersRef.orderByChild("email").equalTo(senderEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Owner owner = userSnapshot.getValue(Owner.class);

                            // Get the owner's first and second name
                            String firstName = owner.getFirst_name();
                            String secondName = owner.getSeconed_name();

                            // Concatenate the first name and second name to get the full name
                            String fullName = firstName + " " + secondName;

                            // Set the text of the senderNameTextView to the owner's full name
                            holder.senderNameTextView.setText(fullName);
                        }
                    } else {
                        Log.e("RequestAdapter", "User not found with email: " + senderEmail);
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    Log.e("RequestAdapter", "Failed to query user data", error.toException());
                }
            });




















            holder.messageTextView.setText(request.getMessage());
            holder.acceptButton.setOnClickListener(v -> {
                request.setStatus("accpeted");
                updateRequestStatus(request);

                Toast.makeText(holder.declineButton.getContext(), "Request accpeted", Toast.LENGTH_SHORT).show();

               addHostToHostList(request);






            });
            holder.declineButton.setOnClickListener(v -> {
                request.setStatus("declined");
                updateRequestStatus(request);
                Toast.makeText(holder.declineButton.getContext(), "Request declined", Toast.LENGTH_SHORT).show();

            });

            // Pass the owner email to the profile activity
            holder.card_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request request = requestList.get(holder.getAdapterPosition());
                    String senderEmail = request.getSenderemail();
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("new data");
                    usersRef.orderByChild("email").equalTo(senderEmail).addListenerForSingleValueEvent(new ValueEventListener(){

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String userId = userSnapshot.getKey();

                                    String firstName = userSnapshot.child("first_name").getValue(String.class);
                                    String gender = userSnapshot.child("gender").getValue(String.class);
                                    String age = userSnapshot.child("age").getValue(String.class);
                                    String address = userSnapshot.child("address").getValue(String.class);
                                    String additional_info = userSnapshot.child("additional_info").getValue(String.class);

                                    //pet
                                    String petName = userSnapshot.child("pet_name").getValue(String.class);
                                    String petGender = userSnapshot.child("pet_gender").getValue(String.class);
                                    String petAge = userSnapshot.child("pet_age").getValue(String.class);
                                    String petColor = userSnapshot.child("pet_color").getValue(String.class);
                                    String petSpecies = userSnapshot.child("pet_species").getValue(String.class);
                                    String petType = userSnapshot.child("pet_type").getValue(String.class);
                                    String additional_ifo_pet = userSnapshot.child("additional_info_pet").getValue(String.class);
                                    String imageUrl = userSnapshot.child("imageUrl").getValue(String.class);

                                    Intent intent = new Intent(v.getContext(), owner_profile_uneditable.class);
                                    //intent.putExtra("userId", userId);
                                    intent.putExtra("first_name", firstName);
                                    intent.putExtra("gender", gender);
                                    intent.putExtra("age", age);
                                    intent.putExtra("address", address);
                                    intent.putExtra("additional_info", additional_info);
                                    intent.putExtra("pet_name", petName);
                                    intent.putExtra("pet_gender", petGender);
                                    intent.putExtra("pet_age", petAge);
                                    intent.putExtra("pet_color", petColor);
                                    intent.putExtra("pet_species", petSpecies);
                                    intent.putExtra("pet_type", petType);
                                    intent.putExtra("additional_info_pet", additional_ifo_pet);
                                    intent.putExtra("imageUrl", imageUrl);
                                    v.getContext().startActivity(intent);

                                }
                            }else {
                                Log.e("RequestAdapter", "User not found with email: " + senderEmail);
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                            Log.e("RequestAdapter", "Failed to query user data", error.toException());

                        }

                    });


                }
            });



        }


        @Override
        public int getItemCount() {
            return requestList.size();
        }


        public void setRequestList(List<Request> requestList) {
            this.requestList = requestList;
            notifyDataSetChanged();
        }

        static class RequestViewHolder extends RecyclerView.ViewHolder {
            TextView senderNameTextView;
            TextView messageTextView;
            Button acceptButton;
            Button declineButton;
            CardView card_profile;

            RequestViewHolder(@NonNull View itemView) {
                super(itemView);
                senderNameTextView = itemView.findViewById(R.id.sender_name);
                messageTextView = itemView.findViewById(R.id.message);
                acceptButton = itemView.findViewById(R.id.accept_button);
                declineButton = itemView.findViewById(R.id.decline_button);
                card_profile = itemView.findViewById(R.id.newitem);


            }




        }

        private void updateRequestStatus(Request request) {
            DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
            String requestId = request.getId();
            String newStatus = "accepted";
            Log.d("RequestAdapter", "Updating request status for request with id " + requestId);
            String receiverEmail1 = request.getReceiveremail();
            requestsRef.child(requestId).setValue(request)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("RequestAdapter", "Request status updated successfully");


                        DatabaseReference receiverDataRef = FirebaseDatabase.getInstance().getReference("host data");
                        receiverDataRef.orderByChild("email").equalTo(receiverEmail1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        String userId = userSnapshot.getKey();
                                        DatabaseReference userCounterRef = receiverDataRef.child(userId).child("counter");
                                        userCounterRef.runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                Integer counterValue = mutableData.getValue(Integer.class);
                                                if (counterValue == null) {
                                                    counterValue = 0;
                                                }
                                                //
                                               // int updatedCounterValue = counterValue + 1;
                                                mutableData.setValue(counterValue + 1);
                                               // mutableData.setValue(String.valueOf(updatedCounterValue));
                                                Map<String, Object> userValues = new HashMap<>();
                                                userValues.put("first_name", receiverEmail1);
                                                return Transaction.success(mutableData);

                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                                if (committed && error == null && currentData != null) {
                                                    Log.d("RequestAdapter", "Counter updated successfully for receiver email: " + receiverEmail);
                                                } else {
                                                    Log.e("RequestAdapter", "Failed to update counter for receiver email: " + receiverEmail, error != null ? error.toException() : null);
                                                }
                                            }
                                        });//new
                                    }
                                } else {
                                    Log.e("RequestAdapter", "Users not found with email: " + receiverEmail);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("RequestAdapter", "Failed to query user data", databaseError.toException());
                            }
                        });




                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure, e.g., show a toast or a dialog
                        Log.e("RequestAdapter", "Failed to update request status", e);
                    });
        }


        private void addHostToHostList(Request request) {
            // Assuming you have a Firebase Realtime Database reference
            DatabaseReference hostListReference = FirebaseDatabase.getInstance().getReference("host_contacts");

            // Create a new host object or retrieve it based on your data structure
            // You might need to customize this based on your actual data structure
            ChatMessage host = new ChatMessage();
            host.setUserId(request.getId());
            host.setReceiverEmail(request.getReceiveremail());
            host.setSenderEmail(request.getSenderemail());
            host.setHostName(request.getHostName());
            host.setOwnerName(request.getOwnerName());

            // Add the host to the host list
            hostListReference.child(host.getUserId()).setValue(host);
        }

    }