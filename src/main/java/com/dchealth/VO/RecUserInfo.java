package com.dchealth.VO;

/**
 * Created by Administrator on 2017/11/25.
 */
public class RecUserInfo {
    private String userName;
    private String status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RecUserInfo(String userName, String status) {
        this.userName = userName;
        this.status = status;
    }

    public RecUserInfo() {
    }
}
