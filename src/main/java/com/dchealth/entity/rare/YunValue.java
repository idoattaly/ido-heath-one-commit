package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/19.
 */
@Entity
@Table(name = "yun_value", schema = "emhbase", catalog = "")
public class YunValue {
    private String id;
    private String doctorId;
    private String name;
    private String idcode;
    private String zflags;
    private String type;
    private String unit;
    private String rangeexp;
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
    @Column(name = "idcode")
    public String getIdcode() {
        return idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    @Basic
    @Column(name = "zflags")
    public String getZflags() {
        return zflags;
    }

    public void setZflags(String zflags) {
        this.zflags = zflags;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "rangeexp")
    public String getRangeexp() {
        return rangeexp;
    }

    public void setRangeexp(String rangeexp) {
        this.rangeexp = rangeexp;
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
        if(modifyDate==null){
            modifyDate = new Timestamp(new Date().getTime());
        }
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

        YunValue yunValue = (YunValue) o;

        if (id != null ? !id.equals(yunValue.id) : yunValue.id != null) return false;
        if (doctorId != null ? !doctorId.equals(yunValue.doctorId) : yunValue.doctorId != null) return false;
        if (name != null ? !name.equals(yunValue.name) : yunValue.name != null) return false;
        if (idcode != null ? !idcode.equals(yunValue.idcode) : yunValue.idcode != null) return false;
        if (zflags != null ? !zflags.equals(yunValue.zflags) : yunValue.zflags != null) return false;
        if (type != null ? !type.equals(yunValue.type) : yunValue.type != null) return false;
        if (unit != null ? !unit.equals(yunValue.unit) : yunValue.unit != null) return false;
        if (rangeexp != null ? !rangeexp.equals(yunValue.rangeexp) : yunValue.rangeexp != null) return false;
        if (note != null ? !note.equals(yunValue.note) : yunValue.note != null) return false;
        if (modifyDate != null ? !modifyDate.equals(yunValue.modifyDate) : yunValue.modifyDate != null) return false;
        if (deptId != null ? !deptId.equals(yunValue.deptId) : yunValue.deptId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (idcode != null ? idcode.hashCode() : 0);
        result = 31 * result + (zflags != null ? zflags.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (rangeexp != null ? rangeexp.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        return result;
    }
}
