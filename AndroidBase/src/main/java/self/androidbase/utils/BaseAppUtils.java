package self.androidbase.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "SimpleDateFormat", "InlinedApi", "NewApi" })
public class BaseAppUtils {
	static Map<String, String> headers=new LinkedHashMap<String, String>();

	public static void addHttpHeader(String key,String value){
		headers.put(key, value);
	}

	/**
	 * 获得手机剩余内容的大小 单位 kb
	 * @param context
	 * @return
	 */
	public static int getRemainMemory(Context context){
		ActivityManager am = (ActivityManager) ((Activity)context).getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);

		int MEM_UNUSED = (int) (mi.availMem / 1024);
		return MEM_UNUSED;
	}

	/**
	 * 获得手机 总内存
	 * @return
	 */
	public static long getmem_TOLAL() {
		long mTotal;
		// /proc/meminfo读出的内核信息进行解释
		String path = "/proc/meminfo";
		String content = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path), 8);
			String line;
			if ((line = br.readLine()) != null) {
				content = line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		int begin = content.indexOf(':');
		int end = content.indexOf('k');
		content = content.substring(begin + 1, end).trim();
		mTotal = Integer.parseInt(content);
		return mTotal;
	}



	/**
	 * 清理内存
	 * @param context
	 */
	public static  void clear(Context context){
		ActivityManager activityManger=(ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list=activityManger.getRunningAppProcesses();
		if(list!=null)
			for(int i=0;i<list.size();i++)
			{
				ActivityManager.RunningAppProcessInfo apinfo=list.get(i);

				String[] pkgList=apinfo.pkgList;

				if(apinfo.importance>ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE)
				{
					for(int j=0;j<pkgList.length;j++)
					{
						activityManger.killBackgroundProcesses(pkgList[j]);
					}
				}
			}
	}


	/**
	 * 格式化 单位为毫秒的为 yyyy-MM-dd HH:mm
	 * @param millis
	 * @return
	 */
	public static String formatDataInfoByMillis(long millis){
		Date dt=new Date(millis);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formatDateInfo(sdf.format(dt));

	}

	/**
	 * 格式化 日期的文字描述
	 * @return
	 */
	public static String formatDateInfo(String dateTime){
		if(dateTime!=null&&dateTime.length()!=0){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Calendar cd=Calendar.getInstance();
			try {
				cd.setTime(sdf.parse(dateTime));
				long minu=(System.currentTimeMillis()-cd.getTimeInMillis())/1000/60;



				if(minu==0){
					return "刚刚";
				}

				if(minu<60&&minu>0){

					return minu+"分钟前";
				}else{
					if(minu<12*60&&minu>0){//12个小时内
						return minu/60+"小时"+minu%60+"分钟前";
					}

					SimpleDateFormat hm=new SimpleDateFormat("HH:mm");
					if(cd.get(Calendar.DATE)==Calendar.getInstance().get(Calendar.DATE)){//今天
						return "今天 "+hm.format(sdf.parse(dateTime));
					}else if(cd.get(Calendar.DATE)+1==Calendar.getInstance().get(Calendar.DATE)){//昨天
						return "昨天 "+hm.format(sdf.parse(dateTime));
					}else if(cd.get(Calendar.DATE)+2==Calendar.getInstance().get(Calendar.DATE)){//前天
						return "前天 "+hm.format(sdf.parse(dateTime));
					}
				}
				return sdf.format(sdf.parse(dateTime));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 将文件转换byte[]
	 * @param filePath
	 * @return
	 */
	public static byte[] convertFileToBytes(String filePath){
		File fl=new File(filePath);
		if(fl.exists()){
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			try {
				FileInputStream fis=new FileInputStream(fl);
				byte bt[] = new byte[1024];
				int c;
				while ((c = fis.read(bt)) > 0) {
					baos.write(bt, 0, c); //将内容写到新文件当中
				}
				fis.close();
				return baos.toByteArray();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	/**
	 * 读取access文件下的内容
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readFromAssets(Context context,String fileName){ 
		try { 
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName),Charset.forName("utf-8")); 
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return null;
	}



	/**
	 * 运行安装包APK
	 * @param file
	 */
	public static void excuteFile(Context context,File file){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 运行安装包APK
	 */
	public static void excuteFile(Context context,Uri uri){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri,
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}


	/**
	 * 保存文件
	 * @param bitmap
	 * @param path
	 */
	public static void saveBitmapToPath(Bitmap bitmap,String path){
		File file=new File(path);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		FileOutputStream fos=null;
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			fos=new FileOutputStream(path);
			fos.write(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(baos!=null){
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(bitmap!=null){
				bitmap.recycle();
			}
		}

	}
	/**  
	 * 旋转图片  
	 * @param angle  
	 * @param bitmap  
	 * @return Bitmap  
	 */    
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {    
		try {
			//旋转图片 动作     
			Matrix matrix = new Matrix();;    
			matrix.postRotate(angle);    
			// 创建新的图片     
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,    
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);    
			return resizedBitmap;
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch(Exception e){
		}
		return bitmap;
	}

	// 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}
	/**
	 * 生成缩略图
	 * @return
	 */
	public static Bitmap decodeFileByCompress(String filePath,Context context){
		File fl=new File(filePath);
		if(fl.exists()){
			Options op = new Options();
			op.inSampleSize =10;
			op.inJustDecodeBounds = false;
			Bitmap bitmap = BaseAppUtils.zoomImg(BitmapFactory.decodeFile(filePath,op),DensityUtils.dip2px(context, 40), DensityUtils.dip2px(context, 40));
			return bitmap;
		}
		return null;
	}


	/**
	 * 拷贝文件
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean copyFile(String oldPath, String newPath) {   
		File file=new File(oldPath);
		if(file.exists()){
			try {
				File newFile=new File(newPath);
				if(!newFile.getParentFile().exists()){
					newFile.getParentFile().mkdirs();
				}
				FileInputStream fosfrom = new FileInputStream(file);
				FileOutputStream fosto = new FileOutputStream(newFile);
				byte bt[] = new byte[1024];
				int c;
				while ((c = fosfrom.read(bt)) > 0) {
					fosto.write(bt, 0, c); //将内容写到新文件当中
				}

				fosfrom.close();
				fosto.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}   


	/**
	 * 拷贝文件
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean copyFile(String oldPath, String newPath,int width,int height) {   
		File file=new File(oldPath);
		if(file.exists()){
			try {
				saveBitmapToPath(zoomImg(BitmapFactory.decodeFile(oldPath), width, width), newPath);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return false;
	}  

	/**
	 * 获得编辑的框的值
	 * @param edite
	 * @return
	 */
	public static String getEditeValue(View edite){
		if(edite instanceof EditText){
			EditText et=(EditText) edite;
			if(et.getText()!=null){
				return et.getText().toString();
			}
		}

		if(edite instanceof TextView){
			TextView et=(TextView) edite;
			if(et.getText()!=null){
				return et.getText().toString();
			}
		}
		return "";
	}


	/**
	 * 设置控件的margin
	 * @param view
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	public static void setMargins(View view,int l,int t,int r,int b) {
		if(view.getLayoutParams() instanceof FrameLayout.LayoutParams){
			FrameLayout.LayoutParams mLayoutPrams=(FrameLayout.LayoutParams)view.getLayoutParams();
			mLayoutPrams.setMargins(l, t , r, b);
			view.setLayoutParams(mLayoutPrams);
		}else if(view.getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams)view.getLayoutParams();
			mLayoutPrams.setMargins(l, t , r, b);
			view.setLayoutParams(mLayoutPrams);
		}
	}

	/**
	 * 设置控件的margin
	 * @param view
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 * @param width
	 * @param height
	 */
	public static void setMargins(View view,int l,int t,int r,int b,int width,int height) {
		if(view.getLayoutParams() instanceof FrameLayout.LayoutParams){
			FrameLayout.LayoutParams mLayoutPrams=(FrameLayout.LayoutParams)view.getLayoutParams();
			mLayoutPrams.width=width;
			mLayoutPrams.height=height;
			mLayoutPrams.setMargins(l, t , r, b);
			view.setLayoutParams(mLayoutPrams);
		}else if(view.getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams)view.getLayoutParams();
			mLayoutPrams.width=width;
			mLayoutPrams.height=height;
			mLayoutPrams.setMargins(l, t , r, b);
			view.setLayoutParams(mLayoutPrams);
		}
	}


	/**
	 * 获得控件的margin
	 * @param view
	 * @return
	 */
	public static Rect getMargins(View view) {
		Rect rect=new Rect();
		if(view.getLayoutParams() instanceof FrameLayout.LayoutParams){
			FrameLayout.LayoutParams mLayoutPrams=(FrameLayout.LayoutParams) view.getLayoutParams();
			rect.set(mLayoutPrams.leftMargin, mLayoutPrams.topMargin, mLayoutPrams.rightMargin, mLayoutPrams.bottomMargin);
		}else if(view.getLayoutParams() instanceof LinearLayout.LayoutParams){
			LinearLayout.LayoutParams mLayoutPrams=(LinearLayout.LayoutParams) view.getLayoutParams();
			rect.set(mLayoutPrams.leftMargin, mLayoutPrams.topMargin, mLayoutPrams.rightMargin, mLayoutPrams.bottomMargin);
		}
		return rect;
	}


	/**
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(Options options,
			int reqWidth, int reqHeight) {  
		final int height = options.outHeight;  
		final int width = options.outWidth;  
		int inSampleSize = 1;  

		if (height > reqHeight || width > reqWidth) {  
			if (width > height) {  
				inSampleSize = Math.round((float) height / (float) reqHeight);  
			} else {  
				inSampleSize = Math.round((float) width / (float) reqWidth);  
			}  

		}
		return inSampleSize;  
	}

	/**
	 * 获得资源文件的Uri
	 * @param context
	 * @param res
	 * @return
	 */
	public static Uri getResourceUri(Context context, int res) {
		try {
			Context packageContext = context.createPackageContext(context.getPackageName(),
					Context.CONTEXT_RESTRICTED);
			Resources resources = packageContext.getResources();
			String appPkg = packageContext.getPackageName();
			String resPkg = resources.getResourcePackageName(res);
			String type = resources.getResourceTypeName(res);
			String name = resources.getResourceEntryName(res);

			Uri.Builder uriBuilder = new Uri.Builder();
			uriBuilder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE);
			uriBuilder.encodedAuthority(appPkg);
			uriBuilder.appendEncodedPath(type);
			if (!appPkg.equals(resPkg)) {
				uriBuilder.appendEncodedPath(resPkg + ":" + name);
			} else {
				uriBuilder.appendEncodedPath(name);
			}
			return uriBuilder.build();
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 获得控件的位图
	 * @param view
	 * @return
	 */
	public static  Bitmap getViewBitmap( View view ){
		view.setDrawingCacheEnabled( true );
		Bitmap bitmap = null;
		try{
			if( null != view.getDrawingCache( ) ){
				bitmap = Bitmap.createScaledBitmap( view.getDrawingCache( ), DensityUtils.getViewSize(view)[0],  DensityUtils.getViewSize(view)[1], false );
			}
		}catch( OutOfMemoryError e ){
			e.printStackTrace( );
		}finally{
			view.setDrawingCacheEnabled( false );
			view.destroyDrawingCache( );
		}

		return bitmap;

	}



	/**
	 * 获得控件的位图
	 * @param view
	 * @return
	 */
	public static  Bitmap getViewBitmap( View view,int width,int height ){
		view.setDrawingCacheEnabled( true );
		Bitmap bitmap = null;
		try{
			if( null != view.getDrawingCache( ) ){
				bitmap = Bitmap.createScaledBitmap(view.getDrawingCache(), width, height, false );
			}
		}catch( OutOfMemoryError e ){
			e.printStackTrace( );
		}finally{
			view.setDrawingCacheEnabled( false );
			view.destroyDrawingCache( );
		}

		return bitmap;

	}



	/**
	 * @Description: 
	 *  判断应用是否安装，传递的是应用的包名
	 * @param targetPackage
	 * @return      
	 * @throws
	 */ 
	public static boolean isPackageExists(Context context,String targetPackage) { 
		List<ApplicationInfo> packages; 
		PackageManager pm; 
		pm = context.getPackageManager(); 
		packages = pm.getInstalledApplications(0); 
		for (ApplicationInfo packageInfo : packages) { 
			if (packageInfo.packageName.equals(targetPackage)) { 
				return true; 
			} 
		} 
		return false; 
	}


	/**
	 * 下载app
	 * @param context
	 * @param url
	 * @param title
	 * @param description
	 */
	public static void downloadFile(Context context,String url,final String title,String description){
		Uri content_url = Uri.parse(url); 
		try {
			if(Build.VERSION.SDK_INT>9){
				final DownloadManager downloadManager = (DownloadManager)context.getSystemService(Activity.DOWNLOAD_SERVICE);
				Request request = new Request(content_url);
				request.setTitle(title);
				request.setDescription(description);
				request.setAllowedNetworkTypes(Request.NETWORK_MOBILE| Request.NETWORK_WIFI);

				final String fileName= url.substring(url.lastIndexOf("/"));

				request.setDestinationInExternalPublicDir("download",fileName);
				request.setVisibleInDownloadsUi(true);

				request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				final long myDownloadReference=downloadManager.enqueue(request);
				Toast.makeText(context,description, Toast.LENGTH_LONG).show();


				IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
				BroadcastReceiver receiver = new BroadcastReceiver() {  
					@Override  
					public void onReceive(Context context, Intent intent) {  
						long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);  
						if (myDownloadReference == reference) {  
							Toast.makeText(context, title+"下载完成！", Toast.LENGTH_LONG).show();
							if(fileName.toLowerCase().contains(".apk")){
								Uri uri=downloadManager.getUriForDownloadedFile(myDownloadReference);
								BaseAppUtils.excuteFile(context, uri);
							}
						}
					}  
				}; 
				context.registerReceiver(receiver, filter);
				return;
			}
		} catch (Exception e) {}

		Intent intent= new Intent();        
		intent.setAction("android.intent.action.VIEW");    
		intent.setData(content_url);  
		context.startActivity(intent);
	}



	/**
	 * 获得程序的名称
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) { 
		PackageManager packageManager = null; 
		ApplicationInfo applicationInfo = null; 
		try { 
			packageManager = context.getApplicationContext().getPackageManager(); 
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0); 
		} catch (PackageManager.NameNotFoundException e) { 
			applicationInfo = null; 
		} 
		String applicationName = 
				(String) packageManager.getApplicationLabel(applicationInfo); 
		return applicationName; 
	}


	/** 
	 * 判断某个界面是否在前台 
	 *  
	 * @param context 
	 * @param className 
	 *            某个界面名称 
	 */  
	public static  boolean isForeground(Context context, String className) {  
		if (context == null || TextUtils.isEmpty(className)) {  
			return false;  
		}  

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningTaskInfo> list = am.getRunningTasks(1);  
		if (list != null && list.size() > 0) {  
			ComponentName cpn = list.get(0).topActivity;  
			if (className.equals(cpn.getClassName())) {  
				return true;  
			}  
		}  
		return false;  
	}  

}
