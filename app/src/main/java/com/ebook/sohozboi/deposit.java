package com.ebook.sohozboi;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class deposit extends AppCompatActivity {





    ImageView backbutton;

    ImageView copy1, copy2;
    TextView bknumber, nagadnumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);



        backbutton = findViewById(R.id.backbutton);
        copy1 = findViewById(R.id.copy1);
        copy2 = findViewById(R.id.copy2);
        bknumber = findViewById(R.id.bknumber);
        nagadnumber = findViewById(R.id.nagadnumber);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                Animatoo.animateSwipeRight(deposit.this);
            }
        });








        String url = "https://www.halalcash.net/apps/exwallet/admin/pmethod.php";


        RequestQueue requestQueue = Volley.newRequestQueue(deposit.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String bkash = jsonObject.getString("bkash");
                                String nagad = jsonObject.getString("nagad");


                                bknumber.setText(bkash);
                                nagadnumber.setText(nagad);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(deposit.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(deposit.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);























        copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobilex = bknumber.getText().toString();

                // Get the clipboard manager using the context
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                // Create a ClipData object to store the copied data
                ClipData clip = ClipData.newPlainText("MobileX", mobilex);

                Toast.makeText(deposit.this, "Bkash number copied", Toast.LENGTH_SHORT).show();

                // Set the data to the clipboard
                clipboard.setPrimaryClip(clip);

            }
        });


        copy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobilex = nagadnumber.getText().toString();

                // Get the clipboard manager using the context
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                // Create a ClipData object to store the copied data
                ClipData clip = ClipData.newPlainText("MobileX", mobilex);

                Toast.makeText(deposit.this, "Nagad number copied", Toast.LENGTH_SHORT).show();

                // Set the data to the clipboard
                clipboard.setPrimaryClip(clip);

            }
        });



    }



}