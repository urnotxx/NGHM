package self.androidbase.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import android.support.multidex.MultiDex;
import self.androidbase.R;

/**
 * Created by Administrator on 2016/2/15.
 */
public class SelfAppContext extends Application {
    public static SelfAppContext appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }
    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static SelfAppContext getInstance() {
        if (appContext == null)
            return new SelfAppContext();
        return appContext;
    }

    /**
     * 图片框架初始化
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)).defaultDisplayImageOptions(image_display_options)
                .memoryCacheSize(2 * 1024 * 1024).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions image_display_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.android_base_img_default)
            .showImageForEmptyUri(R.drawable.android_base_img_default)
            .showImageOnFail(R.drawable.android_base_img_default)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
}
