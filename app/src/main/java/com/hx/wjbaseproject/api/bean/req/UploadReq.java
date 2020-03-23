package com.hx.wjbaseproject.api.bean.req;

import com.hx.wjbaseproject.api.bean.BaseReq;

public class UploadReq extends BaseReq {
    private String userId;
    private String uploadType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }
}
