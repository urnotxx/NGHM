package self.androidbase.extend;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;


/**
 * 自定义dialog
 */
public class SelfDialog extends Dialog {

    private int animViewId;
    private Animation onCloseAnim;
    private View animView;
	private boolean showed;
	public boolean isShowed() {
		return showed;
	}

	public void setShowed(boolean showed) {
		this.showed = showed;
	}

	public SelfDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public SelfDialog(Context context, int theme,int animViewId,Animation anim) {
		super(context, theme);
		this.animViewId=animViewId;
		this.onCloseAnim =anim;
	}

	
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		if(onCloseAnim !=null){
			onCloseAnim.setAnimationListener(new SelfAnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    SelfDialog.super.cancel();
                    super.onAnimationEnd(arg0);
                }
            });
            if (animView != null) {
                animView.clearAnimation();
                animView.startAnimation(onCloseAnim);
            }else {
                if (findViewById(animViewId) != null) {
                    findViewById(animViewId).clearAnimation();
                    findViewById(animViewId).startAnimation(onCloseAnim);
                }
            }
            onCloseAnim =null;
		}else{
			super.cancel();
		}
	}

    @Override
    public void dismiss() {
        if(onCloseAnim !=null){
            onCloseAnim.setAnimationListener(new SelfAnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    SelfDialog.super.dismiss();
                    super.onAnimationEnd(arg0);
                }
            });
            if (animView != null) {
                animView.clearAnimation();
                animView.startAnimation(onCloseAnim);
            }else {
                if (findViewById(animViewId) != null) {
                    findViewById(animViewId).clearAnimation();
                    findViewById(animViewId).startAnimation(onCloseAnim);
                }
            }
            onCloseAnim =null;
        }else{
            super.dismiss();
        }
    }

    public Animation getOnCloseAnim() {
        return onCloseAnim;
    }

    public void setOnCloseAnim(Animation onCloseAnim) {
        this.onCloseAnim = onCloseAnim;
    }

    public int getAnimViewId() {
        return animViewId;
    }

    public void setAnimViewId(int animViewId) {
        this.animViewId = animViewId;
    }

    public View getAnimView() {
        return animView;
    }

    public void setAnimView(View animView) {
        this.animView = animView;
    }
}
