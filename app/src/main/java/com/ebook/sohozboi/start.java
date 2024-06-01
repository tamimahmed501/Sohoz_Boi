package com.ebook.sohozboi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class start extends AppCompatActivity {


    private static final int DELAY_MILLIS = 3000; // 3 seconds delay



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);




        Animatoo.animateFade(start.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(start.this, MainActivity.class);
                startActivity(intent);
                finish();
                Animatoo.animateSwipeLeft(start.this);
            }
        }, DELAY_MILLIS);




    }
}