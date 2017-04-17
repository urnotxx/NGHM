package self.androidbase.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import self.androidbase.R;
import self.androidbase.extend.SelfStateListDrawable;

/**
 * 可配置 点击是的效果 的 FrameLayout
 * @author janesen
 *
 */
public class SelfFrameLayout extends FrameLayout {
	public final static int idPrefix=android.R.id.custom;

	private SelfStateListDrawable stateListDrawable;

	private boolean isSetIdByTop;

	private int endHeight;
	private int endWidth;
	private int pressedBackground;
	private int pressedBackgroundColor;
	private float pressedAlpha;
	private float pressedScale;
	private int touchDownAnimation;
	private int touchUpAnimation;

	public int getTouchDownAnimation() {
		return touchDownAnimation;
	}
	public void setTouchDownAnimation(int touchDownAnimation) {
		this.touchDownAnimation = touchDownAnimation;
	}
	public int getTouchUpAnimation() {
		return touchUpAnimation;
	}
	public void setTouchUpAnimation(int touchUpAnimation) {
		this.touchUpAnimation = touchUpAnimation;
	}
	public int getEndWidth() {
		return endWidth;
	}
	public void setEndWidth(int endWidth) {
		this.endWidth = endWidth;
	}
	public float getPressedScale() {
		return pressedScale;
	}
	public void setPressedScale(float pressedScale) {
		this.pressedScale = pressedScale;
	}
	public boolean isSetIdByTop() {
		return isSetIdByTop;
	}
	public void setSetIdByTop(boolean isSetIdByTop) {
		this.isSetIdByTop = isSetIdByTop;
	}
	public int getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}
	public void setPressedBackgroundColor(int pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}
	public int getEndHeight() {
		return endHeight;
	}
	public SelfStateListDrawable getStateListDrawable() {
		return stateListDrawable;
	}
	public void setStateListDrawable(SelfStateListDrawable stateListDrawable) {
		this.stateListDrawable = stateListDrawable;
	}
	public void setEndHeight(int endHeight) {
		this.endHeight = endHeight;
	}
	public int getPressedBackground() {
		return pressedBackground;
	}
	public void setPressedBackground(int pressedBackground) {
		this.pressedBackground = pressedBackground;
	}
	public float getPressedAlpha() {
		return pressedAlpha;
	}
	public void setPressedAlpha(float pressedAlpha) {
		this.pressedAlpha = pressedAlpha;
	}
	public SelfFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public SelfFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.BaseApp, 0, 0);
		pressedBackground = typeArray.getResourceId(R.styleable.BaseApp_pressedBackground, -1);
		pressedBackgroundColor=typeArray.getColor(R.styleable.BaseApp_pressedBackgroundColor, -1);
		pressedAlpha=typeArray.getFloat(R.styleable.BaseApp_pressedAlpha, 1.0f);
		pressedScale=typeArray.getFloat(R.styleable.BaseApp_pressedScale, 1.0f);

		touchDownAnimation = typeArray.getResourceId(R.styleable.BaseApp_touchDownAnimation, -1);
		touchUpAnimation = typeArray.getResourceId(R.styleable.BaseApp_touchUpAnimation, -1);

		if(getBackground()==null){
			setBackgroundColor(Color.TRANSPARENT);
		}
	}

	@SuppressWarnings("deprecation")
	public void initStyle(){

		int bottom = getPaddingBottom();
	    int top = getPaddingTop();
	    int right =getPaddingRight();
	    int left = getPaddingLeft();

		stateListDrawable =new SelfStateListDrawable(this);
		stateListDrawable.setPressedScale(pressedScale);
		stateListDrawable.setPressedAlpha(pressedAlpha);

		if(touchDownAnimation>0){
			stateListDrawable.setTouchDownAnimation(AnimationUtils.loadAnimation(getContext(), touchDownAnimation));
		}
		if(touchUpAnimation>0){
			stateListDrawable.setTouchUpAnimation(AnimationUtils.loadAnimation(getContext(), touchUpAnimation));
		}

		if(pressedBackground>0){
			stateListDrawable.addState(PRESSED_ENABLED_STATE_SET, this.getResources().getDrawable(pressedBackground));
		}else if(Math.abs(pressedBackgroundColor)>1){
			stateListDrawable.addState(PRESSED_ENABLED_STATE_SET, new ColorDrawable(pressedBackgroundColor));
		}
		else{
			stateListDrawable.addState(PRESSED_ENABLED_STATE_SET, getBackground());
		}
		stateListDrawable.addState(ENABLED_STATE_SET, getBackground());
		setBackgroundDrawable(stateListDrawable);


		 setPadding(left, top, right, bottom);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(isSetIdByTop){
			setId(t);
		}
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
        initStyle();
	}
}
