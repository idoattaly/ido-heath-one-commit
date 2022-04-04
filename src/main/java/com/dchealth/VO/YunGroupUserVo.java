package com.dchealth.VO;

import com.dchealth.entity.common.YunUsers;

/**
 * Created by Administrator on 2017/7/5.
 */
public class YunGroupUserVo {
    private YunUsers yunUsers ;
    private String status;

    public YunGroupUserVo(){

    }
    public YunGroupUserVo(YunUsers yunUsers, String status) {
        this.yunUsers = yunUsers;
        this.status = status;
    }

    public YunUsers getYunUsers() {
        return yunUsers;
    }

    public void setYunUsers(YunUsers yunUsers) {
        this.yunUsers = yunUsers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
