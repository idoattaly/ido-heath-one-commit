package com.dchealth.VO;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Hversion implements Serializable{

    private String doctor ;
    private String num ;
    private String dept ;

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
