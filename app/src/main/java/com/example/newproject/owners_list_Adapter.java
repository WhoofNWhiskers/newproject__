package com.example.newproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class owners_list_Adapter extends RecyclerView.Adapter<owners_list_Adapter.ContactViewHolder> implements SwipeToDeleteCallback.SwipeToDeleteListener {
    private List<ChatMessage> contacts;
    private Context context;
    private SwipeToDeleteCallback.SwipeToDeleteListener swipeToDeleteListener;

    public void onSwipe(int position) {
        // Handle the swipe action and remove the item from the list
        if (position >= 0 && position < contacts.size()) {
            contacts.remove(position);
            notifyItemRemoved(position);
        }
    }


    public owners_list_Adapter(List<ChatMessage> contacts, Context context,SwipeToDeleteCallback.SwipeToDeleteListener listener) {
        this.contacts = contacts;
        this.context = context;
        this.swipeToDeleteListener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_owner_item, parent, false);
        return new ContactViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ChatMessage contact = contacts.get(position);
        holder.contactNameTextView1.setText(contact.getReceiverEmail());


        // Add an OnClickListener to each contact item to open the chat interface
        holder.itemView.setOnClickListener(v -> {
            //Handle opening the chat interface (livechat_Activtiy) with this contact's data
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("senderEmail", contact.getSenderEmail()); // Replace with the sender's email
            intent.putExtra("receiverEmail", contact.getReceiverEmail());
            context.startActivity(intent);
        });
        // Implement swipe-to-delete here
        final float[] initialTouchX = new float[1];
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Record the initial touch position
                    initialTouchX[0] = event.getRawX();
                    break;

                case MotionEvent.ACTION_UP:
                    // Calculate the distance of the swipe
                    float deltaX = event.getRawX() - initialTouchX[0];

                    // Set a threshold to distinguish between a swipe to open chat and a swipe to delete
                    float swipeThreshold = 100; // Adjust this threshold as needed

                    if (Math.abs(deltaX) < swipeThreshold) {
                        // The swipe is within the threshold, open the chat interface
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("senderEmail", contact.getSenderEmail());
                        intent.putExtra("receiverEmail", contact.getReceiverEmail());
                        context.startActivity(intent);
                    } else {
                        // The swipe is beyond the threshold, trigger swipe-to-delete
                        swipeToDeleteListener.onSwipe(holder.getAdapterPosition());
                    }
                    break;
            }
            return true;
        });



    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactNameTextView1;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNameTextView1 = itemView.findViewById(R.id.name1); // Corrected to match the layout
        }
    }

    public void setContacts(List<ChatMessage> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
    public void removeContact(int position) {
        if (position >= 0 && position < contacts.size()) {
            contacts.remove(position);
            notifyItemRemoved(position);
        }
    }

}
