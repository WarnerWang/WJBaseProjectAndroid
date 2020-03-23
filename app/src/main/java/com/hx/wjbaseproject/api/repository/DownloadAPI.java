package com.hx.wjbaseproject.api.repository;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author: 王杰
 * @描述:
 * @文件名: DownloadAPI
 * @包名: com.hx.sports.api.repository
 * @创建时间: 2019-09-24 16:51
 * @修改人: 王杰
 * @公司: 北京和信金谷科技有限公司
 * @备注:
 * @版本号: 1.0.0
 */
public interface DownloadAPI {

    /**
     * 下载文件
     */
    @GET
    Observable<ResponseBody> downloadUrl(@Url String fileUrl);

}
