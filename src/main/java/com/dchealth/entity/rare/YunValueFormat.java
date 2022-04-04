package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/19.
 */
@Entity
@Table(name = "yun_value_format", schema = "emhbase", catalog = "")
public class YunValueFormat {
    private String id;
    private String title;
    private String dict;
    private String relyon;
    private String format;
    private String level;
    private String olddata;
    private Timestamp modifyDate;


    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "dict")
    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    @Basic
    @Column(name = "relyon")
    public String getRelyon() {
        return relyon;
    }

    public void setRelyon(String relyon) {
        this.relyon = relyon;
    }

    @Basic
    @Column(name = "format")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Basic
    @Column(name = "level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Basic
    @Column(name = "olddata")
    public String getOlddata() {
        return olddata;
    }

    public void setOlddata(String olddata) {
        this.olddata = olddata;
    }

    @Basic
    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunValueFormat that = (YunValueFormat) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (dict != null ? !dict.equals(that.dict) : that.dict != null) return false;
        if (relyon != null ? !relyon.equals(that.relyon) : that.relyon != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (olddata != null ? !olddata.equals(that.olddata) : that.olddata != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (dict != null ? dict.hashCode() : 0);
        result = 31 * result + (relyon != null ? relyon.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (olddata != null ? olddata.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}
