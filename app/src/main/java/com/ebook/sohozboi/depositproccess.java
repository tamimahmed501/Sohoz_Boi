package com.ebook.sohozboi;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class depositproccess extends AppCompatActivity {

    TextInputEditText amount, tid;

    LinearLayout addmoney;
    TextInputLayout pnumber;


    ImageView backbutton;


    TextView bknumber, nagadnumber;

    public static String METHOD = "";


    TextView title, seeproccess;
    ImageView logo;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositproccess);


        amount = findViewById(R.id.amount);
        tid = findViewById(R.id.tid);
        addmoney = findViewById(R.id.payment);
        backbutton = findViewById(R.id.backbutton);
        bknumber = findViewById(R.id.bknumber);
        nagadnumber = findViewById(R.id.nagadnumber);

        title = findViewById(R.id.title);
        logo = findViewById(R.id.logo);
        bknumber = findViewById(R.id.bknumber);
        seeproccess = findViewById(R.id.proccess);
        pnumber = findViewById(R.id.pnumber);


        if (!METHOD.isEmpty()){

            if (METHOD.contains("bKash")){

                logo.setImageDrawable(getDrawable(R.drawable.bkash));
                title.setText("Buy with Bkash");
                pnumber.setHint("Bkash Number");


            } else {



                logo.setImageDrawable(getDrawable(R.drawable.nagadlogo));
                title.setText("Buy with Nagad");
                pnumber.setHint("Nagad Number");


            }



        } else {



        }

        seeproccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(depositproccess.this, deposit.class));
                Animatoo.animateSwipeLeft(depositproccess.this);



            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                Animatoo.animateSwipeRight(depositproccess.this);
            }
        });



        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myAmount = amount.getText().toString();
                String myTid = tid.getText().toString();

                if (myAmount.isEmpty()) {
                    amount.setError("Amount cannot be empty");
                } else if (myTid.isEmpty()) {
                    tid.setError("Transaction ID cannot be empty");
                } else {
                    SharedPreferences sharedPreferencesx = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                    String walletid = sharedPreferencesx.getString("walletid", "");



                    Dialog dialog = new Dialog(depositproccess.this);
                    dialog.setContentView(R.layout.load);
                    dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alertbackground));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    dialog.show();





                    String url = "https://halalcash.net/apps/exwallet/deposit.php?walletid=" + walletid + "&tid=" + myTid + "&amount=" + myAmount + "&method="+METHOD;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            dialog.dismiss();

                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("status")) {
                                    String status = jsonResponse.getString("status");

                                    if ("success".equals(status)) {




                                        startActivity(new Intent(depositproccess.this, paymentsuccess.class));
                                        Animatoo.animateSwipeLeft(depositproccess.this);




                                    } else {
                                        // Handle other status
                                        Toast.makeText(depositproccess.this, "Wrong Information", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(depositproccess.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(depositproccess.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Toast.makeText(depositproccess.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(depositproccess.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(depositproccess.this);
    }
}