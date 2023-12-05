package com.example.newproject;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String messageId;
    private String senderEmail;
    private String receiverEmail;
    private String messageText;
    private String status;

    // Add user-specific attributes
    private String userId; // Unique user ID
    private String hostName;// User's name
    private String ownerName;
    private String text;
    private long timestamp;

    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String messageId, String senderEmail, String receiverEmail,String status,String text, long timestamp) {
        this.messageId=messageId;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.status=status;
        this.text=text;
        this.timestamp = timestamp;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
