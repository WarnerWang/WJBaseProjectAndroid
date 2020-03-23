package com.hx.wjbaseproject.api.exception;

import java.io.IOException;

public class ServerError extends IOException {
    private String errorCode;
    private String msg;

    public ServerError(String errorCode, String msg) {
        super("server return error:" + errorCode + ",msg:" + msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
