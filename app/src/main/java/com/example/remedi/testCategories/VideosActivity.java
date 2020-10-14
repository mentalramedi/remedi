package com.example.remedi.testCategories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.remedi.R;

public class VideosActivity extends AppCompatActivity {

    private VideoView vidPlayer;
    private ImageButton playBtn;
    private ProgressBar videoprogress;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        vidPlayer = (VideoView) findViewById(R.id.videoView);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        videoprogress = (ProgressBar) findViewById(R.id.videoprogressBar);
        videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/remedi-82a3f.appspot.com/o/How%20to%20build%20Social%20media%20App%20like%20Snapchat_Instagram_Twitter%20-%20Tutorial%2010%20-%20Logout%20-%20YouTube.mp4?alt=media&token=14af8266-7075-48d1-8f5d-4ffbc34cb3bb");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.videos_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Videos");
        vidPlayer.setVideoURI(videoUri);
        vidPlayer.requestFocus();
        vidPlayer.start();


    }
}
