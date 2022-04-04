package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/28.
 */
@Entity
@Table(name = "yun_record_docment", schema = "emhbase", catalog = "")
public class YunRecordDocment {
    private String id;
    private String folderId;
    private String category;
    private String title;
    private String typecode1;
    private String typecode2;
    private String templetname;
    private String content;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private String doctorId;
    private String modifyLog;

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
    @Column(name = "folder_id")
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    @Basic
    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
    @Column(name = "typecode1")
    public String getTypecode1() {
        return typecode1;
    }

    public void setTypecode1(String typecode1) {
        this.typecode1 = typecode1;
    }

    @Basic
    @Column(name = "typecode2")
    public String getTypecode2() {
        return typecode2;
    }

    public void setTypecode2(String typecode2) {
        this.typecode2 = typecode2;
    }

    @Basic
    @Column(name = "templetname")
    public String getTempletname() {
        return templetname;
    }

    public void setTempletname(String templetname) {
        this.templetname = templetname;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
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
    @Column(name = "doctor_id")
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Basic
    @Column(name = "modify_log")
    public String getModifyLog() {
        return modifyLog;
    }

    public void setModifyLog(String modifyLog) {
        this.modifyLog = modifyLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunRecordDocment that = (YunRecordDocment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (folderId != null ? !folderId.equals(that.folderId) : that.folderId != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (typecode1 != null ? !typecode1.equals(that.typecode1) : that.typecode1 != null) return false;
        if (typecode2 != null ? !typecode2.equals(that.typecode2) : that.typecode2 != null) return false;
        if (templetname != null ? !templetname.equals(that.templetname) : that.templetname != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (modifyLog != null ? !modifyLog.equals(that.modifyLog) : that.modifyLog != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (folderId != null ? folderId.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (typecode1 != null ? typecode1.hashCode() : 0);
        result = 31 * result + (typecode2 != null ? typecode2.hashCode() : 0);
        result = 31 * result + (templetname != null ? templetname.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (modifyLog != null ? modifyLog.hashCode() : 0);
        return result;
    }
}
