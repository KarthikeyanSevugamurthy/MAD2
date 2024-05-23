package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class media extends AppCompatActivity {

    private VideoView videoView;
    private Button play, record, stop, pause, resume,musicbtn,vediobtn,vediorecbtn;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1000);
        }

        videoView = findViewById(R.id.videoView);
        play = findViewById(R.id.playButton);
        record = findViewById(R.id.recordButton);
        stop = findViewById(R.id.stopButton);
        pause = findViewById(R.id.pauseButton);
        resume = findViewById(R.id.resumeButton);
        musicbtn=findViewById(R.id.music);
        vediobtn=findViewById(R.id.vedio);
        vediorecbtn=findViewById(R.id.vediorec);
        setupVideoPlayback();

        play.setOnClickListener(v -> playAudio());
        pause.setOnClickListener(v -> pauseRecording());
        resume.setOnClickListener(v -> resumeRecording());
        record.setOnClickListener(v -> startRecording());
        stop.setOnClickListener(v -> stopRecording());
        musicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(media.this,musicplayer.class);
                startActivity(i);
            }
        });
        vediobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(media.this,vediorecorded.class);
                startActivity(i);
            }
        });
        vediorecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(media.this,Recording_vedio.class);
                startActivity(i);
            }
        });
    }


    private void setupVideoPlayback() {
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vedio;
        Uri videoUri = Uri.parse(videoPath);

        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();
    }

    private void playAudio() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Playing Audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void startRecording() {
        try {
            audioFilePath = getAudioFilePath();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Starting Recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder.pause();
            Toast.makeText(this, "Recording Paused", Toast.LENGTH_SHORT).show();
        }
    }

    private void resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder.resume();
            Toast.makeText(this, "Recording Resumed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
    }

    private String getAudioFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File audioFile = new File(musicDir, "record.mp3");
        return audioFile.getPath();
    }
}

