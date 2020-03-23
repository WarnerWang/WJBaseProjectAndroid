package com.hx.wjbaseproject.api.helper;

import android.text.TextUtils;

import com.hx.wjbaseproject.App;
import com.hx.wjbaseproject.BuildConfig;
import com.hx.wjbaseproject.R;
import com.hx.wjbaseproject.api.bean.BaseReq;
import com.hx.wjbaseproject.api.token.TokenCache;
import com.hx.wjbaseproject.manager.UserManage;
import com.hx.wjbaseproject.util.AppUtil;
import com.hx.wjbaseproject.util.DeviceUtils;


public class CommonHeaderHelper {
//    private static volatile CommonHeaderHelper ins;
//
//    private CommonHeaderHelper() {
//    }
//
//    public static CommonHeaderHelper ins() {
//        if (ins == null) {
//            synchronized (CommonHeaderHelper.class) {
//                if (ins == null) {
//                    ins = new CommonHeaderHelper();
//                }
//            }
//        }
//        return ins;
//    }

    public static BaseReq.HeaderBean genHeader() {
        BaseReq.HeaderBean commonHeader = new BaseReq.HeaderBean();
//        commonHeader.setIp("123.123.110.199");
        commonHeader.setIp(TokenCache.getIns().getNetIpAddress());
        commonHeader.setSource("2");
//        commonHeader.setXingeToken(JPushInterface.getRegistrationID(App.ins()));
//        commonHeader.setXingeToken(XGPushUtil.getToken(App.ins()));
        commonHeader.setXingeToken(TokenCache.getIns().getXingeToken());
        commonHeader.setDeviceType(2);
        commonHeader.setWebViewType(1);

        String buildType = BuildConfig.API_TYPE;
        if (buildType.equals("dev") || buildType.equals("test") || buildType.equals("release")) {
            buildType = "system";
        }
        commonHeader.setDownloadSource(buildType);
        String userId = UserManage.ins().getUserId();
        if (TextUtils.isEmpty(userId)) {
            commonHeader.setUserId("");
        } else {
            commonHeader.setUserId(userId);
        }
        commonHeader.setVersion(AppUtil.getVersionName());
        commonHeader.setDeviceId(DeviceUtils.getUniqueId(App.ins()));
        commonHeader.setAndroidId(DeviceUtils.getAndroidId(App.ins()));
        commonHeader.setMac(DeviceUtils.getMac(App.ins()));
        commonHeader.setImei(DeviceUtils.getImei(App.ins()));
        commonHeader.setUa(DeviceUtils.getUA(App.ins()));
        //获取手机厂商
        String carrier= android.os.Build.MANUFACTURER;
        //获取手机型号
        String model= android.os.Build.MODEL;
        commonHeader.setDeviceVersion(carrier+" "+model);

        commonHeader.setAppName(App.ins().getResources().getString(R.string.app_name));
        return commonHeader;
    }


}
