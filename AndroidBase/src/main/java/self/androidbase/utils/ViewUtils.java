package self.androidbase.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import self.androidbase.extend.SelfAnimationListener;
import self.androidbase.extend.SelfValueAnimation;


public class ViewUtils {

    /**
     * 设置控件的margin
     *
     * @param view
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View view, int l, int t, int r, int b) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams mLayoutPrams = (FrameLayout.LayoutParams) view.getLayoutParams();
            mLayoutPrams.setMargins(l, t, r, b);
            view.setLayoutParams(mLayoutPrams);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams mLayoutPrams = (LinearLayout.LayoutParams) view.getLayoutParams();
            mLayoutPrams.setMargins(l, t, r, b);
            view.setLayoutParams(mLayoutPrams);
        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams mLayoutPrams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            mLayoutPrams.setMargins(l, t, r, b);
            view.setLayoutParams(mLayoutPrams);
        }
    }

    /**
     * 设置控件的margin
     *
     * @param view
     */
    public static void setMargins(View view, int width, int height) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams mLayoutPrams = (FrameLayout.LayoutParams) view.getLayoutParams();
            if (width >= 0) {
                mLayoutPrams.width = width;
            }
            if (height >= 0) {
                mLayoutPrams.height = height;
            }
            view.setLayoutParams(mLayoutPrams);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams mLayoutPrams = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (width >= 0) {
                mLayoutPrams.width = width;
            }
            if (height >= 0) {
                mLayoutPrams.height = height;
            }
            view.setLayoutParams(mLayoutPrams);
        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams mLayoutPrams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (width >= 0) {
                mLayoutPrams.width = width;
            }
            if (height >= 0) {
                mLayoutPrams.height = height;
            }
            view.setLayoutParams(mLayoutPrams);
        }
    }

    /**
     * 设置控件的margin
     *
     * @param view
     * @param l
     * @param t
     * @param r
     * @param b
     * @param width
     * @param height
     */
    public static void setMargins(View view, int l, int t, int r, int b, int width, int height) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams mLayoutPrams = (FrameLayout.LayoutParams) view.getLayoutParams();
            if (width >= 0) {
                mLayoutPrams.width = width;
            }
            if (height >= 0) {
                mLayoutPrams.height = height;
            }
            mLayoutPrams.setMargins(l, t, r, b);
            view.setLayoutParams(mLayoutPrams);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams mLayoutPrams = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (width >= 0) {
                mLayoutPrams.width = width;
            }
            if (height >= 0) {
                mLayoutPrams.height = height;
            }
            mLayoutPrams.setMargins(l, t, r, b);
            view.setLayoutParams(mLayoutPrams);
        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams mLayoutPrams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (width >= 0) {
                mLayoutPrams.width = width;
            }
            if (height >= 0) {
                mLayoutPrams.height = height;
            }
            mLayoutPrams.setMargins(l, t, r, b);
            view.setLayoutParams(mLayoutPrams);
        }
    }


    /**
     * 获得控件的margin
     *
     * @param view
     * @return
     */
    public static Rect getMargins(View view) {
        Rect rect = new Rect();
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams mLayoutPrams = (FrameLayout.LayoutParams) view.getLayoutParams();
            rect.set(mLayoutPrams.leftMargin, mLayoutPrams.topMargin, mLayoutPrams.rightMargin, mLayoutPrams.bottomMargin);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams mLayoutPrams = (LinearLayout.LayoutParams) view.getLayoutParams();
            rect.set(mLayoutPrams.leftMargin, mLayoutPrams.topMargin, mLayoutPrams.rightMargin, mLayoutPrams.bottomMargin);
        }
        return rect;
    }


    /**
     * 释放imageview
     *
     * @param imageView
     */
    public static void releasImageView(ImageView imageView) {
        try {
            ((BitmapDrawable) imageView.getBackground()).getBitmap().recycle();
            imageView.setImageBitmap(null);
        } catch (Exception e) {
            imageView.setImageBitmap(null);
        }
    }


        /**
     * 获得控件的宽高
     *
     * @return int[0] 宽  int[1] 高
     */
    public static int[] getViewSize(View view) {
        int childEndWidth = 0;
        int childEndHeight = 0;
        if (view.getLayoutParams() != null) {
            LayoutParams rlp = view.getLayoutParams();
            childEndWidth = rlp.width;
            childEndHeight = rlp.height;
            if (childEndWidth <= 0 || childEndHeight <= 0) {

                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                view.measure(w, h);
                childEndWidth = view.getMeasuredWidth();
                childEndHeight = view.getMeasuredHeight();
            }
        } else {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            childEndWidth = view.getMeasuredWidth();
            childEndHeight = view.getMeasuredHeight();
        }
        return new int[]{childEndWidth, childEndHeight};
    }


    /**
     * 获得控件的宽高
     *
     * @return int[0] 宽  int[1] 高
     */
    public static int[] getViewSize2(View view) {
        int childEndWidth = 0;
        int childEndHeight = 0;
        if (view.getLayoutParams() != null) {
            LayoutParams rlp = view.getLayoutParams();
            childEndWidth = rlp.width;
            childEndHeight = rlp.height;
            if (childEndWidth <= 0 || childEndHeight <= 0) {

                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.EXACTLY);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.EXACTLY);
                view.measure(w, h);
                childEndWidth = view.getMeasuredWidth();
                childEndHeight = view.getMeasuredHeight();
            }
        } else {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.EXACTLY);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.EXACTLY);
            view.measure(w, h);
            childEndWidth = view.getMeasuredWidth();
            childEndHeight = view.getMeasuredHeight();
        }
        return new int[]{childEndWidth, childEndHeight};
    }


    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            view.clearFocus();
        }
    }


    /**
     * 打开软键盘
     *
     * @param view
     */
    public static void openKeyboard(View view) {
        if (view != null) {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }


    /**
     * 更改viewGroup的子控件，并且只存在一个子控件
     *
     * @param viewGroup
     */
    public static void changeOnlyChildViewToShow(Handler handler, final ViewGroup viewGroup, Class<? extends View> childViewClass) {
        changeOnlyChildViewToShow(viewGroup, getViewInstance(handler, viewGroup, childViewClass));
    }

    /**
     * 更改viewGroup的子控件，并且只存在一个子控件
     *
     * @param viewGroup
     * @param childView
     */
    public static void changeOnlyChildViewToShow(final ViewGroup viewGroup, final View childView) {
        try {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setVisibility(View.GONE);
            }


            if (viewGroup.indexOfChild(childView) == -1) {//如果不存在
                viewGroup.addView(childView);
            }

            childView.setVisibility(View.VISIBLE);
            childView.bringToFront();

            viewGroup.clearAnimation();
            viewGroup.measure(w, h);
            int viewGroupHeight = viewGroup.getHeight();

            childView.measure(w, h);

            int childHeight = childView.getMeasuredHeight();
            if (childHeight == 0) {
                childHeight = childView.getLayoutParams().height;
            }
            int heightSpan = viewGroupHeight - childHeight;
            if (viewGroup instanceof LinearLayout) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, childView.getLayoutParams().height);
                layoutParams.setMargins(0, 0, 0, heightSpan);
                childView.setLayoutParams(layoutParams);
            } else if (viewGroup instanceof FrameLayout) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, childView.getLayoutParams().height);
                layoutParams.setMargins(0, 0, 0, heightSpan);
                childView.setLayoutParams(layoutParams);
            }


            SelfValueAnimation selfValueAnimation = new SelfValueAnimation();
            selfValueAnimation.setEndValue(0);
            selfValueAnimation.setDuration(300);
            selfValueAnimation.setStartValue(heightSpan);
            selfValueAnimation.setValueChange(new SelfValueAnimation.ValueChange() {

                @Override
                public void valueChange(float newValue, float oldValue) {
                    // TODO Auto-generated method stub
                    if (viewGroup instanceof LinearLayout) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childView.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) newValue);
                        childView.setLayoutParams(layoutParams);
                    } else if (viewGroup instanceof FrameLayout) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childView.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) newValue);
                        childView.setLayoutParams(layoutParams);
                    }
                }
            });

            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);
            childView.startAnimation(alphaAnimation);
            viewGroup.startAnimation(selfValueAnimation);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 移除子控件
     *
     * @param viewGroup
     */
    public static void changeOnlyChildViewToHide(final ViewGroup viewGroup) {
        View view = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        if (view == null || view.getVisibility() == View.GONE) {
            return;
        }
        changeOnlyChildViewToHide(viewGroup, view);
    }

    /**
     * 移除子控件
     *
     * @param viewGroup
     * @param childViewClass
     */
    public static void changeOnlyChildViewToHide(final ViewGroup viewGroup, Class<? extends View> childViewClass) {
        View view = null;
        if (childViewClass != null) {
            view = viewGroup.findViewById(childViewClass.hashCode());
            ;
        } else {
            view = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
            if (view == null || view.getVisibility() == View.GONE) {
                return;
            }
        }
        changeOnlyChildViewToHide(viewGroup, view);
    }

    /**
     * 移除子控件
     *
     * @param viewGroup
     */
    public static void changeOnlyChildViewToHide(final ViewGroup viewGroup, final View childView) {
        if (childView != null) {
            viewGroup.clearAnimation();
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);

            childView.measure(w, h);
            int childHeight = childView.getMeasuredHeight();
            if (childHeight == 0) {
                childHeight = childView.getLayoutParams().height;
            }

            SelfValueAnimation selfValueAnimation = new SelfValueAnimation();
            selfValueAnimation.setEndValue(-childHeight);
            selfValueAnimation.setDuration(300);
            selfValueAnimation.setStartValue(0);
            selfValueAnimation.setValueChange(new SelfValueAnimation.ValueChange() {

                @Override
                public void valueChange(float newValue, float oldValue) {
                    // TODO Auto-generated method stub
                    if (viewGroup instanceof LinearLayout) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childView.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) newValue);
                        childView.setLayoutParams(layoutParams);
                    } else if (viewGroup instanceof FrameLayout) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childView.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) newValue);
                        childView.setLayoutParams(layoutParams);
                    }
                }
            });
            selfValueAnimation.setAnimationListener(new SelfAnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    childView.setVisibility(View.GONE);
                    System.gc();
                }
            });
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(300);
            alphaAnimation.setFillAfter(true);
            childView.startAnimation(alphaAnimation);
            viewGroup.startAnimation(selfValueAnimation);
        }
    }


    private static View getViewInstance(Handler handler, ViewGroup viewGroup, Class<? extends View> childViewClass) {
        try {
            View childView = viewGroup.findViewById(childViewClass.hashCode());
            if (childView == null) {
                Constructor<? extends View> constructor = childViewClass.getConstructor(Context.class, Handler.class);
                childView = constructor.newInstance(viewGroup.getContext(), handler);
                childView.setId(childViewClass.hashCode());
                viewGroup.addView(childView);
            }
            childView.setVisibility(View.VISIBLE);
            childView.bringToFront();
            return childView;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 键盘打开是移动位置
     *
     * @param keyboardHeight
     */
    public static void moveOrHideByKeyboard(final View viewGroup, View tagetEdite, final int keyboardHeight, final boolean isOutWindow) {
        viewGroup.clearAnimation();
        float endValue = 0;
        if (keyboardHeight > 0 && tagetEdite != null) {
            int[] targetLocation = new int[2];
            tagetEdite.getLocationInWindow(targetLocation);
            int bottomHeight = (int) (DensityUtils.getHeightInPx(viewGroup.getContext()) - targetLocation[1] - (tagetEdite.getHeight() + DensityUtils.dip2px(viewGroup.getContext(), 5)));
//            if (bottomHeight > keyboardHeight) {
//                return;
//            }
            endValue = (keyboardHeight - bottomHeight) + ViewUtils.getMargins(viewGroup).bottom;

            if (!isOutWindow) {
                if (setHeightByTarget(viewGroup, keyboardHeight - DensityUtils.dip2px(viewGroup.getContext(), 5))) {
                    ViewUtils.setMargins(viewGroup, 0, 0, 0, (int) (endValue * 0.8));//避免出现太大的跳动
                }
            }
        }

        if (!isOutWindow && endValue == 0) {
            setHeightByTarget(viewGroup, -1);
        }

        final SelfValueAnimation valueAnimation = new SelfValueAnimation();
        valueAnimation.setStartValue(ViewUtils.getMargins(viewGroup).bottom);
        valueAnimation.setEndValue((int) endValue);
        valueAnimation.setDuration(300);
        valueAnimation.setValueChange(new SelfValueAnimation.ValueChange() {
            @Override
            public void valueChange(float newValue, float oldValue) {
                ViewUtils.setMargins(viewGroup, 0, 0, 0, (int) newValue);
            }
        });
        viewGroup.startAnimation(valueAnimation);
    }


    //记录原有的高度
    private static Map<Object, Integer> oldHeights = new HashMap<>();

    /**
     * 设置高度+targetHeight 不超过屏幕高度
     *
     * @param targetHeight
     */
    public static boolean setHeightByTarget(View view, int targetHeight) {
        if (targetHeight >= 0) {
            float minHeight = (DensityUtils.getHeightInPx(view.getContext()) - targetHeight - DensityUtils.statusBarHeight2(view.getContext()));
            int viewHeight = view.getHeight();
            if (viewHeight > minHeight) {
                oldHeights.put(view, viewHeight);
                setMargins(view, -1, (int) minHeight);
                return true;
            }
        } else {
            if (oldHeights.containsKey(view)) {
                setMargins(view, -1, oldHeights.get(view));
            }
        }
        return false;
    }


}
