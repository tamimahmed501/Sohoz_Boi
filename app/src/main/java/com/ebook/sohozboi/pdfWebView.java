package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class pdfWebView extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private AlertDialog progressDialog;
    private TextView progressPercentage;

    private ImageView rotateButton, back;
    private TextView bookName, pageNumberTextView;
    private SeekBar seekBar;
    private RelativeLayout toplayout, bottomlayout;

    public static String PDFLINK2 = ""; // Ensure this is properly initialized with your PDF URL

    public static String BOOKNAME = "";
    private float mDownPosX = 0;
    private float mDownPosY = 0;

    private int currentPage = 1; // Track current page number
    private PdfViewModel pdfViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_web_view);

        pdfViewModel = new ViewModelProvider(this).get(PdfViewModel.class);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressbar);
        bookName = findViewById(R.id.bookName);
        pageNumberTextView = findViewById(R.id.pageNumberTextView); // TextView to display page number
        rotateButton = findViewById(R.id.rotate);
        seekBar = findViewById(R.id.seekBar);
        toplayout = findViewById(R.id.toplayout);
        bottomlayout = findViewById(R.id.bottomlayout);
        back = findViewById(R.id.back);

        bookName.setText(BOOKNAME);

        if (pdfViewModel.webView == null) {
            pdfViewModel.webView = webView;
            setupWebView();
            loadPdf();
        } else {
            // Attach the existing WebView from ViewModel
            webView = pdfViewModel.webView;
        }

        // Toggle visibility of top and bottom layouts on WebView click
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownPosX = event.getX();
                        mDownPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float upX = event.getX();
                        float upY = event.getY();
                        float deltaX = upX - mDownPosX;
                        float deltaY = upY - mDownPosY;

                        if (Math.abs(deltaX) < 5 && Math.abs(deltaY) < 5) {
                            toggleTopBottomLayout();
                        }
                        break;
                }
                return false;
            }
        });

        // Handle rotation button click to toggle between portrait and landscape orientations
        rotateButton.setOnClickListener(v -> toggleOrientation());

        // Set up SeekBar for zoom control
        seekBar.setMax(200); // Adjust this value based on your zoom range
        seekBar.setProgress(100); // Default zoom level (100% scale)
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calculate zoom level (scale)
                float zoom = (float) progress / 100.0f;
                // Apply zoom level to WebView
                webView.setInitialScale((int) (zoom * 100)); // Set initial scale
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
    }

    private void setupWebView() {
        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true); // Enable built-in zoom controls
        webSettings.setDisplayZoomControls(false); // Hide the zoom controls buttons
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // Show progress dialog while loading PDF
        showProgressDialog("Loading PDF...");

        // Handle page navigation within the WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Show loading animation
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hide loading animation
                progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                // Get current page number when page finishes loading
                getCurrentPageNumber();
            }
        });

        // Handle external links within the WebView
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // Update progress bar and progress dialog
                progressBar.setProgress(newProgress);
                progressPercentage.setText(newProgress + "%");
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadPdf() {
        // Load the PDF URL using Google Docs viewer
        String pdfViewerUrl = "https://docs.google.com/viewer?embedded=true&url=" + PDFLINK2;
        webView.loadUrl(pdfViewerUrl);
    }

    private void showProgressDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);

        progressBar = dialogView.findViewById(R.id.progressBar);
        progressPercentage = dialogView.findViewById(R.id.progressPercentage);

        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            webView.stopLoading();
            dialog.dismiss();
        });

        progressDialog = builder.create();
        progressDialog.show();
    }

    private void toggleTopBottomLayout() {
        if (toplayout.getVisibility() == View.VISIBLE) {
            toplayout.setVisibility(View.GONE);
            bottomlayout.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hideSystemUI();
            }
        } else {
            toplayout.setVisibility(View.VISIBLE);
            bottomlayout.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                showSystemUI();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void hideSystemUI() {
        getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showSystemUI() {
        getWindow().getInsetsController().show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
    }

    private void toggleOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    // Method to get current PDF page number
    private void getCurrentPageNumber() {
        webView.evaluateJavascript("javascript:PDFViewerApplication.pdfViewer.currentPageNumber", value -> {
            try {
                int pageNumber = Integer.parseInt(value);
                currentPage = pageNumber;
                updatePageNumber(currentPage);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    // Method to update page number TextView
    private void updatePageNumber(int pageNumber) {
        runOnUiThread(() -> {
            pageNumberTextView.setText("Page " + pageNumber);
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            finishAndRemoveTask();
            finish();
            Animatoo.animateSwipeRight(pdfWebView.this);
        }
    }
}
