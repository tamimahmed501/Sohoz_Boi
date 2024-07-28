package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class signin extends AppCompatActivity {
    private static final int DELAY_MILLIS = 4000; // 3 seconds delay

    TextView submit, c1, c2, c0, c3;
    TextInputEditText number, password, cpassword, name;
    TextInputLayout numberLayout, passwordLayout, cpasswordLayout;
    TextView gotologin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signin);



        submit = findViewById(R.id.submit);
        number = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.password2);
        numberLayout = findViewById(R.id.numberLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        cpasswordLayout = findViewById(R.id.cpasswordLayout);
        gotologin = findViewById(R.id.gotologin);
        name = findViewById(R.id.name);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c0 = findViewById(R.id.c0);
        c3 = findViewById(R.id.c3);


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

        gotologin.setOnClickListener(v -> {
            startActivity(new Intent(signin.this, login.class));
            Animatoo.animateSwipeLeft(signin.this);
        });

        number.addTextChangedListener(new GenericTextWatcher(numberLayout));
        password.addTextChangedListener(new GenericTextWatcher(passwordLayout));
        cpassword.addTextChangedListener(new GenericTextWatcher(cpasswordLayout));

        submit.setOnClickListener(v -> {
            if (validateNumber() && validatePassword() && validatePasswordMatch()) {



                Dialog dialog = new Dialog(signin.this);
                dialog.setContentView(R.layout.load);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alertbackground));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.show();

                String emailx = number.getText().toString(); // Assuming number field is used for email
                String passwordx = password.getText().toString();
                String namex = name.getText().toString();


                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("name", namex);
                    requestBody.put("email", emailx);
                    requestBody.put("password", passwordx);

                    String url = "https://sohozboi-server.vercel.app/api/v1/user/auth/signup";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            requestBody,
                            response -> {
                                dialog.dismiss();
                                try {
                                    if (response.getBoolean("success")) {
                                        String token = response.getJSONObject("data").getString("token");

                                        // Save token to SharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("authToken", token);
                                        editor.apply();

                                        login.PASSWORD=passwordx;
                                        login.MOBILE=emailx;
                                        Toast.makeText(signin.this, "Creation Successfull", Toast.LENGTH_SHORT).show();

                                        // Navigate to the next activity
                                        Intent intent = new Intent(signin.this, login.class); // Replace NextActivity.class with the actual next activity
                                        startActivity(intent);
                                        Animatoo.animateSwipeLeft(signin.this);
                                    } else {
                                        // Handle error response
                                        String message = response.getString("message");
                                        // Show error message to user

                                        dialog.dismiss();

                                        Toast.makeText(signin.this, message, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Handle JSON parsing error

                                    dialog.dismiss();
                                    Toast.makeText(signin.this, "Something Wrong", Toast.LENGTH_SHORT).show();

                                }
                            },
                            error -> {
                                dialog.dismiss();
                                error.printStackTrace();
                                // Handle Volley error

                                Toast.makeText(signin.this, "Something Wrong", Toast.LENGTH_SHORT).show();

                            }
                    );

                    // Add the request to the RequestQueue
                    RequestQueue requestQueue = Volley.newRequestQueue(signin.this);
                    requestQueue.add(jsonObjectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    // Handle JSON creation error
                    Toast.makeText(signin.this, "Token: No token4", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean validateNumber() {
        String mobile = number.getText().toString();
        if (mobile.isEmpty() || mobile.length() < 10) {
            //numberLayout.setError("Incorrect number");

            c0.setVisibility(View.VISIBLE);
            c0.setTextColor(getResources().getColor(R.color.invalid));

            c1.setVisibility(View.GONE);
            c3.setVisibility(View.GONE);
            c2.setVisibility(View.GONE);

            c0.setText("* Invalid number or Email");

            return false;
        } else {
            c1.setVisibility(View.GONE);
            c3.setVisibility(View.GONE);
            c2.setVisibility(View.GONE);

            c0.setVisibility(View.VISIBLE);
            c0.setTextColor(getResources().getColor(R.color.valid));
            c0.setText("* Valid number or Email");

            numberLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordText = password.getText().toString();
        boolean isLengthValid = passwordText.length() >= 6 && passwordText.length() <= 30;
        boolean hasSpecialChar = passwordText.matches(".*[!#@*&$].*");

        // Make the criteria visible
        c1.setVisibility(View.VISIBLE);
        c2.setVisibility(View.VISIBLE);

        c0.setVisibility(View.GONE);
        c3.setVisibility(View.GONE);

        // Validate length
        if (isLengthValid) {
            c1.setTextColor(getResources().getColor(R.color.valid));
        } else {
            c1.setTextColor(getResources().getColor(R.color.invalid));
        }

        // Validate special character presence
        if (hasSpecialChar) {
            c2.setTextColor(getResources().getColor(R.color.valid));
        } else {
            c2.setTextColor(getResources().getColor(R.color.invalid));
        }

        // Return overall validation result
        return isLengthValid && hasSpecialChar;
    }

    private boolean validatePasswordMatch() {
        String passwordText = password.getText().toString();
        String cpasswordText = cpassword.getText().toString();
        if (!passwordText.equals(cpasswordText)) {
            c0.setVisibility(View.GONE);
            c1.setVisibility(View.GONE);
            c2.setVisibility(View.GONE);

            c3.setVisibility(View.VISIBLE);
            c3.setText("* Password not match");
            c3.setTextColor(getResources().getColor(R.color.invalid));

            return false;
        } else {
            c0.setVisibility(View.GONE);
            c1.setVisibility(View.GONE);
            c2.setVisibility(View.GONE);

            c3.setVisibility(View.VISIBLE);
            c3.setText("* Password match");
            c3.setTextColor(getResources().getColor(R.color.valid));
            cpasswordLayout.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAndRemoveTask();
        Animatoo.animateSwipeRight(signin.this);
    }

    private class GenericTextWatcher implements TextWatcher {
        private final TextInputLayout layout;

        public GenericTextWatcher(TextInputLayout layout) {
            this.layout = layout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (layout == numberLayout) {
                validateNumber();
            } else if (layout == passwordLayout) {
                validatePassword();
            } else if (layout == cpasswordLayout) {
                validatePasswordMatch();
            }
        }
    }
}
