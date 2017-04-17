package com.zzx.haoniu.app_nghmuser.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.nostra13.universalimageloader.utils.L;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.MsgInfo;
import com.zzx.haoniu.app_nghmuser.entity.OrderInfo;
import com.zzx.haoniu.app_nghmuser.entity.PayInfo;
import com.zzx.haoniu.app_nghmuser.entity.WebSocketInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

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

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        L.d("TAG", "onClose:" + arg0);
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
                String msg = info.getContent().getMessage();
                if (info.getContent().getTitle() == 1) {
                    CommonEventBusEnity enity = new CommonEventBusEnity("newMsgInfo", null);
                    MsgInfo msgInfo = JSON.parseObject(msg, MsgInfo.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("newMsgInfo", msgInfo);
                    enity.setMap(map);
                    EventBus.getDefault().post(enity);
                } else if (info.getContent().getTitle() == 2) {//司机接单之后  收到的推送
                    if (msg != null && !StringUtils.isEmpty(msg)) {
                        CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("jieOrderInfo", null);
                        OrderInfo orderInfo = JSON.parseObject(msg, OrderInfo.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderInfo", orderInfo);
                        commonEventBusEnity.setMap(map);
                        EventBus.getDefault().post(commonEventBusEnity);
                    }
                } else if (info.getContent().getTitle() == 5) {//司机接到客人之后  收到的推送
                    CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("picked", null);
                    EventBus.getDefault().post(commonEventBusEnity);
                } else if (info.getContent().getTitle() == 3) {//司机到达终点  结束订单的信息
                    if (msg != null && !StringUtils.isEmpty(msg)) {
                        CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("payInfo", null);
                        PayInfo payInfo = JSON.parseObject(msg, PayInfo.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("payInfo", payInfo);
                        commonEventBusEnity.setMap(map);
                        EventBus.getDefault().post(commonEventBusEnity);
                    }
                } else if (info.getContent().getTitle() == 6) {//司机取消订单
                    if (msg != null && !StringUtils.isEmpty(msg)) {
                        CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("cancleOrderInfo", null);
                        OrderInfo payInfo = JSON.parseObject(msg, OrderInfo.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderInfo", payInfo);
                        commonEventBusEnity.setMap(map);
                        EventBus.getDefault().post(commonEventBusEnity);
                    }
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