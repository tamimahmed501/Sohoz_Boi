package com.ebook.sohozboi;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class profile extends Fragment {

    TextView login, createaccount, name, email, id, logout;
    LinearLayout profilelay, loginlay;

    private static final String PREF_NAME = "MyPrefs";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);

        login = myView.findViewById(R.id.login);
        createaccount = myView.findViewById(R.id.createaccount);
        name = myView.findViewById(R.id.name);
        email = myView.findViewById(R.id.email);
        id = myView.findViewById(R.id.id);
        profilelay = myView.findViewById(R.id.profilelay);
        loginlay = myView.findViewById(R.id.loginlay);
        logout = myView.findViewById(R.id.logout);



        logout.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Permission Required").setMessage("Do you want to Log out?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences sharedPreferencesx = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editorx = sharedPreferencesx.edit();
                    editorx.clear();
                    editorx.apply();



                    Toast.makeText(getContext(), "Log-out Successfull.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), start.class));
                    Animatoo.animateSwipeRight(getContext());

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();



        });











        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), com.ebook.sohozboi.login.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), com.ebook.sohozboi.signin.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });














        SharedPreferences sharedPreferencesx = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String idx = sharedPreferencesx.getString("id", "");
        String namex = sharedPreferencesx.getString("name", "");
        String emailx = sharedPreferencesx.getString("email", "");
        String status = sharedPreferencesx.getString("status", "");

        //String verified = sharedPreferencesx.getString("isVerified", "");

        if (status.contains("login")) {
            loginlay.setVisibility(View.GONE);
            profilelay.setVisibility(View.VISIBLE);

            name.setText(namex);
            id.setText("ID: " + idx);
            email.setText(emailx);
        } else {
            profilelay.setVisibility(View.GONE);
            loginlay.setVisibility(View.VISIBLE);
        }


        return myView;
    }
}
