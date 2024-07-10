package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfread2 extends AppCompatActivity {

    TextView pdfText, bookname;
    ImageView rotateButton, back;
    SeekBar seekBar;
    String textData;

    public static String TEXTLINK = ""; // Ensure this is properly initialized with your text file URL
    public static String BOOKNAME = "";

    RelativeLayout toplayout, bottomlayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfread2);

        pdfText = findViewById(R.id.pdfText);
        rotateButton = findViewById(R.id.rotate);
        seekBar = findViewById(R.id.seekBar);
        toplayout = findViewById(R.id.toplayout);
        bottomlayout = findViewById(R.id.bottomlayout);
        back = findViewById(R.id.back);
        bookname = findViewById(R.id.bookname);

        bookname.setText(BOOKNAME);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                Animatoo.animateSwipeRight(pdfread2.this);
            }
        });

        pdfText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        if (savedInstanceState != null) {
            textData = savedInstanceState.getString("textData");
            pdfText.setText(textData);
        } else {
            new RetrieveTextStream().execute(TEXTLINK);
        }

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

        // Set up the seekbar to adjust the text size
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float textSize = 12.0f + (progress / 2.0f);  // Adjust text size range as needed
                pdfText.setTextSize(textSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void hideSystemUI() {
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showSystemUI() {
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textData", textData);
    }

    class RetrieveTextStream extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(pdfread2.this);
            progressDialog.setMessage("Downloading Text...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                int totalSize = urlConnection.getContentLength();
                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    int totalRead = 0;
                    while ((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                        totalRead += line.getBytes().length;
                        publishProgress((int) ((totalRead * 100) / totalSize));
                    }
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            textData = result;
            pdfText.setText(result);
        }
    }
}
