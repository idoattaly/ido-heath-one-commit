package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/20.
 */
@Entity
@Table(name = "yun_dis_templet", schema = "emhbase", catalog = "")
public class YunDisTemplet {
    private String id;
    private String doctorId;
    private String dcode;
    private String mblx;
    private String title;
    private String mbsj;
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
    @Column(name = "dcode")
    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    @Basic
    @Column(name = "mblx")
    public String getMblx() {
        return mblx;
    }

    public void setMblx(String mblx) {
        this.mblx = mblx;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "mbsj")
    public String getMbsj() {
        return mbsj;
    }

    public void setMbsj(String mbsj) {
        this.mbsj = mbsj;
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

        YunDisTemplet that = (YunDisTemplet) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (dcode != null ? !dcode.equals(that.dcode) : that.dcode != null) return false;
        if (mblx != null ? !mblx.equals(that.mblx) : that.mblx != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (mbsj != null ? !mbsj.equals(that.mbsj) : that.mbsj != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (deptId != null ? !deptId.equals(that.deptId) : that.deptId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (dcode != null ? dcode.hashCode() : 0);
        result = 31 * result + (mblx != null ? mblx.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (mbsj != null ? mbsj.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        return result;
    }
}
