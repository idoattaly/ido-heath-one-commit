package com.dchealth.VO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 工作流模板
 * Created by Administrator on 2017/6/27.
 */
public class YunDisTemplateVo {

    private String id;
    private String doctorId;
    private String dcode;
    private String mblx;
    private String title;
    private List<ModelTemplateVo> mbsj=new ArrayList<>();
    private String note;
    private Timestamp modifyDate;
    private String deptId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public List<ModelTemplateVo> getMbsj() {
        return mbsj;
    }

    public void setMbsj(List<ModelTemplateVo> mbsj) {
        this.mbsj = mbsj;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
