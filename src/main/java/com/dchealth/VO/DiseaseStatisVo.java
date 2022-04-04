package com.dchealth.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */
public class DiseaseStatisVo implements Serializable {
    private String dcode;
    private List<String> months = new ArrayList<>();
    private List<DcodeCountInfo> dcodeCountInfos = new ArrayList<>();

    public DiseaseStatisVo(){

    }

    public DiseaseStatisVo(String dcode, List<String> months, List<DcodeCountInfo> dcodeCountInfos) {
        this.dcode = dcode;
        this.months = months;
        this.dcodeCountInfos = dcodeCountInfos;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<DcodeCountInfo> getDcodeCountInfos() {
        return dcodeCountInfos;
    }

    public void setDcodeCountInfos(List<DcodeCountInfo> dcodeCountInfos) {
        this.dcodeCountInfos = dcodeCountInfos;
    }
}
