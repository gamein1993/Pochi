package com.kiprogram.kigame.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;

import com.kiprogram.kigame.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView ivCharacter;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean isJumping = false;
    private float characterY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ツールバーの表示
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(null);

        // メンバ変数設定
        ivCharacter = findViewById(R.id.ivCharacter);

        // 画像設定
        ivCharacter.setImageResource(R.drawable.egg_01);

        // 非同期処理
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isJumping) {
                            float f = ivCharacter.getY();
                            ivCharacter.setY(ivCharacter.getY() + 20);
                            isJumping = false;
                        } else {
                            ivCharacter.setY(ivCharacter.getY() - 20);
                            isJumping = true;
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
