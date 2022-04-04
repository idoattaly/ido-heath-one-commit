package com.dchealth.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public class FollowUpPlainVo implements Serializable {

    private String follow_interval_time;
    private String follow_pattern_type;
    private String follow_advance_time;
    private String follow_app_type;
    private String follow_work;
    private List<FollowRecordData> data = new ArrayList<>();

    public FollowUpPlainVo(){

    }

    public FollowUpPlainVo(String follow_interval_time, String follow_pattern_type, String follow_advance_time, String follow_app_type, String follow_work, List<FollowRecordData> data) {
        this.follow_interval_time = follow_interval_time;
        this.follow_pattern_type = follow_pattern_type;
        this.follow_advance_time = follow_advance_time;
        this.follow_app_type = follow_app_type;
        this.follow_work = follow_work;
        this.data = data;
    }

    public String getFollow_interval_time() {
        return follow_interval_time;
    }

    public void setFollow_interval_time(String follow_interval_time) {
        this.follow_interval_time = follow_interval_time;
    }

    public String getFollow_pattern_type() {
        return follow_pattern_type;
    }

    public void setFollow_pattern_type(String follow_pattern_type) {
        this.follow_pattern_type = follow_pattern_type;
    }

    public String getFollow_advance_time() {
        return follow_advance_time;
    }

    public void setFollow_advance_time(String follow_advance_time) {
        this.follow_advance_time = follow_advance_time;
    }

    public String getFollow_app_type() {
        return follow_app_type;
    }

    public void setFollow_app_type(String follow_app_type) {
        this.follow_app_type = follow_app_type;
    }

    public List<FollowRecordData> getData() {
        return data;
    }

    public void setData(List<FollowRecordData> data) {
        this.data = data;
    }

    public String getFollow_work() {
        return follow_work;
    }

    public void setFollow_work(String follow_work) {
        this.follow_work = follow_work;
    }
}
