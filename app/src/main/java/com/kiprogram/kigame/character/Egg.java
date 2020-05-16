package com.kiprogram.kigame.character;

import com.kiprogram.kigame.R;

public class Egg {

    private int normalImage = R.drawable.egg_01;
    private int crackImage1 = R.drawable.egg_02;
    private int crackImage2 = R.drawable.egg_03;
    private int crackImage3 = R.drawable.egg_04;

    private final int CRACK_TIME = 60;
    private final int CRACK_TIME_FIRST_STAGE = (int) (CRACK_TIME * 0.25);
    private final int CRACK_TIME_SECOND_STAGE = (int) (CRACK_TIME * 0.5);
    private final int CRACK_TIME_THIRD_STAGE = (int) (CRACK_TIME * 0.75);



    /** ステータス */
    private int image;
    private int damage;


    public Egg() {
        this.image = normalImage;
        this.damage = 0;
    }

    public int getImage() {
        return image;
    }

    public boolean crack(int damage) {
        this.damage += damage;
        if (this.damage > CRACK_TIME) {
            return true;
        }
        if (this.damage > CRACK_TIME_THIRD_STAGE) {
            image = crackImage3;
            return false;
        }
        if (this.damage > CRACK_TIME_SECOND_STAGE) {
            image = crackImage2;
            return false;
        }
        if (this.damage > CRACK_TIME_FIRST_STAGE) {
            image = crackImage1;
            return false;
        }
        return false;
    }

//    @Override
//    public void run() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                crack(1000);
//
//                if (isJumping) {
//                    iv.setY(iv.getY() + 100);
//                    isJumping = false;
//                } else {
//                    // 5分の1でjumpする。
//                    if (0 == getRandom(5)) {
//                        jump();
//                    }
//                }
//
//                sp.setObject(KiSpKey.EGG, this);
//                sp.apply();
//
//                if (crackStatus < 0) {
//                    evolution.evolution();
//                }
//            }
//        });
//    }


}
