package self.androidbase.views;


import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import self.androidbase.R;
import self.androidbase.extend.SelfDialog;
import self.androidbase.utils.DensityUtils;
import self.androidbase.utils.ViewUtils;

public class SelfTipView {


	public static Dialog showTipView(Context context,int x,int y,final LinkedHashMap<String, OnClickListener> menusMap){
		return makeTipView(context, x, y, menusMap, null);

	}

	public static Dialog showTipView(Context context,int x,int y,final LinkedHashMap<String, OnClickListener> menusMap,Object tagData){
        return makeTipView(context, x, y, menusMap, tagData);

	}

	static FrameLayout rootContainer=null;
	private static Dialog makeTipView(Context context, int x, int y,
									final LinkedHashMap<String, OnClickListener> menusMap, Object tagData) {
		
		if(context instanceof Service){
			return null;
		}

		if(menusMap==null){
			return null;
		}

		if(menusMap.size()==0){
			return null;
		}


		int tipHeight=45;//单位dp
		int arrowLeftMargin=x;
		int arrowWidth=38;//单位dp
		int arrowHeight=28;//单位dp
		int countWidth=0;
		Object[] keyArrary=menusMap.keySet().toArray();



		List<MenuItemView> menuViews=new ArrayList<>();
		for (int i = 0; i < keyArrary.length; i++) {
			String str = keyArrary[i].toString();

			LinearLayout llMenuItem = new LinearLayout(context);
			llMenuItem.setOrientation(LinearLayout.HORIZONTAL);
			FrameLayout.LayoutParams llMenuItemLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DensityUtils.dip2px(context, tipHeight));
			llMenuItem.setLayoutParams(llMenuItemLayoutParams);

			TextView tvMenuName = new TextView(context);
			tvMenuName.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			tvMenuName.setText(str);
			tvMenuName.setClickable(false);
			tvMenuName.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			tvMenuName.setTextSize(12f);
			tvMenuName.setTextColor(Color.WHITE);
			tvMenuName.setPadding(DensityUtils.dip2px(context,10f),0,DensityUtils.dip2px(context,10f),0);

			llMenuItem.addView(tvMenuName);

			View vLine = new View(context);
			LinearLayout.LayoutParams vLineP = new LinearLayout.LayoutParams(DensityUtils.dip2px(context, 0.5f), LayoutParams.MATCH_PARENT);
			vLineP.setMargins(0, DensityUtils.dip2px(context, 1.5f), 0, DensityUtils.dip2px(context, 5));
			vLine.setLayoutParams(vLineP);
			vLine.setBackgroundColor(Color.parseColor("#444444"));


			if(i!=keyArrary.length-1){
				llMenuItem.addView(vLine);
			}

			int width= ViewUtils.getViewSize(llMenuItem)[0];

			MenuItemView menuItemView=new MenuItemView();
			menuItemView.text=str;
			menuItemView.width=width;
			menuItemView.view=llMenuItem;
			menuViews.add(menuItemView);
			countWidth+=width;
		}

		int windowX=(x-countWidth/2);
		if(windowX<0){
			windowX=0;
		}else if(windowX>((int) (DensityUtils.getWidthInPx(context)-countWidth))){
			windowX= (int) (DensityUtils.getWidthInPx(context)-countWidth);
		}

		arrowLeftMargin=(x-windowX)-DensityUtils.dip2px(context,arrowWidth)/2;

		arrowLeftMargin=Math.max(5,arrowLeftMargin);

		rootContainer=new FrameLayout(context);
		rootContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

		final SelfDialog sDialog=new SelfDialog(context, R.style.SelfDialogStyle);
		

		final FrameLayout container=new FrameLayout(context);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		LinearLayout tipBgContainer=new LinearLayout(context);
		tipBgContainer.setOrientation(LinearLayout.VERTICAL);
		tipBgContainer.setLayoutParams(new LayoutParams(countWidth, LayoutParams.WRAP_CONTENT));


		LinearLayout tipBg=new LinearLayout(context);
		tipBg.setBackgroundResource(R.drawable.android_base_new_tip);
		tipBg.setLayoutParams(new LayoutParams(countWidth, DensityUtils.dip2px(context, tipHeight)));


		LinearLayout tipBgArrow=new LinearLayout(context);
		tipBgArrow.setBackgroundResource(R.drawable.android_base_new_tip_arrow);

		LinearLayout.LayoutParams  tipBgArrowLineP=new LinearLayout.LayoutParams(DensityUtils.dip2px(context,arrowWidth), DensityUtils.dip2px(context,arrowHeight));
		tipBgArrowLineP.setMargins(arrowLeftMargin, -DensityUtils.dip2px(context,8), 0, 0);
		tipBgArrow.setLayoutParams(tipBgArrowLineP);

		tipBgContainer.addView(tipBg);
		tipBgContainer.addView(tipBgArrow);

		container.addView(tipBgContainer);


		LinearLayout llTipMenusContainer=new LinearLayout(context);
		llTipMenusContainer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT ));




		int currLeftMargin=0;

		for (int i = 0; i < menuViews.size(); i++) {

			final MenuItemView menuItemView=menuViews.get(i);

			FrameLayout flItem=new FrameLayout(context);
			flItem.setLayoutParams(new LayoutParams(menuItemView.width, LayoutParams.MATCH_PARENT));


			LinearLayout tipPressedContainer=new LinearLayout(context);
			tipPressedContainer.setOrientation(LinearLayout.VERTICAL);
			FrameLayout.LayoutParams fll=new FrameLayout.LayoutParams(countWidth,LayoutParams.MATCH_PARENT);
			fll.setMargins(-currLeftMargin, 0, 0, 0);
			tipPressedContainer.setLayoutParams(fll);
			tipPressedContainer.setClickable(true);

			tipPressedContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(menusMap.get(menuItemView.text)!=null){
						menusMap.get(menuItemView.text).onClick(arg0);
					}
					sDialog.cancel();
				}
			});

			if(tagData!=null){
				tipPressedContainer.setTag(tagData);
			}

			SelfLinearLayout tipPressed=new SelfLinearLayout(context);
			tipPressed.setPressedBackground(R.drawable.android_base_new_tip_pressed);
			tipPressed.setLayoutParams(tipBg.getLayoutParams());
			tipPressed.initStyle();


			SelfLinearLayout tipPressedArrow=new SelfLinearLayout(context);
			tipPressedArrow.setPressedBackground(R.drawable.android_base_new_tip_pressed_arrow);
			tipPressedArrow.setLayoutParams(tipBgArrow.getLayoutParams());
			tipPressedArrow.initStyle();

			tipPressedContainer.addView(tipPressed);
			tipPressedContainer.addView(tipPressedArrow);

			flItem.addView(tipPressedContainer);
			flItem.addView(menuItemView.view);



			llTipMenusContainer.addView(flItem);

			currLeftMargin+=menuItemView.width;
		}


		//llTipMenusContainer.removeViewAt(llTipMenusContainer.getChildCount()-1);
		container.addView(llTipMenusContainer);

		Window win = sDialog.getWindow();
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		layoutParams.x = windowX;
		layoutParams.y = y - (DensityUtils.dip2px(context, tipHeight)+DensityUtils.statusBarHeight2(context));//STATUS_HEIGHT;

		win.setAttributes(layoutParams);
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, PixelFormat.TRANSPARENT);
        sDialog.setContentView(container);
        sDialog.setCanceledOnTouchOutside(true);
		sDialog.show();
        return sDialog;
	}


	static class MenuItemView{
		public String text;
		public int width;
		public View view;
	}


}
