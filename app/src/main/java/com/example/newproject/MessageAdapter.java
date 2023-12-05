package com.example.newproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Message> messages;
    private String currentSenderEmail;


    public MessageAdapter(Context context, List<Message> messages, String currentSenderEmail) {
        this.context = context;
        this.messages = messages;
        this.currentSenderEmail = currentSenderEmail;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message2, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);

        if ((isImageUrl(message.getMessage()) || isVideoUrl(message.getMessage()))) {
            // This is an image or a video message
            if (holder.imageViewChat != null) {
                holder.imageViewChat.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getMessage())
                        .override(100, 100)
                        .centerCrop()
                        .into(holder.imageViewChat);
                holder.imageViewChat.setOnClickListener(v -> {
                    Intent intent;
                    if (isVideoUrl(message.getMessage())) {
                        intent = new Intent(context, FullScreenVideoActivity.class);
                        intent.putExtra("videoUrl", message.getMessage());
                    } else {
                        intent = new Intent(context, FullSizeImageActivity.class);
                        intent.putExtra("IMAGE_URL", message.getMessage());
                    }
                    context.startActivity(intent);
                });
            }
            if (holder.showMessage != null) {
                holder.showMessage.setVisibility(View.GONE);
            }
            if (holder.showMessage1 != null) {
                holder.showMessage1.setVisibility(View.GONE);
            }
        }else {
            if (holder.imageViewChat != null) {
                holder.imageViewChat.setVisibility(View.GONE);
            }


            if (getItemViewType(position) == MSG_TYPE_RIGHT) {
                if (holder.showMessage != null) {
                    holder.showMessage.setText(message.getMessage());
                    holder.showMessage.setVisibility(View.VISIBLE);
                }
                if (holder.showMessage1 != null) {
                    holder.showMessage1.setVisibility(View.GONE);
                }
                if (holder.tvSenderName != null) {
                    holder.tvSenderName.setText(currentSenderEmail);
                }


            } else {
                if (holder.showMessage1 != null) {
                    holder.showMessage1.setText(message.getMessage());
                    holder.showMessage1.setVisibility(View.VISIBLE);
                }
                if (holder.showMessage != null) {
                    holder.showMessage.setVisibility(View.GONE);
                }
                if (holder.tvSenderName != null) {
                    holder.tvSenderName.setText(message.getSenderId());
                }


            }

        }



    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        Log.d("MessageAdapter", "Message: " + message.getMessage() + ", Sender: " + message.getSenderId() + ", Current: " + currentSenderEmail);

        if (message.getSenderId().trim().equalsIgnoreCase(currentSenderEmail.trim())) {
            Log.d("MessageAdapter", "Returning MSG_TYPE_RIGHT");
            return MSG_TYPE_RIGHT; // this is a sent message
        } else {
            return MSG_TYPE_LEFT; // this is a received message
        }
    }

    private boolean isImageUrl(String url) {
        return url != null && (
                url.endsWith(".jpg") ||
                        url.endsWith(".jpeg") ||
                        url.endsWith(".png") ||
                        url.contains("firebasestorage.googleapis.com")
        );
    }
    private boolean isVideoUrl(String url) {
        return url != null && (
                url.endsWith(".mp4") ||
                        url.endsWith(".3gp") ||
                        url.endsWith(".mkv") ||
                        url.endsWith(".webm")
        );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public TextView showMessage1;
        public TextView tvSenderName;
        public ImageView imageViewChat; // Add this



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            showMessage1 = itemView.findViewById(R.id.show_message1);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            imageViewChat = itemView.findViewById(R.id.image_view_chat); // Add this


        }
    }
}