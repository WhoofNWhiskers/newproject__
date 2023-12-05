package com.example.newproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.wallet.PaymentsClient;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class payment extends AppCompatActivity {
    private CardView cardPaypal, cardStcPay;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "AQPS7Q-2CunhXsajj1kLbJuXVAsqet27rZQknmyETUdDug7Lv3_zixM8vQoB-qSqr-r7Di87KJJCNt9o";
    private static final int REQUEST_CODE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
    private PaymentsClient paymentsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        cardPaypal = findViewById(R.id.card_paypal);
        cardStcPay = findViewById(R.id.card_stcpay);


        Intent intent1 = new Intent(this, PayPalService.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent1);

        cardPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPayPalPayment();
            }
        });

        cardStcPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger STC Pay payment flow
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("paymentExample", confirm.toJSONObject().toString(4));

                        // Create a receipt
                        JSONObject paymentDetails = confirm.toJSONObject().getJSONObject("response");
                        String paymentId = paymentDetails.getString("id");
                        String state = paymentDetails.getString("state");
                        String createTime = paymentDetails.getString("create_time");
                        String intent = paymentDetails.getString("intent");

                        // Start ReceiptActivity
                        Intent receiptIntent = new Intent(this, ReceiptActivity.class);
                        receiptIntent.putExtra("paymentId", paymentId);
                        receiptIntent.putExtra("state", state);
                        receiptIntent.putExtra("createTime", createTime);
                        receiptIntent.putExtra("intent", intent);
                        startActivity(receiptIntent);

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }







    @Override
    protected void onDestroy() {



        stopService(new Intent(this, PayPalService.class));


        super.onDestroy();
    }


    private void launchPayPalPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.75"), "USD", "sample item",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }


}