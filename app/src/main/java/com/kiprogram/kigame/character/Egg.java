package com.kiprogram.kigame.character;

public class Egg {
    private final int normalImage;
    private final int crackImage1;
    private final int crackImage2;
    private final int crackImage3;

    // 割れるまでの時間
    private static final long CRACK_TIME = 60000;
    private static final long CRACK_TIME_FIRST_STAGE = 60000;

    // 割れ具合
    private long crackCondition = 60000;
    // ステータス
    private int status = 0;

    public Egg(int normalImage, int crackImage1, int crackImage2, int crackImage3) {
        this.normalImage = normalImage;
        this.crackImage1 = crackImage1;
        this.crackImage2 = crackImage2;
        this.crackImage3 = crackImage3;
    }

    public void secondly() {
        crackCondition -= 1000;

    }


}
