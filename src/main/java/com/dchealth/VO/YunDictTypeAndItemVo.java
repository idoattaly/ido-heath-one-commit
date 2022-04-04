package com.dchealth.VO;

import com.dchealth.entity.common.YunDictitem;
import com.dchealth.entity.common.YunDicttype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
public class YunDictTypeAndItemVo implements Serializable{
    private String doctorId;
    private List<YunDicttype> yunDicttypes = new ArrayList<>();
    private List<YunDictitem> yunDictitems = new ArrayList<>();

    public YunDictTypeAndItemVo(){

    }

    public YunDictTypeAndItemVo(String doctorId, List<YunDicttype> yunDicttypes, List<YunDictitem> yunDictitems) {
        this.doctorId = doctorId;
        this.yunDicttypes = yunDicttypes;
        this.yunDictitems = yunDictitems;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<YunDicttype> getYunDicttypes() {
        return yunDicttypes;
    }

    public void setYunDicttypes(List<YunDicttype> yunDicttypes) {
        this.yunDicttypes = yunDicttypes;
    }

    public List<YunDictitem> getYunDictitems() {
        return yunDictitems;
    }

    public void setYunDictitems(List<YunDictitem> yunDictitems) {
        this.yunDictitems = yunDictitems;
    }
}
