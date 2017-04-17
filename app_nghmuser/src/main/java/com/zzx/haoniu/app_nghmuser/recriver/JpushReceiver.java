package com.zzx.haoniu.app_nghmuser.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.app_nghmuser.activity.MainActivity;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.JpushInfo;
import com.zzx.haoniu.app_nghmuser.entity.OrderInfo;
import com.zzx.haoniu.app_nghmuser.entity.PayInfo;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String registrationId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            AppContext.getInstance().saveRegistrationId(registrationId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
            L.d("EXTRA_EXTRA:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (extra != null && !StringUtils.isEmpty(extra)) {
                JpushInfo info = JSON.parseObject(extra, JpushInfo.class);
                if (info.getTitle().equals("2") && info.getContent() != null) {
                    // 2   司机接单之后  收到的推送
                    CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("jieOrderInfo", null);
                    OrderInfo orderInfo = JSON.parseObject(info.getContent(), OrderInfo.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderInfo", orderInfo);
                    commonEventBusEnity.setMap(map);
                    EventBus.getDefault().post(commonEventBusEnity);
                } else if (info.getTitle().equals("5")) {// 5   司机接到客人之后  收到的推送
                    CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("picked", null);
                    EventBus.getDefault().post(commonEventBusEnity);
                } else if (info.getTitle().equals("3") && info.getContent() != null) {// 3   司机到达终点  结束订单的信息
                    CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("payInfo", null);
                    PayInfo payInfo = JSON.parseObject(info.getContent(), PayInfo.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("payInfo", payInfo);
                    commonEventBusEnity.setMap(map);
                    EventBus.getDefault().post(commonEventBusEnity);
                } else if (info.getTitle().equals("6") && info.getContent() != null) {//  6  司机取消订单
                    CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("cancleOrderInfo", null);
                    OrderInfo payInfo = JSON.parseObject(info.getContent(), OrderInfo.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderInfo", payInfo);
                    commonEventBusEnity.setMap(map);
                    EventBus.getDefault().post(commonEventBusEnity);
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
    }
}
