package com.zzx.haoniu.hzcxdj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.utils.L;
import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.adapter.MainMsgAdapter;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxdj.entity.MsgInfo;
import com.zzx.haoniu.hzcxdj.entity.OnLineInfo;
import com.zzx.haoniu.hzcxdj.entity.OrderInfo;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.recriver.PositionService;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;
import com.zzx.haoniu.hzcxdj.websocket.WebSocketWorker;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @Bind(R.id.ivLeft)
    ImageView ivLeft;
    @Bind(R.id.tvTimeM)
    TextView tvTimeM;
    @Bind(R.id.tvCountM)
    TextView tvCountM;
    @Bind(R.id.tvMoneyM)
    TextView tvMoneyM;
    @Bind(R.id.tvPreM)
    TextView tvPreM;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.tvShoucheM)
    TextView tvShoucheM;
    @Bind(R.id.ivQuanM)
    ImageView ivQuanM;
    @Bind(R.id.tvQuanM)
    TextView tvQuanM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        steepStatusBar();
        serviceIntent = new Intent(MainActivity.this, PositionService.class);
        startService(serviceIntent);
        initView();
        startAnim();
        EventBus.getDefault().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initService();
            }
        }).start();
        AppContext.getInstance().onLine(mContext, 1, "");
    }

    private List<MsgInfo> msgInfos;
    private MainMsgAdapter msgAdapter;
    private boolean orderState = true;//是否接单
    public static Intent serviceIntent = null;
    private boolean isStop = true;
    private String strServiceTime = null;

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("delMsg")) {
            int position = Integer.parseInt(event.getMessage());
            delNotices(position);
        } else if (event.getSendCode().equals("shouche")) {
            isStop = true;
            stopJieDan();
        } else if (event.getSendCode().equals("jiedan")) {
            handler.sendEmptyMessageDelayed(0, 600000);
            isStop = false;
            strServiceTime = event.getMessage();
            initTime();
            startJieDan();
        } else if (event.getSendCode().equals("exit")) {
            finish();
        } else if (event.getSendCode().equals("djgrabOrderInfo")) {
            OrderInfo orderInfo = (OrderInfo) event.getMap().get("orderInfo");
            Bundle bundle = new Bundle();
            bundle.putSerializable("orderInfo", orderInfo);
            startActivity(JieDanDialogActivity.class, bundle);
        } else if (event.getSendCode().equals("unLogInTaxi")) {
            AppContext.getInstance().cleanLoginCallback();
            AppContext.getInstance().cleanUserInfo();
            startActivity(new Intent(mContext, LoginActivity.class));
            ToastUtils.showTextToast(mContext, "登录已过期,请重新登录!");
            finish();
        } else if (event.getSendCode().equals("onLineInfo")) {
            OnLineInfo onLineInfo = (OnLineInfo) event.getMap().get("onLineInfo");
            if (onLineInfo != null)
                initMsg(onLineInfo);
        } else if (event.getSendCode().equals("newMsgInfo")) {
            MsgInfo msgInfo = (MsgInfo) event.getMap().get("newMsgInfo");
            if (msgInfo != null) {
                msgInfos.add(0, msgInfo);
                msgAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initMsg(OnLineInfo info) {
        if (info.getCloseRate() != null && !StringUtils.isEmpty(info.getCloseRate()))
            tvPreM.setText(info.getCloseRate());
        tvMoneyM.setText(info.getDriveramount() + "元");
        tvCountM.setText(info.getBtotalOrderNumber() + "");
    }

    private int serciceTime = 0;

    private void initTime() {
        if (strServiceTime != null) {
            serciceTime = Integer.parseInt(strServiceTime);
        }
        if (serciceTime < 60) {
            tvTimeM.setText(serciceTime + "分钟");
        } else {
            int hour = (int) Math.floor(serciceTime / 60);
            int mintues = serciceTime % 60;
            tvTimeM.setText(hour + "." + mintues + "小时");
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isStop) {
                return;
            } else {
                serciceTime = serciceTime + 10;
                if (serciceTime < 60) {
                    tvTimeM.setText(serciceTime + "分钟");
                } else {
                    int hour = (int) Math.floor(serciceTime / 60);
                    int mintues = serciceTime % 60;
                    tvTimeM.setText(hour + "." + mintues + "小时");
                }
            }
        }
    };

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        msgInfos = new ArrayList<>();
        msgAdapter = new MainMsgAdapter(mContext, msgInfos, 1);
        recyclerView.setAdapter(msgAdapter);
        msgAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ivLeft, R.id.tvShoucheM, R.id.ivQuanM})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeft:
                Bundle bundle = new Bundle();
                bundle.putBoolean("orderState", orderState);
                startActivity(UserInfoActivity.class, bundle);
                break;
            case R.id.tvShoucheM:
                AppContext.getInstance().onLine(mContext, 0, "收车中...");
                break;
            case R.id.ivQuanM:
                AppContext.getInstance().onLine(mContext, 1, "上线中...");
                break;
        }
    }

    private void stopJieDan() {
        orderState = false;
        tvShoucheM.setVisibility(View.GONE);
        tvQuanM.setText("出车");
        tvQuanM.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));//红色
        stopAnim();
    }

    private void startJieDan() {
        if (!orderState) {
            orderState = true;
            tvShoucheM.setVisibility(View.VISIBLE);
            tvQuanM.setText("接单中");
            tvQuanM.setTextColor(Color.rgb(0x09, 0xa6, 0xed));//蓝色09A6ED
            startAnim();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        msgInfos.clear();
        requestNotices();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }


    Animation operatingAnim;

    public void startAnim() {
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_quan);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (null != operatingAnim) {
            ivQuanM.startAnimation(operatingAnim);
        }
    }

    public void stopAnim() {
        if (operatingAnim != null) {
            ivQuanM.clearAnimation();
        }
    }


    private void initService() {
        URI uri = null;
        try {
            uri = new URI(AppConfig.mainWebSocker + "?" + AppContext.getInstance().getLoginCallback().getSecret() +
                    AppContext.getInstance().getLoginCallback().getToken() + AppContext.getInstance().getLoginCallback().getAppType());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            L.d("TAG", "initService1：" + e.toString());
        }
        WebSocketWorker webSocketWorker = new WebSocketWorker(uri, new Draft_17());
        try {
            webSocketWorker.connectBlocking();//此处如果用webSocketWorker.connect();会出错，需要多注意
//            webSocketWorker.send("test");
            try {
                String json = "{content:\"" + "快车端" +
                        "\",type:\"" + 1 + "\"}";
                webSocketWorker.send(json);
            } catch (NotYetConnectedException e) {
                e.printStackTrace();
                return;
            } catch (WebsocketNotConnectedException exception) {
                exception.printStackTrace();
                return;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            L.d("TAG", "initService2：" + e.toString());
        }
    }

    private void requestNotices() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestNoticesList, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "获取消息:" + json);
                    List<MsgInfo> msgInfoList = JSON.parseArray(json, MsgInfo.class);
                    if (msgInfoList != null && msgInfoList.size() > 0) {
                        msgInfos.addAll(msgInfoList);
                    }
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                msgAdapter.notifyDataSetChanged();
            }
        });
    }

    private void delNotices(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("noticeId", msgInfos.get(position).getNotice_id());
        ApiClient.requestNetHandle(mContext, AppConfig.requestDelNotices, "删除中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                msgInfos.remove(position);
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(serviceIntent);
        AppContext.getInstance().onLine(mContext, 0, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        WebSocketWorker.orderState = 1;
    }
}
