package self.androidbase.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import self.androidbase.utils.DensityUtils;

/**
 * 字母索引的view
 * @author janesen
 *
 */
@SuppressLint("DrawAllocation")
public class SelfLetterView extends View {

	public SelfLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	int charHeight=0;
	int currChar=-1;
	float charStartY=0;
	List<Character> letters=new ArrayList<Character>();
	
	
	private LetterSelected letterSelect;
	
	public LetterSelected getLetterSelect() {
		return letterSelect;
	}


	public void setLetterSelect(LetterSelected letterSelect) {
		this.letterSelect = letterSelect;
	}

	

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint pain=new Paint();
		pain.setColor(Color.WHITE);
		pain.setTextSize(DensityUtils.dip2px(getContext(), makeFontSizeByHeight(27)));

		Rect bounds = new Rect();
		pain.getTextBounds("A", 0, 1, bounds);
		charHeight=bounds.height()+DensityUtils.dip2px(getContext(), 5);

		float y=(getHeight()-(bounds.height()*27+ DensityUtils.dip2px(getContext(), 5)*22))/2;
		charStartY=y;
		
		y = drawChar(canvas, pain, bounds, y, '#');
		for(char c='A';c<='Z';c++){
			y = drawChar(canvas, pain, bounds, y, c);
		}

		canvas.save();
		canvas.restore();
	}


	private float drawChar(Canvas canvas, Paint pain, Rect bounds, float y,
			char c) {
		pain.getTextBounds(c+"", 0, 1, bounds);
		float x=(getWidth()-bounds.width())/2;
		if(c==currChar){
			pain.setColor(Color.RED);
		}else{
			pain.setColor(Color.WHITE);
		}
		canvas.drawText(c+"", x,y, pain);
		letters.add(c);
		y=y+bounds.height()+DensityUtils.dip2px(getContext(), 5);
		return y;
	}


	public float makeFontSizeByHeight(int strLength){
		float startSize=16.0f;
		Rect bounds = new Rect();
		TextPaint tp=new TextPaint();
		tp.setTextSize(DensityUtils.dip2px(getContext(), startSize));
		tp.getTextBounds("A", 0, 1, bounds);

		while(((bounds.height()+DensityUtils.dip2px(getContext(), 5))*strLength)>getHeight()){
			startSize=startSize-1;
			tp.setTextSize(DensityUtils.dip2px(getContext(), startSize));
			tp.getTextBounds("A", 0, 1, bounds);
		}
		return startSize;
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int index=(int)Math.ceil( ((event.getY()-charStartY)/charHeight));
			if(index<letters.size()&&index>=0){
				if(letters.get(index)!=currChar){
					currChar=letters.get(index);
					if(letterSelect!=null){
						letterSelect.letterSelect(letters.get(index)+"",getWidth(),event.getY(),event.getRawY());	
					}
				}
			}else{
				currChar=-1;
				if(letterSelect!=null){
					letterSelect.letterSelect(null,getWidth(),event.getY(),event.getRawY());
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			currChar=-1;
			if(letterSelect!=null){
				letterSelect.letterSelect(null,getWidth(),event.getY(),event.getRawY());
			}
			break;
		}
		invalidate();
		return true;
	}
	
	
	
	
	public interface LetterSelected{
		void letterSelect(String charStr, float x, float y, float rawY);
	}


}
