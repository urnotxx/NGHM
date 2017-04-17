package com.zzx.haoniu.hzcxkuaiche.utils;

import android.annotation.SuppressLint;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String[] mSimArray2 = { "未知", "未插卡", "锁定状态，需要用户的PIN码解锁", "锁定状态，需要用户的PUK码解锁", "锁定状态，需要网络的PIN码解锁",
			"就绪" };
	// public static String[] mSimArray = {"UNKNOWN", "ABSENT", "PIN_REQUIRED",
	// "PUK_REQUIRED",
	// "NETWORK_LOCKED", "READY"};
	public static String[] mVoiceArray = { "NONE", "GSM", "CDMA", "SIP" };
	// public static String[] mNetArray = {"UNKNOWN", "GPRS", "EDGE", "UMTS",
	// "CDMA",
	// "CDMA - EvDo rev. 0", "CDMA - EvDo rev. A", "CDMA - 1xRTT", "HSDPA",
	// "HSUPA", "HSPA",
	// "iDEN", "CDMA - EvDo rev. B", "LTE", "CDMA - eHRPD", "HSPA+"};
	public static String[] mClassArray = { "UNKNOWN", "2G", "3G", "4G" };

	public static int TYPE_UNKNOWN = 0;
	public static int TYPE_2G = 1;
	public static int TYPE_3G = 2;
	public static int TYPE_4G = 3;

	public static int TYPE_GSM = 1;
	public static int TYPE_CDMA = 2;
	public static int TYPE_LTE = 3;
	public static int TYPE_WCDMA = 4;

	@SuppressLint("SimpleDateFormat")
	public static String getNowDateTime() {
		SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d_date = new Date();
		String s_date = "";
		s_date = s_format.format(d_date);
		return s_date;
	}

	public static String getNetworkTypeName(TelephonyManager tm, int net_type) {
		String type_name = "";
		try {
			Method method = tm.getClass().getMethod("getNetworkTypeName", Integer.TYPE);
			type_name = (String) method.invoke(tm, net_type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type_name;
	}

	public static int getClassType(TelephonyManager tm, int net_type) {
		int class_type = 0;
		try {
			Method method = tm.getClass().getMethod("getNetworkClass", Integer.TYPE);
			class_type = (Integer) method.invoke(tm, net_type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return class_type;
	}

	public static String getClassName(TelephonyManager tm, int net_type) {
		int class_type = getClassType(tm, net_type);
		return mClassArray[class_type];
	}

}
