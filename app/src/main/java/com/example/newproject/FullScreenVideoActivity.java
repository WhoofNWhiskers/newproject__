package com.example.newproject;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenVideoActivity extends AppCompatActivity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);


        videoView = findViewById(R.id.video_view);

        String videoUrl = getIntent().getStringExtra("videoUrl");
        if (videoUrl != null) {
            videoView.setVideoURI(Uri.parse(videoUrl));
            videoView.start();
        } else {
            Toast.makeText(this, "No video URL provided", Toast.LENGTH_SHORT).show();
        }
    }
}