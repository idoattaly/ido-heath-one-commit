package com.dchealth.VO;

import java.util.List;

public class ResearchGroupsVo {

    private String id;
    private String researchGroupName;
    private String researchDiseaseId;
    private String groupDesc;
    private String groupInInfo;
    private String manyHospitalFlag;
    private String dataShareLevel;
    private String status;
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResearchGroupName() {
        return researchGroupName;
    }

    public void setResearchGroupName(String researchGroupName) {
        this.researchGroupName = researchGroupName;
    }

    public String getResearchDiseaseId() {
        return researchDiseaseId;
    }

    public void setResearchDiseaseId(String researchDiseaseId) {
        this.researchDiseaseId = researchDiseaseId;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupInInfo() {
        return groupInInfo;
    }

    public void setGroupInInfo(String groupInInfo) {
        this.groupInInfo = groupInInfo;
    }

    public String getManyHospitalFlag() {
        return manyHospitalFlag;
    }

    public void setManyHospitalFlag(String manyHospitalFlag) {
        this.manyHospitalFlag = manyHospitalFlag;
    }

    public String getDataShareLevel() {
        return dataShareLevel;
    }

    public void setDataShareLevel(String dataShareLevel) {
        this.dataShareLevel = dataShareLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ResearchGroupsVo(String id, String researchGroupName, String researchDiseaseId, String groupDesc, String groupInInfo, String manyHospitalFlag, String dataShareLevel, String status, String userName) {
        this.id = id;
        this.researchGroupName = researchGroupName;
        this.researchDiseaseId = researchDiseaseId;
        this.groupDesc = groupDesc;
        this.groupInInfo = groupInInfo;
        this.manyHospitalFlag = manyHospitalFlag;
        this.dataShareLevel = dataShareLevel;
        this.status = status;
        this.userName = userName;
    }

    public ResearchGroupsVo() {
    }
}
