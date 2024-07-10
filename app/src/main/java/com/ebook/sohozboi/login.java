package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    TextView createaccount, login;
    TextInputEditText mobile, password;
    private static final String PREF_NAME = "MyPrefs";

    public static String MOBILE = "";
    public static String PASSWORD = "";

    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createaccount = findViewById(R.id.createaccount);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        requestQueue = Volley.newRequestQueue(this);

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

        createaccount.setOnClickListener(v -> {
            startActivity(new Intent(login.this, signin.class));
            Animatoo.animateSwipeLeft(login.this);
        });

        login.setOnClickListener(v -> {
            String email = mobile.getText().toString();
            String passwordx = password.getText().toString();
            loginUser(email, passwordx);
        });




        if (PASSWORD!=null){

            password.setText(PASSWORD);
            mobile.setText(MOBILE);


        }

    }

    private void loginUser(String email, String password) {
        Dialog dialog = new Dialog(login.this);
        dialog.setContentView(R.layout.load);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alertbackground));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        String url = "https://server.shohozboi.com/api/v1/user/auth/login";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            String accessToken = response.getJSONObject("data").getString("accesstoken");
                            saveAccessToken(accessToken);






                            TextView status;

                            status = dialog.findViewById(R.id.status);

                            status.setText("Data matching");


                            fetchUserData(accessToken, dialog);
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();

                            TextView status;

                            status = dialog.findViewById(R.id.status);

                            status.setText("Login Failed");


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(login.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                },
                error -> {
                    dialog.dismiss();
                    error.printStackTrace();
                    Toast.makeText(login.this, "Login error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(loginRequest);
    }

    private void saveAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authToken", accessToken);
        editor.apply();
    }

    private void fetchUserData(String accessToken, Dialog dialog) {
        String url = "https://server.shohozboi.com/api/v1/user/get-me";
        JsonObjectRequest userRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject userData = response.getJSONObject("data");
                            saveUserData(userData);
                            Toast.makeText(login.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                            TextView status;

                            status = dialog.findViewById(R.id.status);

                            status.setText("Login Successfull");







                        } else {
                            String message = response.getString("message");
                            Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();


                            TextView status;

                            status = dialog.findViewById(R.id.status);

                            status.setText("Login Failed");


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(login.this, "Error parsing user data", Toast.LENGTH_SHORT).show();

                        TextView status;

                        status = dialog.findViewById(R.id.status);

                        status.setText("Login Failed");


                    }
                },
                error -> {
                    dialog.dismiss();
                    error.printStackTrace();
                    Toast.makeText(login.this, "User data fetch error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", accessToken);
                return headers;
            }
        };

        requestQueue.add(userRequest);
    }

    private void saveUserData(JSONObject userData) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString("status", "login");
            editor.putString("id", userData.getString("_id"));
            editor.putString("name", userData.getString("name"));
            editor.putString("email", userData.getString("email"));
            editor.putBoolean("isVerified", userData.getBoolean("isVerified"));
            editor.putString("createdAt", userData.getString("createdAt"));
            editor.putString("updatedAt", userData.getString("updatedAt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();


        // Start MainActivity with an Intent
        Intent intent = new Intent(com.ebook.sohozboi.login.this, MainActivity.class);

        // Add a flag to indicate navigation to Offer fragment
        intent.putExtra("NAVIGATE_TO_PROFILE", true);

        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAndRemoveTask();
        Animatoo.animateSwipeRight(login.this);
    }
}
