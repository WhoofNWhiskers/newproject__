package com.example.newproject;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullSizeImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_image);
        setContentView(R.layout.activity_full_size_image);

        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        ImageView imageView = findViewById(R.id.full_size_image);

        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

    }
