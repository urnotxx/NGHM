package self.androidbase.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.FrameLayout;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SelfPullRefreshLayout extends FrameLayout implements SelfDragFrameLayout.OnDragInterceptTouchListener {
    public SelfPullRefreshLayout(Context context) {
        super(context);
    }

    @Override
    public boolean isIntercept(MotionEvent event) {
        return false;
    }
//	private View pullRefreshHead=null;
//	private SelfDragFrameLayout pullRefreshContent=null;
//	private TextView tvPullState=null;
//	private ImageView imageView =null;
//	private FrameLayout flMask;
//	private boolean isRefreshing=false;
//	private CircularProgressBar circularP;
//	private float lastMotionY;
//	private int activePointerId=-1;
//	private OnPullListener onPullListener;
//    private boolean isInit;
//
//	public SelfPullRefreshLayout(Context context) {
//		super(context);
//        if (getBackground() == null) {
//            setBackgroundColor(Color.BLACK);
//        }
//    }
//
//
//	public SelfPullRefreshLayout(Context context, AttributeSet attrs) {
//		super(context, attrs);
//        if (getBackground() == null) {
//            setBackgroundColor(Color.BLACK);
//        }
//	}
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (!isInit) {
//            initView();
//        }
//    }
//
//    private float currDegree =1;
//	public void initView(){
//		// TODO Auto-generated constructor stub
//		try {
//            isInit=true;
//			if(pullRefreshContent==null){
//				pullRefreshContent=new SelfDragFrameLayout(getContext());
//				pullRefreshContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//                pullRefreshContent.setDragDirection(SelfDragFrameLayout.DIRECTION_TOP);
//                pullRefreshContent.setOnDragInterceptTouchListener(this);
//				for (int i = 0; i < this.getChildCount(); i++) {
//					View child=getChildAt(i);
//					removeViewAt(i);
//					pullRefreshContent.addView(child);
//					child.bringToFront();
//				}
//
//
//				flMask=new FrameLayout(getContext());
//				flMask.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//				flMask.setBackgroundColor(Color.parseColor("#99ffffff"));
//				flMask.setClickable(true);
//				flMask.setVisibility(View.GONE);
//
//				addView(flMask);
//
//				pullRefreshHead=LayoutInflater.from(getContext()).inflate(R.layout.android_base_pullrefresh_head, null);
//
//				tvPullState=(TextView) pullRefreshHead.findViewById(R.id.android_base_tvPullState);
//				imageView =(ImageView) pullRefreshHead.findViewById(R.id.android_base_imgArrow);
//                circularP = (CircularProgressBar) pullRefreshHead.findViewById(R.id.android_base_circularP);
//                circularP.setVisibility(GONE);
//
//				addView(pullRefreshHead);
//				addView(pullRefreshContent);
//
//				tvPullState.setTag(-1);
//
//				pullRefreshContent.setDragListener(new SelfDragFrameLayout.SimpleDragListener() {
//
//
//                    @Override
//                    public void onStartMove() {
//
//                    }
//
//                    @Override
//					public void onPositionChange(Rect oldRect,Rect newRect) {
//						// TODO Auto-generated method stub
//						int pullRefreshHeadHeight= DensityUtils.getViewSize(pullRefreshHead)[1];
//						float radio=(float)newRect.top/(float)pullRefreshHeadHeight;
//
//						if(newRect.top>=pullRefreshHeadHeight){
//							tvPullState.setText("松开刷新");
//							if(Build.VERSION.SDK_INT<11){
//								rotaArrow(1);
//							}else{
//								if(tvPullState.getTag().equals(0)){
//									currDegree = currDegree *-1;
//								}
//							}
//
//							tvPullState.setTag(1);
//
//							pullRefreshContent.getAnimTargetRect().top=pullRefreshHeadHeight;
//							ViewUtils.setMargins(pullRefreshHead, 0, pullRefreshContent.getMargins().top - pullRefreshHeadHeight, 0, 0);
//						}else{
//							tvPullState.setText("下拉刷新");
//							tvPullState.setTag(0);
//							if(Build.VERSION.SDK_INT<11){
//								rotaArrow(0);
//							}
//							pullRefreshContent.getAnimTargetRect().top=0;
//						}
//						if(Build.VERSION.SDK_INT>11){
//							if(currDegree >0){
//								imageView.setRotation(Math.min(currDegree * radio * 180, currDegree * 180));
//							}else{
//								imageView.setRotation(Math.max(currDegree * radio * 180, currDegree * 180));
//							}
//							if(imageView.getRotation()==0){
//								currDegree =1;
//							}
//						}
//					}
//
//                    @Override
//                    public void onDragCancel(Rect rect) {
//
//                    }
//
//                    @Override
//                    public void onStopMove() {
//                        if(tvPullState.getTag().equals(1)){
//                            tvPullState.setText("正在刷新");
//                            tvPullState.setTag(2);
//                            circularP.setVisibility(VISIBLE);
//                            if(onPullListener!=null){
//                                onPullListener.startRefresh();
//                            }
//                            imageView.setVisibility(View.GONE);
//                            isRefreshing=true;
//                        }
//                    }
//
//				});
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void showMask(){
//		flMask.setVisibility(View.VISIBLE);
//		flMask.bringToFront();
//	}
//
//	public void hideMask(){
//		flMask.setVisibility(View.GONE);
//	}
//
//
//	public void refreshDone(){
//		isRefreshing=false;
//		pullRefreshContent.getAnimTargetRect().top=0;
//		pullRefreshContent.resetPosition();
//
//		tvPullState.setText("下拉刷新");
//		imageView.setVisibility(VISIBLE);
//        circularP.setVisibility(GONE);
//
//		TextView tvUpdateTime=(TextView) pullRefreshHead.findViewById(R.id.android_base_tvUpdateTime);
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		tvUpdateTime.setText("最后更新：" + sdf.format(new Date()));
//
//		if(Build.VERSION.SDK_INT>11){
//			imageView.setRotation(0);
//		}
//	}
//
//	public void rotaArrow(int span){
//		if((imageView.getTag()==null|| imageView.getTag().equals(180))&&span==1){
//			RotateAnimation rotateAnim=new RotateAnimation(180, 0,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//			rotateAnim.setDuration(300);
//			rotateAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//			rotateAnim.setFillAfter(true);
//			imageView.startAnimation(rotateAnim);
//			imageView.setTag(0);
//		}
//		if((imageView.getTag()!=null&& imageView.getTag().equals(0))&&span==0){
//			RotateAnimation rotateAnim=new RotateAnimation(0, 180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//			rotateAnim.setDuration(300);
//			rotateAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//			rotateAnim.setFillAfter(true);
//			imageView.startAnimation(rotateAnim);
//			imageView.setTag(180);
//		}
//	}
//
//	public View getPullRefreshHead() {
//		return pullRefreshHead;
//	}
//
//
//	public void setPullRefreshHead(View pullRefreshHead) {
//		this.pullRefreshHead = pullRefreshHead;
//	}
//
//
//	public TextView getTvPullState() {
//		return tvPullState;
//	}
//
//
//	public void setTvPullState(TextView tvPullState) {
//		this.tvPullState = tvPullState;
//	}
//
//
//	public boolean isRefreshing() {
//		return isRefreshing;
//	}
//
//
//	public void setRefreshing(boolean isRefreshing) {
//		this.isRefreshing = isRefreshing;
//	}
//
//    public OnPullListener getOnPullListener() {
//        return onPullListener;
//    }
//
//    public void setOnPullListener(OnPullListener onPullListener) {
//        this.onPullListener = onPullListener;
//    }
//
//
//	public interface OnPullListener {
//        boolean isPull(MotionEvent event);
//        void startRefresh();
//	}
//
//
//	@Override
//	public boolean isIntercept(MotionEvent ev) {
////		if(onPullListener !=null&& onPullListener.isPull(ev)){
////			switch (ev.getAction() & MotionEvent.ACTION_MASK) {
////			case MotionEvent.ACTION_DOWN:
////				lastMotionY=ev.getY();
////				activePointerId = ev.getPointerId(0);
////				break;
////			case MotionEvent.ACTION_MOVE:
////				final int pointerIndex = ev.findPointerIndex(activePointerId);
////				if (pointerIndex == -1) {
////					break;
////				}
////				final float y = ev.getY(pointerIndex);
////				float dy = y - lastMotionY;
////				return dy>0;
////			}
////		}
//		return onPullListener !=null&& onPullListener.isPull(ev);
//	}

}
