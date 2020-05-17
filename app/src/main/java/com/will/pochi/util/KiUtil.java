package com.will.pochi.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Random;

public class KiUtil {
    /**
     * ランダム値を生成します。
     * @param max 最大値 (最大値といいつつ0を含むのでmax値にはなりません。)
     * @return ランダム値
     */
    public static int getRandom(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    /**
     * ピクセルをdp換算して返します。
     * @param px ピクセル
     * @return dp
     */
    public static int convertPixelsToDp(Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    /**
     * dpをピクセル換算して返します。
     * @param dp dp
     * @return ピクセル
     */
    public static int convertDpToPixel(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi /160f);
        return (int) px;
    }
}
