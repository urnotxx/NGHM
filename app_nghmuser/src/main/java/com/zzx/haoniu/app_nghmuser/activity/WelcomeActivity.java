package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.http.LoginCallback;

import java.util.Set;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        steepStatusBar();
        ImageView ivWelcome = (ImageView) findViewById(R.id.ivWelcome);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1500);
        ivWelcome.startAnimation(animation);
        handlerLogin.sendEmptyMessageDelayed(0, 1500);
    }

    Handler handlerLogin = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!AppContext.getInstance().checkCallBackUser()) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            } else {
                LoginCallback callback = AppContext.getInstance().getLoginCallback();
                JPushInterface.setAlias(getApplicationContext(), callback.getLoginId(), new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                    }
                });
                JPushInterface.resumePush(getApplicationContext());
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
            WelcomeActivity.this.finish();
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

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
