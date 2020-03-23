package com.hx.wjbaseproject.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import okhttp3.HttpUrl;

/**
 * Created by xiaoming on 16/7/24.
 */
public class HttpUtil {

    /**
     * 获取排好序的键值对
     *
     * @param url
     * @return
     */
    public static String getSortKV(HttpUrl url) {
        Set<String> paraSet = url.queryParameterNames();
        StringBuilder stringBuilder = new StringBuilder();
        if (paraSet != null && paraSet.size() > 0) {
            ArrayList<String> paraList = new ArrayList<>(paraSet);
            Collections.sort(paraList);
            for (int i = 0; i < paraList.size(); i++) {
                String para = paraList.get(i);
                String value = url.queryParameter(para);
                stringBuilder.append(para);
                stringBuilder.append("=");
                stringBuilder.append(value);
                if (i < (paraList.size() - 1)) {
                    stringBuilder.append("&");
                }
            }
        }
        return stringBuilder.toString();
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
