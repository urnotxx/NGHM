package self.androidbase.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import self.androidbase.extend.SelfValueAnimation;
import self.androidbase.utils.DensityUtils;


public class SelfDragFrameLayout extends FrameLayout {
    public static final int DIRECTION_NONE=-1;
	public static final int DIRECTION_LEFT=0;
	public static final int DIRECTION_TOP=1;
	public static final int DIRECTION_RIGHT=2;
	public static final int DIRECTION_BOTTOM=3;
	public static final int DIRECTION_ROUND=4;
	public static final float MAXDRAGFACTOR=0.7f;


	public static final float defaultDragFactor=0.7f;

	private boolean canDrag=true;//是否可以拖拽
	private SimpleDragListener dragListener;
	private OnDragInterceptTouchListener onDragInterceptTouchListener;
	private Interpolator animInterpolator=new AccelerateDecelerateInterpolator();//动画形式
	private AnimationListener animationListener;
	private long animDuration=200;//动画时间
	private boolean animRunning=false;
	private float screenWidth;
	private float screenHeight;
	private int currWidth;//当前的宽度
	private int currHeight;//当前的高度
	private int dragDirection=DIRECTION_LEFT;//拖拽的方向
	private float dragFactor=1.0f;
	private boolean isDraging=false;
	private boolean autoSlow=true;
	private boolean isFixHeight;//是否固定高度，即不是用MARCH_PARENT属性，高度使用固定值
	private boolean isDispatch=true;
	private int touchSlop;
	private int activePointerId=-1;
	private float lastMotionX;
	private float lastMotionY;
	private Rect initRect;
	private Rect animTargetRect=new Rect();

	public SelfDragFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initData(context);
	}

	public SelfDragFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initData(context);
	}

	public void initData(Context context){
		screenWidth= DensityUtils.getWidthInPx(context);
		screenHeight=DensityUtils.getHeightInPx(context);

		final ViewConfiguration configuration = ViewConfiguration.get(context);
		touchSlop = configuration.getScaledTouchSlop();
		if(getBackground()==null){
			setBackgroundColor(Color.TRANSPARENT);
		}
		this.setClickable(true);
	}

	/**
	 * 注： 要进入此方法 必须设置view 的背景  可设置透明
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(currWidth!=getWidth()||currHeight!=getHeight()){//避免重复刷新布局
			if(getWidth()!=0){
				currWidth=getWidth();
			}
			if(getHeight()!=0){
				currHeight=getHeight();
				if(isFixHeight){
					setWidthOrHeight(-1,currHeight);
				}
			}
		}
		if(initRect==null){
			initRect=getMargins();
		}
	}


	protected boolean checkTouchSlop(float dx, float dy) {
		switch (dragDirection) {
		case DIRECTION_TOP:
		case DIRECTION_BOTTOM:
			return Math.abs(dy) > touchSlop && Math.abs(dy) > Math.abs(dx);

		default:
			return Math.abs(dx) > touchSlop && Math.abs(dx) > Math.abs(dy);
		}
	}

	protected boolean checkTouch(float dx, float dy) {
		switch (dragDirection) {
		case DIRECTION_LEFT:
			return dx>0&&Math.abs(dy)<20;
		case DIRECTION_RIGHT:
			return dx<0&&Math.abs(dy)<20;
		case DIRECTION_TOP:
			return dy>0&&Math.abs(dx)<20;
		case DIRECTION_BOTTOM:
			return dy<0&&Math.abs(dx)<20;
		}
		return true;
	}


	boolean flag=true;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(onDragInterceptTouchListener !=null){
			if((isDraging|| onDragInterceptTouchListener.isIntercept(ev))&&canDrag&&!animRunning){
				isDispatch=false;
				if(flag){
					super.dispatchTouchEvent(ev);
					flag=false;
				}
				return onTouchEvent(ev);
			}else{
				lastMotionX=ev.getX();
				lastMotionY=ev.getY();
				if(autoSlow){
					dragFactor=1f;
				}
				activePointerId = ev.getPointerId(0);
			}
		}
		isDispatch=true;
		flag=true;
		return super.dispatchTouchEvent(ev);
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(onDragInterceptTouchListener !=null){
			return !isDispatch;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(onDragInterceptTouchListener !=null){
			if((isDraging|| onDragInterceptTouchListener.isIntercept(ev))&&canDrag&&!animRunning){
                if (dragDirection == DIRECTION_NONE) {
                    return true;
                }
				final int action = ev.getAction() & MotionEvent.ACTION_MASK;
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					lastMotionX=ev.getX();
					lastMotionY=ev.getY();
					if(autoSlow){
						dragFactor=defaultDragFactor;
					}

					activePointerId = ev.getPointerId(0);
					if(dragListener!=null){
						dragListener.onStartMove();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					final int pointerIndex = ev.findPointerIndex(activePointerId);
					if (pointerIndex == -1) {
						break;
					}
					final float x = ev.getX(pointerIndex);
					float dx = (x - lastMotionX)*dragFactor;
					final float y = ev.getY(pointerIndex);
					float dy = (y - lastMotionY)*dragFactor;

					Rect oldRect=getMargins();
					onMoveEvent(dx,dy);
					if(dragListener!=null){
						dragListener.onPositionChange(oldRect,getMargins());
					}

					if(dragFactor<MAXDRAGFACTOR){
						lastMotionX=x;
						lastMotionY=y;
					}
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					if(dragListener!=null){
						dragListener.onDragCancel(getMargins());
					}
					resetPosition();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					final int index = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
					lastMotionX = ev.getX(index);
					lastMotionY = ev.getY(index);
					activePointerId = ev.getPointerId(index);
					break;
					case MotionEvent.ACTION_POINTER_UP:
						onPointerUp(ev);
						lastMotionX = ev.getX(ev.findPointerIndex(activePointerId));
						lastMotionY = ev.getY(ev.findPointerIndex(activePointerId));
						break;

				}
				return canDrag;
			}
		}
		return super.onTouchEvent(ev);
	}


	protected void onMoveEvent(float dx, float dy) {
        isDraging = true;
        int l = initRect.left;
        int t = initRect.top;
        int r = initRect.right;
        int b = initRect.bottom;


        switch (dragDirection) {
            case DIRECTION_ROUND:
                l = (int) (getMargins().left + Math.ceil(dx));
                t = (int) (getMargins().top + Math.ceil(dy));
                break;
            case DIRECTION_LEFT:
                l = (int) (getMargins().left + Math.ceil(dx));
                if (l < initRect.left) {
                    isDraging = false;
                    l = initRect.left;
                }
                if (autoSlow) {
                    float currFactor = getMargins().left / screenWidth;
                    if (currFactor > 0.5) {
                        dragFactor = 1 - currFactor;
                    } else {
                        dragFactor = defaultDragFactor;
                    }
                    if (l >= screenWidth * 0.9) {
                        l = (int) (screenWidth * 0.9);
                    }
                }
                break;
            case DIRECTION_TOP:
                t = (int) (getMargins().top + Math.ceil(dy));
                if (t < initRect.top) {
                    t = initRect.top;
                    isDraging = false;
                }
                if (autoSlow) {
                    float currFactor = getMargins().top / screenHeight;
                    if (currFactor > 0.5) {
                        dragFactor = 1 - currFactor;
                    } else {
                        dragFactor = defaultDragFactor;
                    }
                    if (t >= screenHeight * 0.8) {
                        t = (int) (screenHeight * 0.8);
                    }
                }
                break;
            case DIRECTION_RIGHT:
                l = (int) (getMargins().left + Math.ceil(dx));
                if (l > initRect.left) {
                    isDraging = false;
                    l = initRect.left;
                }
                if (autoSlow) {
                    float currFactor = Math.abs(getMargins().left) / screenWidth;
                    if (currFactor > 0.5) {
                        dragFactor = 1 - currFactor;
                    } else {
                        dragFactor = defaultDragFactor;
                    }
                    if (Math.abs(l) >= screenWidth * 0.9) {
                        l = -(int) (screenWidth * 0.9);
                    }
                }
                break;
            case DIRECTION_BOTTOM:
                t = (int) (getMargins().top + Math.ceil(dy));
                if (t > initRect.top) {
                    isDraging = false;
                    t = initRect.top;
                }
                if (autoSlow) {
                    float currFactor = Math.abs(getMargins().top) / screenHeight;
                    if (currFactor > 0.5) {
                        dragFactor = 1 - currFactor;
                    } else {
                        dragFactor = defaultDragFactor;
                    }
                    if (Math.abs(t) >= screenHeight * 0.9) {
                        t = -(int) (screenHeight * 0.9);
                    }
                }
                break;
        }

        if (isFixHeight) {
            setMargins(l, t, r, b, -1, currHeight);
        } else {
            setMargins(l, t, r, b);
        }
    }



	public void setMargins(int l,int t,int r,int b) {
		if(getLayoutParams() instanceof LayoutParams){
			LayoutParams mLayoutPrams=(LayoutParams) getLayoutParams();
			mLayoutPrams.setMargins(l, t , r, b);
			setLayoutParams(mLayoutPrams);
		}else if(getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams) getLayoutParams();
			mLayoutPrams.setMargins(l, t , r, b);
			setLayoutParams(mLayoutPrams);
		}
	}

	public void setMargins(int l,int t,int r,int b,int width,int height) {
		if(getLayoutParams() instanceof LayoutParams){
			LayoutParams mLayoutPrams=(LayoutParams) getLayoutParams();
			if(width>=0){
				mLayoutPrams.width=width;
			}
			if(height>=0){
				mLayoutPrams.height=height;
			}
			mLayoutPrams.setMargins(l, t , r, b);
			setLayoutParams(mLayoutPrams);
		}else if(getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams) getLayoutParams();
			if(width>=0){
				mLayoutPrams.width=width;
			}
			if(height>=0){
				mLayoutPrams.height=height;
			}
			mLayoutPrams.setMargins(l, t , r, b);
			setLayoutParams(mLayoutPrams);
		}
	}

	public Rect getMargins() {
		Rect rect=new Rect();
		if(getLayoutParams() instanceof LayoutParams){
			LayoutParams mLayoutPrams=(LayoutParams) getLayoutParams();
			rect.set(mLayoutPrams.leftMargin, mLayoutPrams.topMargin, mLayoutPrams.rightMargin, mLayoutPrams.bottomMargin);
		}else if(getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams) getLayoutParams();
			rect.set(mLayoutPrams.leftMargin, mLayoutPrams.topMargin, mLayoutPrams.rightMargin, mLayoutPrams.bottomMargin);
		}
		return rect;
	}

	public void setWidthOrHeight(int width,int height) {
		if(getLayoutParams() instanceof LayoutParams){
			LayoutParams mLayoutPrams=(LayoutParams) getLayoutParams();
			if(width>=0){
				mLayoutPrams.width=width;
			}
			if(height>=0){
				mLayoutPrams.height=height;
			}
			setLayoutParams(mLayoutPrams);
		}else if(getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams) getLayoutParams();
			if(width>=0){
				mLayoutPrams.width=width;
			}
			if(height>=0){
				mLayoutPrams.height=height;
			}
			setLayoutParams(mLayoutPrams);
		}
	}



	private void onPointerUp(MotionEvent ev) {
		final int pointerIndex = ev.getActionIndex();
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == activePointerId) {
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			lastMotionX = ev.getX(newPointerIndex);
			activePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	public boolean  checkIsOut(){
		Rect currRect=getMargins();
		switch (dragDirection) {
		case DIRECTION_LEFT:
			return Math.abs(currRect.left)>currWidth;
		case DIRECTION_RIGHT:
			return Math.abs(currRect.right)>currWidth;
		case DIRECTION_TOP:
			return Math.abs(currRect.top)>currHeight;
		case DIRECTION_BOTTOM:
			return Math.abs(currRect.bottom)>currHeight;
		}
		return false;
	}

	SelfValueAnimation valueAnim;
	public void resetPosition(){
		this.clearAnimation();
		//((ViewGroup)this.getParent()).clearAnimation();
		if(valueAnim==null){
			valueAnim=new SelfValueAnimation();
		}
		valueAnim.setDuration(animDuration);
		valueAnim.setInterpolator(animInterpolator);
		valueAnim.setValues(0, 1);
		final Rect currRect=getMargins();
		valueAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				animRunning=true;
				if(animationListener!=null){
					animationListener.onAnimationStart(arg0);
				}
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				if(animationListener!=null){
					animationListener.onAnimationRepeat(arg0);
				}
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				if(dragListener!=null){
					dragListener.onStopMove();
				}
				isDraging=false;
				animRunning=false;
				if(animationListener!=null){
					animationListener.onAnimationEnd(arg0);
				}
			}
		});
		valueAnim.setValueChange(new SelfValueAnimation.ValueChange() {

			@Override
			public void valueChange(float newValue, float oldValue) {
				// TODO Auto-generated method stub
				Rect oldRect=getMargins();
				switch (dragDirection) {
				case DIRECTION_ROUND:
					setMargins(mergeValue(currRect.left,initRect.left+animTargetRect.left,newValue),mergeValue(currRect.top,initRect.top+animTargetRect.top,newValue),initRect.right,initRect.bottom);
					break;
				case DIRECTION_RIGHT:
				case DIRECTION_LEFT:
					setMargins(mergeValue(currRect.left,initRect.left+animTargetRect.left,newValue),initRect.top+animTargetRect.top,  initRect.right, initRect.bottom);
					break;
				case DIRECTION_BOTTOM:
				case DIRECTION_TOP:
					setMargins(initRect.left+animTargetRect.left,mergeValue(currRect.top,initRect.top+animTargetRect.top,newValue),  initRect.right, initRect.bottom);
					break;
				}
				if(dragListener!=null&&isDraging){
					dragListener.onPositionChange(oldRect,getMargins());
				}
			}
		});
		this.startAnimation(valueAnim);
	}

	public int mergeValue(int startValue,int endValue,float interpolatedTime){
		float newValue=0;
		if(startValue<endValue){
			newValue=(startValue+Math.abs(endValue-startValue)*interpolatedTime);
		}else{
			newValue=(startValue-Math.abs(endValue-startValue)*interpolatedTime);
		}
		return (int)newValue;
	}


	public SimpleDragListener getDragListener() {
		return dragListener;
	}

	public void setDragListener(SimpleDragListener dragListener) {
		this.dragListener = dragListener;
	}

	public OnDragInterceptTouchListener getOnDragInterceptTouchListener() {
		return onDragInterceptTouchListener;
	}

	public void setOnDragInterceptTouchListener(
            OnDragInterceptTouchListener onDragInterceptTouchListener) {
		this.onDragInterceptTouchListener = onDragInterceptTouchListener;
	}

	public boolean isCanDrag() {
		return canDrag;
	}

	public void setCanDrag(boolean canDrag) {
		this.canDrag = canDrag;
	}

	public int getDragDirection() {
		return dragDirection;
	}

	public void setDragDirection(int dragDirection) {
		this.dragDirection = dragDirection;
	}

	public int getCurrWidth() {
		return currWidth;
	}

	public void setCurrWidth(int currWidth) {
		this.currWidth = currWidth;
	}

	public int getCurrHeight() {
		return currHeight;
	}

	public void setCurrHeight(int currHeight) {
		this.currHeight = currHeight;
	}

	public int getActivePointerId() {
		return activePointerId;
	}

	public void setActivePointerId(int activePointerId) {
		this.activePointerId = activePointerId;
	}

	public float getLastMotionX() {
		return lastMotionX;
	}

	public void setLastMotionX(float lastMotionX) {
		this.lastMotionX = lastMotionX;
	}

	public float getLastMotionY() {
		return lastMotionY;
	}

	public void setLastMotionY(float lastMotionY) {
		this.lastMotionY = lastMotionY;
	}

	public Interpolator getAnimInterpolator() {
		return animInterpolator;
	}

	public void setAnimInterpolator(Interpolator animInterpolator) {
		this.animInterpolator = animInterpolator;
	}

	public long getAnimDuration() {
		return animDuration;
	}

	public void setAnimDuration(long animDuration) {
		this.animDuration = animDuration;
	}

	public Rect getInitRect() {
		return initRect;
	}

	public void setInitRect(Rect initRect) {
		this.initRect = initRect;
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	public boolean isAnimRunning() {
		return animRunning;
	}

	public void setAnimRunning(boolean animRunning) {
		this.animRunning = animRunning;
	}

	public float getDragFactor() {
		return dragFactor;
	}

	public void setDragFactor(float dragFactor) {
		if(dragFactor>MAXDRAGFACTOR){
			dragFactor=MAXDRAGFACTOR;
		}
		this.dragFactor = dragFactor;
	}

	public boolean isAutoSlow() {
		return autoSlow;
	}

	public void setAutoSlow(boolean autoSlow) {
		this.autoSlow = autoSlow;
	}

	public Rect getAnimTargetRect() {
		return animTargetRect;
	}

	public void setAnimTargetRect(Rect animTargetRect) {
		this.animTargetRect = animTargetRect;
	}



	public boolean isDraging() {
		return isDraging;
	}

	public void setDraging(boolean isDraging) {
		this.isDraging = isDraging;
	}

	public int getTouchSlop() {
		return touchSlop;
	}

	public void setTouchSlop(int touchSlop) {
		this.touchSlop = touchSlop;
	}

	public boolean isFixHeight() {
		return isFixHeight;
	}

	public void setFixHeight(boolean isFixHeight) {
		this.isFixHeight = isFixHeight;
	}

	public AnimationListener getAnimationListener() {
		return animationListener;
	}

	public void setAnimationListener(AnimationListener animationListener) {
		this.animationListener = animationListener;
	}



	public interface SimpleDragListener{
		public void onStartMove();
		public void onPositionChange(Rect oldRect, Rect newRect);
		public void onDragCancel(Rect rect);
		public void onStopMove();
	}

	public interface OnDragInterceptTouchListener {
		/**
		 * 是否拦截 touch 事件
		 * @return
		 */
		public boolean isIntercept(MotionEvent event);
	}
}
