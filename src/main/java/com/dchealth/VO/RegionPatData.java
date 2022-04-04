package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */
public class RegionPatData {
    private String region;
    private List<HospitalPatData> hospitalPatDatas = new ArrayList<>();

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<HospitalPatData> getHospitalPatDatas() {
        return hospitalPatDatas;
    }

    public void setHospitalPatDatas(List<HospitalPatData> hospitalPatDatas) {
        this.hospitalPatDatas = hospitalPatDatas;
    }
}
