package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class free_buy_success extends AppCompatActivity {

    ImageView  back;
    TextView dashbord;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_buy_success);

        back = findViewById(R.id.back);
        dashbord = findViewById(R.id.dashboard);



        // Change the status bar color to white and text to black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                View decorView = window.getDecorView();
                if (decorView != null) {
                    WindowInsetsController insetsController = decorView.getWindowInsetsController();
                    if (insetsController != null) {
                        insetsController.setSystemBarsAppearance(
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        );
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(free_buy_success.this, MainActivity.class));
                Animatoo.animateSwipeRight(free_buy_success.this);

            }
        });



        dashbord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                startActivity(new Intent(free_buy_success.this, myOrder.class));
                Animatoo.animateSwipeLeft(free_buy_success.this);



            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(free_buy_success.this, MainActivity.class));
        Animatoo.animateSwipeRight(free_buy_success.this);

    }
}