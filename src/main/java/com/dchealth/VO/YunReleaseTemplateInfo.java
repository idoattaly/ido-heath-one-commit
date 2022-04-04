package com.dchealth.VO;

import com.dchealth.entity.rare.YunReleaseTemplet;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/30.
 */
public class YunReleaseTemplateInfo {
    private YunReleaseTemplet yunReleaseTemplet;
    private String userName;
    private String userId;
    private String mobile;

    public YunReleaseTemplet getYunReleaseTemplet() {
        return yunReleaseTemplet;
    }

    public void setYunReleaseTemplet(YunReleaseTemplet yunReleaseTemplet) {
        this.yunReleaseTemplet = yunReleaseTemplet;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
