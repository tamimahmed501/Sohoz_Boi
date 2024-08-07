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
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class pdfread extends AppCompatActivity {

    public static String PDF_URL = "";
    public static String PDF_FILENAME = "";
    public static String BOOKNAME = "";
    private ProgressBar progressBar;
    private TextView progressPercentage;
    private AlertDialog progressDialog;
    private DownloadPDFTask downloadPDFTask;
    private String savedPdfPath;
    private boolean isPdfSaved = false;
    private byte[] pdfData;
    private PDFView pdfView;
    private static final int BUFFER_SIZE = 8192; // 8KB buffer size

    ImageView rotateButton, back;
    TextView bookName;
    SeekBar seekBar;

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
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected Boolean doInBackground(String... urls) {
            String pdfUrl = urls[0];
            try {
                Request request = new Request.Builder()
                        .url(pdfUrl)
                        .build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    return false;
                }

                int fileLength = (int) response.body().contentLength();
                InputStream input = response.body().byteStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
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
                savePdfToFile(pdfData, savedPdfPath);
                return true;
            } catch (Exception e) {
                Log.e("Download PDF", "Error downloading PDF", e);
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            runOnUiThread(() -> {
                if (progressBar != null) {
                    progressBar.setProgress(values[0]);
                    progressPercentage.setText(values[0] + "%");
                }
            });
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

    private void savePdfToFile(byte[] pdfData, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfData);
        } catch (Exception e) {
            Log.e("Save PDF", "Error saving PDF", e);
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
        outState.putString("textData", "1234");
        outState.putByteArray("pdfData", pdfData);
    }
}
