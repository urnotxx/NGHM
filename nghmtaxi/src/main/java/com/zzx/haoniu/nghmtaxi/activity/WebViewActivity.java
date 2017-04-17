package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.zzx.haoniu.nghmtaxi.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.activity_web_view)
    RelativeLayout activityWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityWebView);
        webView.loadUrl("http://121.40.142.141:8090/Jfinal/driver/enroll");
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
