package iosdialog.animation.SlideExit.animation.FadeExit;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import iosdialog.animation.BaseAnimatorSet;

public class FadeExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration));
	}
}
