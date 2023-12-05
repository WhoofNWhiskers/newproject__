package com.example.newproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class petTracker extends AppCompatActivity {


    OkHttpClient client;
    String getURL = "https://blynk.cloud/external/api/get?token=xfmCx1wl1OsLuAW2bPXaekIn8EiYgy0r&V1";
    String getURL2 = "https://blynk.cloud/external/api/get?token=xfmCx1wl1OsLuAW2bPXaekIn8EiYgy0r&V2";
    String postURL = " ";
    TextView textView;
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_tracker);

        client = new OkHttpClient();
        textView = findViewById(R.id.textData);
        textView2 = findViewById(R.id.textData2);

        Button buttonGet = findViewById(R.id.btnGet);


        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();//get latitude
                get2();//get longitude
                //intent.setData(Uri.parse("https://maps.google.com/maps/place/"+textView.getText()+","+textView2.getText()));
                gotoUrl("https://maps.google.com/maps/place/" + textView.getText() + "," + textView2.getText());

            }
        });




    }

    public void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void get() {

        //Request request = new Request.Builder().url(getURL).build();
        okhttp3.Request request=new okhttp3.Request.Builder().url(getURL).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            textView.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                });
            }
        });


    }


    public void get2() {

        okhttp3.Request request=new okhttp3.Request.Builder().url(getURL2).build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            textView2.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                });
            }
        });


}


}