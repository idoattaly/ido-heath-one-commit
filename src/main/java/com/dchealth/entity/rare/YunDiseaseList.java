package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/16.
 */
@Entity
@Table(name = "yun_disease_list", schema = "emhbase", catalog = "")
public class YunDiseaseList {
    private String id;
    private String doctorId;
    private String name;
    private String dcode;
    private String bw;
    private String xt;
    private String yc;
    private String note;
    private Timestamp modifyDate;
    private String deptId;

    @Id
    @Column(name = "id")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "doctor_id")
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "dcode")
    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    @Basic
    @Column(name = "bw")
    public String getBw() {
        return bw;
    }

    public void setBw(String bw) {
        this.bw = bw;
    }

    @Basic
    @Column(name = "xt")
    public String getXt() {
        return xt;
    }

    public void setXt(String xt) {
        this.xt = xt;
    }

    @Basic
    @Column(name = "yc")
    public String getYc() {
        return yc;
    }

    public void setYc(String yc) {
        this.yc = yc;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Basic
    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        if(modifyDate==null){
            modifyDate = new Timestamp(new Date().getTime());
        }
        this.modifyDate = modifyDate;
    }

    @Basic
    @Column(name = "dept_id")
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunDiseaseList that = (YunDiseaseList) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (dcode != null ? !dcode.equals(that.dcode) : that.dcode != null) return false;
        if (bw != null ? !bw.equals(that.bw) : that.bw != null) return false;
        if (xt != null ? !xt.equals(that.xt) : that.xt != null) return false;
        if (yc != null ? !yc.equals(that.yc) : that.yc != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (deptId != null ? !deptId.equals(that.deptId) : that.deptId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dcode != null ? dcode.hashCode() : 0);
        result = 31 * result + (bw != null ? bw.hashCode() : 0);
        result = 31 * result + (xt != null ? xt.hashCode() : 0);
        result = 31 * result + (yc != null ? yc.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        return result;
    }
}
