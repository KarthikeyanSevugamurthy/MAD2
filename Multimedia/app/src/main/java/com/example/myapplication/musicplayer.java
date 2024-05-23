package com.example.myapplication;

import static com.example.myapplication.R.*;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class musicplayer extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_musicplayer);

        Button playButton = findViewById(id.playButton);
        Button pauseButton = findViewById(id.pauseButton);
        Button stopButton = findViewById(id.stopButton);
        seekBar = findViewById(id.seekBar);

        mediaPlayer = MediaPlayer.create(this, raw.song2);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start(); // Start playback if not already playing
                        updateSeekBar();
                    }
                } else {
                    // Create and start MediaPlayer if it's null
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), raw.song2);
                    mediaPlayer.start();
                    updateSeekBar();
                }
            }
        });


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // Pause playback if playing
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset(); // Reset MediaPlayer to its initial state
                    seekBar.setProgress(0); // Reset SeekBar progress
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

        // Set up SeekBar listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pause the MediaPlayer when the user starts dragging the SeekBar
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    handler.removeCallbacks(seekBarUpdateRunnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Resume the MediaPlayer when the user stops dragging the SeekBar
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    updateSeekBar();
                }
            }
        });
    }

    private void updateSeekBar() {
        if (mediaPlayer != null) {
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(seekBarUpdateRunnable, 1000); // Update SeekBar every second
        }
    }

    private Runnable seekBarUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(seekBarUpdateRunnable);
    }
}
