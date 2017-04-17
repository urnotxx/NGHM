package self.androidbase.views;

import java.util.Calendar;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

import self.androidbase.R;
import self.androidbase.utils.DensityUtils;

@SuppressLint("NewApi")
public class SelfDateTimeView {

	public static Dialog selectDateTime(Context context,String title,OnDateTimeConfirm dateTimeConfirm){
		if(context==null){
			return null;
		}
		return creatDialog(context,-1, title,2,dateTimeConfirm);
	}

	public static Dialog selectDateTime(Context context,int icon,String title,OnDateTimeConfirm dateTimeConfirm){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title,2,dateTimeConfirm);
	}

	public static Dialog selectDate(Context context,String title,OnDateTimeConfirm dateTimeConfirm){
		if(context==null){
			return null;
		}
		return creatDialog(context,-1, title,0,dateTimeConfirm);
	}

	public static Dialog selectDate(Context context,int icon,String title,OnDateTimeConfirm dateTimeConfirm){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title,0,dateTimeConfirm);
	}

	
	public static Dialog selectTime(Context context,String title,OnDateTimeConfirm dateTimeConfirm){
		if(context==null){
			return null;
		}
		return creatDialog(context,-1, title,1,dateTimeConfirm);
	}

	public static Dialog selectTime(Context context,int icon,String title,OnDateTimeConfirm dateTimeConfirm){
		if(context==null){
			return null;
		}
		return creatDialog(context,icon, title,1,dateTimeConfirm);
	}

	
	private static Dialog creatDialog(Context context,int icon, String title,int type,final OnDateTimeConfirm dateTimeConfirm) {
		final Dialog dialog=new Dialog(context, R.style.SelfDialogStyle);
		
		final Calendar calendar=Calendar.getInstance();
		final LinkedHashMap<String, OnClickListener> btns=new LinkedHashMap<String, OnClickListener>();
		btns.put("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(dateTimeConfirm!=null){
					dateTimeConfirm.selectedDateTime(calendar);
				}
			}
		});
		btns.put("取消", null);

		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view=inflater.inflate(R.layout.android_base_view_meun_dialog, null);

		if(icon<0){
			view.findViewById(R.id.android_base_imgIcon).setVisibility(View.GONE);
		}else{
			ImageView imgIcon=(ImageView) view.findViewById(R.id.android_base_imgIcon);
			imgIcon.setImageResource(icon);
		}

		TextView tvDialogContent=(TextView) view.findViewById(R.id.android_base_tvDialogContent);
		tvDialogContent.setVisibility(View.GONE);



		final View datetime=inflater.inflate(R.layout.android_base_view_datetime, null);

		
		
		DatePicker datePicker=(DatePicker) datetime.findViewById(R.id.android_base_datePicker);
		
		if(type==0||type==2){
			int year=calendar.get(Calendar.YEAR);
			int monthOfYear=calendar.get(Calendar.MONTH);
			int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

			final TextView android_base_date=(TextView) datetime.findViewById(R.id.android_base_date);
			android_base_date.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
			datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){

				public void onDateChanged(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					android_base_date.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
					calendar.set(year, monthOfYear, dayOfMonth);
				}
			});
			datetime.findViewById(R.id.android_base_date).setVisibility(View.VISIBLE);
		}else{
			datetime.findViewById(R.id.android_base_date).setVisibility(View.GONE);
			datePicker.setVisibility(View.GONE);
		}

		TimePicker timePicker= (TimePicker) datetime.findViewById(R.id.android_base_timePicker);
		if(type==1||type==2){
			datetime.findViewById(R.id.android_base_time).setVisibility(View.VISIBLE);
			timePicker.setIs24HourView(true);
			timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

				@Override
				public void onTimeChanged(TimePicker arg0, int h, int m) {
					// TODO Auto-generated method stub
					TextView android_base_date=(TextView) datetime.findViewById(R.id.android_base_time);
					android_base_date.setText(h+"时"+m+"分");
					calendar.set(Calendar.HOUR_OF_DAY, h);
					calendar.set(Calendar.MINUTE, m);
					
				}
			});
			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			
		}else{
			datetime.findViewById(R.id.android_base_time).setVisibility(View.GONE);
			timePicker.setVisibility(View.GONE);
		}

		LinearLayout android_base_otherViewContainer=(LinearLayout) view.findViewById(R.id.android_base_otherViewContainer);
		android_base_otherViewContainer.addView(datetime);


		TextView tvDialogTitle=(TextView) view.findViewById(R.id.android_base_tvDialogTitle);
		tvDialogTitle.setText(title);


		if(btns!=null){
			LinearLayout llDialogBtns=(LinearLayout) view.findViewById(R.id.android_base_llDialogBtns);

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
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(btns.get(menuName)!=null){
							btns.get(menuName).onClick(arg0);
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



	
	public interface OnDateTimeConfirm{
		public void selectedDateTime(Calendar calendar);
	}



}
