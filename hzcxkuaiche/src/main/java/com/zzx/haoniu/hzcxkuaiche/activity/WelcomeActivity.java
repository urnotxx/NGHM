package com.zzx.haoniu.hzcxkuaiche.activity;

import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
            if (AppContext.getInstance().checkCallBackUser() && AppContext.getInstance().checkUser()) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
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
    public void initView( ) {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
