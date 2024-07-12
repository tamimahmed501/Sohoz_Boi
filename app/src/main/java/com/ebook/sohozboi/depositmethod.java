package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class depositmethod extends AppCompatActivity {


    CardView bkash, nagad, stripe;
    RelativeLayout bkash_layout, nagad_layout, stripe_layout;


    public static String CURRENCY = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositmethod);


        bkash = findViewById(R.id.bkash);
        nagad = findViewById(R.id.nagad);
        bkash_layout = findViewById(R.id.bkashbg);
        nagad_layout = findViewById(R.id.nagadbg);
        stripe = findViewById(R.id.stripe);
        stripe_layout = findViewById(R.id.stripebg);




        if (CURRENCY.contains("BDT")){


            bkash_layout.setBackground(getResources().getDrawable(R.drawable.paymentbf2));
            nagad_layout.setBackground(getResources().getDrawable(R.drawable.paymentbf2));
            stripe_layout.setBackground(getResources().getDrawable(R.drawable.paymentbf));

            bkash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    startActivity(new Intent(depositmethod.this, depositproccess.class));
                    depositproccess.METHOD="bKash";
                    Animatoo.animateSwipeLeft(depositmethod.this);
                    depositproccess.TYPE="bKash";
                }
            });



            nagad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(depositmethod.this, depositproccess.class));
                    depositproccess.METHOD="NAGAD";
                    Animatoo.animateSwipeLeft(depositmethod.this);
                    depositproccess.TYPE="Nagad";
                }
            });


        } else {


            bkash_layout.setBackground(getResources().getDrawable(R.drawable.paymentbf));
            nagad_layout.setBackground(getResources().getDrawable(R.drawable.paymentbf));
            stripe_layout.setBackground(getResources().getDrawable(R.drawable.paymentbf2));

            stripe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(depositmethod.this, "Stripe Payment Coming Soon", Toast.LENGTH_SHORT).show();
                }
            });

        }










    }
}