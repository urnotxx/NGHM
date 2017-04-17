package iosdialog.dialogsamples.extra;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import iosdialog.animation.Attention.Swing;
import iosdialog.dialog.utils.CornerUtils;
import iosdialog.dialog.widget.base.BaseDialog;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.R;


public class CustomBaseDialog extends BaseDialog<CustomBaseDialog> {
    public TextView tv_cancel;
    public TextView tv_exit;
    public TextView tv_content;

    public CustomBaseDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new Swing());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(context, R.layout.dialog_custom_base, null);
        tv_cancel = ViewFindUtils.find(inflate, R.id.tv_cancel);
        tv_exit = ViewFindUtils.find(inflate, R.id.tv_exit);
        tv_content = ViewFindUtils.find(inflate, R.id.tv_content);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        tv_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }
}
