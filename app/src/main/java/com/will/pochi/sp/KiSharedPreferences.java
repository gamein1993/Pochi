package com.will.pochi.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

public class KiSharedPreferences {
    /** SharedPreferencesの保存設定名 */
    public static final String NAME = "Game";

    /** SharedPreferences */
    public SharedPreferences sp;
    public Editor editor;

    /**
     * コンストラクタ
     * @param context コンテキスト
     */
    public KiSharedPreferences(Context context) {
        sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * 値を設定します。
     * @param key 値を設定するkey
     * @param value 値
     */
    public void setValue(KiSpKey key, CharSequence value) {
        if (editor == null) {
            editor = sp.edit();
        }
        editor.putString(key.text, value.toString());
    }

    /**
     * 値を設定します。
     * @param key 値を設定するkey
     * @param value 値
     */
    public void setValue(KiSpKey key, int value) {
        setValue(key, String.valueOf(value));
    }

    /**
     * 値を設定します。
     * @param key 値を設定するkey
     * @param value 値
     */
    public void setValue(KiSpKey key, boolean value) {
        setValue(key, String.valueOf(value));
    }

    /**
     * オブジェクト保存
     * @param key
     * @param object
     */
    public void setObject(KiSpKey key, Object object) {
        Gson gson = new Gson();
        setValue(key, gson.toJson(object));
    }

    /**
     * 値を取得します。 値が設定されていない場合は null が返ります。
     * @param key 値を取得するフィールド
     * @return 値
     */
    public String getString(KiSpKey key) {
        return sp.getString(key.text, null);
    }

    /**
     * 値を取得します。 値が設定されていない場合は Integer.MIN_VALUE が返ります。
     * @param key 値を取得するフィールド
     * @return 値
     */
    public int getInt(KiSpKey key) {
        String str = getString(key);
        if (str == null) {
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(str);
    }

    /**
     * 値を取得します。 値が設定されていない場合は false が返ります。
     * @param key 値を取得するフィールド
     * @return 値
     */
    public boolean getBoolean(KiSpKey key) {
        return Boolean.parseBoolean(getString(key));
    }

    public <T> T getObject(KiSpKey key, Class<T> classOfT) {
        Gson gson = new Gson();
        String str = getString(key);
        if (str == null) return null;
        return gson.fromJson(getString(key), classOfT);
    }

    public void remove(KiSpKey key) {
        if (editor == null) {
            editor = sp.edit();
        }
        editor.remove(key.text);
    }

    /**
     * 設定した値を保存します。
     */
    public void apply() {
        editor.apply();
        editor = null;
    }
}
