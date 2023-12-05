package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);


        // Get the Intent that started this activity and extract the payment details
        Intent intent = getIntent();
        String paymentId = intent.getStringExtra("paymentId");
        String state = intent.getStringExtra("state");
        String createTime = intent.getStringExtra("createTime");
        String paymentIntent = intent.getStringExtra("intent");

        // Capture the layout's TextView and set the payment details as its text
        TextView textView = findViewById(R.id.receipt_text);
        textView.setText("Payment ID: " + paymentId + "\n"
                + "State: " + state + "\n"
                + "Time Created: " + createTime + "\n"
                + "Intent: " + paymentIntent);
    }
    }
