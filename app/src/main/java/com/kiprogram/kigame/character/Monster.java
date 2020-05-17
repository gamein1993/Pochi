package com.kiprogram.kigame.character;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.widget.ImageView;

import com.kiprogram.kigame.R;
import com.kiprogram.kigame.item.Meal;
import com.kiprogram.kigame.item.Play;

public class Monster {



    protected int rightImage = R.drawable.dog_1;
    protected int leftImage = R.drawable.dog_2;

    private String name;
    /** ステータス */
    private int image;
    private static final int TIME_TO_EVOLUTION = 600;
    private int evolution;
    private static final int TIME_TO_DIE = 1500;
    private int damage;
    private static final int FULL_STOMACH = 120;
    private int hungry;
    private boolean isHungry;
    private static final int STRESS_FULL = 150;
    private int stress;
    private boolean isStressful;

    private boolean isStopped;



    public Monster() {
        this.image = rightImage;
        this.evolution = 0;
        this.damage = 0;
        this.hungry = 0;
        this.stress = 0;
        this.isHungry = true;
        this.isStopped = false;
        this.isStressful = false;
    }

    public void setName(CharSequence name) {
        this.name = name.toString();
    }
    public String getName() {
        return this.name;
    }

    public int getImage() {
        return image;
    }
    public int getRightImageId() {
        return rightImage;
    }
    public String getEvolution() {
        return String.valueOf(evolution);
    }
    public String getDamage() {
        return String.valueOf(damage);
    }
    public String getHungry() {
        return String.valueOf(hungry);
    }
    public String getStress() {
        return String.valueOf(stress);
    }


    public void landing(ImageView iv) {
        iv.setY(iv.getY() + 20);
    }
    public void jump(ImageView iv) {
        iv.setY(iv.getY() - 20);
    }
    public void right(ImageView iv) {
        image = rightImage;
        iv.setImageResource(rightImage);
        iv.setX(iv.getX() + 20);
    }
    public void left(ImageView iv) {
        image = leftImage;
        iv.setImageResource(leftImage);
        iv.setX(iv.getX() - 20);
    }

    public boolean growUp(int count) {
        evolution += 1;
        return evolution > TIME_TO_EVOLUTION;
    }

    public boolean damage(int count) {
        damage += 1;
        return damage > TIME_TO_DIE;
    }

    public void hungry(Context context, int count) {
        hungry -= 1;
        if (hungry <= 0) {
            hungry = 0;
            damage += 2;
            if (!isHungry) {
                call(context, "default", name, "お腹が空いたわんっ！");
            }
            isHungry = true;
        }
    }

    public void stress(Context context, int count) {
        stress += 1;
        if (stress >= STRESS_FULL) {
            damage += 2;
            if (!isStressful) {
                call(context, "default", name, "あそんでほしいわんっ！");
            }
            isStressful = true;
        }
    }

    public void eat(Context context, ImageView iv, Meal meal) {
        hungry = FULL_STOMACH;
        isHungry = false;
        isStopped = true;
        Resources resources = context.getResources();
        Double x = resources.getDisplayMetrics().widthPixels * 0.20;
        iv.setTranslationX(x.floatValue());
        left(iv);
    }
    public void ate() {
        isStopped = false;
    }

    public void play(ImageView iv, Play play) {
        stress = 0;
        isStressful = false;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void call(Context context, String channelId, String title, String message) {
        // Notification Channel 設定
        NotificationChannel channel = new NotificationChannel(channelId, title , NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(message);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        channel.setShowBadge(true);

        // NotificationManager 取得
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .build();

            // 通知
            notificationManager.notify(R.string.app_name, notification);
        }
    }

}
