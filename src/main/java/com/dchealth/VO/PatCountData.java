package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */
public class PatCountData {
    private SexTotal sexTotal;
    private List<RegionPatData> regionPatDatas = new ArrayList<>();
    private List<SexRangeData> sexRangeData = new ArrayList<>();

    public PatCountData() {
    }

    public PatCountData(SexTotal sexTotal, List<RegionPatData> regionPatDatas, List<SexRangeData> sexRangeData) {
        this.sexTotal = sexTotal;
        this.regionPatDatas = regionPatDatas;
        this.sexRangeData = sexRangeData;
    }

    public SexTotal getSexTotal() {
        return sexTotal;
    }

    public void setSexTotal(SexTotal sexTotal) {
        this.sexTotal = sexTotal;
    }

    public List<SexRangeData> getSexRangeData() {
        return sexRangeData;
    }

    public void setSexRangeData(List<SexRangeData> sexRangeData) {
        this.sexRangeData = sexRangeData;
    }

    public List<RegionPatData> getRegionPatDatas() {
        return regionPatDatas;
    }

    public void setRegionPatDatas(List<RegionPatData> regionPatDatas) {
        this.regionPatDatas = regionPatDatas;
    }
}
