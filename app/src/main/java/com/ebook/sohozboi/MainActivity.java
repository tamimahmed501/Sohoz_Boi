package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    LinearLayout bottomlay;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frame);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame, new home());
        fragmentTransaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment destinationFragment = null;
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                // Get the index of the current fragment
                int currentIndex = -1;
                int destinationIndex = -1;
                int i = item.getItemId();

                if (currentFragment instanceof home) {
                    currentIndex = 0;
                } else if (currentFragment instanceof mybook) {
                    currentIndex = 1;
                } else if (currentFragment instanceof myaudiobook) {
                    currentIndex = 2;
                } else if (currentFragment instanceof Author) {
                    currentIndex = 3;
                } else if (currentFragment instanceof profile) {
                    currentIndex = 4;
                }

                if (i == R.id.home) {
                    destinationFragment = new home();
                    destinationIndex = 0;
                } else if (i == R.id.mybook) {
                    destinationFragment = new mybook();
                    destinationIndex = 1;
                } else if (i == R.id.audiobook) {
                    destinationFragment = new myaudiobook();
                    destinationIndex = 2;
                } else if (i == R.id.author) {
                    destinationFragment = new Author();
                    destinationIndex = 3;
                } else if (i == R.id.profile) {
                    destinationFragment = new profile();
                    destinationIndex = 4;
                }

                // Set the animation direction based on the comparison of indices
                if (currentIndex < destinationIndex) {
                    // Current fragment index is less than destination index, animate right to left
                    fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                } else {
                    // Current fragment index is greater than or equal to destination index, animate left to right
                    fragmentTransaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                }

                if (destinationFragment != null) {
                    fragmentTransaction.replace(R.id.frame, destinationFragment);
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        // Check for the flag in the Intent
        if (getIntent().getBooleanExtra("NAVIGATE_TO_PROFILE", false)) {
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frame, new profile());
            fragmentTransaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }

        if (getIntent().getBooleanExtra("NAVIGATE_TO_MYBOOK", false)) {
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frame, new mybook());
            fragmentTransaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.mybook);
        }

        if (getIntent().getBooleanExtra("NAVIGATE_TO_AUTHOR", false)) {
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frame, new Author());
            fragmentTransaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.author);
        }

        if (getIntent().getBooleanExtra("NAVIGATE_TO_AUDIOBOOK", false)) {
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frame, new myaudiobook());
            fragmentTransaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.audiobook);
        }

        if (getIntent().getBooleanExtra("NAVIGATE_TO_ACCOUNT", false)) {
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frame, new profile());
            fragmentTransaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }










        getToken();






    }





    private void getToken(){


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        SharedPreferences sharedPreferencesx = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String password = sharedPreferencesx.getString("password", "");
        String email = sharedPreferencesx.getString("email", "");



        String url = "https://sohozboi-server.vercel.app/api/v1/user/auth/login";
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


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                },
                error -> {
                    error.printStackTrace();

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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("এপ থেকে বের হতে চান?")
                .setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("না", null)
                .show();
    }
}
