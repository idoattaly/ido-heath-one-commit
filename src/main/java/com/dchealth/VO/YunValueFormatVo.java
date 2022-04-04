package com.dchealth.VO;

import com.dchealth.entity.rare.YunValue;
import com.dchealth.entity.rare.YunValueFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
public class YunValueFormatVo implements Serializable {
    private String doctorId;
    private List<YunValue> yunValues = new ArrayList<>();
    private List<YunValueFormat> yunValueFormats = new ArrayList<>();

    public YunValueFormatVo(String doctorId, List<YunValue> yunValues, List<YunValueFormat> yunValueFormats) {
        this.doctorId = doctorId;
        this.yunValues = yunValues;
        this.yunValueFormats = yunValueFormats;
    }

    public YunValueFormatVo(){

    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<YunValue> getYunValues() {
        return yunValues;
    }

    public void setYunValues(List<YunValue> yunValues) {
        this.yunValues = yunValues;
    }

    public List<YunValueFormat> getYunValueFormats() {
        return yunValueFormats;
    }

    public void setYunValueFormats(List<YunValueFormat> yunValueFormats) {
        this.yunValueFormats = yunValueFormats;
    }
}
