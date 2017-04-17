package self.androidbase.views;

import java.util.LinkedHashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import self.androidbase.R;
import self.androidbase.utils.DensityUtils;

public class SelfPromptView {

	public static Dialog showPromptView(Context context,String title,String hint,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,-1, title, null,hint,-1,true, btns);
	}

	public static Dialog showPromptView(Context context,String title,String defaultContent,String hint,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,-1, title, defaultContent,hint,-1,true, btns);
	}

	public static Dialog showPromptView(Context context,int icon,String title,String defaultContent,String hint,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title, defaultContent,hint,-1,true, btns);
	}

	public static Dialog showPromptView(Context context,int icon,String title,String defaultContent,String hint,boolean isCheckNull,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title, defaultContent,hint,-1,isCheckNull, btns);
	}

	public static Dialog showPromptView(Context context,int icon,String title,String hint,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title,null, hint,-1,true, btns);
	}

	public static Dialog showPromptView(Context context,int icon,String title,String hint,boolean isCheckNull,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title,null, hint,-1,isCheckNull, btns);
	}

	public static Dialog showPromptView(Context context,int icon,String title,String hint,int inputType,final LinkedHashMap<String, OnClickListener> btns){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title,null, hint,inputType,true, btns);
	}

	private static Dialog creatDialog(final Context context,int icon, String title,String defaultContent,
			String hint,int inputType,final boolean isCheckNull, final LinkedHashMap<String, OnClickListener> btns) {
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view=inflater.inflate(R.layout.android_base_view_prompt, null);

		if(icon<0){
			view.findViewById(R.id.android_base_imgIcon).setVisibility(View.GONE);
		}else{
			ImageView imgIcon=(ImageView) view.findViewById(R.id.android_base_imgIcon);
			imgIcon.setImageResource(icon);
		}

		final EditText android_base_etContent=(EditText) view.findViewById(R.id.android_base_etContent);
		if(hint!=null){
			android_base_etContent.setHint(hint);
		}else{
			android_base_etContent.setHint("请输入");
		}

		if(defaultContent!=null){
			android_base_etContent.setText(defaultContent);
		}
		if(inputType!=-1){
			android_base_etContent.setInputType(inputType);
		}


		TextView tvDialogTitle=(TextView) view.findViewById(R.id.android_base_tvDialogTitle);
		tvDialogTitle.setText(title);

		LinearLayout llDialogBtns=(LinearLayout) view.findViewById(R.id.android_base_llDialogBtns);

		final Dialog dialog=new Dialog(context, R.style.SelfDialogStyle);


		if(btns!=null){
			Object[] menuNames= btns.keySet().toArray();
			for (int i = 0; i <menuNames.length; i++) {


				final String menuName=menuNames[i].toString();

				SelfTextView tv=new SelfTextView(context);
				tv.setPressedColor(Color.parseColor("#f0f0f0"));
				tv.setText(menuName);

				tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
				tv.setTextSize(16f);
				tv.setClickable(true);

				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(btns.get(menuName)!=null){
							if(isCheckNull&&(android_base_etContent.getText()==null||android_base_etContent.getText().toString().length()==0)){
								return;
							}
							v.setTag(android_base_etContent.getText());
							btns.get(menuName).onClick(v);

							InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);   
							imm.hideSoftInputFromWindow(android_base_etContent.getWindowToken(),0); 
						}
						dialog.cancel();
					}
				});
				tv.setTextColor(Color.parseColor("#444444"));
				LinearLayout llLine=new LinearLayout(context);
				llLine.setBackgroundColor(Color.parseColor("#999999"));
				if(btns.size()!=2){
					tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dip2px(context, 45)));
					tv.setBackgroundResource(R.drawable.android_base_menu_bg_center);
					if( i==0){
						tv.setBackgroundResource(R.drawable.android_base_menu_bg_bot);
					}


					llLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
				}else{
					llDialogBtns.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(0, DensityUtils.dip2px(context, 45), 0.5f);
					tv.setLayoutParams(llp);

					tv.setBackgroundResource(R.drawable.android_base_menu_bg_bot_right);
					if( i==1){
						tv.setBackgroundResource(R.drawable.android_base_menu_bg_bot_left);
					}

					llLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
				}
				tv.initStye();
				llDialogBtns.addView(llLine,0);
				llDialogBtns.addView(tv,0);
			}

			llDialogBtns.removeViewAt(llDialogBtns.getChildCount()-1);
		}
		dialog.setContentView(view,new LayoutParams((int) DensityUtils.getWidthInPx(context), LayoutParams.MATCH_PARENT));
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {

			}
		});
		dialog.show();

		return dialog;
	}

}
