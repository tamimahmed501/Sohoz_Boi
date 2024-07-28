package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class readparagraph extends AppCompatActivity {

    TextView paragraph, title;
    ImageView rotateButton, back;
    SeekBar seekBar;

    public static String TITLE = "";
    public static String CONTENT = "";
    public static String MEANING = "";



    private SeekBar brightnessSeekBar;
    private ContentResolver cResolver;
    private WindowManager.LayoutParams layoutParams;

    ImageView white, white2, white3, white4, white1;



    RelativeLayout toplayout, bottomlayout, bglay;

    ImageView background, rotate, textsizee, save, brightness;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readparagraph);




        rotateButton = findViewById(R.id.rotate);
        seekBar = findViewById(R.id.seekBar);
        toplayout = findViewById(R.id.toplayout);
        bottomlayout = findViewById(R.id.bottomlayout);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        paragraph = findViewById(R.id.paragraph);
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title.setText(Html.fromHtml(TITLE, Html.FROM_HTML_MODE_COMPACT));
            paragraph.setText(Html.fromHtml(CONTENT, Html.FROM_HTML_MODE_COMPACT)+"\n\nParagraph Meaning\n\n"+Html.fromHtml(MEANING, Html.FROM_HTML_MODE_COMPACT));
        } else {
            title.setText(Html.fromHtml(TITLE));
            paragraph.setText(Html.fromHtml(CONTENT)+"\n\nParagraph Meaning\n\n"+Html.fromHtml(MEANING));

        }



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
                paragraph.setBackgroundColor(bgColor);
                white4.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                paragraph.setTextColor(getResources().getColor(R.color.black));
                updateStatusBarColor(bgColor);
            }
        });

        white3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white4);
                paragraph.setBackgroundColor(bgColor);
                white3.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                paragraph.setTextColor(getResources().getColor(R.color.white));
                updateStatusBarColor(bgColor);
            }
        });

        white2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white3);
                paragraph.setBackgroundColor(bgColor);
                white2.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                paragraph.setTextColor(getResources().getColor(R.color.white));
                updateStatusBarColor(bgColor);
            }
        });

        white1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white1);
                paragraph.setBackgroundColor(bgColor);
                white1.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                paragraph.setTextColor(getResources().getColor(R.color.black));
                updateStatusBarColor(bgColor);
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIamage();
                int bgColor = getResources().getColor(R.color.white2);
                paragraph.setBackgroundColor(bgColor);
                white.setImageDrawable(getResources().getDrawable(R.drawable.tikmark));
                paragraph.setTextColor(getResources().getColor(R.color.black));
                updateStatusBarColor(bgColor);
            }
        });

        title.setText(TITLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        paragraph.setOnClickListener(new View.OnClickListener() {
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
        paragraph.setTextSize(seekBar.getProgress() + 10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paragraph.setTextSize(progress + 10);
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



    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSwipeRight(readparagraph.this);
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
