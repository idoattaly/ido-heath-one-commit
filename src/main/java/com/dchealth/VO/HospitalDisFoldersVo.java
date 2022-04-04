package com.dchealth.VO;

public class HospitalDisFoldersVo {

    private String hospitalName;
    private long num;

    public HospitalDisFoldersVo() {
    }

    public HospitalDisFoldersVo(String hospitalName, long num) {
        this.hospitalName = hospitalName;
        this.num = num;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
