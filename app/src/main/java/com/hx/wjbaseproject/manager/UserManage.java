package com.hx.wjbaseproject.manager;

import android.text.TextUtils;

import com.hx.wjbaseproject.api.token.TokenCache;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserManage {
    public enum LoginType {
        Phone, WeChat
    }

    private static volatile UserManage ins;
    private boolean ignoreUpdate;

    private UserManage() {
    }

    public static UserManage ins() {
        if (ins == null) {
            synchronized (UserManage.class) {
                if (ins == null) {
                    ins = new UserManage();
                }
            }
        }
        return ins;
    }

    public static boolean isLoggedIn() {
        String appToken = TokenCache.getIns().getAppToken();
        return !TextUtils.isEmpty(appToken);
    }

    /**
     * 判断是否登录
     * @return
     */
    public static boolean isLogin(){
        return false;
    }

    public String getUserId() {
        return null;
    }

    public boolean isIgnoreUpdate() {
        return ignoreUpdate;
    }

    public void setIgnoreUpdate(boolean ignoreUpdate) {
        this.ignoreUpdate = ignoreUpdate;
    }

}
