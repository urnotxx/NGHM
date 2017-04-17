package com.zzx.haoniu.app_nghmuser.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 应用程序配置保存数据
 *
 * @autor:Bin
 * @version:1.0
 * @created:2014-7-28下午5:05:33
 **/
public class Preference {

    public static boolean getBoolPreferences(Context context, String key,
                                             boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, defValue);
    }

    public static int getIntPreferences(Context context, String key,
                                        int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                key, defValue);
    }

    public static long getLongPreferences(Context context, String key,
                                          long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
                key, defValue);
    }

    public static String getStringPreferences(Context context, String key,
                                              String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, defValue);
    }

    public static void saveBoolPreferences(Context context, String key,
                                           boolean defValue) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key, defValue);
        editor.commit();
    }

    public static void saveIntPreferences(Context context, String key,
                                          int defValue) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putInt(key, defValue);
        editor.commit();
    }

    public static void saveLongPreferences(Context context, String key,
                                           long defValue) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putLong(key, defValue);
        editor.commit();
    }

    public static void saveStringPreferences(Context context, String key,
                                             String defValue) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString(key, defValue);
        editor.commit();
    }

}
