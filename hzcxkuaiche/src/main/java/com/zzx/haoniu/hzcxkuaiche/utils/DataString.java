package com.zzx.haoniu.hzcxkuaiche.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/11/10.
 */
public class DataString {
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "-" + mMonth + "-" + mDay + "  星期" + mWay;
    }

    public static String yesterday() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String getLongTime(int dateType) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case 0: // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case 1: // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case 2: // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case 3: // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case 4: // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case 5: // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
// System.out.println(df.format(c.getTime()));
                break;
            case 6: // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
// System.out.println(df.format(c.getTime()));
                break;
            case 7: // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
// System.out.println(df.format(c.getTime()));
                break;
        }
        String dateString = formatter.format(c.getTimeInMillis());
        return dateString;
    }

    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("hh:mm");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 2012-02-24
        date = java.sql.Date.valueOf(str);
        return date;
    }

    /**
     * 按照提供的格式将字符串转换成Date类型
     *
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd mm:HH:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 按照提供的格式将字符串转换成Date类型
     *
     * @param dateStr
     * @return
     */
    public static Date toDateYMDE(String dateStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}