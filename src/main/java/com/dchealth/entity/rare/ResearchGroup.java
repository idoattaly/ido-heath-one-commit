package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/10/19.
 */
@Entity
@Table(name = "research_group", schema = "emhbase", catalog = "")
public class ResearchGroup {
    private String id;
    private String researchGroupName;
    private String researchDiseaseId;
    private String groupDesc;
    private String groupInInfo;
    private String manyHospitalFlag;
    private String dataShareLevel;
    private String status;

    @Column(name = "id")
    @Id
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "research_group_name", nullable = true, length = 200)
    public String getResearchGroupName() {
        return researchGroupName;
    }

    public void setResearchGroupName(String researchGroupName) {
        this.researchGroupName = researchGroupName;
    }

    @Basic
    @Column(name = "research_disease_id", nullable = true, length = 64)
    public String getResearchDiseaseId() {
        return researchDiseaseId;
    }

    public void setResearchDiseaseId(String researchDiseaseId) {
        this.researchDiseaseId = researchDiseaseId;
    }

    @Basic
    @Column(name = "group_desc", nullable = true, length = -1)
    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    @Basic
    @Column(name = "group_in_info", nullable = true, length = -1)
    public String getGroupInInfo() {
        return groupInInfo;
    }

    public void setGroupInInfo(String groupInInfo) {
        this.groupInInfo = groupInInfo;
    }

    @Basic
    @Column(name = "many_hospital_flag", nullable = true, length = 2)
    public String getManyHospitalFlag() {
        return manyHospitalFlag;
    }

    public void setManyHospitalFlag(String manyHospitalFlag) {
        this.manyHospitalFlag = manyHospitalFlag;
    }

    @Basic
    @Column(name = "data_share_level", nullable = true, length = 2)
    public String getDataShareLevel() {
        return dataShareLevel;
    }

    public void setDataShareLevel(String dataShareLevel) {
        this.dataShareLevel = dataShareLevel;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResearchGroup that = (ResearchGroup) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (researchGroupName != null ? !researchGroupName.equals(that.researchGroupName) : that.researchGroupName != null)
            return false;
        if (researchDiseaseId != null ? !researchDiseaseId.equals(that.researchDiseaseId) : that.researchDiseaseId != null)
            return false;
        if (groupDesc != null ? !groupDesc.equals(that.groupDesc) : that.groupDesc != null) return false;
        if (groupInInfo != null ? !groupInInfo.equals(that.groupInInfo) : that.groupInInfo != null) return false;
        if (manyHospitalFlag != null ? !manyHospitalFlag.equals(that.manyHospitalFlag) : that.manyHospitalFlag != null)
            return false;
        if (dataShareLevel != null ? !dataShareLevel.equals(that.dataShareLevel) : that.dataShareLevel != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (researchGroupName != null ? researchGroupName.hashCode() : 0);
        result = 31 * result + (researchDiseaseId != null ? researchDiseaseId.hashCode() : 0);
        result = 31 * result + (groupDesc != null ? groupDesc.hashCode() : 0);
        result = 31 * result + (groupInInfo != null ? groupInInfo.hashCode() : 0);
        result = 31 * result + (manyHospitalFlag != null ? manyHospitalFlag.hashCode() : 0);
        result = 31 * result + (dataShareLevel != null ? dataShareLevel.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
