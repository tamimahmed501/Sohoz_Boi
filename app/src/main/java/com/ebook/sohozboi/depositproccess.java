package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class depositproccess extends AppCompatActivity {

    TextInputEditText number, tid;
    LinearLayout payment;
    TextInputLayout pnumber;
    ImageView backbutton;
    TextView bknumber, nagadnumber, title, seeprocess;
    ImageView logo;
    LottieAnimationView lottie;

    public static String METHOD = "";
    private static final String PREF_NAME = "MyAppPrefs";
    public static String BOOKID = "";
    public static String AMOUNT = "";
    public static String CURRENCY = "";
    public static String TYPE = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositproccess);

        tid = findViewById(R.id.tid);
        number = findViewById(R.id.number);
        payment = findViewById(R.id.payment);
        backbutton = findViewById(R.id.backbutton);
        bknumber = findViewById(R.id.bknumber);
        nagadnumber = findViewById(R.id.nagadnumber);
        title = findViewById(R.id.title);
        logo = findViewById(R.id.logo);
        seeprocess = findViewById(R.id.proccess);
        pnumber = findViewById(R.id.pnumber);
        lottie = findViewById(R.id.lottie);



        if (!METHOD.isEmpty()) {
            if (METHOD.contains("bKash")) {
                logo.setImageDrawable(getDrawable(R.drawable.bkash));
                title.setText("Buy with Bkash");
                pnumber.setHint("Bkash Number");
            } else {
                logo.setImageDrawable(getDrawable(R.drawable.nagadlogo));
                title.setText("Buy with Nagad");
                pnumber.setHint("Nagad Number");
            }
        }

        seeprocess.setOnClickListener(v -> {
            startActivity(new Intent(depositproccess.this, deposit.class));
            Animatoo.animateSwipeLeft(depositproccess.this);
        });

        backbutton.setOnClickListener(v -> {
            onBackPressed();
            Animatoo.animateSwipeRight(depositproccess.this);
        });

        payment.setOnClickListener(v -> {
            String number1 = number.getText().toString();
            String tid1 = tid.getText().toString();

            if (number1.length() == 11) {
                if (!tid1.isEmpty()) {
                    lottie.setVisibility(View.VISIBLE);

                    // URL of the API
                    String url = "https://sohozboi-server.vercel.app/api/v1/order/create";

                    // Create JSON object for the request body
                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("books", new JSONArray(Arrays.asList(BOOKID)));
                        requestBody.put("totalAmount", AMOUNT);
                        requestBody.put("currency", CURRENCY);

                        JSONObject paymentDetails = new JSONObject();
                        paymentDetails.put("method", "manual");

                        JSONObject manualMethod = new JSONObject();
                        manualMethod.put("type", TYPE.toLowerCase());
                        manualMethod.put("transactionId", tid1);
                        manualMethod.put("senderNumber", number1);

                        paymentDetails.put("manualMethod", manualMethod);
                        requestBody.put("paymentDetails", paymentDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Create a new request queue
                    RequestQueue queue = Volley.newRequestQueue(depositproccess.this);

                    // Create a new JsonObjectRequest
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    lottie.setVisibility(View.GONE);
                                    Log.d("Response", response.toString());
                                    try {
                                        int statusCode = response.getInt("statusCode");
                                        String message = response.getString("message");


                                        if (statusCode==201){



                                            startActivity(new Intent(depositproccess.this, depositsuccess.class));
                                            Animatoo.animateSwipeLeft(depositproccess.this);
                                            finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                String data = new String(error.networkResponse.data);
                                Log.e("VolleyError", "Status Code: " + statusCode);
                                Log.e("VolleyError", "Data: " + data);
                            }
                            lottie.setVisibility(View.GONE);
                        }

                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            SharedPreferences sharedPreferencesx = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                            String authToken = sharedPreferencesx.getString("authToken", "");
                            headers.put("Authorization", authToken); // Replace with your actual token
                            //headers.put("Content-Type", "application/json");
                            return headers;
                        }
                    };

                    // Add the request to the request queue
                    queue.add(jsonObjectRequest);
                } else {
                    tid.setError("Transaction ID can't be empty");
                }
            } else {
                number.setError("Enter Valid Number");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(depositproccess.this);
    }
}
