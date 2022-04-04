package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/17.
 */
@Entity
@Table(name = "yun_sample_type",schema = "emhbase",catalog = "")
public class YunSampleType {
    private String id;
    private String sampleType;
    private String sampleTypeName;
    private String sampleGroup;
    private String sampleGroupName;

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
    @Column(name = "sample_type")
    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    @Basic
    @Column(name = "sample_type_name")
    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    @Basic
    @Column(name = "sample_group")
    public String getSampleGroup() {
        return sampleGroup;
    }

    public void setSampleGroup(String sampleGroup) {
        this.sampleGroup = sampleGroup;
    }

    @Basic
    @Column(name = "sample_group_name")
    public String getSampleGroupName() {
        return sampleGroupName;
    }

    public void setSampleGroupName(String sampleGroupName) {
        this.sampleGroupName = sampleGroupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunSampleType yunSampleType = (YunSampleType) o;

        if (id != null ? !id.equals(yunSampleType.id) : yunSampleType.id != null) return false;
        if (sampleType != null ? !sampleType.equals(yunSampleType.sampleType) : yunSampleType.sampleType != null) return false;
        if (sampleTypeName != null ? !sampleTypeName.equals(yunSampleType.sampleTypeName) : yunSampleType.sampleTypeName != null) return false;
        if (sampleGroup != null ? !sampleGroup.equals(yunSampleType.sampleGroup) : yunSampleType.sampleGroup != null) return false;
        if (sampleGroupName != null ? !sampleGroupName.equals(yunSampleType.sampleGroupName) : yunSampleType.sampleGroupName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sampleType != null ? sampleType.hashCode() : 0);
        result = 31 * result + (sampleTypeName != null ? sampleTypeName.hashCode() : 0);
        result = 31 * result + (sampleGroup != null ? sampleGroup.hashCode() : 0);
        result = 31 * result + (sampleGroupName != null ? sampleGroupName.hashCode() : 0);
        return result;
    }
}
