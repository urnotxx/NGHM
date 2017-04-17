package com.zzx.haoniu.app_nghmuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.mywheeladapter.AbstractWheelTextAdapter;
import com.zzx.haoniu.app_nghmuser.mywheelview.OnWheelChangedListener;
import com.zzx.haoniu.app_nghmuser.mywheelview.OnWheelScrollListener;
import com.zzx.haoniu.app_nghmuser.mywheelview.WheelView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/10/25.
 */
public class StartTimeDialog extends Dialog implements View.OnClickListener, OnWheelScrollListener, OnWheelChangedListener {

    public StartTimeDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_startime);
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.TOP);
        window.setAttributes(lp);
        mContext = context;
        initView();
        initData();
    }

    private List<String> dayList, hourList, minuteList;

    private static Context mContext;
    private OnClickListener cancleListener, confimListener;// 按钮单击监听事件
    private WheelView wheelDay;
    private WheelView wheelHour;
    private WheelView wheelMinute;
    private int mSelectDayIndex;
    private int mSelectHourIndex;
    private int mSelectMinuteIndex;
    private TimeAdapter dayAdapter, hourAdapter, minuteAdapter;

    private void initData() {
        initListData();
        dayAdapter = new TimeAdapter(mContext, getDay(), 0, maxTextSize, minTextSize);
        hourAdapter = new TimeAdapter(mContext, getHour(0), 0, maxTextSize, minTextSize);
        minuteAdapter = new TimeAdapter(mContext, getMinute(0, 0), 0, maxTextSize, minTextSize);
        wheelDay.setVisibleItems(5);
        wheelDay.setViewAdapter(dayAdapter);
        wheelDay.setCurrentItem(0);
        wheelHour.setVisibleItems(5);
        wheelHour.setViewAdapter(hourAdapter);
        wheelHour.setCurrentItem(0);
        wheelMinute.setVisibleItems(5);
        wheelMinute.setViewAdapter(minuteAdapter);
        wheelMinute.setCurrentItem(0);
        wheelDay.addChangingListener(this);
        wheelHour.addChangingListener(this);
        wheelMinute.addChangingListener(this);
        wheelDay.addScrollingListener(this);
        wheelHour.addScrollingListener(this);
        wheelMinute.addScrollingListener(this);
    }

    private void initListData() {
        dayList = new ArrayList<>();
//        dayList.add("今天");
//        dayList.add("明天");
//        dayList.add("后天");
        hourList = new ArrayList<>();
//        for (int i = 0; i < 24; i++) {
//            hourList.add(i + "点");
//        }
        minuteList = new ArrayList<>();
//        for (int i = 0; i < 12; ) {
//            hourList.add(i + "分");
//            i += 5;
//        }
    }

    public String[] getSelectIndex() {
        String selects[] = new String[]{"" + mSelectDayIndex, "" + mSelectHourIndex, "" + mSelectMinuteIndex
                , dayList.get(mSelectDayIndex), hourList.get(mSelectHourIndex), minuteList.get(mSelectMinuteIndex)
        };
        return selects;
    }

    public List<String> getDayList() {
        if (dayList == null)
            dayList = new ArrayList<>();
        return dayList;
    }

    public List<String> getHourList() {
        if (hourList == null)
            hourList = new ArrayList<>();
        return hourList;
    }

    public List<String> getMinuteList() {
        if (minuteList == null)
            minuteList = new ArrayList<>();
        return minuteList;
    }

    private List<String> getDay() {
        if (dayList != null && dayList.size() > 0) {
            return dayList;
        }
        List<String> list = new ArrayList<>();
        list.add("今天");
        list.add("明天");
        list.add("后天");
        return dayList = list;
    }

    private List<String> getHour(int day) {
        List<String> list = new ArrayList<>();
        if (day == 0) {
            Date date = new Date();
            int hours = date.getHours();
            for (int i = hours + 1; i < 24; i++) {
                list.add(i + "点");
            }
        } else {
            for (int i = 0; i < 24; i++) {
                list.add(i + "点");
            }
        }
        return hourList = list;
    }

    private List<String> getMinute(int day, int hour) {
        List<String> list = new ArrayList<>();
        if (day == 0 && hour == 0) {
            Date date = new Date();
            int minutes = date.getMinutes();
            for (int i = minutes; i < 60; ) {
                list.add(i + "分");
                i += 5;
            }
        } else {
            for (int i = 0; i < 60; ) {
                list.add(i + "分");
                i += 5;
            }
        }
        return minuteList = list;
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        switch (wheel.getId()) {
            case R.id.wheelDay:
                String currentText = (String) dayAdapter.getItemText(wheelDay.getCurrentItem());
                setTextviewSize(currentText, dayAdapter);
                mSelectDayIndex = newValue;
                hourAdapter = new TimeAdapter(mContext, getHour(mSelectDayIndex), 0, maxTextSize, minTextSize);
                wheelHour.setViewAdapter(hourAdapter);
                wheelHour.setCurrentItem(0);
                mSelectHourIndex = 0;
                minuteAdapter = new TimeAdapter(mContext, getMinute(mSelectDayIndex, mSelectHourIndex), 0, maxTextSize, minTextSize);
                wheelMinute.setViewAdapter(minuteAdapter);
                wheelMinute.setCurrentItem(0);
                mSelectMinuteIndex = 0;
                break;
            case R.id.wheelHour:
                String currentText1 = (String) hourAdapter.getItemText(wheelHour.getCurrentItem());
                setTextviewSize(currentText1, hourAdapter);
                mSelectHourIndex = newValue;
                minuteAdapter = new TimeAdapter(mContext, getMinute(mSelectDayIndex, mSelectHourIndex), 0, maxTextSize, minTextSize);
                wheelMinute.setViewAdapter(minuteAdapter);
                wheelMinute.setCurrentItem(0);
                mSelectMinuteIndex = 0;
                break;
            case R.id.wheelMinute:
                String currentText2 = (String) minuteAdapter.getItemText(wheelMinute.getCurrentItem());
                setTextviewSize(currentText2, minuteAdapter);
                mSelectMinuteIndex = newValue;
                break;
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        switch (wheel.getId()) {
            case R.id.wheelDay:
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, dayAdapter);
                break;
            case R.id.wheelHour:
                String currentText1 = (String) hourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText1, hourAdapter);
                break;
            case R.id.wheelMinute:
                String currentText2 = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText2, minuteAdapter);
                break;
        }
    }

    private void initView() {
        findViewById(R.id.tvCancle).setOnClickListener(this);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        wheelDay = (WheelView) findViewById(R.id.wheelDay);
        wheelHour = (WheelView) findViewById(R.id.wheelHour);
        wheelMinute = (WheelView) findViewById(R.id.wheelMinute);
    }

    /**
     * 设置确定取消选择按钮
     */
    public void setButton(OnClickListener cancleListener, OnClickListener confimListener) {
        this.cancleListener = cancleListener;
        this.confimListener = confimListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancle:
                if (cancleListener != null) {
                    cancleListener.onClick(this, 0);
                }
                break;
            case R.id.tvConfirm:
                if (confimListener != null) {
                    confimListener.onClick(this, 0);
                }
                break;
        }
    }

    private int maxTextSize = 24;
    private int minTextSize = 18;

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }


    private class TimeAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected TimeAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, TimeAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }
}
