package com.hx.wjbaseproject.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.hx.wjbaseproject.api.exception.ServerError;
import com.hx.wjbaseproject.api.repository.DownloadAPI;
import com.hx.wjbaseproject.api.token.TokenCache;
import com.hx.wjbaseproject.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: 王杰
 * @描述:
 * @文件名: OkDownloadManager
 * @包名: com.hx.sports.api
 * @创建时间: 2019-09-24 16:49
 * @修改人: 王杰
 * @公司: 北京和信金谷科技有限公司
 * @备注:
 * @版本号: 1.0.0
 */
public class OkDownloadManager {
    private static final String TAG = UploadService.class.getSimpleName();

    public static Observable<Bitmap> getDownloadBitmap(final String url, final String fileName){
        Observable<Bitmap> map = Api.ins().getDownloadManager()
                .getDownloadResponse(url)
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        if (responseBody != null) {
                            Logger.d("收到的responseBody不为空！");
                        }
                        if (writeResponseBodyToDisk(responseBody, fileName, url)) {
                            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + fileName);
                            return bitmap;
                        }
                        return null;
                    }
                });
        return map;
    }

    private static boolean writeResponseBodyToDisk(ResponseBody body, String fileName, String fileUrl) {//保存图片到本地
        try {
            // todo change the file location/name according to your needs

            String pathname = Environment.getExternalStorageDirectory() + "/" + fileName;
            File futureStudioIconFile = new File(pathname);
            Logger.d("文件的保存地址为：" + futureStudioIconFile.getAbsolutePath());
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    Logger.d("file download: " + fileSizeDownloaded / fileSize * 100);
                    Logger.d("file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();

//                SPUtils.put((Context) getMyView(), "adPictureAddress", ((Context) getMyView()).getExternalFilesDir(null) + File.separator + fileName);
//                SPUtils.put((Context) getMyView(), "adPictureUrl", fileUrl);
//                TokenCache.getIns().saveSplashAdFilePath(pathname);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static Observable<ResponseBody> getDownloadResponse(String url){
        return getDownload().downloadUrl(url)
                .timeout(60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static DownloadAPI getDownload() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response;
                        try {
                            response = chain.proceed(request);
                        } catch (SocketTimeoutException exception) {
                            throw new ServerError("", "网络连接超时");
                        }

                        return response;
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ins().getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(DownloadAPI.class);
    }

    private static final class OkHttpLogger implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            Logger.i(TAG, "" + message);
        }

    }
}
