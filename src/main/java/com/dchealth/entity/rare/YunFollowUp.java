package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/23.
 */
@Entity
@Table(name = "yun_follow_up", schema = "emhbase", catalog = "")
public class YunFollowUp {
    private String id;
    private String patientId;
    private String serialNumber;
    private Timestamp followDate;
    private Timestamp remindDate;
    private Timestamp modifyDate;
    private String hstatus;
    private String title;
    private String dcode;
    private String note;

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
    @Column(name = "patient_id")
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "serial_number")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Basic
    @Column(name = "follow_date")
    public Timestamp getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Timestamp followDate) {
        this.followDate = followDate;
    }

    @Basic
    @Column(name = "remind_date")
    public Timestamp getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Timestamp remindDate) {
        this.remindDate = remindDate;
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
    @Column(name = "hstatus")
    public String getHstatus() {
        return hstatus;
    }

    public void setHstatus(String hstatus) {
        this.hstatus = hstatus;
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
    @Column(name = "dcode")
    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunFollowUp that = (YunFollowUp) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (patientId != null ? !patientId.equals(that.patientId) : that.patientId != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (followDate != null ? !followDate.equals(that.followDate) : that.followDate != null) return false;
        if (remindDate != null ? !remindDate.equals(that.remindDate) : that.remindDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (hstatus != null ? !hstatus.equals(that.hstatus) : that.hstatus != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (dcode != null ? !dcode.equals(that.dcode) : that.dcode != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (followDate != null ? followDate.hashCode() : 0);
        result = 31 * result + (remindDate != null ? remindDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (hstatus != null ? hstatus.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (dcode != null ? dcode.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}
