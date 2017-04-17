package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.GuildAdapter;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.UserGuild;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class UserGuildActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guild);
        ButterKnife.bind(this);
        steepStatusBar();
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<UserGuild> infos;
    private GuildAdapter adapter;

    @Override
    public void initView() {
        tvTitle.setText("用户指南");
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        infos = new ArrayList<>();
        infos.add(new UserGuild("1", "快车计价规则", "起步价0元+时长费0.3元/分钟+里程费1.6元/公里\n" +
                "最低消费8元。当里程费、时长费合计不足最低消费时，直接按最低消费额计算。\n" +
                "拼车价一律8折起，系统会更具实时状况，动态调整拼车折扣"));
        infos.add(new UserGuild("2", "代驾计费规则", "起步价28元（含8公里）+里程费4元/公里（8公里以后开始计费）\n" +
                "等候费：司机到达后可免费等待10分钟，超过后每分钟收您1元"));
        infos.add(new UserGuild("3", "取消规则", "司机接单后即建立了约定，，若您主动取消订单，将会赔偿一定的爽约金" +
                "，若是司机主动取消，您无需进行投诉，平台将会对司机进行惩罚。"));
        infos.add(new UserGuild("4", "申诉", "如遇司机随意加价、恶意拼车、刷单等情况时，您可选择立即取消订单，" +
                "及时联系平台客服进行申诉，平台核实后将对司机进行惩罚。"));
        adapter = new GuildAdapter(mContext, infos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mRefreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
