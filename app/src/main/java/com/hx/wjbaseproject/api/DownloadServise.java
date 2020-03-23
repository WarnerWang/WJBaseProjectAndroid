package com.hx.wjbaseproject.api;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hx.wjbaseproject.util.Logger;
import com.hx.wjbaseproject.util.SpUtils;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

public class DownloadServise extends Service {
    private static final String TAG = DownloadServise.class.getSimpleName();
    /**apk 文件存储路径*/
    public static final String SP_DOWNLOAD_PATH = "download.path";

    public static final int HANDLE_DOWNLOAD = 0x001;
    public static final String BUNDLE_KEY_DOWNLOAD_URL = "download_url";
    public static final float UNBIND_SERVICE = 2.0F;

    private Activity activity;
    private DownloadBinder binder;
    private DownloadManager downloadManager;
    private DownloadChangeObserver downloadObserver;
    private BroadcastReceiver downLoadBroadcast;
    private ScheduledExecutorService scheduledExecutorService;

    //下载任务ID
    private long downloadId;
    private String downloadUrl;
    private String downLoadFileName;
    public static OnProgressListener onProgressListener;

    public Handler downLoadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (onProgressListener != null && HANDLE_DOWNLOAD == msg.what) {
                //被除数可以为0，除数必须大于0
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    onProgressListener.onProgress(msg.arg1 / (float) msg.arg2);
                }
            }
            return false;
        }
    });

//    private MyHandler downLoadHandler = new MyHandler(this);
//    static class MyHandler extends Handler {
//        WeakReference weakReference;
//        public MyHandler(Context activity) {
//            weakReference = new WeakReference(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (onProgressListener != null && HANDLE_DOWNLOAD == msg.what) {
//                //被除数可以为0，除数必须大于0
//                if (msg.arg1 >= 0 && msg.arg2 > 0) {
//                    onProgressListener.onProgress(msg.arg1 / (float) msg.arg2);
//                }
//            }
//        }
//    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        downloadUrl = intent.getStringExtra(BUNDLE_KEY_DOWNLOAD_URL);
        downloadApk(downloadUrl);
        return binder;
    }

    /**
     * 下载最新APK
     */
    private void downloadApk(String url) {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadObserver = new DownloadChangeObserver();
        downLoadFileName = url.split("/")[url.split("/").length - 1];
        registerContentObserver();

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "my.apk");
//        request.setDestinationInExternalFilesDir(this, null, "my.apk");
        //创建目录下载
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downLoadFileName);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //request.setMimeType("application/cn.trinea.download.file"); //用于响应点击的打开文件
        //机型适配
        request.setMimeType("application/vnd.android.package-archive");
        //通知栏显示
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setTitle("菠菜汪");
//        request.setDescription("正在下载中...");
//        request.setVisibleInDownloadsUi(true);
//        request.allowScanningByMediaScanner();  //准许被系统扫描到
        downloadId = downloadManager.enqueue(request);
        registerBroadcast(); //下载成功和点击通知栏动作监听
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        /**注册service 广播 1.任务完成时 2.进行中的任务被点击*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(downLoadBroadcast = new DownLoadBroadcast(), intentFilter);
    }

    /**
     * 注销广播
     */
    private void unregisterBroadcast() {
        if (downLoadBroadcast != null) {
            unregisterReceiver(downLoadBroadcast);
            downLoadBroadcast = null;
        }
    }

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        /** observer download change **/
        if (downloadObserver != null) {
            getContentResolver().registerContentObserver(
                    Uri.parse("content://downloads/my_downloads"), false, downloadObserver);
        }
    }

    /**
     * 注销ContentObserver
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    /**
     * 关闭定时器，线程等操作
     */
    private void close() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }

        if (downLoadHandler != null) {
            downLoadHandler.removeCallbacksAndMessages(null);
        }
        unregisterBroadcast();
        unregisterContentObserver();
    }

    /**
     * 发送Handler消息更新进度和状态
     * 将查询结果从子线程中发往主线程（handler方式），以防止ANR
     */
    private void updateProgress() {
        int[] bytesAndStatus = getBytesAndStatus(downloadId);
        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

    /**
     * 绑定此DownloadService的Activity实例
     *
     * @param activity
     */
    public void setTargetActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 接受下载完成广播
     */
    private class DownLoadBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            switch (intent.getAction()) {
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                    if (downloadId == downId && downId != -1 && downloadManager != null) {
                        Uri downIdUri = downloadManager.getUriForDownloadedFile(downloadId);

                        close();
                        if (onProgressListener != null) {
                            onProgressListener.onProgress(UNBIND_SERVICE);
                        }
                        if (downIdUri != null) {
                            Logger.i("path : "+ downIdUri.getPath());
                            SpUtils.getInstance().put(SP_DOWNLOAD_PATH, getRealFilePath(context, downIdUri));
                            Logger.i(TAG, "广播监听下载完成，APK存储路径为 ：" + downIdUri.getPath());
//                            installApk(context, downIdUri);
//                            installApk(context, downLoadFileName);
                            if (onProgressListener != null) {
                                onProgressListener.onDownloadSuccess(downLoadFileName);
                            }
                        }

                    }
                    break;

                case DownloadManager.ACTION_NOTIFICATION_CLICKED:

                    break;
            }
        }
    }

    /**
     * 监听下载进度
     */
    private class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(downLoadHandler);
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        /**
         * 当所监听的Uri发生改变时，就会回调此方法
         * @param selfChange 此值意义不大, 一般情况下该回调值false
         */
        @Override
        public void onChange(boolean selfChange) {
            scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 1, TimeUnit.SECONDS); //在子线程中查询
        }
    }

    public class DownloadBinder extends Binder {
        /**
         * 返回当前服务的实例
         * @return
         */
        public DownloadServise getService() {
            return DownloadServise.this;
        }

    }

    public interface OnProgressListener {
        /**
         * 下载进度
         * @param fraction 已下载/总大小
         */
        void onProgress(float fraction);

        /**
         * 下载完成
         * @param downLoadFileName
         */
        void onDownloadSuccess(String downLoadFileName);
    }

    /**
     * 对外开发的方法
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        DownloadServise.onProgressListener = onProgressListener;
    }


    public String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void installApk(Context context, String fileName) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(apkPath, "application/vnd.android.package-archive");
//        context.startActivity(intent);

        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.sports.fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public static void installApk(final Context context, String fileName, final int installPermissCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                new MaterialDialog.Builder(context)
                        .title("安装权限")
                        .content("安装应用需要打开未知来源权限，请去设置中开启权限")
                        .positiveText("确定")
                        .negativeText("取消")
                        .cancelable(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                toInstallPermissionSettingIntent(context,installPermissCode);
                            }
                        })
                        .show();
                return;
            }
        }


        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void toInstallPermissionSettingIntent(Context context, int installPermissCode) {
        Uri packageURI = Uri.parse("package:"+context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        if (context instanceof Activity) {
            ((Activity)context).startActivityForResult(intent, installPermissCode);
        }else {
            context.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
