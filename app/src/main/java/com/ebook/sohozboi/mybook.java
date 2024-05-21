package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;


public class mybook extends Fragment {

    LinearLayout booklay;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_mybook, container, false);

        booklay =myView.findViewById(R.id.booklay);



        booklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), paragraph.class));
                Animatoo.animateSwipeLeft(getContext());

            }
        });




        return myView;
    }
}