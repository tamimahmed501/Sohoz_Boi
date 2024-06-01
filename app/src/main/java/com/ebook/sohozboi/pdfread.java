package com.ebook.sohozboi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfread extends AppCompatActivity {

    private static final String PDF_URL = "https://firebasestorage.googleapis.com/v0/b/my-reader-2-513d8.appspot.com/o/E-book.pdf?alt=media&token=41405295-d903-4508-a5b8-48fa1707465c";
    private static final String PDF_FILENAME = "downloaded_pdf.pdf";

    private SeekBar textSizeSeekBar;
    private PDFView pdfView;
    private TextView pageNumberView;
    private ProgressBar progressBar;
    private TextView progressPercentage;
    private AlertDialog progressDialog;
    private RetrievePDFStream retrievePDFStream;
    private DatabaseHelper dbHelper;
    private String savedPdfPath;
    private boolean isNightMode = false;
    private Switch nightModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfread);

        pdfView = findViewById(R.id.pdfView);
        pageNumberView = findViewById(R.id.pageNumberView);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar);

        dbHelper = new DatabaseHelper(this);

        savedPdfPath = dbHelper.getPDFPath(PDF_URL);

        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update PDF text size based on seek bar progress
                updateTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNightMode = isChecked;
                if (savedPdfPath != null) {
                    loadPDF(savedPdfPath, textSizeSeekBar.getProgress());
                }
            }
        });

        if (savedPdfPath != null) {
            float initialTextSize = 12; // Initial text size, adjust as needed
            loadPDF(savedPdfPath, initialTextSize);
        } else {
            showDownloadDialog();
        }
    }

    private void updateTextSize(int progress) {
        // Calculate the text size based on the progress
        float textSize = 12 + (float) progress / 2; // Example calculation, adjust as needed

        // Load PDF with the updated text size
        loadPDF(savedPdfPath, textSize);
    }

    private void showDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Download PDF")
                .setMessage("Do you want to download the PDF?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        retrievePDFStream = new RetrievePDFStream();
                        retrievePDFStream.execute(PDF_URL);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .show();
    }

    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loading PDF");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);

        progressBar = dialogView.findViewById(R.id.progressBar);
        progressPercentage = dialogView.findViewById(R.id.progressPercentage);

        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                retrievePDFStream.cancel(true);
                dialog.dismiss();
            }
        });

        progressDialog = builder.create();
        progressDialog.show();
    }

    private void loadPDF(String path, float textSize) {
        File file = new File(path);
        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .nightMode(isNightMode)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        pageNumberView.setText("Page: " + (page + 1) + " / " + pageCount);
                    }
                })
                .pageFitPolicy(FitPolicy.BOTH)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .defaultPage(0)
                .spacing(0)
                .swipeHorizontal(false)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .enableAnnotationRendering(false)
                .nightMode(isNightMode)
                .enableAntialiasing(true)
                .enableAnnotationRendering(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableSwipe(true)
                .load();
    }

    private void savePDFToLocal(ByteArrayInputStream inputStream) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), PDF_FILENAME);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            dbHelper.insertPDF(PDF_URL, file.getAbsolutePath());
            Toast.makeText(this, "PDF saved locally", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            loadPDF(file.getAbsolutePath(), textSizeSeekBar.getProgress());
        } catch (IOException e) {
            Log.e("PDF Save", "Error saving PDF", e);
        }
    }

    private class RetrievePDFStream extends AsyncTask<String, Integer, ByteArrayInputStream> {

        @Override
        protected ByteArrayInputStream doInBackground(String... strings) {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                int fileLength = urlConnection.getContentLength();

                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int total = 0;
                    int count;
                    while ((count = inputStream.read(data, 0, 1024)) != -1) {
                        total += count;
                        buffer.write(data, 0, count);
                        if (fileLength > 0) {
                            publishProgress((int) (total * 100 / fileLength));
                        }
                    }
                    byte[] pdfBytes = buffer.toByteArray();
                    byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
                }
            } catch (IOException e) {
                Log.e("PDF Stream", "Error fetching PDF", e);
            }
            return byteArrayInputStream;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressBar.setProgress(progress[0]);
            progressPercentage.setText(progress[0] + "%");
        }

        @Override
        protected void onPostExecute(ByteArrayInputStream byteArrayInputStream) {
            if (byteArrayInputStream != null) {
                savePDFToLocal(byteArrayInputStream);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
        }
    }
}
