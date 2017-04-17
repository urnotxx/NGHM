package com.zzx.haoniu.app_nghmuser.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.SearchAdapterList;
import com.zzx.haoniu.app_nghmuser.entity.ComAddInfo;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialogsamples.utils.L;
import self.androidbase.utils.DensityUtils;

public class SearchActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

    public static final int RESUALT_PLACE = 12;
    @Bind(R.id.tvHomeS)
    TextView tvHomeS;
    @Bind(R.id.tvCompanyS)
    TextView tvCompanyS;
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private SearchAdapterList adapter;
    private ListView lvSearch;
    private TextView tvLoadMore, tvNoData;
    private LinearLayout llTopS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mContext = this;
        if (getIntent().getExtras() != null) {
            city = getIntent().getStringExtra("city");
            flag = getIntent().getIntExtra("flag", 0);
        }
        initView();
        initEvent();
        initData();
        requestCommeonAddress();
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

    private int flag = 0;// 1 添加常用地址   2 搜索地址

    private boolean isFrist = false;
    private String city;

    private void initEvent() {
        findViewById(R.id.llSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (edSearch.getText().toString() != null && !StringUtils.isEmpty(edSearch.getText().toString())) {
                        if (keyEvent != null) {
                            switch (keyEvent.getAction()) {
                                case KeyEvent.ACTION_UP:
                                    //发送请求
                                    keyWord = edSearch.getText().toString();
                                    doSearchQuery();
                                    hintKbTwo();
                                    return true;
                                default:
                                    return true;
                            }
                        }
                    } else {
                        ToastUtils.showTextToast(mContext, "请输入搜索关键字");
                        return false;
                    }
                }
                return false;
            }
        });
        findViewById(R.id.tvLoadMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyWord == "") {
                    ToastUtils.showTextToast(mContext, "请输入您要查找的内容");
                    return;
                }
                nextSearch();
            }
        });
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("searchName", poiItems.get(i).getTitle());
                intent.putExtra("searchLat", poiItems.get(i).getLatLonPoint().getLatitude());
                intent.putExtra("searchLng", poiItems.get(i).getLatLonPoint().getLongitude());
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(RESUALT_PLACE, intent);
                finish();
            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        poiItems.clear();
        currentPage = 0;
        poiResult = null;
        showProgressDialog();// 显示进度框
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    private List<PoiItem> poiItems;

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        dissmissProgressDialog();// 隐藏对话框
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    this.poiResult = poiResult;
                    List<PoiItem> poiItems1 = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    poiItems.addAll(poiItems1);
                }
                tvLoadMore.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
            } else {
                ToastUtils.showTextToast(mContext, "未搜索到结果");
                tvNoData.setVisibility(View.VISIBLE);
                findViewById(R.id.scrollView).setVisibility(View.GONE);
            }
            setListViewHeightBasedOnChildren(lvSearch);
            adapter.notifyDataSetChanged();
        } else {
            setListViewHeightBasedOnChildren(lvSearch);
            adapter.notifyDataSetChanged();
            ToastUtils.showTextToast(mContext, i + "");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }


    private void nextSearch() {
        showProgressDialog();// 显示进度框
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                tvLoadMore.setVisibility(View.GONE);
                dissmissProgressDialog();
                ToastUtils.showTextToast(mContext, "已加载全部!");
            }
        }
    }

    private Context mContext;
    private EditText edSearch;

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void initView() {
        edSearch = (EditText) findViewById(R.id.edSearch);
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        llTopS = (LinearLayout) findViewById(R.id.llTopS);
        if (flag == 1) {
            llTopS.setVisibility(View.GONE);
        } else {
            llTopS.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        infos = new ArrayList<>();
        poiItems = new ArrayList<>();
        adapter = new SearchAdapterList(mContext, poiItems);
        lvSearch.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 重新计算ListView的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + DensityUtils.dip2px(mContext, 10f);
        listView.setLayoutParams(params);
    }

    private List<ComAddInfo> infos;

    private void requestCommeonAddress() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestComAdd, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "常用地址:" + json);
                    List<ComAddInfo> comAddInfos = JSON.parseArray(json, ComAddInfo.class);
                    if (comAddInfos != null && comAddInfos.size() > 0) {
                        infos.addAll(comAddInfos);
                        initCommeonAddress();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void initCommeonAddress() {
        if (infos != null && infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).getAddress_title().equals("家")) {
                    tvHomeS.setText(infos.get(i).getAddress());
                } else if (infos.get(i).getAddress_title().equals("公司")) {
                    tvCompanyS.setText(infos.get(i).getAddress());
                }
            }
        }
    }

    @OnClick({R.id.llHomeS, R.id.llCompangS})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llHomeS:
                if (infos != null && infos.size() > 0) {
                    if (infos.size() == 1) {
                        if (infos.get(0).getAddress_title().equals("家")) {
                            Intent intent = new Intent();
                            intent.putExtra("searchName", infos.get(0).getAddress());
                            intent.putExtra("searchLat", infos.get(0).getLatitude());
                            intent.putExtra("searchLng", infos.get(0).getLongitude());
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                            setResult(RESUALT_PLACE, intent);
                            finish();
                        } else {
                            startActivity(new Intent(mContext, CommeonAddressActivity.class));
                            finish();
                        }
                    } else if (infos.size() == 2) {
                        for (int i = 0; i < infos.size(); i++) {
                            if (infos.get(i).getAddress_title().equals("家")) {
                                Intent intent = new Intent();
                                intent.putExtra("searchName", infos.get(i).getAddress());
                                intent.putExtra("searchLat", infos.get(i).getLatitude());
                                intent.putExtra("searchLng", infos.get(i).getLongitude());
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                                setResult(RESUALT_PLACE, intent);
                                finish();
                            }
                        }
                    }
                } else {
                    startActivity(new Intent(mContext, CommeonAddressActivity.class));
                    finish();
                }
                break;
            case R.id.llCompangS:
                if (infos != null && infos.size() > 0) {
                    if (infos.size() == 1) {
                        if (infos.get(0).getAddress_title().equals("公司")) {
                            Intent intent = new Intent();
                            intent.putExtra("searchName", infos.get(0).getAddress());
                            intent.putExtra("searchLat", infos.get(0).getLatitude());
                            intent.putExtra("searchLng", infos.get(0).getLongitude());
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                            setResult(RESUALT_PLACE, intent);
                            finish();
                        } else {
                            startActivity(new Intent(mContext, CommeonAddressActivity.class));
                            finish();
                        }
                    } else if (infos.size() == 2) {
                        for (int i = 0; i < infos.size(); i++) {
                            if (infos.get(i).getAddress_title().equals("公司")) {
                                Intent intent = new Intent();
                                intent.putExtra("searchName", infos.get(i).getAddress());
                                intent.putExtra("searchLat", infos.get(i).getLatitude());
                                intent.putExtra("searchLng", infos.get(i).getLongitude());
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                                setResult(RESUALT_PLACE, intent);
                                finish();
                            }
                        }
                    }
                } else {
                    startActivity(new Intent(mContext, CommeonAddressActivity.class));
                    finish();
                }
                break;
        }
    }
}
