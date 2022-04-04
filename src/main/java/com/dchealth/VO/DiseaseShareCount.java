package com.dchealth.VO;

/**
 * Created by Administrator on 2017/9/1.
 */
public class DiseaseShareCount {
    private String dcode;
    private String dcodeName;
    private int count;

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getDcodeName() {
        return dcodeName;
    }

    public void setDcodeName(String dcodeName) {
        this.dcodeName = dcodeName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
