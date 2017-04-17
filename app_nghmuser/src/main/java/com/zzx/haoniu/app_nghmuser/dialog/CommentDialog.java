package com.zzx.haoniu.app_nghmuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/24.
 */

public class CommentDialog extends Dialog implements View.OnClickListener {
    public TextView tvCom1DC;
    public TextView tvCom2DC;
    public TextView tvCom3DC;
    public TextView tvCom4DC;
    EditText edCommentDC;
    TextView tvCommentDC;
    LinearLayout llCancleDC;

    public CommentDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_comment);
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
        tvCom1DC = (TextView) findViewById(R.id.tvCom1DC);
        tvCom2DC = (TextView) findViewById(R.id.tvCom2DC);
        tvCom3DC = (TextView) findViewById(R.id.tvCom3DC);
        tvCom4DC = (TextView) findViewById(R.id.tvCom4DC);
        tvCommentDC = (TextView) findViewById(R.id.tvCommentDC);
        edCommentDC = (EditText) findViewById(R.id.edCommentDC);
        ratingBarDC = (RatingBar) findViewById(R.id.ratingBarDC);
        llCancleDC = (LinearLayout) findViewById(R.id.llCancleDC);
    }

    private void initEvent() {
        tvCom1DC.setOnClickListener(this);
        tvCom2DC.setOnClickListener(this);
        tvCom3DC.setOnClickListener(this);
        tvCom4DC.setOnClickListener(this);
        tvCommentDC.setOnClickListener(this);
        llCancleDC.setOnClickListener(this);
    }

    private Context mContext;
    public boolean isOne, isTwo, isThree, isFour;
    public RatingBar ratingBarDC;

    @OnClick({R.id.llCancleDC, R.id.tvCom1DC, R.id.tvCom2DC, R.id.tvCom3DC, R.id.tvCom4DC, R.id.tvCommentDC})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llCancleDC:
                if (cancleListener != null) {
                    cancleListener.onClick(this, 0);
                }
                break;
            case R.id.tvCom1DC:
                if (clickOne != null) {
                    clickOne.onClick(this, 0);
                }
                break;
            case R.id.tvCom2DC:
                if (clickOne != null) {
                    clickTwo.onClick(this, 0);
                }
                break;
            case R.id.tvCom3DC:
                if (clickOne != null) {
                    clickThree.onClick(this, 0);
                }
                break;
            case R.id.tvCom4DC:
                if (clickOne != null) {
                    cliskFour.onClick(this, 0);
                }
                break;
            case R.id.tvCommentDC:
                if (confimListener != null) {
                    confimListener.onClick(this, 0);
                }
                break;
        }
    }

    private OnClickListener cancleListener, confimListener;// 按钮单击监听事件
    private OnClickListener clickOne, clickTwo, clickThree, cliskFour;

    /**
     * 设置确定取消选择按钮
     */
    public void setCanConClick(DialogInterface.OnClickListener cancleListener, DialogInterface.OnClickListener confimListener) {
        this.cancleListener = cancleListener;
        this.confimListener = confimListener;
    }

    /**
     * 设置评价选择按钮
     */
    public void setComClick(DialogInterface.OnClickListener clickOne, DialogInterface.OnClickListener clickTwo
            , DialogInterface.OnClickListener clickThree, DialogInterface.OnClickListener cliskFour) {
        this.clickOne = clickOne;
        this.clickTwo = clickTwo;
        this.clickThree = clickThree;
        this.cliskFour = cliskFour;
    }

    public String getComText() {
        if (edCommentDC.getText().toString() == null || StringUtils.isEmpty(edCommentDC.getText().toString()))
            return null;
        else
            return edCommentDC.getText().toString();
    }
}
