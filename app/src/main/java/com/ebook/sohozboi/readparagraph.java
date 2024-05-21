package com.ebook.sohozboi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;


public class readparagraph extends AppCompatActivity {

    public static String TITLE = "";
    public static String CONTENT = "";
    TextView title, content;
    ImageView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readparagraph);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        back = findViewById(R.id.back);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title.setText(Html.fromHtml(TITLE, Html.FROM_HTML_MODE_COMPACT));
            content.setText(Html.fromHtml(CONTENT, Html.FROM_HTML_MODE_COMPACT));

        } else {
            title.setText(Html.fromHtml(TITLE));
            content.setText(Html.fromHtml(CONTENT));

        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                finishAndRemoveTask();
                Animatoo.animateSwipeRight(readparagraph.this);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAndRemoveTask();
        Animatoo.animateSwipeRight(readparagraph.this);
    }
}