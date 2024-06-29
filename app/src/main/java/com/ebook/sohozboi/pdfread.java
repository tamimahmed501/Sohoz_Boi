package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfread extends AppCompatActivity {

    public static String PDF_URL = "";
    public static String PDF_FILENAME = "";
    private static final String PREF_NAME = "MyPrefs";
    public static String BOOKNAME = "";
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ProgressBar progressBar;
    private TextView progressPercentage;
    private AlertDialog progressDialog;
    private DownloadPDFTask downloadPDFTask;
    private String savedPdfPath;

    private boolean isPdfSaved = false;
    private byte[] pdfData;

    private PDFView pdfView;

    ImageView rotateButton, back;
    TextView bookName;
    SeekBar seekBar;
    String textData;
    String txtUrl = "https://firebasestorage.googleapis.com/v0/b/druto-pay.appspot.com/o/Documentation%20For%20SOHOZ%20BOI.txt?alt=media&token=f3a887df-cb21-4181-9b59-97d4a15bd7bf"; // Change this to your txt file URL

    RelativeLayout toplayout, bottomlayout;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfread);


        pdfView = findViewById(R.id.pdfView);
        bookName = findViewById(R.id.bookName);
        rotateButton = findViewById(R.id.rotate);
        seekBar = findViewById(R.id.seekBar);
        toplayout = findViewById(R.id.toplayout);
        bottomlayout = findViewById(R.id.bottomlayout);
        back = findViewById(R.id.back);

        bookName.setText(BOOKNAME);

        back.setOnClickListener(v -> {
            onBackPressed();
            finish();
            Animatoo.animateSwipeRight(pdfread.this);
        });

        pdfView.setOnClickListener(v -> {
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
        });

        rotateButton.setOnClickListener(v -> {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        // Set up SeekBar for zoom control
        seekBar.setMax(200); // Adjust this value based on your zoom range
        seekBar.setProgress(100); // Default zoom level (100% scale)
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float zoom = progress / 100.0f; // Convert progress to a zoom scale factor
                pdfView.zoomTo(zoom);
                pdfView.invalidate(); // Redraw PDFView
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

        if (savedInstanceState != null) {
            pdfData = savedInstanceState.getByteArray("pdfData");
            if (pdfData != null) {
                displayPdfFromBytes(pdfData);
            }
        } else {
            savedPdfPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + PDF_FILENAME;
            File pdfFile = new File(savedPdfPath);
            if (pdfFile.exists()) {
                isPdfSaved = true;
                displayPdfFromFile(pdfFile);
            } else {
                showProgressDialog("Downloading Book");
                downloadPDFTask = new DownloadPDFTask();
                downloadPDFTask.execute(PDF_URL);
            }
        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
            finish();
            Animatoo.animateSwipeRight(pdfread.this);

    }




    private void displayPdfFromFile(File file) {
        pdfView.fromFile(file).load();
    }

    private void displayPdfFromBytes(byte[] pdfData) {
        pdfView.fromBytes(pdfData).load();
    }

    private class DownloadPDFTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            String pdfUrl = urls[0];
            try {
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return false;
                }

                int fileLength = connection.getContentLength();

                InputStream input = connection.getInputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                long total = 0;
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    total += bytesRead;
                    if (fileLength > 0) {
                        publishProgress((int) (total * 100 / fileLength));
                    }
                    output.write(buffer, 0, bytesRead);
                }
                input.close();
                pdfData = output.toByteArray();
                return true;
            } catch (Exception e) {
                Log.e("Download PDF", "Error downloading PDF", e);
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (progressBar != null) {
                progressBar.setProgress(values[0]);
                progressPercentage.setText(values[0] + "%");
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result) {
                displayPdfFromBytes(pdfData);
            } else {
                Toast.makeText(pdfread.this, "Failed to download Book", Toast.LENGTH_SHORT).show();
            }
        }
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
            if (downloadPDFTask != null) {
                downloadPDFTask.cancel(true);
            }
            dialog.dismiss();
        });

        progressDialog = builder.create();
        progressDialog.show();
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
        outState.putByteArray("pdfData", pdfData);
    }


}
