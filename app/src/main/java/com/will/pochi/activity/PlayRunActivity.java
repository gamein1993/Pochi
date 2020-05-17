package com.will.pochi.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.will.pochi.R;

public class PlayRunActivity extends AppCompatActivity {
    public class EXTRA_FIELD {
        public static final String IMAGE_ID = "image_id";
    }

    private static final int CLEAR_COUNT = 100;

    private FrameLayout frameLayout;
    private Chronometer chronometer;
    private TextView tvStartLabel;
    private ImageView ivCharacter;
    private int characterWidth;
    private int characterHeight;

    private int displayWidth;
    private int displayHeight;

    private int distance;
    private boolean startFlag;
    private boolean endFlag;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_run);

        this.frameLayout = findViewById(R.id.frameLayout);
        this.chronometer = findViewById(R.id.chronometer);
        this.tvStartLabel = findViewById(R.id.tvStartLabel);
        this.ivCharacter = findViewById(R.id.ivCharacter);
        ivCharacter.setImageResource(getIntent().getIntExtra(EXTRA_FIELD.IMAGE_ID, Integer.MIN_VALUE));
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        this.displayWidth = metrics.widthPixels;
        this.displayHeight = metrics.heightPixels;
        this.startFlag = false;
        this.endFlag = false;
        this.count = 0;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.characterWidth = ivCharacter.getWidth();
        this.characterHeight = ivCharacter.getHeight();

        // キャラクタ縦位置設定
        ivCharacter.setY((float) ((displayHeight - characterHeight) * 0.85));

        // ゴールまでの距離取得
        distance = displayWidth - characterWidth;

        ivCharacter.setX(calcCharacterX(distance, count));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (endFlag) {
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!startFlag) {
                startFlag = true;
                tvStartLabel.setVisibility(View.INVISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                return super.onTouchEvent(event);
            }

            count++;
            ivCharacter.setX(calcCharacterX(distance, count));

            if (count >= CLEAR_COUNT) {
                chronometer.stop();
                endFlag = true;
                tvStartLabel.setVisibility(View.VISIBLE);
                tvStartLabel.setText("ゴール！ タイムは" + chronometer.getText() + "！");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, 3000);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        this.displayWidth = metrics.widthPixels;
        this.displayHeight = metrics.heightPixels;
        ivCharacter.setY((float) ((displayHeight - characterHeight) * 0.85));
        // ゴールまでの距離取得
        distance = displayWidth - characterWidth;
        ivCharacter.setX(calcCharacterX(distance, count));
    }

    private float calcCharacterX(int distance, int count) {
        double par = ((double) count) / ((double) CLEAR_COUNT);
        return (float) (distance * par);
    }
}
