package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class depositmethod extends AppCompatActivity {


    CardView bkash, nagad;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositmethod);


        bkash = findViewById(R.id.bkash);
        nagad = findViewById(R.id.nagad);




        bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(depositmethod.this, depositproccess.class));
                depositproccess.METHOD="bKash";
                Animatoo.animateSwipeLeft(depositmethod.this);
            }
        });



        nagad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(depositmethod.this, depositproccess.class));
                depositproccess.METHOD="NAGAD";
                Animatoo.animateSwipeLeft(depositmethod.this);
            }
        });





























    }
}