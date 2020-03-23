package com.hx.wjbaseproject;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.hx.wjbaseproject.util.AppUtil;
import com.hx.wjbaseproject.util.Logger;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * @author: 王杰
 * @描述:
 * @文件名: App
 * @包名: com.hxjg.sports
 * @创建时间: 2019-07-23 17:03
 * @修改人: 王杰
 * @公司: 北京和信金谷科技有限公司
 * @备注:
 * @版本号: 1.0.0
 */
public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();

    private static App mInstance;

    public static App ins() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (inMainProcess()) {
            mInstance = this;
            Logger.i(TAG, "----------application onCreate----------");
            AppUtil.getNetIp();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = getProcessName(this);
        return packageName.equals(processName);
    }


    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
