package self.androidbase.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ScrollView;


import java.util.List;

import self.androidbase.extend.SelfValueAnimation;
import self.androidbase.utils.DensityUtils;

/**
 * 有弹性的ScrollView
 * 实现下拉弹回和上拉弹回
 * 
 */
@SuppressLint("NewApi")
public class SelfBoundScrollView extends ScrollView {

	private BoundChange boundChange;
	private ScrollChange scrollChange;

	public ScrollChange getScrollChange() {
		return scrollChange;
	}

	public void setScrollChange(ScrollChange scrollChange) {
		this.scrollChange = scrollChange;
	}

	public BoundChange getBoundChange() {
		return boundChange;
	}

	public void setBoundChange(BoundChange boundChange) {
		this.boundChange = boundChange;
	}

	private static float MOVE_FACTOR = 0.3f;
	private List<View> refreshViewsStateOnScroll;
	private static int ANIM_TIME = 100;

	private ViewGroup contentView; 

	public boolean needRefresh;

	private boolean isStop;

	private float startY;
	private float startY2;
	private int currTop;
	boolean secondIsPress=false;
	boolean isIntervalTouch=false;
	float firstRawY;
	int moveType=-1;//0向上 1 向下 -1两者都不是


	private boolean isPullTop=true;
	private boolean isPullBottom=true;

	private boolean isShowBezier;
	private int bezierColor;
	private Paint paint;
	private Path path;

	private int bezierStartX;
	private int bezierStartY;
	// 手势坐标
	private float touchX =0;
	private float touchY = 0;

	// 锚点坐标
	private float anchorX ;
	private float anchorY;


	private float defaultRadius= DensityUtils.dip2px(getContext(), 8);

	// 定点圆半径
	private float radius;
	public boolean isBreak=false;

	public int getBezierColor() {
		return bezierColor;
	}

	public void setBezierColor(int bezierColor) {
		this.bezierColor = bezierColor;
	}

	public boolean isShowBezier() {
		return isShowBezier;
	}

	public void setShowBezier(boolean isShowBezier) {
		this.isShowBezier = isShowBezier;
	}
	public boolean isPullTop() {
		return isPullTop;
	}

	public void setPullTop(boolean isPullTop) {
		this.isPullTop = isPullTop;
	}

	public boolean isPullBottom() {
		return isPullBottom;
	}

	public void setPullBottom(boolean isPullBottom) {
		this.isPullBottom = isPullBottom;
	}



	public List<View> getRefreshViewsStateOnScroll() {
		return refreshViewsStateOnScroll;
	}

	public void setRefreshViewsStateOnScroll(List<View> refreshViewsStateOnScroll) {
		this.refreshViewsStateOnScroll = refreshViewsStateOnScroll;
	}

	public SelfBoundScrollView(Context context) {
		super(context);
		if(Build.VERSION.SDK_INT>8){
			setOverScrollMode(OVER_SCROLL_NEVER);
		}
		if(getBackground()==null){
			setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public SelfBoundScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(Build.VERSION.SDK_INT>8){
			setOverScrollMode(OVER_SCROLL_NEVER);
		}
		if(getBackground()==null){
			setBackgroundColor(Color.TRANSPARENT);
		}
	}


	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			contentView = (ViewGroup) getChildAt(0);
		}
	}

	public ViewGroup getContentView() {
		return contentView;
	}

	public void setContentView(ViewGroup contentView) {
		this.contentView = contentView;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if(contentView == null) return;
	}


	private void calculate(){
		if(touchY>0){
			float distance = (float) Math.sqrt(Math.pow(touchY-bezierStartY, 2) + Math.pow(touchX-bezierStartX, 2));
			radius = -distance/15+defaultRadius;

			anchorX =  (touchX + bezierStartX)/2;
			anchorY =  (touchY + bezierStartY)/4;
			if(radius <defaultRadius/3){
				isBreak=true;
				path.reset();

			}

			if(!isBreak){
				// 根据角度算出四边形的四个点
				float offsetX = (float) (radius*Math.sin(Math.atan((touchY - bezierStartY) / (touchX - bezierStartX))));
				float offsetY = (float) (radius*Math.cos(Math.atan((touchY - bezierStartY) / (touchX - bezierStartX))));

				float x1 = bezierStartX - offsetX+2;
				float y1 = bezierStartY + offsetY;

				float x2 = touchX - offsetX;
				float y2 = touchY + offsetY;

				float x3 = touchX + offsetX;
				float y3 = touchY - offsetY;

				float x4 = bezierStartX + offsetX-2;
				float y4 = bezierStartY - offsetY;

				path.reset();
				path.moveTo(x1, y1);
				path.quadTo(anchorX, anchorY, x2, y2);
				path.lineTo(x3, y3);
				path.quadTo(anchorX, anchorY, x4, y4);
				path.lineTo(x1, y1);
			}
		}
	}


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(isShowBezier&&contentView.getChildCount()!=0){
			if(paint==null){
				path = new Path();
				bezierStartX=getWidth()/2;
				bezierStartY=(int) defaultRadius/2;
				paint = new Paint();
				paint.setAntiAlias(true);
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				paint.setStrokeWidth(2);
				paint.setColor(bezierColor);
			}
			if(isBreak){
				canvas.drawColor(Color.TRANSPARENT);
				canvas.save();
				if(!isStop){
					needRefresh=true;
				}
			}else{
				needRefresh=false;
				isStop=false;
				calculate();
				canvas.drawPath(path, paint);
				canvas.drawCircle(bezierStartX, bezierStartY, radius, paint);
				canvas.save();
			}
		}
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			moveType=-1;
			isIntervalTouch=false;
			firstRawY=ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(this.getScrollY()==0){
				isIntervalTouch=true;
			}

			if((this.getScrollY()==(contentView.getHeight()-this.getHeight())||contentView.getHeight()<this.getHeight())){
				isIntervalTouch=true;
			}

			if(Math.abs(ev.getRawY()-firstRawY)>50&&isIntervalTouch){
				return true;//拦截touch事件
			}
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}



	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		refreshViewsState();
		super.onScrollChanged(l, t, oldl, oldt);
		if(scrollChange!=null){
			scrollChange.onScrollChanged(l, t, oldl, oldt);
		}
	}




	public void refreshViewsState(){
		if(refreshViewsStateOnScroll!=null){
			for (View view : refreshViewsStateOnScroll) {
				view.refreshDrawableState();
			}
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (contentView == null) {
			return super.dispatchTouchEvent(ev);
		}

		// TODO Auto-generated method stub

		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:

			if(this.getScrollY()==0&&ev.getRawY()-firstRawY>0&&isPullTop){
				if(moveType!=0){
					startY=ev.getY(0);
					moveType=0;

					if(isBreak){
						isBreak=false;
					}
				}
			}


			if((this.getScrollY()==(contentView.getHeight()-this.getHeight())||contentView.getHeight()<this.getHeight())&&ev.getRawY()-firstRawY<0&&isPullBottom){
				if(moveType!=1){
					startY=ev.getY(0);
					moveType=1;

					if(isBreak){
						isBreak=false;
					}
				}

			}
			if(moveType!=-1){

				this.clearAnimation();
				int span=0;

				if(ev.getPointerCount()>=2){
					secondIsPress=true;
					currTop=contentView.getTop();
					if(startY2!=0){
						span=(int)((ev.getY(1)+ev.getY(0)-startY2)*MOVE_FACTOR);
					}
					startY2=ev.getY(1)+ev.getY(0);

				}
				if(ev.getPointerCount()==1){
					if(secondIsPress){
						startY=ev.getY(0);
						secondIsPress=false;
						startY2=0;
					}
					span=(int)((ev.getY(0)-startY)*MOVE_FACTOR);
				}


				if(Math.abs((span+currTop))>500){
					span=(int) (span*0.2);
				}
				contentView.layout(contentView.getLeft(),span+currTop, contentView.getRight(), contentView.getHeight()+(span+currTop));


				if(moveType==0){
					drawBezier();
				}else{
					isStop=true;
					isBreak=true;
					needRefresh=false;
					invalidate();
				}

				if(boundChange!=null)
				{
					boundChange.boundChange(1,span+currTop);
				}

				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			currTop=0;
			this.clearAnimation();
			if(moveType!=-1){
				AnimationMove am=new AnimationMove(contentView,contentView.getTop(),0,moveType);
				am.setDuration(ANIM_TIME);
				am.setInterpolator(new LinearInterpolator());
				this.startAnimation(am);
			}
			moveType=-1;
			break;
		}
		return super.onTouchEvent(ev);
	}


	public void drawBezier(){
		if(isShowBezier){
			int[] location=new int[2];
			contentView.getLocationInWindow(location);

			int parentLocation[]=new int[2];
			this.getLocationOnScreen(parentLocation);
			touchX=getWidth()/2;
			touchY=location[1]-parentLocation[1]+50;

			invalidate();
		}
	}

	public interface BoundChange{
		void boundChange(int state, int value);
	}

	public interface ScrollChange{
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}



	public void scrollTo(final int x, int y,boolean isAnim) {
		// TODO Auto-generated method stub

		if(isAnim){
			this.clearAnimation();
			SelfValueAnimation sva=new SelfValueAnimation();
			sva.setValues(getScrollY(), y);
			sva.setDuration(300);
			sva.setInterpolator(new AccelerateDecelerateInterpolator());
			sva.setValueChange(new SelfValueAnimation.ValueChange() {

				@Override
				public void valueChange(float newValue, float oldValue) {
					// TODO Auto-generated method stub
					SelfBoundScrollView.this.scrollTo(x, (int) newValue);
				}
			});
			this.startAnimation(sva);
		}else{
			this.scrollTo(x, y);
		}
	}


	public void scrollTo(final int x, int y,boolean isAnim,AnimationListener al) {
		// TODO Auto-generated method stub

		if(isAnim){
			this.clearAnimation();
			SelfValueAnimation sva=new SelfValueAnimation();
			sva.setValues(getScrollY(), y);
			sva.setDuration(300);
			sva.setInterpolator(new AccelerateDecelerateInterpolator());
			sva.setAnimationListener(al);
			sva.setValueChange(new SelfValueAnimation.ValueChange() {

				@Override
				public void valueChange(float newValue, float oldValue) {
					// TODO Auto-generated method stub
					SelfBoundScrollView.this.scrollTo(x, (int) newValue);
				}
			});
			this.startAnimation(sva);
		}else{
			this.scrollTo(x, y);
		}
	}

	class  AnimationMove  extends Animation{

		View view;
		int moveType;
		int startValue;
		int endValue;
		public AnimationMove(View view,int startValue,int endValue,int moveType){
			this.view=view;
			this.startValue=startValue;
			this.endValue=endValue;
			this.moveType=moveType;
		}

		int span=-1;
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			if(span!=0){
				if(startValue<endValue){
					span=(int)((startValue+Math.abs(endValue-startValue)*interpolatedTime));
				}else{
					drawBezier();
					span=(int)((startValue-Math.abs(endValue-startValue)*interpolatedTime));
				}

				view.layout(contentView.getLeft(),span, contentView.getRight(), contentView.getHeight()+span);

				if(boundChange!=null)
				{
					boundChange.boundChange(0, span);
				}
			}else{
				if(span==0){//停止了
					isStop=true;
					span=-1;
					if(boundChange!=null)
					{
						boundChange.boundChange(-1, 0);
					}
					isBreak=true;
					invalidate();
				}else{
					if(boundChange!=null)
					{
						boundChange.boundChange(0, span);
					}
				}

			}

		}

	}

}