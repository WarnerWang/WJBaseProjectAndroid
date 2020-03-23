package com.hx.wjbaseproject.api.repository;

import com.hx.wjbaseproject.api.bean.resp.UploadResp;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

public interface FileUploadAPI {

    /**
     * 文件上传
     * 头像上传 uploadType 传2
     */
    @Multipart
    @POST("/sports_web_mobile_transfer/fileUpload/upload")
    Observable<UploadResp> upload(@PartMap Map<String, RequestBody> params);
}
