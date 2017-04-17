package com.zzx.haoniu.app_nghmuser.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作SharedPreference的工具类
 *
 * @author Pinger
 */
public class PreferenceUtil {

    /**
     * 构造私有化
     */
    private PreferenceUtil() {
    }

    /**
     * 获取SharedPreferences对象
     */
    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences("config",Context.MODE_PRIVATE);

    }

    /**
     * 获取Boolean类型的数据，默认返回true
     *
     * @param context
     */
    public static Boolean getBoolean(Context context, String key) {
        // 默认返回true
        return getSp(context).getBoolean(key, true);
    }

    /**
     * 获取Boolean类型的数据
     *
     * @param context
     */
    public static Boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        return getSp(context).getBoolean(key, defaultValue);
    }

    /**
     * 将boolean类型的值存到私有文件
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        getSp(context).edit().putBoolean(key, value).commit();
    }

    /**
     * 获取String类型数据，默认返回空字符串
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getSp(context).getString(key, "");
    }

    /**
     * 获取String类型数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key,
                                   String defaultValue) {
        return getSp(context).getString(key, defaultValue);
    }

    /**
     * 存储String类型的数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        getSp(context).edit().putString(key, value).commit();
    }

    /**
     * 获取Int类型数据，默认0
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getSp(context).getInt(key, 0);
    }

    /**
     * 获取Int类型数据
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        return getSp(context).getInt(key, defaultValue);
    }

    /**
     * 存储Int类型的数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        getSp(context).edit().putInt(key, value).commit();
    }
}
