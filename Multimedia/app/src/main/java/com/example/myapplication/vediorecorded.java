package com.example.myapplication;



import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class vediorecorded extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar seekBar;
    private MediaController mediaController;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vediorecorded);

        // Initialize VideoView
        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar); // Initialize SeekBar
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample));

        // Initialize MediaController
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set up SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.start();
            }
        });

        // Start updating SeekBar
        updateSeekBar();
    }

    private void updateSeekBar() {
        seekBar.setMax(videoView.getDuration());
        seekBar.setProgress(videoView.getCurrentPosition());

        // Schedule the next update in 1 second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
            }
        }, 1000);
    }

    public void playVideo(View v) {
        videoView.start();
    }

    public void pauseVideo(View v) {
        videoView.pause();
    }

    public void stopVideo(View v) {
        videoView.stopPlayback();
        videoView.resume(); // Reset the video to the beginning
    }
}
