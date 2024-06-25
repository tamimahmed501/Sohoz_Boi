package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfread extends AppCompatActivity {

    private static final String PDF_URL = "https://firebasestorage.googleapis.com/v0/b/druto-pay.appspot.com/o/%E0%A6%9A%E0%A6%B0%20%E0%A6%95%E0%A7%81%E0%A6%95%E0%A6%B0%E0%A6%BF%20%E0%A6%AE%E0%A7%81%E0%A6%95%E0%A6%B0%E0%A6%BF.pdf?alt=media&token=5809398a-95a0-4a3a-802b-2f66e7e0824f";
    private static final String PDF_FILENAME = "downloaded_pdf.pdf";
    private static final String PREF_NAME = "MyPrefs";

    private SeekBar textSizeSeekBar;
    private ProgressBar progressBar;
    private TextView progressPercentage;
    private AlertDialog progressDialog;
    private ExtractAndDisplayTextTask extractAndDisplayTextTask;
    private String savedPdfPath;
    private TextView extractedTextView;

    LinearLayout setting;

    private boolean isPdfSaved = false;

    private byte[] pdfData;

    RelativeLayout toplayout, bottomlayout;
    ScrollView scroolview;
    ImageView settingsclick;

    private SeekBar brightnessSeekBar;
    private ContentResolver contentResolver;
    TextView whitebg, whitebg2, darkbg, darkbg2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfread);

        PDFBoxResourceLoader.init(getApplicationContext());

        textSizeSeekBar = findViewById(R.id.textSizeSeekBar);
        extractedTextView = findViewById(R.id.extractedTextView);
        toplayout = findViewById(R.id.toplayout);
        bottomlayout = findViewById(R.id.bottomlayout);
        scroolview = findViewById(R.id.scroolView);
        setting = findViewById(R.id.setting);
        settingsclick = findViewById(R.id.settingsclick);


        whitebg = findViewById(R.id.whitebg);
        whitebg2 = findViewById(R.id.whitebg2);
        darkbg = findViewById(R.id.darkbg);
        darkbg2 = findViewById(R.id.darkbg2);



        contentResolver = getContentResolver();
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Check for write settings permission
        if (!Settings.System.canWrite(this)) {
            Toast.makeText(this, "Please grant write settings permission", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        } else {
            initializeSeekBar();
        }




        whitebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                extractedTextView.setBackgroundColor(getColor(R.color.white));


            }
        });



        whitebg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                extractedTextView.setBackgroundColor(getColor(R.color.whitebg2));


            }
        });


        darkbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                extractedTextView.setBackgroundColor(getColor(R.color.darkbg));


            }
        });

        darkbg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                extractedTextView.setBackgroundColor(getColor(R.color.darkbg2));


            }
        });



        settingsclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (setting.getVisibility()==View.VISIBLE){

                    setting.setVisibility(View.GONE);

                } else {

                    setting.setVisibility(View.VISIBLE);
                }


            }
        });


        savedPdfPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + PDF_FILENAME;

        File pdfFile = new File(savedPdfPath);
        if (pdfFile.exists()) {
            isPdfSaved = true;
            //extractAndDisplayText();
            SharedPreferences sharedPreferencesx = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

            String pdftext = sharedPreferencesx.getString(PDF_URL, "");

            extractedTextView.setText(pdftext);
        } else {

            showProgressDialog("Downloading PDF");
            new DownloadPDFTask().execute(PDF_URL);


        }






        extractedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toplayout.getVisibility() == View.VISIBLE) {
                    toplayout.setVisibility(View.GONE);
                    bottomlayout.setVisibility(View.GONE);
                } else {
                    toplayout.setVisibility(View.VISIBLE);
                    bottomlayout.setVisibility(View.VISIBLE);
                }
            }
        });


        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float textSize = 12 + (float) progress / 2;
                extractedTextView.setTextSize(textSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


    }

    @Override
    public void onBackPressed() {
        if (pdfData != null && !isPdfSaved) {
            new AlertDialog.Builder(this)
                    .setTitle("Save PDF")
                    .setMessage("Do you want to save the PDF locally?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            savePdfLocally();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pdfread.super.onBackPressed();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private void savePdfLocally() {
        try {
            File file = new File(savedPdfPath);
            try (FileOutputStream output = new FileOutputStream(file)) {
                output.write(pdfData);
                isPdfSaved = true;
                Toast.makeText(this, "PDF saved locally", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Save PDF", "Error saving PDF", e);
        }
    }

    private void extractAndDisplayText() {
        extractAndDisplayTextTask = new ExtractAndDisplayTextTask();
        extractAndDisplayTextTask.execute();
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
                extractAndDisplayText();
            } else {
                Toast.makeText(pdfread.this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ExtractAndDisplayTextTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog("Extracting Text");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try (PDDocument pdfDocument = PDDocument.load(pdfData)) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setSortByPosition(true);

                int totalPages = pdfDocument.getNumberOfPages();
                StringBuilder extractedText = new StringBuilder();

                for (int i = 1; i <= totalPages; i++) {
                    pdfStripper.setStartPage(i);
                    pdfStripper.setEndPage(i);
                    extractedText.append(pdfStripper.getText(pdfDocument));
                    publishProgress((int) ((i / (float) totalPages) * 100));
                }

                return extractedText.toString();
            } catch (IOException e) {
                Log.e("Text Extraction", "Error extracting text", e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (progressBar != null && progressPercentage != null) {
                progressBar.setProgress(values[0]);
                progressPercentage.setText(values[0] + "%");
            }
        }

        @Override
        protected void onPostExecute(String text) {
            progressDialog.dismiss();
            if (text != null) {
                extractedTextView.setText(text);

                SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(PDF_URL, text);

                editor.apply();


            } else {
                Toast.makeText(pdfread.this, "Failed to extract text", Toast.LENGTH_SHORT).show();
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (extractAndDisplayTextTask != null) {
                    extractAndDisplayTextTask.cancel(true);
                }
                dialog.dismiss();
            }
        });

        progressDialog = builder.create();
        progressDialog.show();
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Settings.System.canWrite(this)) {
                initializeSeekBar();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeSeekBar() {
        // Initialize SeekBar with current brightness
        try {
            int brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            brightnessSeekBar.setProgress(brightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        // Set listener for SeekBar
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Change brightness
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
