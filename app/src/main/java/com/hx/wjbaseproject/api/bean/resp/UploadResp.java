package com.hx.wjbaseproject.api.bean.resp;

import com.hx.wjbaseproject.api.bean.ApiModelProperty;
import com.hx.wjbaseproject.api.bean.BaseResp;

public class UploadResp extends BaseResp {

    @ApiModelProperty("//文件地址，无前缀")
    private String filePath;

    @ApiModelProperty("//文件地址")
    private String thumbPath;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
}
