package com.dchealth.VO;

/**
 * Created by Administrator on 2017/7/13.
 */
public class RemindInfo {

    private String serial;
    private String follow;
    private String remind;

    public RemindInfo(){

    }

    public RemindInfo(String serial, String follow, String remind) {
        this.serial = serial;
        this.follow = follow;
        this.remind = remind;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }
}
