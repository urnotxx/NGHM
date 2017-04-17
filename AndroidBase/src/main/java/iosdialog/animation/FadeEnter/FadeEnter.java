package iosdialog.animation.FadeEnter;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import iosdialog.animation.BaseAnimatorSet;

public class FadeEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(duration));
	}
}
