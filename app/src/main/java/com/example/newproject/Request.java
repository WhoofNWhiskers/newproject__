package com.example.newproject;

import java.io.Serializable;

public class Request implements Serializable {

    private String senderemail;
    private String receiveremail;
    private String message;
    private long timestamp;
    private String status;
    private String hostName;// User's name
    private String ownerName;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    private String Id;



    public Request() { }


    public Request(String senderemail, String receiveremail, String message,String hostName,String ownerName, long timestamp, String status,String Id) {
        this.senderemail = senderemail;
        this.receiveremail = receiveremail;
        this.message = message;
        this.hostName=hostName;
        this.ownerName=ownerName;
        this.timestamp = timestamp;
        this.status = status;
        this.Id=Id;
    }

    public String getSenderemail() {
        return senderemail;
    }

    public void setSenderemail(String senderemail) {
        this.senderemail = senderemail;
    }

    public String getReceiveremail() {
        return receiveremail;
    }

    public void setReceiveremail(String receiveremail) {
        this.receiveremail = receiveremail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}