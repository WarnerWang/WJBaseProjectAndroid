package com.hx.wjbaseproject.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hx.wjbaseproject.App;
import com.hx.wjbaseproject.api.token.TokenCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AppUtil {
    public static String getVersionName() {
        // 获取packagemanager的实例
        String version = "";
        PackageManager packageManager = App.ins().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(App.ins().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static void getNetIp(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL infoUrl = null;
                InputStream inStream = null;
                String line = "";
                try {
                    infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        while ((line = reader.readLine()) != null)
                            strber.append(line + "\n");
                        inStream.close();
                        if (!StringUtils.isEmpty(strber)) {
                            // 从反馈的结果中提取出IP地址
                            int start = strber.indexOf("{");
                            int end = strber.indexOf("}");
                            if (end > 0 && end<strber.length()) {
                                String json = strber.substring(start, end + 1);
                                if (json != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(json);
                                        line = jsonObject.optString("cip");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (!StringUtils.isEmpty(line)) {
                                    Logger.i("getNetIp", line);
                                    TokenCache.getIns().setNetIpAddress(line);
                                }
                            }
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("getNetIp", line+"  ");
                TokenCache.getIns().setNetIpAddress(line);
            }
        }).start();
    }


    /**
     * 获取MetaData信息
     *
     * @param name
     * @param def
     * @return
     */
    public static String getMetaDataValue(Context context, String name,
                                          String def) {
        String value = getMetaDataValue(context, name);
        return (value == null) ? def : value;
    }

    public static String getMetaDataValue(Context context, String name) {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name
                    + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
