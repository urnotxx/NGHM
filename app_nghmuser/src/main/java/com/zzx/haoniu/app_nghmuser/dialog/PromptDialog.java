package com.zzx.haoniu.app_nghmuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class PromptDialog extends Dialog {
    public PromptDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_prompt);
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        mContext = context;
        initView();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tvLeftP);
        tvRight = (TextView) findViewById(R.id.tvRightP);
        tvTitle = (TextView) findViewById(R.id.tvPrompt);
        tvContent = (TextView) findViewById(R.id.tvContentP);
    }

    private Context mContext;
    public TextView tvLeft, tvRight, tvTitle, tvContent;

}
