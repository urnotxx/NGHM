package self.androidbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


/**
 * http请求工具类
 */
public class OKHttpUtils {
    private static final Map<String, String> headers = new HashMap<String, String>();

    public static void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private static OKHttpUtils okHttpUtils;

    public static OKHttpUtils getInstance(Context context) {
        if (okHttpUtils == null) {
            okHttpUtils = new OKHttpUtils();
            okHttpUtils.memoryDataInfo = context.getSharedPreferences("httpLocalData", 0);
        }
        return okHttpUtils;
    }

    public static OKHttpUtils getInstance() {
        if (okHttpUtils == null) {
            okHttpUtils = new OKHttpUtils();
        }
        return okHttpUtils;
    }


    private Handler mHandler;
    private OkHttpClient mOkHttpClient;
    public SharedPreferences memoryDataInfo;

    private OKHttpUtils() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @SuppressLint("NewApi")
    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(30, TimeUnit.MINUTES);
            if (VERSION.SDK_INT >= 9) {
                mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
            }
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return mOkHttpClient;
        }
        return mOkHttpClient.clone();
    }

    /**
     * 执行post 异步请求
     *
     * @param url
     * @param params
     */
    public void post(final String url, final Map<String, Object> params, final HttpCallBack callback) {
        doRequest(url, new AsyncMakeParams() {

            @Override
            public Map<String, Object> doCreateParams() {
                // TODO Auto-generated method stub
                return params;
            }
        }, callback, false, false,false,false);
    }

    /**
     * 执行post 异步请求
     *
     * @param url
     * @param params
     */
    public void post(final String url, final Map<String, Object> params, final HttpCallBack callback, boolean isCache) {
        doRequest(url, new AsyncMakeParams() {

            @Override
            public Map<String, Object> doCreateParams() {
                // TODO Auto-generated method stub
                return params;
            }
        }, callback, isCache, false,false,false);
    }


    /**
     * 执行post 异步请求
     *
     * @param url
     * @param params
     */
    public void post(final String url, final Map<String, Object> params, final HttpCallBack callback, boolean isCache, final boolean isDoRefresh) {
        doRequest(url, new AsyncMakeParams() {

            @Override
            public Map<String, Object> doCreateParams() {
                // TODO Auto-generated method stub
                return params;
            }
        }, callback, isCache, isDoRefresh,false,false);
    }


    /**
     * 执行post 异步请求
     *
     * @param url
     */
    public void post(final String url, final AsyncMakeParams asyncMakeParams, final HttpCallBack callback) {
        doRequest(url, asyncMakeParams, callback, false, false,false,false);
    }


    /**
     * 执行post 异步请求
     *
     * @param url
     * @param params
     */
    public void get(final String url, final Map<String, Object> params, final HttpCallBack callback) {
        doRequest(url, new AsyncMakeParams() {

            @Override
            public Map<String, Object> doCreateParams() {
                // TODO Auto-generated method stub
                return params;
            }
        }, callback, false, false,true,false);
    }


    /**
     * 执行post 异步请求
     *
     * @param url
     */
    public void get(final String url, final HttpCallBack callback) {
        doRequest(url, new AsyncMakeParams() {

            @Override
            public Map<String, Object> doCreateParams() {
                // TODO Auto-generated method stub
                return null;
            }
        }, callback, false, false,true,false);
    }


    /**
     * 执行post 异步请求
     *
     * @param url
     */
    public void get(final String url, final HttpCallBack callback,boolean isSynchronize) {
        doRequest(url, new AsyncMakeParams() {

            @Override
            public Map<String, Object> doCreateParams() {
                // TODO Auto-generated method stub
                return null;
            }
        }, callback, false, false,true,isSynchronize);
    }


    /**
     * 执行post 异步请求
     *
     * @param url
     */
    public void doRequest(final String url, final AsyncMakeParams asyncMakeParams, final HttpCallBack callback, final boolean isCache, final boolean isDoRefresh,final boolean isGet,boolean isSynchronize) {
        if (callback == null) {
            throw new RuntimeException("Callback must not null.");
        }
        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, Object> params = asyncMakeParams.doCreateParams();
                    if(params==null)params=new HashMap<>();
                    Request.Builder request =null;
                    final String cacheKey = MD5Encrypt.MD5(url + params.toString());
                    if (memoryDataInfo != null) {
                        final String cacheHtml = memoryDataInfo.getString(cacheKey, null);
                        if (isCache && cacheHtml != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    callback.onResult(true, cacheHtml);
                                }
                            });
                            if (!isDoRefresh) {
                                return;
                            }
                        }
                    }

                    if (isGet) {
                        if (params.size() > 0) {
                            request = new Request.Builder()
                                    .url(url + "?" + convertToGet(params))
                                    .tag(cacheKey);
                        } else {
                            request = new Request.Builder()
                                    .url(url)
                                    .tag(cacheKey);
                        }
                    }else{
                        MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
                        FormEncodingBuilder formEncodingBuilder=new FormEncodingBuilder();
                        Set<String> keys = params.keySet();

                        boolean haveFile=false;
                        for (String key : keys) {
                            Object value = params.get(key);
                            if(value==null)continue;
                            if (value instanceof File) {
                                File file = (File) value;
                                multipartBuilder.addPart(Headers.of(
                                                "Content-Disposition",
                                                "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                                        RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                                haveFile=true;
                            } else if (value instanceof File[]) {
                                File[] files = (File[]) value;
                                for (File file : files) {
                                    multipartBuilder.addPart(Headers.of(
                                                    "Content-Disposition",
                                                    "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                                            RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                                    haveFile=true;
                                }
                            } else {
                                multipartBuilder.addPart(
                                        Headers.of("Content-Disposition", "form-data; name=\""+key+"\""),
                                        RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), value.toString()));
                                formEncodingBuilder.add(key, value.toString());
                            }
                        }

                        if(params.size()>0){

                            if (haveFile) {//如果有附件必须使用 multipart/form-data
                                request = new Request.Builder()
                                        .url(url)
                                        .tag(cacheKey)
                                        .post(multipartBuilder.build());

                            }else{
                                request = new Request.Builder()
                                        .url(url)
                                        .tag(cacheKey)
                                        .post(formEncodingBuilder.build());

                            }
                        }else{
                            request = new Request.Builder()
                                    .url(url)
                                    .post(null)
                                    .tag(cacheKey);
                        }
                    }

                    Set<String> headerKeys = headers.keySet();
                    for (String string : headerKeys) {
                        request.addHeader(string, headers.get(string));
                    }
                    final Response response = getOkHttpClient().newCall(request.build()).execute();
                    final String html = response.body().string();

                    response.body().close();

                    if (response.isSuccessful()) {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                callback.onResult(true, html);
                                if (isCache&&memoryDataInfo!=null) {
                                    SharedPreferences.Editor editor = memoryDataInfo.edit();
                                    editor.putString(cacheKey, html);
                                    editor.commit();
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                callback.onResult(false, "请求失败，请稍后重试！" + response.message());
                            }
                        });
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            if (e instanceof ConnectException) {
                                callback.onResult(false, "请求断开，请检查您的网络连接是否正常！");
                            } else if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                callback.onResult(false, "请求超时，请稍后重试！");
                            } else {
                                callback.onResult(false, "请求失败，发生未知错误！");
                            }
                        }
                    });
                }
            }
        };
        if(isSynchronize){
            runnable.run();
        }else{
            new Thread(runnable).start();
        }
    }


    /**
     * 下载文件支持断点下载
     *
     * @param url
     * @param callback
     */
    public void downFile(Context context, String url, final HttpDownFileCallBack callback) {
        downFile(context, url, null, callback);
    }

    /**
     * 下载文件支持断点下载
     *
     * @param url
     * @param callback
     */
    public void downFile(Context context, String url, String localPath, final HttpDownFileCallBack callback) {
        if (localPath == null) {
            localPath = context.getFilesDir() + "/DownFiles/" + MD5Encrypt.MD5(url);
        }
        final File doneFile = new File(localPath);
        if (doneFile.exists()) {//如果存在已经下载完成的文件
            if (callback != null) {
                callback.onDownLoad(doneFile.length(), doneFile.length(), doneFile.getPath());
            }
            return;
        }

        final File tempFile = new File(localPath + "temp");
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }

        Request.Builder builder = new Request.Builder().url(url);
        Set<String> headerKeys = headers.keySet();
        for (String string : headerKeys) {
            builder.addHeader(string, headers.get(string));
        }
        //断点下载
        builder.addHeader("RANGE", "bytes=" + String.valueOf(tempFile.length()) + "-");
        getOkHttpClient().newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e instanceof ConnectException) {
                    callback.onDownFail("请求断开，请检查您的网络连接是否正常！");
                } else if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                    callback.onDownFail("请求超时，请稍后重试！");
                } else {
                    callback.onDownFail("请求失败，发生未知错误！");
                }
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final long tempLength = tempFile.length();
                final long countLength = response.body().contentLength() + tempLength;

                RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw");
                randomAccessFile.seek(tempLength);
                InputStream inputStream = response.body().byteStream();
                byte[] buffer = new byte[512];
                int readLength = -1;
                while ((readLength = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, readLength);
                    //下载完毕
                    if (randomAccessFile.length() >= countLength) {
                        tempFile.renameTo(doneFile);
                    }
                    //是否继续下载
                    if (callback != null && !callback.onDownLoad(countLength, randomAccessFile.length(), doneFile.getPath())) {
                        break;
                    }
                }
                randomAccessFile.close();
                inputStream.close();
            }
        });
    }

    /**
     * 删除指定缓存
     */
    public void deleteCache(String url, Map<String, Object> params) {
        if (memoryDataInfo != null) {
            SharedPreferences.Editor editor = memoryDataInfo.edit();
            editor.remove(MD5Encrypt.MD5(url + params.toString()));
            editor.commit();
        }
    }


    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 将参数转换成get形式
     * @param params
     * @return
     */
    private String convertToGet(Map<String, Object> params){
        List<String> list=new ArrayList<String>();
        Set<String> keys=params.keySet();
        for (String key : keys) {
            list.add(key+"="+params.get(key));
        }
        return StringUtils.join(list,"&");
    }


    public interface HttpCallBack {
        void onResult(boolean success, String response);
    }

    public interface HttpDownFileCallBack {
        boolean onDownLoad(long countLength, long downLength, String localPath);

        void onDownFail(String message);
    }

    /**
     * 异步生成参数,适用于文件处理，在主线程的话，会有卡顿现象
     *
     * @author Janesen
     */
    public interface AsyncMakeParams {
        public Map<String, Object> doCreateParams();
    }

}

