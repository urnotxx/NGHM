package iosdialog.animation.SlideExit.dialogsamples.extra;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import iosdialog.animation.FlipEnter.FlipVerticalSwingEnter;
import iosdialog.dialog.widget.base.TopBaseDialog;
import iosdialog.dialogsamples.utils.T;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.R;


public class ShareTopDialog extends TopBaseDialog<ShareTopDialog> {
    private LinearLayout ll_wechat_friend_circle;
    private LinearLayout ll_wechat_friend;
    private LinearLayout ll_qq;
    private LinearLayout ll_sms;

    public ShareTopDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public ShareTopDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(context, R.layout.dialog_share, null);
        ll_wechat_friend_circle = ViewFindUtils.find(inflate, R.id.ll_wechat_friend_circle);
        ll_wechat_friend = ViewFindUtils.find(inflate, R.id.ll_wechat_friend);
        ll_qq = ViewFindUtils.find(inflate, R.id.ll_qq);
        ll_sms = ViewFindUtils.find(inflate, R.id.ll_sms);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        ll_wechat_friend_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "朋友圈");
                dismiss();
            }
        });
        ll_wechat_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "微信");
                dismiss();
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "QQ");
                dismiss();
            }
        });
        ll_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, "短信");
                dismiss();
            }
        });
    }
}
