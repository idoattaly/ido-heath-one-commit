package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * 疾病模板
 * Created by Administrator on 2017/7/3.
 */
public class YunReleaseTemplateVo {

    private String id;
    private String hstatus;
    private String dcode;
    private String mblx;
    private String title;
    private String hversion;
    private Object mbsj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHstatus() {
        return hstatus;
    }

    public void setHstatus(String hstatus) {
        this.hstatus = hstatus;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getMblx() {
        return mblx;
    }

    public void setMblx(String mblx) {
        this.mblx = mblx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHversion() {
        return hversion;
    }

    public void setHversion(String hversion) {
        this.hversion = hversion;
    }

    public Object getMbsj() {
        return mbsj;
    }

    public void setMbsj(Object mbsj) {
        this.mbsj = mbsj;
    }
}