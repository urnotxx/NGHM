package com.zzx.haoniu.hzcxkuaiche.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.view.ColorAnimationView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    private static String[] mImages = new String[]{"http://i-7.vcimg.com/trim/08f0ad123748513a5e9734ea37f18139328349/trim.jpg",
            "http://5.26923.com/download/pic/000/327/09213402b15c69c15b0349c484f23e42.jpg",
            "http://img4q.duitang.com/uploads/item/201412/20/20141220110030_sHPRz.png",
            "http://dl.bizhi.sogou.com/images/2014/07/10/719831.jpg", "http://h.hiphotos.baidu.com/zhidao/pic/item/d058ccbf6c81800a8ed6afccb03533fa838b47c4.jpg",
            "http://img4q.duitang.com/uploads/item/201412/20/20141220110030_sHPRz.png"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        MyFragmentStatePager adpter = new MyFragmentStatePager(getSupportFragmentManager());
        ColorAnimationView colorAnimationView = (ColorAnimationView) findViewById(R.id.ColorAnimationView);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adpter);
        /**
         *  首先，你必须在 设置 Viewpager的 adapter 之后在调用这个方法
         *  第二点，setmViewPager(ViewPager mViewPager,Object obj, int count, int... colors)
         *         第一个参数 是 你需要传人的 viewpager
         *         第二个参数 是 一个实现了ColorAnimationView.OnPageChangeListener接口的Object,用来实现回调
         *         第三个参数 是 viewpager 的 孩子数量
         *         第四个参数 int... colors ，你需要设置的颜色变化值~~ 如何你传人 空，那么触发默认设置的颜色动画
         * */
        /**
         *  Frist: You need call this method after you set the Viewpager adpter;
         * Second: setmViewPager(ViewPager mViewPager,Object obj， int count, int... colors)
         *          so,you can set any length colors to make the animation more cool!
         * Third: If you call this method like below, make the colors no data, it will create
         *          a change color by default.
         * */
        colorAnimationView.setmViewPager(viewPager, mImages.length);
        colorAnimationView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("TAG", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TAG", "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("TAG", "onPageScrollStateChanged");
            }
        });
    }

    public class MyFragmentStatePager
            extends FragmentStatePagerAdapter {

        public MyFragmentStatePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new MyFragment(position);
        }

        @Override
        public int getCount() {
            return mImages.length;
        }
    }

    @SuppressLint("ValidFragment")
    public static class MyFragment
            extends Fragment {
        private int position;

        public MyFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ImageView imageView = new ImageView(getActivity());
//            imageView.setImageResource(resource[position]);
            ImageLoader.getInstance().displayImage(mImages[position], imageView);
            return imageView;
        }
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
