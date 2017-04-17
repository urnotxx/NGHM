package iosdialog.dialog.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import iosdialog.dialog.utils.CornerUtils;
import iosdialog.dialog.widget.internal.BaseAlertDialog;


@SuppressWarnings("deprecation")
public class NormalDialog extends BaseAlertDialog<NormalDialog> {
    /**
     * title underline
     */
    private View v_line_title;
    /**
     * vertical line between btns
     */
    private View v_line_vertical;
    /**
     * vertical line between btns
     */
    private View v_line_vertical2;
    /**
     * horizontal line above btns
     */
    private View v_line_horizontal;
    /**
     * title underline color(标题下划线颜色)
     */
    private int titleLineColor = Color.parseColor("#0061AEDC");
    /**
     * title underline height(标题下划线高度)
     */
    private float titleLineHeight_DP = 1f;
    /**
     * btn divider line color(对话框之间的分割线颜色(水平+垂直))
     */
    private int dividerColor = Color.parseColor("#DCDCDC");

    public static final int STYLE_ONE = 0;
    public static final int STYLE_TWO = 1;
    private int style = STYLE_ONE;
    private int editTag = 0;

    public NormalDialog(Context context) {
        super(context);

        /** default value*/
        titleTextColor = Color.parseColor("#61AEDC");
        titleTextSize_SP = 22f;
        contentTextColor = Color.parseColor("#383838");
        contentTextSize_SP = 17f;
        leftBtnTextColor = Color.parseColor("#8a000000");
        rightBtnTextColor = Color.parseColor("#8aFF4081");
        middleBtnTextColor = Color.parseColor("#8a000000");
        /** default value*/
    }

    @Override
    public View onCreateView() {
        /** title */
        tv_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ll_container.addView(tv_title);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(60), dp2px(60));
        layoutParams.setMargins(0, dp2px(10), 0, 0);
        imageView.setLayoutParams(layoutParams);
        ll_container.setGravity(Gravity.CENTER);
        ll_container.addView(imageView);

        /** title underline */
        v_line_title = new View(context);
        ll_container.addView(v_line_title);

        if (editTag == 1) {
            /** editText_content */
            editText_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ll_container.addView(editText_content);
        } else {
            /** content */
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.gravity = Gravity.CENTER;
            tv_content.setLayoutParams(ll);
            ll_container.addView(tv_content);
        }
        v_line_horizontal = new View(context);
        v_line_horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        ll_container.addView(v_line_horizontal);

        /** btns */
        tv_btn_left.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(45), 1));
        ll_btns.addView(tv_btn_left);

        v_line_vertical = new View(context);
        v_line_vertical.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
        ll_btns.addView(v_line_vertical);

        tv_btn_middle.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(45), 1));
        ll_btns.addView(tv_btn_middle);

        v_line_vertical2 = new View(context);
        v_line_vertical2.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
        ll_btns.addView(v_line_vertical2);

        tv_btn_right.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(45), 1));
        ll_btns.addView(tv_btn_right);

        ll_container.addView(ll_btns);

        return ll_container;
    }

    @Override
    public void setUiBeforShow() {
        super.setUiBeforShow();
        /** title */
        if (style == STYLE_ONE) {
            tv_title.setMinHeight(dp2px(48));
            tv_title.setGravity(Gravity.CENTER_VERTICAL);
            tv_title.setPadding(dp2px(15), dp2px(5), dp2px(0), dp2px(5));
            tv_title.setVisibility(isTitleShow ? View.VISIBLE : View.GONE);
        } else if (style == STYLE_TWO) {
            tv_title.setGravity(Gravity.CENTER);
            tv_title.setPadding(dp2px(0), dp2px(15), dp2px(0), dp2px(0));
        }
        /** title underline */
        v_line_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                dp2px(titleLineHeight_DP)));
        v_line_title.setBackgroundColor(titleLineColor);
        v_line_title.setVisibility(isTitleShow && style == STYLE_ONE ? View.VISIBLE : View.GONE);

        if (editTag == 1) {
            /** edittext_content */
            if (style == STYLE_ONE) {
                editText_content.setPadding(dp2px(15), dp2px(10), dp2px(15), dp2px(10));
                editText_content.setMinHeight(dp2px(68));
                editText_content.setGravity(contentGravity);
            } else if (style == STYLE_TWO) {
                editText_content.setPadding(dp2px(10), dp2px(10), dp2px(10), dp2px(10));
                editText_content.setMinHeight(dp2px(56));
                editText_content.setGravity(Gravity.CENTER_VERTICAL);
            }

        } else {
            /** content */
            if (style == STYLE_ONE) {
                tv_content.setPadding(dp2px(15), dp2px(10), dp2px(15), dp2px(10));
                tv_content.setMinHeight(dp2px(68));
                tv_content.setGravity(Gravity.CENTER);
            } else if (style == STYLE_TWO) {
                tv_content.setPadding(dp2px(15), dp2px(7), dp2px(15), dp2px(20));
                tv_content.setMinHeight(dp2px(56));
                tv_content.setGravity(Gravity.CENTER);
            }
        }


        /** btns */
        v_line_horizontal.setBackgroundColor(dividerColor);
        v_line_vertical.setBackgroundColor(dividerColor);
        v_line_vertical2.setBackgroundColor(dividerColor);

        if (btnNum == 1) {
            tv_btn_left.setVisibility(View.GONE);
            tv_btn_right.setVisibility(View.GONE);
            v_line_vertical.setVisibility(View.GONE);
            v_line_vertical2.setVisibility(View.GONE);
        } else if (btnNum == 2) {
            tv_btn_middle.setVisibility(View.GONE);
            v_line_vertical.setVisibility(View.GONE);
        }

        /**set background color and corner radius */
        float radius = dp2px(cornerRadius_DP);
        ll_container.setBackgroundDrawable(CornerUtils.cornerDrawable(bgColor, radius));
        tv_btn_left.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, 0));
        tv_btn_right.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, 1));
        tv_btn_middle.setBackgroundDrawable(CornerUtils.btnSelector(btnNum == 1 ? radius : 0, bgColor, btnPressColor, -1));
    }

    // --->属性设置
    public void setLeftText(String left) {
        btnLeftText = left;
    }

    public void setRightText(String right) {
        btnRightText = right;
    }
    // --->属性设置

    /**
     * set style(设置style)
     */
    public NormalDialog style(int style) {
        this.style = style;
        return this;
    }

    /**
     * set style(设置style) editTag 1 editText  else meiyou
     */
    public NormalDialog setEditText(int editTag) {
        this.editTag = editTag;
        return this;
    }


    /**
     * set title underline color(设置标题下划线颜色)
     */
    public NormalDialog titleLineColor(int titleLineColor) {
        this.titleLineColor = titleLineColor;
        return this;
    }

    /**
     * set title underline height(设置标题下划线高度)
     */
    public NormalDialog titleLineHeight(float titleLineHeight_DP) {
        this.titleLineHeight_DP = titleLineHeight_DP;
        return this;
    }

    /**
     * set divider color between btns(设置btn分割线的颜色)
     */
    public NormalDialog dividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    /**
     * set divider color between btns(设置btn分割线的颜色)
     */
    public String getContentTest() {
        if (editTag == 0) {
            return tv_content.getText().toString();
        } else {
            return editText_content.getText().toString();
        }
    }
}
