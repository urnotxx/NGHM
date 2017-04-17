package self.androidbase.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.LinkedHashMap;

import self.androidbase.R;
import self.androidbase.extend.SelfAnimationListener;
import self.androidbase.extend.SelfDialog;
import self.androidbase.utils.DensityUtils;

/**
 * 从底部滑出的菜单view
 *
 * @author Janesen
 */
public class SelfOptionMenuView {
    public static Dialog showMenu(final Context context, LinkedHashMap<String, OnClickListener> menusMap, String title) {
        if (menusMap != null) {
            return makeMenu(context, menusMap, new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                }
            }, title);
        }
        return null;
    }

    public static Dialog showMenu(final Context context, LinkedHashMap<String, OnClickListener> menusMap) {
        if (menusMap != null) {
            return makeMenu(context, menusMap, new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                }
            }, null);
        }
        return null;
    }

    public static Dialog showMenu(final Context context, LinkedHashMap<String, OnClickListener> menusMap, final OnClickListener cancelClick) {
        if (menusMap != null) {
            return makeMenu(context, menusMap, new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    cancelClick.onClick(arg0);
                }
            }, null);
        }
        return null;
    }


    public static Dialog showMenu(final Context context, LinkedHashMap<String, OnClickListener> menusMap, final OnClickListener cancelClick, String title) {
        if (menusMap != null) {
            return makeMenu(context, menusMap, new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    cancelClick.onClick(arg0);
                }
            }, title);
        }
        return null;
    }

    private static Dialog makeMenu(final Context context, final LinkedHashMap<String, OnClickListener> menusMap, final OnClickListener cancelClick, String title) {
        try {
            TranslateAnimation ta_close = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    1.0f);
            ta_close.setDuration(200);
            ta_close.setInterpolator(new LinearInterpolator());
            ta_close.setFillAfter(true);


            final SelfDialog dialog = new SelfDialog(context, R.style.SelfDialogStyle, R.id.android_base_llMenuPanel, ta_close);


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.android_base_view_menu_option, null);

            SelfBoundScrollView selfBoundScrollView = (SelfBoundScrollView) view.findViewById(R.id.android_base_menu_scrollView);
            selfBoundScrollView.setPullTop(false);
            selfBoundScrollView.setPullBottom(false);

            final View llMenuBg = view.findViewById(R.id.android_base_llMenuBg);
            llMenuBg.setOnClickListener(cancelClick);
            TextView tvMenuTitle = (TextView) view.findViewById(R.id.android_base_tvMenuTitle);
            if (title != null) {
                tvMenuTitle.setVisibility(View.VISIBLE);
                tvMenuTitle.setText(title);
            } else {
                tvMenuTitle.setVisibility(View.GONE);
            }

            TextView menuCancel = (TextView) view.findViewById(R.id.android_base_menuCancel);
            menuCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            LinearLayout llMenuContainer = (LinearLayout) view.findViewById(R.id.android_base_llMenuContainer);

            if (menusMap != null) {
                Object[] menuNames = menusMap.keySet().toArray();
                for (int i = 0; i < menuNames.length; i++) {
                    LinearLayout ll = new LinearLayout(context);
                    ll.setBackgroundColor(Color.parseColor("#999999"));
                    ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
                    llMenuContainer.addView(ll);

                    final String menuName = menuNames[i].toString();

                    SelfTextView tv = new SelfTextView(context);
                    tv.setPressedColor(Color.parseColor("#f0f0f0"));
                    tv.setText(menuName);

                    tv.setBackgroundResource(R.drawable.android_base_menu_bg_center);
                    if (i == 0 && menuNames.length != 1 && title == null) {
                        tv.setBackgroundResource(R.drawable.android_base_menu_bg_top);
                    }

                    if (i == menuNames.length - 1 && menuNames.length != 1) {
                        tv.setBackgroundResource(R.drawable.android_base_menu_bg_bot);
                    }

                    if (menuNames.length == 1) {
                        if (title != null) {
                            tv.setBackgroundResource(R.drawable.android_base_menu_bg_bot);
                        } else {
                            tv.setBackgroundResource(R.drawable.android_base_menu_bg_round);
                        }
                    }
                    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv.setTextSize(18f);
                    tv.setClickable(true);
                    tv.setPadding(menuCancel.getPaddingLeft(), menuCancel.getPaddingTop(), menuCancel.getPaddingRight(), menuCancel.getPaddingBottom());

                    if (menusMap.get(menuName) != null) {
                        tv.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                llMenuBg.setTag(v);
                                dialog.cancel();
                                v.setClickable(false);
                                menusMap.get(menuName).onClick(v);
                            }
                        });
                    }

                    tv.setLayoutParams(menuCancel.getLayoutParams());
                    tv.setTextColor(Color.parseColor("#199FD7"));
                    tv.initStye();
                    llMenuContainer.addView(tv);
                }

            }


            if (title == null) {
                llMenuContainer.removeViewAt(0);//移除第一条线
            }

            dialog.setContentView(view, new LayoutParams((int) DensityUtils.getWidthInPx(context), LayoutParams.MATCH_PARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    if (cancelClick != null) {
                        cancelClick.onClick(llMenuBg);
                    }
                }
            });

            dialog.setOnKeyListener(new OnKeyListener() {

                boolean isClosed = false;

                @Override
                public boolean onKey(DialogInterface d, int arg1, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (dialog.isShowed() && (event.getKeyCode() == KeyEvent.KEYCODE_MENU || event.getKeyCode() == KeyEvent.KEYCODE_BACK) && !isClosed) {
                        isClosed = true;
                        d.cancel();
                    }
                    return true;
                }
            });
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            dialog.show();


            TranslateAnimation ta_show = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                    0.0f);
            ta_show.setStartOffset(100);
            ta_show.setDuration(200);
            ta_show.setInterpolator(new LinearInterpolator());
            ta_show.setFillAfter(true);
            ta_show.setAnimationListener(new SelfAnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    dialog.setShowed(true);
                    super.onAnimationEnd(arg0);
                }
            });

            view.findViewById(R.id.android_base_llMenuPanel).startAnimation(ta_show);

            return dialog;
        } catch (Exception e) {
        }
        return null;
    }


}
