package com.zzx.haoniu.hzcxkuaiche.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.nostra13.universalimageloader.utils.L;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.MsgInfo;
import com.zzx.haoniu.hzcxkuaiche.entity.OnLineInfo;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.entity.WebSocketInfo;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/10.
 */

public class WebSocketWorker extends WebSocketClient {

    public WebSocketWorker(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    /**
     * 接收推送 ：0 可接单   3  拼单订单，已接到一位客人
     * 不接受推送：1 接到订单  且订单不拼单    2接到一条拼单推送，还未接到客人   4已接两条订单
     **/
    public static int jiedanState = 0;

    /**
     * 1 接单Dialog正在显示
     * 2 接单Dialog已消失
     */
    public static int dialogShow = 2;
    /**
     * 当前订单是1还是2
     */
    public static int orderState = 1;

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        L.d("TAG", "onClose:" + arg0);
        EventBus.getDefault().post(new CommonEventBusEnity("wsCloseK", null));
    }

    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        L.d("TAG", "onError:" + arg0.toString());
    }

    @Override
    public void onMessage(String arg0) {
        // TODO Auto-generated method stub
        try {
            WebSocketInfo info = JSON.parseObject(arg0, WebSocketInfo.class);
            if (info != null) {
                if (info.getContent().getTitle() == 1) {
                    if (info.getType() == 3) {//下单推送订单给司机
                        CommonEventBusEnity enity = new CommonEventBusEnity("kcgrabOrderInfo", null);
                        String msg = info.getContent().getMessage();
                        if (msg != null && !StringUtils.isEmpty(msg)) {
                            OrderInfo orderInfo = JSON.parseObject(msg, OrderInfo.class);
                            Map<String, Object> map = new HashMap<>();
                            map.put("orderInfo", orderInfo);
                            enity.setMap(map);
                            EventBus.getDefault().post(enity);
                        }
                    } else if (info.getType() == 4) {//接收到新的信息推送
                        String msg = info.getContent().getMessage();
                        CommonEventBusEnity enity = new CommonEventBusEnity("newMsgInfo", null);
                        MsgInfo msgInfo = JSON.parseObject(msg, MsgInfo.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("newMsgInfo", msgInfo);
                        enity.setMap(map);
                        EventBus.getDefault().post(enity);
                    }
                } else if (info.getContent().getTitle() == 4) {//推送给司机  订单已经被销毁
                    String orderId = info.getContent().getMessage();
                    CommonEventBusEnity enity = new CommonEventBusEnity("userCancleOrderInfo", orderId);
                    EventBus.getDefault().post(enity);
                } else if (info.getContent().getTitle() == 5) {//上线和接单 接到客人 取消订单 终点结算 都会推送一次
                    CommonEventBusEnity enity = new CommonEventBusEnity("onLineInfo", "");
                    String msg = info.getContent().getMessage();
                    if (msg != null && !StringUtils.isEmpty(msg)) {
                        OnLineInfo onLineInfo = JSON.parseObject(msg, OnLineInfo.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("onLineInfo", onLineInfo);
                        enity.setMap(map);
                    }
                    EventBus.getDefault().post(enity);
                }
            }
        } catch (JSONException jsonException) {
            L.d("TAG", "onMessage:" + jsonException.toString());
        }
    }


    @Override
    public void onOpen(ServerHandshake arg0) {
        // TODO Auto-generated method stub
        L.d("TAG", "onOpen:" + arg0.toString());
    }

}