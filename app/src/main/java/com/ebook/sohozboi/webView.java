package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class webView extends AppCompatActivity {

    WebView webView;
    LottieAnimationView lottie;
    ImageView backbutton;

    TextView toptext;

    public static String URL = "";
    public static String TOPTEXT = "";
    public static String YTB = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_web_view);

        backbutton = findViewById(R.id.backbutton);
        webView = findViewById(R.id.webView);
        lottie = findViewById(R.id.lottie);
        toptext = findViewById(R.id.toptext);

        toptext.setText(TOPTEXT);

        Animatoo.animateFade(webView.this);



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                Animatoo.animateSwipeRight(webView.this);


            }
        });

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Improve WebView performance
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);

        // Enable responsive design
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // Handle page navigation within the WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Show loading animation
                lottie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hide loading animation
                lottie.setVisibility(View.GONE);
            }
        });

        // Handle external links within the WebView
        webView.setWebChromeClient(new WebChromeClient());

        // Load the URL
        webView.loadUrl(URL);

        // Handle back button press to navigate back in the WebView history
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    // Handle back button press when there is no history
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSwipeRight(webView.this);

    }
}

