package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfread2 extends AppCompatActivity {

    TextView pdfText, bookname;
    ImageView rotateButton, back;
    SeekBar seekBar;
    String textData;

    private SeekBar brightnessSeekBar;
    private ContentResolver cResolver;
    private WindowManager.LayoutParams layoutParams;

    ImageView white, white2, white3, white4, white1;

    public static String TEXTLINK = ""; // Ensure this is properly initialized with your text file URL
    public static String BOOKNAME = "";
    public static String AUTHORNAME = "";

    RelativeLayout toplayout, bottomlayout, bglay;

    ImageView background, rotate, textsizee, save, brightness;

    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    public static Bitmap Mybitmap = null;
    boolean isBookDownloaded = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfread2);

        dbHelper = new DatabaseHelper(this);

        pdfText = findViewById(R.id.pdfText);
        rotateButton = findViewById(R.id.rotate);
        seekBar = findViewById(R.id.seekBar);
        toplayout = findViewById(R.id.toplayout);
        bottomlayout = findViewById(R.id.bottomlayout);
        back = findViewById(R.id.back);
        bookname = findViewById(R.id.bookname);
        white1 = findViewById(R.id.white1);
        white2 = findViewById(R.id.white2);
        white3 = findViewById(R.id.white3);
        white4 = findViewById(R.id.white4);
        white = findViewById(R.id.white);
        bglay = findViewById(R.id.bglay);
        background = findViewById(R.id.background);
        rotate = findViewById(R.id.rotate);
        textsizee = findViewById(R.id.textsizee);
        save = findViewById(R.id.save);
        brightness = findViewById(R.id.brightness);

        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLay();
                brightnessSeekBar.setVisibility(View.VISIBLE);
            }
        });

        textsizee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLay();
                seekBar.setVisibility(View.VISIBLE);
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLay();
                bglay.setVisibility(View.VISIBLE);
            }
        });

        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        cResolver = getContentResolver();
        layoutParams = getWindow().getAttributes();

        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        try {
            int currentBrightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
            brightnessSeekBar.setProgress(currentBrightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, progress);
                layoutParams.screenBrightness = progress / (float) 255;
                getWindow().setAttributes(layoutParams);
            }
        });

        white4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white);
                pdfText.setBackgroundColor(bgColor);
                white4.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                pdfText.setTextColor(getResources().getColor(R.color.black));
                updateStatusBarColor(bgColor);
            }
        });

        white3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white4);
                pdfText.setBackgroundColor(bgColor);
                white3.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                pdfText.setTextColor(getResources().getColor(R.color.white));
                updateStatusBarColor(bgColor);
            }
        });

        white2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white3);
                pdfText.setBackgroundColor(bgColor);
                white2.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                pdfText.setTextColor(getResources().getColor(R.color.white));
                updateStatusBarColor(bgColor);
            }
        });

        white1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white1);
                pdfText.setBackgroundColor(bgColor);
                white1.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                pdfText.setTextColor(getResources().getColor(R.color.black));
                updateStatusBarColor(bgColor);
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white2);
                pdfText.setBackgroundColor(bgColor);
                white.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                pdfText.setTextColor(getResources().getColor(R.color.black));
                updateStatusBarColor(bgColor);
            }
        });

        bookname.setText(BOOKNAME);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        checkIfBookDownloaded();

        if (savedInstanceState != null) {
            textData = savedInstanceState.getString("textData");
            pdfText.setText(textData);
        } else {
            if (isBookDownloaded) {
                loadTextFromDatabase();
            } else {
                new RetrieveTextStream().execute(TEXTLINK);
            }
        }

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentOrientation = getRequestedOrientation();
                if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

        seekBar.setProgress(10);
        pdfText.setTextSize(seekBar.getProgress() + 10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pdfText.setTextSize(progress + 10);
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

    private void checkIfBookDownloaded() {
        database = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_TEXT
        };

        String selection = DatabaseHelper.COLUMN_BOOK_NAME + " = ?";
        String[] selectionArgs = { BOOKNAME };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            isBookDownloaded = true;
            textData = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEXT));
        }

        if (cursor != null) {
            cursor.close();
        }

        database.close();
    }

    private void loadTextFromDatabase() {
        pdfText.setText(textData);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (isBookDownloaded) {
            finish();
            Animatoo.animateSwipeRight(pdfread2.this);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to save this downloaded text file?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveTextToDatabase();
                            finish();
                            Animatoo.animateSwipeRight(pdfread2.this);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Animatoo.animateSwipeRight(pdfread2.this);
                        }
                    })
                    .show();
        }
    }

    private void saveTextToDatabase() {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOK_NAME, BOOKNAME);
        values.put(DatabaseHelper.COLUMN_TEXT, textData);
        values.put(DatabaseHelper.AUTHOR_NAME, AUTHORNAME);

        // Convert bitmap to byte array
        if (Mybitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Mybitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] bitmapData = outputStream.toByteArray();
            values.put(DatabaseHelper.IMAGE_BITMAP, Base64.encodeToString(bitmapData, Base64.DEFAULT));
        }

        long newRowId = database.insert(DatabaseHelper.TABLE_NAME, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Text saved successfully", Toast.LENGTH_SHORT).show();
        }

        database.close();
    }

    @SuppressLint("StaticFieldLeak")
    private class RetrieveTextStream extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(pdfread2.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            textData = result;
            pdfText.setText(result);
        }
    }

    private void hideLay() {
        seekBar.setVisibility(View.GONE);
        brightnessSeekBar.setVisibility(View.GONE);
        bglay.setVisibility(View.GONE);
    }

    private void resetIamage() {
        white4.setImageDrawable(null);
        white3.setImageDrawable(null);
        white2.setImageDrawable(null);
        white1.setImageDrawable(null);
        white.setImageDrawable(null);
    }

    private void updateStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void hideSystemUI() {
        final WindowInsetsController insetsController = getWindow().getInsetsController();
        if (insetsController != null) {
            // Hide only the navigation bar
            insetsController.hide(WindowInsets.Type.navigationBars());
            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showSystemUI() {
        final WindowInsetsController insetsController = getWindow().getInsetsController();
        if (insetsController != null) {
            insetsController.show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        }
    }

}
