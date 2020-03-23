package com.hx.wjbaseproject.api.bean;

import com.hx.wjbaseproject.util.JsonUtil;

import java.io.Serializable;

public class BaseResp implements Serializable {


    /**
     * errCode : string
     * errMsg : string
     */

    private String errCode;
    private String errMsg;

    public BaseResp() {
    }

    public BaseResp(String errCode) {
        this.errCode = errCode;
    }

    public BaseResp(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {

        return JsonUtil.toJSONString(this);
    }
}
