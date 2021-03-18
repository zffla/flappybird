package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.io.IOException;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FlappyBird bird = findViewById(R.id.fb_game);
        bird.setListener(new FlappyBird.GameOverListener() {
            @Override
            public void onGameOver() {
                finish();
            }
        });


    }

}
