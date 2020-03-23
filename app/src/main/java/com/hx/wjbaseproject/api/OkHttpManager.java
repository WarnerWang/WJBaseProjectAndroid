package com.hx.wjbaseproject.api;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hx.wjbaseproject.api.bean.BaseResp;
import com.hx.wjbaseproject.api.exception.ServerError;
import com.hx.wjbaseproject.api.token.TokenCache;
import com.hx.wjbaseproject.eventbus.IEvent;
import com.hx.wjbaseproject.manager.UserManage;
import com.hx.wjbaseproject.util.AppUtil;
import com.hx.wjbaseproject.util.DesUtils;
import com.hx.wjbaseproject.util.JsonUtil;
import com.hx.wjbaseproject.util.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

import static com.hx.wjbaseproject.api.Api.DEFAULT_DES_KEY;
import static com.hx.wjbaseproject.api.Api.HOST_WITH_DES;

/**
 */
public class OkHttpManager {

    private static final String TAG = OkHttpManager.class.getSimpleName();

    private volatile static OkHttpManager okHttpManager;

    public OkHttpClient newClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String param = "";
                        RequestBody requestBody = request.body();
                        if (requestBody != null) {
                            Buffer sink = new Buffer();
                            requestBody.writeTo(sink);
                            param = sink.readUtf8();
                            JSONObject jsonObject = new JSONObject();

                            String token = TokenCache.getIns().getAppToken();
//                            ToastUtil.ins().show("接口请求 ： "+TokenCache.getIns().getAppToken(),true);
                            Logger.i("接口请求 ： ",TokenCache.getIns().getAppToken());
                            Logger.i("3333333333333");
                            if (TextUtils.isEmpty(token)) {
                                jsonObject.put("token", "");
                            } else {
                                jsonObject.put("token", token);
                            }

                            String userId = UserManage.ins().getUserId();
                            if (TextUtils.isEmpty(userId)) {
                                jsonObject.put("userId", "");
                            } else {
                                jsonObject.put("userId", userId);
                            }
                            String version = AppUtil.getVersionName();
                            if (TextUtils.isEmpty(version)) {
                                jsonObject.put("version", "");
                            } else {
                                String[] versions = version.split("-");
                                if (versions.length > 0) {
                                    String versionString = versions[0];
                                    jsonObject.put("version", versionString.replace("v",""));
                                }

                            }
                            String desParam = "";
                            if (HOST_WITH_DES) {
                                try {
                                    String secretKey = "";
                                    desParam = DesUtils.encryptDES(secretKey, param, request.url().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            jsonObject.put("content", desParam);
                            String jsonString = jsonObject.toJSONString();
                            if (HOST_WITH_DES) {
                                try {
                                    jsonString = DesUtils.encryptDES(DEFAULT_DES_KEY, jsonString);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            RequestBody newRequestBody = RequestBody.create(requestBody.contentType(), jsonString);
                            Logger.i(TAG, "**************************************** request.start ****************************************");
                            request = request.newBuilder().put(newRequestBody).build();
                        }

                        Response response;
                        try {
                            response = chain.proceed(request);
                        } catch (SocketTimeoutException exception) {
                            throw new ServerError("", "网络连接超时");
                        } catch (Exception e) {
                            Logger.printException(e);
                            throw e;
                        }

                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            String content = responseBody.string();
                            String errCode;
                            String errMsg;
//                            Logger.i(TAG, "解密前:" + content);
                            if (HOST_WITH_DES) {
                                content = DesUtils.decryptDES(DEFAULT_DES_KEY, content);
                            }

                            String requestUrl = request.url().toString();

                            Logger.i(TAG, "\nrequestUrl=" + requestUrl + ",\nparam=" + param + ",\ncontent:" + content);
                            Logger.i(TAG, "**************************************** request.end ****************************************");
                            try {
                                BaseResp baseResp = JsonUtil.parseObject(content, BaseResp.class);
                                errCode = baseResp.getErrCode();
                                errMsg = baseResp.getErrMsg();
                            } catch (Exception e) {
                                throw new ServerError("json error", "\ncontent:" + content + "\n" + " url:" + request.url().toString() + "\n" + e.getMessage());
                            }
                            if (!"0000".equals(errCode)) {
                                Logger.e("server return:" + content);
                                Logger.e("server return error:" + errCode + ", msg:" + errMsg + ", url:" + request.url().toString());
                                if ("6888".equals(errCode)) {
                                    EventBus.getDefault().post(new IEvent.Logout(true));
                                }
                                throw new ServerError(errCode, errMsg);
                            }

                            ResponseBody newResponseBody = ResponseBody
                                    .create(responseBody.contentType(), content);
                            response = response.newBuilder()
                                    .body(newResponseBody)
                                    .build();
                        }
                        return response;
                    }
                })
                .addInterceptor(interceptor)
                .build();
        return okHttpClient;
    }

    private OkHttpManager() {
    }

    public OkHttpClient newWeChatClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return okHttpClient;
    }

    public static OkHttpManager getSingleton() {
        if (okHttpManager == null) {
            synchronized (OkHttpManager.class) {
                if (okHttpManager == null) {
                    okHttpManager = new OkHttpManager();
                }
            }
        }
        return okHttpManager;
    }

    private static final class OkHttpLogger implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            if (HOST_WITH_DES) {
                message = DesUtils.decryptDES(DEFAULT_DES_KEY, message);
            }
//            Logger.i(TAG, "" + message);
        }

    }
}
