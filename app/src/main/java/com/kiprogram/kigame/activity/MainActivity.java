package com.kiprogram.kigame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kiprogram.kigame.R;
import com.kiprogram.kigame.character.Egg;
import com.kiprogram.kigame.character.Monster;
import com.kiprogram.kigame.character.Monster2;
import com.kiprogram.kigame.item.Meat;
import com.kiprogram.kigame.item.Run;
import com.kiprogram.kigame.sp.KiSharedPreferences;
import com.kiprogram.kigame.sp.KiSpKey;
import com.kiprogram.kigame.util.KiUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private class REQUEST_CODE {
        private static final int PLAY_RUN = 1000;
    }

    private static final long PERIOD = 10;

    private KiSharedPreferences sp;
    private Timer timer;
    private Handler handler;
    private FrameLayout frameLayout;
    private ImageView ivCharacter;
    private boolean isJumping = false;

    private TextView tvDamage;
    private TextView tvHungry;
    private TextView tvStress;
    private TextView tvEvolution;

    private enum Chara {
        EGG,
        MONSTER,
        MONSTER2
    }
    private Chara chara;
    private Egg egg;
    private Monster monster;
    private Monster2 monster2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アクションバー設定
        setActionBar();

        // メンバ変数
        this.sp = new KiSharedPreferences(this);
        this.timer = new Timer();
        this.handler = new Handler();
        this.frameLayout = findViewById(R.id.frameLayout);
        this.ivCharacter = findViewById(R.id.ivCharacter);

        tvDamage = findViewById(R.id.tvDamage);
        tvHungry = findViewById(R.id.tvHungry);
        tvStress = findViewById(R.id.tvStress);
        tvEvolution = findViewById(R.id.tvEvolution);

        this.egg = sp.getObject(KiSpKey.EGG, Egg.class);
        this.monster = sp.getObject(KiSpKey.MONSTER, Monster.class);
        this.monster2 = sp.getObject(KiSpKey.MONSTER2, Monster2.class);
        if (egg == null && monster == null && monster2 == null) this.egg = new Egg();

        if (egg != null) {
            this.chara = Chara.EGG;
            ivCharacter.setImageResource(egg.getImage());
            timer.schedule(new EggTimerTask(), 0, PERIOD);
        }
        if (monster != null) {
            this.chara = Chara.MONSTER;
            ivCharacter.setImageResource(monster.getImage());
            timer.schedule(new MonsterTimerTask(), 0, PERIOD);
        }
        if (monster2 != null) {
            this.chara = Chara.MONSTER2;
            ivCharacter.setImageResource(monster2.getImage());
            timer.schedule(new MonsterTimerTask2(), 0, PERIOD);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private class EggTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (egg.crack(1)) {
                        timer.cancel();
                        timer = new Timer();
                        monster = new Monster();
                        ivCharacter.setImageResource(monster.getImage());
                        chara = Chara.MONSTER;
                        sp.remove(KiSpKey.EGG);
                        sp.setObject(KiSpKey.MONSTER, monster);
                        sp.apply();
                        timer.schedule(new MonsterTimerTask(), 0, PERIOD);
                        return;
                    }
                    sp.setObject(KiSpKey.EGG, egg);
                    sp.apply();
                    ivCharacter.setImageResource(egg.getImage());
                }
            });
        }
    }

    private class MonsterTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (monster.growUp(1)) {
                        timer.cancel();
                        timer = new Timer();
                        monster2 = new Monster2();
                        ivCharacter.setImageResource(monster2.getImage());
                        chara = Chara.MONSTER2;
                        sp.remove(KiSpKey.MONSTER);
                        sp.setObject(KiSpKey.MONSTER2, monster2);
                        sp.apply();
                        timer.schedule(new MonsterTimerTask2(), 0, PERIOD);
                        return;
                    }
                    if (monster.damage(1)) {
                        timer.cancel();
                        timer = null;
                        return;
                    }
                    monster.hungry(getApplicationContext(), 1);
                    monster.stress(getApplicationContext(), 1);
                    move();
                    sp.setObject(KiSpKey.MONSTER, monster);
                    sp.apply();
                    ivCharacter.setImageResource(monster.getImage());

                    tvDamage.setText(monster.getDamage());
                    tvHungry.setText(monster.getHungry());
                    tvStress.setText(monster.getStress());
                    tvEvolution.setText(monster.getEvolution());
                }
            });
        }

        private void move() {
            if (monster.isStopped()) {
                return;
            }
            if (isJumping) {
                landing();
                return;
            }
            int random = KiUtil.getRandom(20);
            if (random == 0) jump();
            if (random == 1) right();
            if (random == 2) left();
        }

        private void landing() {
            monster.landing(ivCharacter);
            isJumping = false;
        }
        private void jump() {
            monster.jump(ivCharacter);
            isJumping = true;
        }
        private void right() {
            monster.right(ivCharacter);
        }
        private void left() {
            monster.left(ivCharacter);
        }
    }

    private class MonsterTimerTask2 extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (monster2.growUp(1)) {
                        timer.cancel();
//                        timer = null;
                        return;
                    }
//                    if (monster2.damage(1)) {
//                        timer.cancel();
//                        timer = null;
//                        return;
//                    }
                    monster2.hungry(getApplicationContext(), 1);
                    monster2.stress(getApplicationContext(), 1);
                    move();
                    sp.setObject(KiSpKey.MONSTER2, monster2);
                    sp.apply();
                    ivCharacter.setImageResource(monster2.getImage());

                    tvDamage.setText(monster2.getDamage());
                    tvHungry.setText(monster2.getHungry());
                    tvStress.setText(monster2.getStress());
                    tvEvolution.setText(monster2.getEvolution());
                }
            });
        }

        private void move() {
            if (monster2.isStopped()) {
                return;
            }
            if (isJumping) {
                landing();
                return;
            }
            int random = KiUtil.getRandom(20);
            if (random == 0) jump();
            if (random == 1) right();
            if (random == 2) left();
        }

        private void landing() {
            monster2.landing(ivCharacter);
            isJumping = false;
        }
        private void jump() {
            monster2.jump(ivCharacter);
            isJumping = true;
        }
        private void right() {
            monster2.right(ivCharacter);
        }
        private void left() {
            monster2.left(ivCharacter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemMeat) {
            Meat meat = new Meat();
            switch (chara) {
                case EGG:
                    Toast.makeText(this, "今は使用できません。", Toast.LENGTH_SHORT).show();
                    break;
                case MONSTER:
                    monster.eat(this, ivCharacter, meat);
                    final ImageView iv = new ImageView(this);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(KiUtil.convertDpToPixel(this, 100), KiUtil.convertDpToPixel(this, 100));
                    iv.setLayoutParams(params);
                    iv.setImageResource(meat.getImageId());
                    frameLayout.addView(iv);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.eat);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            frameLayout.removeView(iv);
                            monster.ate();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    iv.startAnimation(animation);
                    break;
                case MONSTER2:
                    monster2.eat(this, ivCharacter, meat);
                    final ImageView iv2 = new ImageView(this);
                    FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(KiUtil.convertDpToPixel(this, 100), KiUtil.convertDpToPixel(this, 100));
                    iv2.setLayoutParams(params2);
                    iv2.setImageResource(meat.getImageId());
                    frameLayout.addView(iv2);
                    Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.eat);
                    animation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            frameLayout.removeView(iv2);
                            monster.ate();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    iv2.startAnimation(animation2);
                    break;
            }
        }

        if (item.getItemId() == R.id.itemRun) {
            switch (chara) {
                case EGG:
                    Toast.makeText(this, "今は使用できません。", Toast.LENGTH_SHORT).show();
                    break;
                case MONSTER:
                    Intent intent = new Intent(getApplicationContext(), PlayRunActivity.class);
                    intent.putExtra(PlayRunActivity.EXTRA_FIELD.IMAGE_ID, monster.getRightImageId());
                    startActivityForResult(intent, REQUEST_CODE.PLAY_RUN);
                    break;
                case MONSTER2:
                    Intent intent2 = new Intent(getApplicationContext(), PlayRunActivity.class);
                    intent2.putExtra(PlayRunActivity.EXTRA_FIELD.IMAGE_ID, monster2.getRightImageId());
                    startActivityForResult(intent2, REQUEST_CODE.PLAY_RUN);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE.PLAY_RUN) {
            // TODO ストレス軽減
            switch (chara) {
                case MONSTER:
                    monster.play(ivCharacter, new Run());
                    break;
                case MONSTER2:
                    monster2.play(ivCharacter, new Run());
                    break;
            }

        }
    }

    /**
     * アクションバーの設定をします。
     */
    private void setActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(null);
    }


}
