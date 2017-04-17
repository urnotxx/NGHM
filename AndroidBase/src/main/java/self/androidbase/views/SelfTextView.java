package self.androidbase.views;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import self.androidbase.R;
import self.androidbase.extend.SelfStateListDrawable;


/**
 * 可配置点击效果的TextView
 * @author janesen
 *
 */
public class SelfTextView extends TextView {
	private SelfStateListDrawable stateListDrawable;
	private int pressedColor;
	private int pressedBackground;
	private float pressedAlpha;
	private int shadowColor;
	private int pressedBackgroundColor;
	private float pressedScale;
	private float shadowRadius_;
	private float shadowDx_;
	private float shadowDy_;

	private int touchDownAnimation;
	private int touchUpAnimation;
	private int defaultColor;
	
	public SelfStateListDrawable getStateListDrawable() {
		return stateListDrawable;
	}

	public void setStateListDrawable(SelfStateListDrawable stateListDrawable) {
		this.stateListDrawable = stateListDrawable;
	}

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

	public float getPressedScale() {
		return pressedScale;
	}

	public void setPressedScale(float pressedScale) {
		this.pressedScale = pressedScale;
	}

	public int getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}

	public void setPressedBackgroundColor(int pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}

	public float getShadowRadius_() {
		return shadowRadius_;
	}

	public void setShadowRadius_(float shadowRadius_) {
		this.shadowRadius_ = shadowRadius_;
	}

	public float getShadowDx_() {
		return shadowDx_;
	}

	public void setShadowDx_(float shadowDx_) {
		this.shadowDx_ = shadowDx_;
	}

	public float getShadowDy_() {
		return shadowDy_;
	}

	public void setShadowDy_(float shadowDy_) {
		this.shadowDy_ = shadowDy_;
	}

	public int getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(int shadowColor) {
		this.shadowColor = shadowColor;
	}

	public int getPressedColor() {
		return pressedColor;
	}

	public void setPressedColor(int pressedColor) {
		this.pressedColor = pressedColor;
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

	public SelfTextView(Context context) {
		super(context);
		defaultColor=getTextColors().getDefaultColor();
	}


	public SelfTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.BaseApp, 0, 0);
		pressedColor = typeArray.getColor(R.styleable.BaseApp_pressedColor, -1);
		shadowColor = typeArray.getColor(R.styleable.BaseApp_shadowColor, -1);
		pressedBackground = typeArray.getResourceId(R.styleable.BaseApp_pressedBackground, -1);
		pressedAlpha=typeArray.getFloat(R.styleable.BaseApp_pressedAlpha, 1.0f);
		pressedBackgroundColor=typeArray.getColor(R.styleable.BaseApp_pressedBackgroundColor, -1);
		pressedScale=typeArray.getFloat(R.styleable.BaseApp_pressedScale, 1.0f);
		
		shadowRadius_=typeArray.getFloat(R.styleable.BaseApp_shadowRadius_, 5f);
		shadowDx_=typeArray.getFloat(R.styleable.BaseApp_shadowDx_, 4f);
		shadowDy_=typeArray.getFloat(R.styleable.BaseApp_shadowDy_, 3f);

		touchDownAnimation = typeArray.getResourceId(R.styleable.BaseApp_touchDownAnimation, -1);
		touchUpAnimation = typeArray.getResourceId(R.styleable.BaseApp_touchUpAnimation, -1);
		
		defaultColor=getTextColors().getDefaultColor();
		
		setClickable(true);
	}

	@SuppressWarnings("deprecation")
	public void initStye(){
		
		int bottom = getPaddingBottom();
	    int top = getPaddingTop();
	    int right =getPaddingRight();
	    int left = getPaddingLeft();
		
		if(Math.abs(pressedColor)>1){
			ColorStateList csl=new ColorStateList(new int[][]{PRESSED_ENABLED_STATE_SET,ENABLED_STATE_SET}, new int[]{pressedColor,defaultColor});
			setTextColor(csl);
		}else{
			ColorStateList csl=new ColorStateList(new int[][]{PRESSED_ENABLED_STATE_SET,ENABLED_STATE_SET}, new int[]{Color.argb((int)(pressedAlpha*255), Color.red(defaultColor), Color.green(defaultColor), Color.blue(defaultColor)),defaultColor});
			setTextColor(csl);
		}


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
		}else{
			stateListDrawable.addState(PRESSED_ENABLED_STATE_SET, getBackground());
		}
		stateListDrawable.addState(ENABLED_STATE_SET, getBackground());
		setBackgroundDrawable(stateListDrawable);
		
		if(Math.abs(shadowColor)>1){
			setShadowLayer(shadowRadius_, shadowDx_,shadowDy_, shadowColor);
		}
		
		
	    setPadding(left, top, right, bottom);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		initStye();
	}


	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {  
	}

}
