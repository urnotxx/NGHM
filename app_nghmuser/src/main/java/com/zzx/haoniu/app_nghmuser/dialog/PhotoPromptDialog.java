package com.zzx.haoniu.app_nghmuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class PhotoPromptDialog extends Dialog implements View.OnClickListener {
    public PhotoPromptDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_photoprompt);
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.TOP);
        window.setAttributes(lp);
        mContext = context;
        initView();
        initEvent();
    }

    private void initView() {
        tvUploadPhoto = (TextView) findViewById(R.id.tvUploadPhoto);
    }

    private Context mContext;
    private TextView tvUploadPhoto;
    private OnClickListener clickUpLoad;

    public void setUploadPhoto(DialogInterface.OnClickListener clickUpLoad) {
        this.clickUpLoad = clickUpLoad;
    }

    private void initEvent() {
        tvUploadPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUploadPhoto:
                if (clickUpLoad != null) {
                    clickUpLoad.onClick(this, 0);
                }
                break;
        }
    }
}
