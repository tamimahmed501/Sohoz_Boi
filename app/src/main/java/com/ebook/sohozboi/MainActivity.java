package com.ebook.sohozboi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ebook.sohozboi.R;
import com.ebook.sohozboi.home;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1 ;

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

              int i = item.getItemId();
                if (i==0) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new home());
                    fragmentTransaction.commit();

                } else if (i==1) {
                    FragmentManager fmx = getSupportFragmentManager();
                    FragmentTransaction fragmentTransactionx = fmx.beginTransaction();
                    //fragmentTransactionx.replace(R.id.frame, new appointment());
                    fragmentTransactionx.commit();
                } else if (i==2) {
                    FragmentManager fmy = getSupportFragmentManager();
                    FragmentTransaction fragmentTransactiony = fmy.beginTransaction();
                    //fragmentTransactiony.replace(R.id.frame, new internet()); // Assuming there is a dashboard fragment class
                    fragmentTransactiony.commit();
                } else if (i==3) {
                    FragmentManager fmy = getSupportFragmentManager();
                    FragmentTransaction fragmentTransactiony = fmy.beginTransaction();
                    //fragmentTransactiony.replace(R.id.frame, new notification()); // Assuming there is a dashboard fragment class
                    fragmentTransactiony.commit();
                }else if (i==4) {
                    FragmentManager fmy = getSupportFragmentManager();
                    FragmentTransaction fragmentTransactiony = fmy.beginTransaction();
                    //fragmentTransactiony.replace(R.id.frame, new user()); // Assuming there is a dashboard fragment class
                    fragmentTransactiony.commit();
                }




                return true;
            }
        });










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

    // Declare the launcher at the top of your Activity/Fragment:
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });





}

