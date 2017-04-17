package self.androidbase.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import self.androidbase.R;
import self.androidbase.extend.SelfAnimationListener;
import self.androidbase.utils.FileUtils;
import self.androidbase.utils.ImageUtils;
import uk.co.senab.photoview.PhotoView;

public class CameraActivity extends Activity implements SurfaceHolder.Callback{

	private boolean isStop;
	private Camera camera;
	private SurfaceView sufaceView;
	private Handler handler=new Handler();
	private Context context;

	private Bitmap bitmap=null;
	private String imgPath="";
	private int imgWidth;
	private int imgHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_base_tool_camera);
		DisplayMetrics dm = new DisplayMetrics();   
		getWindowManager().getDefaultDisplay().getMetrics(dm);   

		context=this;

		sufaceView=(SurfaceView) findViewById(R.id.android_base_surfaceView);
		sufaceView.getHolder().addCallback(this);
		sufaceView.getHolder().setKeepScreenOn(true);  
		sufaceView.setFocusable(true);

		if(getIntent().getExtras()!=null){
			imgPath=getIntent().getExtras().getString("imgPath");

			imgHeight=getIntent().getExtras().getInt("imgHeight");
			imgWidth=getIntent().getExtras().getInt("imgWidth");
		}
	}

	public final int id=R.id.android_base_llPZ;
	/**
	 * 通讯录项点击事件
	 * @param v
	 */
	public void onClickByCamera(final View v){
		ScaleAnimation scaleA=(ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.android_base_click_scale);
		scaleA.setAnimationListener(new SelfAnimationListener(){
			@Override
			public void onAnimationRepeat(Animation arg0) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							if(v.getId()==R.id.android_base_llBack){
								CameraActivity.this.finish();
							}else if(v.getId()==R.id.android_base_llFocuse){
								if(camera!=null){
									camera.autoFocus(new AutoFocusCallback() {  
										@Override  
										public void onAutoFocus(boolean success, Camera camera) {  
											if(success){  
												camera.cancelAutoFocus();
											}  
										}  
									});
								}
							}else if(v.getId()==R.id.android_base_llNo){
								findViewById(R.id.android_base_bottom_btncontainer).setVisibility(View.VISIBLE);
								findViewById(R.id.android_base_flCamera).setVisibility(View.VISIBLE);
								findViewById(R.id.android_base_flPicture).setVisibility(View.INVISIBLE);
								camera.startPreview();
								isStop=false;
							}else if(v.getId()==R.id.android_base_llPZ){
								isStop=true;
								Toast.makeText(context, "拍照中…", Toast.LENGTH_SHORT).show();
								findViewById(R.id.android_base_bottom_btncontainer).setVisibility(View.GONE);
								
								camera.autoFocus(new AutoFocusCallback() {  
									@Override  
									public void onAutoFocus(boolean success, Camera arg1) {  
										camera.cancelAutoFocus();
										pictureCamera();
									}  
								});
							}else if(v.getId()==R.id.android_base_llYes){
								PhotoView imgZoom=(PhotoView) findViewById(R.id.android_base_imageZoom);
								imgZoom.setImageBitmap(null);
								FileUtils.saveBitmapToPath(bitmap, imgPath,true);
								Intent data=new Intent();
								data.putExtra("imgPath", imgPath);
								setResult(RESULT_OK,data);
								CameraActivity.this.finish();
							}
						}catch (OutOfMemoryError e){
							System.gc();
							Toast.makeText(context, "内存不足，程序已自动清理！请稍后再试！", Toast.LENGTH_LONG).show();
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});
		v.startAnimation(scaleA);
	}


	/** 
	 *   播放系统拍照声音 
	 */  
	public void shootSound()  
	{  
		//		MediaPlayer mediaPlayer01;
		//		mediaPlayer01 = MediaPlayer.create(CameraActivity.this, R.raw.pz);
		//		mediaPlayer01.start();
	} 

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initCamera(holder);

	}


	/**
	 * 初始化相机参数
	 * @param holder
	 */
	@SuppressWarnings("deprecation")
	public void initCamera(SurfaceHolder holder){
		findViewById(R.id.android_base_flCamera).setVisibility(View.VISIBLE);
		findViewById(R.id.android_base_flPicture).setVisibility(View.INVISIBLE);
		try {
			camera = Camera.open();
			Camera.Parameters parameters = camera.getParameters();
			List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
			{
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}
			parameters.setPictureSize(parameters.getSupportedPictureSizes().get(0).width,parameters.getSupportedPictureSizes().get(0).height);
			parameters.setPictureFormat(PixelFormat.JPEG); 
			camera.setDisplayOrientation(90);
			camera.setParameters(parameters);  
			camera.setPreviewDisplay(holder);
			
			startTimerFocus();
			camera.startPreview();
			
		} catch (Exception e) {
			Toast.makeText(context, "对不起，打开相机失败，请检查您的手机是否允许开启相机！", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			CameraActivity.this.finish();
		}
	}
	
	public void startTimerFocus(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					if(!isStop){
						try {
							if(camera!=null){
								handler.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											camera.autoFocus(new AutoFocusCallback() {  
												@Override  
												public void onAutoFocus(boolean success, Camera camera) {  
													if(success){  
														camera.cancelAutoFocus();
													}  
												}  

											});
										} catch (Exception e) {
											isStop=true;
										}
									}
								});
							}
							Thread.sleep(3000);
						} catch (Exception e) {
							isStop=true;
						}

					}
				}
				
			}
		}).start();
	}

	/**
	 * 拍照
	 */
	public void pictureCamera(){
		camera.takePicture(null, null, new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera arg1) {
				// TODO Auto-generated method stub
				//camera.autoFocus(null);
				shootSound();
				int sourceWidth=camera.getParameters().getPictureSize().width;
				camera.stopPreview();
				BitmapFactory.Options options = new BitmapFactory.Options();
				if(sourceWidth>1000){
					options.inSampleSize=3;
				}

				WeakReference<Bitmap> softBitmap=new WeakReference<Bitmap>(BitmapFactory.decodeByteArray(data, 0, data.length,options));

				bitmap = ImageUtils.rotaingImageView(90,softBitmap.get());

				if(imgWidth>0&&imgHeight>0){
					bitmap=ImageUtils.zoomImg(bitmap, imgWidth, imgHeight);
				}

				PhotoView imgZoom=(PhotoView) findViewById(R.id.android_base_imageZoom);
				imgZoom.setImageBitmap(bitmap);

				findViewById(R.id.android_base_flCamera).setVisibility(View.INVISIBLE);
				findViewById(R.id.android_base_flPicture).setVisibility(View.VISIBLE);
				System.gc();
			}
		});
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

		// TODO Auto-generated method stub
		if(camera!=null){
			camera.stopPreview();
			camera.release();
			isStop=true;
			camera=null;
		}
	}
}
