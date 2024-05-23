package com.example.myapplication;


import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Playback extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaRecorder mediaRecorder;
    private SurfaceHolder surfaceHolder;
    private Button buttonRecord;
    private Button buttonPlayback;
    private boolean isRecording = false;
    private String outputFile;
    private int rotationDegree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_vedio);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        buttonRecord = findViewById(R.id.buttonrecord);
        buttonPlayback = findViewById(R.id.buttonplayback);

        outputFile = getExternalCacheDir().getAbsolutePath() + "/recorded_video.mp4";
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (isRecording) {
            stopRecording();
        }
        releaseMediaRecorder();
    }

    public void toggleRecording(View view) {
        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        if (prepareMediaRecorder()) {
            try {
                mediaRecorder.start();
                isRecording = true;
                buttonRecord.setText("Stop Recording");
                buttonPlayback.setVisibility(View.GONE);
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                releaseMediaRecorder();
                isRecording = false;
                buttonRecord.setText("Record");
                buttonPlayback.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to prepare MediaRecorder", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            buttonRecord.setText("Record");
            buttonPlayback.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoSize(320, 240); // Adjust size as needed
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setOrientationHint(90); // Set orientation

        // Set the preview display for the media recorder
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

        // Set the orientation hint based on device orientation
        setOrientationHint();

        try {
            mediaRecorder.prepare();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
    }

    private void setOrientationHint() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotationDegree = (info.orientation + degrees) % 360;
            rotationDegree = (360 - rotationDegree) % 360;  // compensate the mirror
        } else {  // back-facing
            rotationDegree = (info.orientation - degrees + 360) % 360;
        }
    }

    public void playbackVideo(View view) {
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(outputFile);
        videoView.start();

        // Show the Exit button when video playback starts
        Button buttonExit = findViewById(R.id.buttonexit);
        buttonExit.setVisibility(View.VISIBLE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;

        // Set the preview display for the media recorder
        if (mediaRecorder != null) {
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void exitApp(View view) {
        finish(); // Close the activity
    }
}