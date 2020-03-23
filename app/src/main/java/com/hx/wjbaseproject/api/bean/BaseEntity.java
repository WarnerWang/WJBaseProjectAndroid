package com.hx.wjbaseproject.api.bean;

import com.hx.wjbaseproject.util.JsonUtil;

import java.io.Serializable;

public class BaseEntity implements Serializable{

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}
