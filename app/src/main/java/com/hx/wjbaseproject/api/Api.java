package com.hx.wjbaseproject.api;

import com.hx.wjbaseproject.BuildConfig;
import com.hx.wjbaseproject.api.repository.HomePageAPI;
import com.hx.wjbaseproject.api.token.TokenCache;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * http://www.hexinjingu.com/api/sports/
 * <p>
 * http://www.hexinjingu.com/zentaopms/www/my-todo.html
 * bugs
 */
public class Api {
    public static final String TAG = Api.class.getSimpleName();

    public enum ApiType {
        dev,
        release,
    }

    public static final ApiType API_TYPE = ApiType.valueOf(BuildConfig.API_TYPE);

    public static final Boolean HOST_WITH_DES = true;//是否使用加密

    public static final String DEFAULT_DES_KEY = "hxsports";//默认加密秘钥

    private static final String HOST_DEBUG = "http://dev.hexinjingu.com/sports_web_mobile_transfer/service/";
    private static final String HOST_TEST = "http://test.hexinjingu.com/sports_web_mobile_transfer/service/";
    private static final String BUSS_TEST = "http://www.hexinjingu.com/sports_web_mobile_transfer/service/";
    private static final String HOST_RELEASE = "https://www.88lot.com/sports_web_mobile_transfer/service/";

    private static final String HOST_DEBUG_DES = "http://dev.hexinjingu.com/sports_web_mobile_transfer/main/";
//    private static final String HOST_TEST_DES = "http://www.hexinjingu.com/sports_web_mobile_transfer/main/";
    private static final String HOST_TEST_DES = "http://bocaiwang.hexinjingu.com:10000/test_sports_web_mobile_transfer/main/";
    private static final String HOST_BUSS_DES = "http://bocaiwang.hexinjingu.com:10000/sports_web_mobile_transfer/main/";

//    private static final String HOST_TEST_DES = "http://www.hexinjingu.com/sports_web_mobile_transfer/newmain/";
    private static final String HOST_RELEASE_DES = "https://www.88lot.com/sports_web_mobile_transfer/main/";
//    private static final String HOST_RELEASE_DES = "https://www.88lot.com/sports_web_mobile_transfer/newmain/";

    private static final String HOST_WECHAT = "https://api.weixin.qq.com/";


    //************************************************************
    private volatile static Api api;

    private HomePageAPI homePageAPI;
    private UploadService uploadService;
    private OkDownloadManager downloadManager;

    public static Api ins() {
        if (api == null) {
            synchronized (Api.class) {
                if (api == null) {
                    api = new Api();
                }
            }
        }
        return api;
    }

    private Api() {
        updateApi();
    }

    public boolean isDebug() {
        if (API_TYPE.equals(ApiType.dev)) {
            return true;
        } else {
            return false;
        }
    }

    public String getBaseUrl() {
        String url = HOST_DEBUG;
        switch (API_TYPE) {
            case dev:
                url = HOST_WITH_DES ? HOST_DEBUG_DES : HOST_DEBUG;
                break;
            case release:
                url = HOST_WITH_DES ? HOST_RELEASE_DES : HOST_RELEASE;
                break;
            default:
                url = HOST_WITH_DES ? HOST_RELEASE_DES : HOST_RELEASE;
//                url = HOST_TEST_DES;
                break;
        }
        if (isDebug()) {
            int urlType = TokenCache.getIns().getLoadUrlType();
            if (urlType == 1) {
                url = HOST_WITH_DES ? HOST_DEBUG_DES : HOST_DEBUG;
            }else if (urlType == 2) {
                url = HOST_WITH_DES ? HOST_TEST_DES : HOST_TEST;
            }else if (urlType == 4) {
                url = HOST_WITH_DES ? HOST_BUSS_DES : BUSS_TEST;
            }else if (urlType == 3) {
                url = HOST_WITH_DES ? HOST_RELEASE_DES : HOST_RELEASE;
            }
        }
        return url;
    }

    public static int getDefaultLoadUrlType(){
        if (API_TYPE.equals(ApiType.dev)) {
            return 1;
        }else {
            return 3;
        }
    }

    public HomePageAPI getHomePageAPI() {
        return homePageAPI;
    }

    public UploadService getUploadService() {
        return uploadService;
    }

    public OkDownloadManager getDownloadManager() {
        return downloadManager;
    }

    public void updateApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(OkHttpManager.getSingleton().newClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Retrofit wechatRetrofit = new Retrofit.Builder()
                .baseUrl(HOST_WECHAT)
                .client(OkHttpManager.getSingleton().newWeChatClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        homePageAPI = retrofit.create(HomePageAPI.class);
        uploadService = new UploadService();
        downloadManager = new OkDownloadManager();
    }
}

