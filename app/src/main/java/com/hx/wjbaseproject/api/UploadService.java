package com.hx.wjbaseproject.api;

import android.text.TextUtils;

import com.hx.wjbaseproject.api.bean.req.UploadReq;
import com.hx.wjbaseproject.api.bean.resp.UploadResp;
import com.hx.wjbaseproject.api.exception.ServerError;
import com.hx.wjbaseproject.api.repository.FileUploadAPI;
import com.hx.wjbaseproject.manager.UserManage;
import com.hx.wjbaseproject.util.Logger;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaoming on 16/9/9.
 */
public class UploadService {
    private static final String TAG = UploadService.class.getSimpleName();

    /**
     * @param path
     * @return photo url or exceptions
     */
    //头像上传
    public Observable<UploadResp> uploadHeadImg(String path) {
        Logger.i(TAG, "uploadHeadImg()" + path);
        if (TextUtils.isEmpty(path)) {
            return Observable.error(new NullPointerException("file is null"));
        }

        File file = new File(path);
        if (!file.exists()) {
            return Observable.error(new NullPointerException("file is not exists"));
        }
        if (!file.isFile()) {
            return Observable.error(new NullPointerException("file is not file"));
        }

        UploadReq uploadReq = new UploadReq();
        uploadReq.setUserId(UserManage.ins().getUserId());
        uploadReq.setUploadType("2");

        RequestBody userIdBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), UserManage.ins().getUserId());

        RequestBody uploadTypeBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), "2");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("userId", userIdBody);
        params.put("uploadType", uploadTypeBody);
        params.put("file\"; filename=\"" + file.getName(), requestBody);


        return getIUpload().upload(params)
                .timeout(60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param path
     * @return photo url or exceptions
     */
    //语音上传
    public Observable<UploadResp> uploadVoice(String path) {
        Logger.i(TAG, "uploadVoice()" + path);
        if (TextUtils.isEmpty(path)) {
            return Observable.error(new NullPointerException("file is null"));
        }

        File file = new File(path);
        if (!file.exists()) {
            return Observable.error(new NullPointerException("file is not exists"));
        }
        if (!file.isFile()) {
            return Observable.error(new NullPointerException("file is not file"));
        }

        UploadReq uploadReq = new UploadReq();
        uploadReq.setUserId(UserManage.ins().getUserId());
        uploadReq.setUploadType("-1");

        RequestBody userIdBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), UserManage.ins().getUserId());

        RequestBody uploadTypeBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), "-1");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("userId", userIdBody);
        params.put("uploadType", uploadTypeBody);
        params.put("file\"; filename=\"" + file.getName(), requestBody);


        return getIUpload().upload(params)
                .timeout(60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private FileUploadAPI getIUpload() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
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

        return retrofit.create(FileUploadAPI.class);
    }

    private static final class OkHttpLogger implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            Logger.i(TAG, "" + message);
        }

    }
}
