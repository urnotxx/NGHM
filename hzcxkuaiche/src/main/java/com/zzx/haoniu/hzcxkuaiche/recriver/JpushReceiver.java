package com.zzx.haoniu.hzcxkuaiche.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.hzcxkuaiche.activity.MainActivity;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.JpushInfo;
import com.zzx.haoniu.hzcxkuaiche.entity.OnLineInfo;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;

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
    /**
     * 当前订单是1还是2
     */
    public static int orderState = 0;

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
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (extra != null && !StringUtils.isEmpty(extra)) {
                JpushInfo jpushInfo = JSON.parseObject(extra, JpushInfo.class);
                if (jpushInfo.getTitle().equals("1") && jpushInfo.getContent() != null &&
                        !StringUtils.isEmpty(jpushInfo.getContent())) {//下单推送订单给司机
                    CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("kcgrabOrderInfo", null);
                    OrderInfo orderInfo = JSON.parseObject(jpushInfo.getContent(), OrderInfo.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderInfo", orderInfo);
                    commonEventBusEnity.setMap(map);
                    EventBus.getDefault().post(commonEventBusEnity);
                } else if (jpushInfo.getTitle().equals("5") && jpushInfo.getContent() != null) {
                    //上线和接单 接到客人 取消订单 终点结算 都会推送一次
                    CommonEventBusEnity enity = new CommonEventBusEnity("onLineInfo", "");
                    OnLineInfo onLineInfo = JSON.parseObject(jpushInfo.getContent(), OnLineInfo.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("onLineInfo", onLineInfo);
                    enity.setMap(map);
                    EventBus.getDefault().post(enity);
                } else if (jpushInfo.getTitle().equals("4") && jpushInfo.getContent() != null) {//推送给司机  订单已经被销毁
                    CommonEventBusEnity enity = new CommonEventBusEnity("userCancleOrderInfo", jpushInfo.getContent());
                    EventBus.getDefault().post(enity);
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
