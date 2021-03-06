package com.zzx.haoniu.hzcxdj.storage;



import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.http.LoginCallback;
import com.zzx.haoniu.hzcxdj.http.UserInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;


public class Storage {
    /**
     * 清除缓存用户信息数据
     */
    public static void ClearUserInfo() {
        new File(AppContext.getInstance().getFilesDir().getPath() + "/"
                + "hzcxdj_userinfo.bean").delete();
    }

    /**
     * 清除缓存用户信息数据
     */
    public static void ClearLoginCallback() {
        new File(AppContext.getInstance().getFilesDir().getPath() + "/"
                + "hzcxdj_loginCallback.bean").delete();
    }

    /**
     * 保存用户信息至缓存
     *
     * @param loginCallback
     */
    public static void saveLoginCallback(LoginCallback loginCallback) {
        try {
            // L.d("TAG", HXAppContext.getInstance().getCacheDir().getPath() +
            // "/"
            // + "yfb_userinfo.bean");
            new ObjectOutputStream(new FileOutputStream(new File(AppContext
                    .getInstance().getFilesDir().getPath()
                    + "/" + "hzcxdj_loginCallback.bean"))).writeObject(loginCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户信息至缓存
     *
     * @param loginCallback
     */
    public static void saveUsersInfo(UserInfo loginCallback) {
        try {
            new ObjectOutputStream(new FileOutputStream(new File(AppContext
                    .getInstance().getFilesDir().getPath()
                    + "/" + "hzcxdj_userinfo.bean"))).writeObject(loginCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存数据
     *
     * @return
     */
    public static UserInfo GetUserInfo() {
        File file = new File(AppContext.getInstance().getFilesDir().getPath()
                + "/" + "hzcxdj_userinfo.bean");
        if (!file.exists())
            return null;

        if (file.isDirectory())
            return null;

        if (!file.canRead())
            return null;

        try {
            @SuppressWarnings("resource")
            UserInfo userInfo = (UserInfo) new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(file)))
                    .readObject();
            return userInfo;
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取缓存数据
     *
     * @return
     */
    public static LoginCallback GetLoginCallback() {
        File file = new File(AppContext.getInstance().getFilesDir().getPath()
                + "/" + "hzcxdj_loginCallback.bean");
        if (!file.exists())
            return null;

        if (file.isDirectory())
            return null;

        if (!file.canRead())
            return null;

        try {
            @SuppressWarnings("resource")
            LoginCallback userInfo = (LoginCallback) new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(file)))
                    .readObject();
            return userInfo;
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存registrationId至缓存
     *
     * @param registrationId
     */
    public static void saveRegistrationId(String registrationId) {
        try {
            new ObjectOutputStream(new FileOutputStream(new File(AppContext
                    .getInstance().getFilesDir().getPath()
                    + "/" + "hzcxdj_registrationId.bean"))).writeObject(registrationId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存数据
     *
     * @return
     */
    public static String getRegistrationId() {
        File file = new File(AppContext.getInstance().getFilesDir().getPath()
                + "/" + "hzcxdj_registrationId.bean");
        if (!file.exists())
            return null;
        if (file.isDirectory())
            return null;
        if (!file.canRead())
            return null;
        try {
            @SuppressWarnings("resource")
            String registrationId = (String) new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(file)))
                    .readObject();
            return registrationId;
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清除registrationId信息数据
     */
    public static void ClearRegistrationId() {
        new File(AppContext.getInstance().getFilesDir().getPath() + "/"
                + "hzcxdj_registrationId.bean").delete();
    }
}
