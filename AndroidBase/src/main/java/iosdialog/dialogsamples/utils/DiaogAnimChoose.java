package iosdialog.dialogsamples.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;


import java.util.ArrayList;

import iosdialog.animation.Attention.Flash;
import iosdialog.animation.Attention.RubberBand;
import iosdialog.animation.Attention.ShakeHorizontal;
import iosdialog.animation.Attention.ShakeVertical;
import iosdialog.animation.Attention.Swing;
import iosdialog.animation.Attention.Tada;
import iosdialog.animation.BaseAnimatorSet;
import iosdialog.animation.BounceEnter.BounceBottomEnter;
import iosdialog.animation.BounceEnter.BounceEnter;
import iosdialog.animation.BounceEnter.BounceLeftEnter;
import iosdialog.animation.BounceEnter.BounceRightEnter;
import iosdialog.animation.BounceEnter.BounceTopEnter;
import iosdialog.animation.FadeEnter.FadeEnter;
import iosdialog.animation.FadeExit.FadeExit;
import iosdialog.animation.FallEnter.FallEnter;
import iosdialog.animation.FallEnter.FallRotateEnter;
import iosdialog.animation.FlipEnter.FlipBottomEnter;
import iosdialog.animation.FlipEnter.FlipHorizontalEnter;
import iosdialog.animation.FlipEnter.FlipHorizontalSwingEnter;
import iosdialog.animation.FlipEnter.FlipLeftEnter;
import iosdialog.animation.FlipEnter.FlipRightEnter;
import iosdialog.animation.FlipEnter.FlipTopEnter;
import iosdialog.animation.FlipEnter.FlipVerticalEnter;
import iosdialog.animation.FlipEnter.FlipVerticalSwingEnter;
import iosdialog.animation.FlipExit.FlipHorizontalExit;
import iosdialog.animation.FlipExit.FlipVerticalExit;
import iosdialog.animation.Jelly;
import iosdialog.animation.NewsPaperEnter;
import iosdialog.animation.SlideEnter.SlideBottomEnter;
import iosdialog.animation.SlideEnter.SlideLeftEnter;
import iosdialog.animation.SlideEnter.SlideRightEnter;
import iosdialog.animation.SlideEnter.SlideTopEnter;
import iosdialog.animation.SlideExit.SlideBottomExit;
import iosdialog.animation.SlideExit.SlideLeftExit;
import iosdialog.animation.SlideExit.SlideRightExit;
import iosdialog.animation.SlideExit.SlideTopExit;
import iosdialog.animation.ZoomEnter.ZoomInBottomEnter;
import iosdialog.animation.ZoomEnter.ZoomInEnter;
import iosdialog.animation.ZoomEnter.ZoomInLeftEnter;
import iosdialog.animation.ZoomEnter.ZoomInRightEnter;
import iosdialog.animation.ZoomEnter.ZoomInTopEnter;
import iosdialog.animation.ZoomExit.ZoomInExit;
import iosdialog.animation.ZoomExit.ZoomOutBottomExit;
import iosdialog.animation.ZoomExit.ZoomOutExit;
import iosdialog.animation.ZoomExit.ZoomOutLeftExit;
import iosdialog.animation.ZoomExit.ZoomOutRightExit;
import iosdialog.animation.ZoomExit.ZoomOutTopExit;
import iosdialog.dialog.listener.OnOperItemClickL;
import iosdialog.dialog.widget.ActionSheetDialog;
import iosdialog.dialogsamples.ui.DialogHomeActivity;

public class DiaogAnimChoose {
    public static void showAnim(final Context context) {
        final Class<?> cs[] = {BounceEnter.class,//
                BounceTopEnter.class,//
                BounceBottomEnter.class,//
                BounceLeftEnter.class,//
                BounceRightEnter.class,//
                FlipHorizontalEnter.class,//
                FlipHorizontalSwingEnter.class,//
                FlipVerticalEnter.class,//
                FlipVerticalSwingEnter.class,//
                FlipTopEnter.class,//
                FlipBottomEnter.class,//
                FlipLeftEnter.class,//
                FlipRightEnter.class,//
                FadeEnter.class, //
                FallEnter.class,//
                FallRotateEnter.class,//
                SlideTopEnter.class,//
                SlideBottomEnter.class,//
                SlideLeftEnter.class, //
                SlideRightEnter.class,//
                ZoomInEnter.class,//
                ZoomInTopEnter.class,//
                ZoomInBottomEnter.class,//
                ZoomInLeftEnter.class,//
                ZoomInRightEnter.class,//

                NewsPaperEnter.class,//
                Flash.class,//
                ShakeHorizontal.class,//
                ShakeVertical.class,//
                Jelly.class,//
                RubberBand.class,//
                Swing.class,//
                Tada.class,//
        };

        ArrayList<String> itemList = new ArrayList<String>();
        for (Class<?> c : cs) {
            itemList.add(c.getSimpleName());
        }

        final String[] contents = new String[itemList.size()];
        final ActionSheetDialog dialog = new ActionSheetDialog(context, //
                itemList.toArray(contents), null);
        dialog.title("使用内置show动画设置对话框显示动画\r\n指定对话框将显示效果")//
                .titleTextSize_SP(14.5f)//
                .layoutAnimation(null)//
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String animType = contents[position];
                    ((DialogHomeActivity) context).setBasIn((BaseAnimatorSet) cs[position].newInstance());
                   T.showShort(context, animType + "设置成功");
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void dissmissAnim(final Context context) {
        final Class<?> cs[] = {FlipHorizontalExit.class,//
                FlipVerticalExit.class,//
                FadeExit.class,//
                SlideTopExit.class,//
                SlideBottomExit.class,//
                SlideLeftExit.class, //
                SlideRightExit.class,//
                ZoomOutExit.class,//
                ZoomOutTopExit.class,//
                ZoomOutBottomExit.class,//
                ZoomOutLeftExit.class,//
                ZoomOutRightExit.class,//
                ZoomInExit.class,//
        };

        ArrayList<String> itemList = new ArrayList<String>();
        for (Class<?> c : cs) {
            itemList.add(c.getSimpleName());
        }

        final String[] contents = new String[itemList.size()];
        final ActionSheetDialog dialog = new ActionSheetDialog(context, //
                itemList.toArray(contents), null);
        dialog.title("使用内置dismiss动画设置对话框消失动画\r\n指定对话框将消失效果")//
                .titleTextSize_SP(14.5f)//
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String animType = contents[position];
                    ((DialogHomeActivity) context).setBasOut((BaseAnimatorSet) cs[position].newInstance());
                    T.showShort(context, animType + "设置成功");
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
