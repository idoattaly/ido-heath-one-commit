package com.dchealth.VO;

import com.dchealth.entity.common.HospitalDict;

import java.util.List;

public class ResearchGroupHospitalVo {

    private String id;
    private String researchGroupName;
    private String researchDiseaseId;
    private String groupDesc;
    private String groupInInfo;
    private String manyHospitalFlag;
    private List<HospitalDictVo> hospitals;
    private List<HospitalProtocolVo> hospitalProtocolIds;
    private String dataShareLevel;
    private String status;

    public ResearchGroupHospitalVo() {
    }

    public ResearchGroupHospitalVo(String id, String researchGroupName, String researchDiseaseId, String groupDesc, String groupInInfo, String manyHospitalFlag, List<HospitalDictVo> hospitals, List<HospitalProtocolVo> hospitalProtocolIds, String dataShareLevel, String status) {
        this.id = id;
        this.researchGroupName = researchGroupName;
        this.researchDiseaseId = researchDiseaseId;
        this.groupDesc = groupDesc;
        this.groupInInfo = groupInInfo;
        this.manyHospitalFlag = manyHospitalFlag;
        this.hospitals = hospitals;
        this.hospitalProtocolIds = hospitalProtocolIds;
        this.dataShareLevel = dataShareLevel;
        this.status = status;
    }

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

    public List<HospitalDictVo> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<HospitalDictVo> hospitals) {
        this.hospitals = hospitals;
    }

    public List<HospitalProtocolVo> getHospitalProtocolIds() {
        return hospitalProtocolIds;
    }

    public void setHospitalProtocolIds(List<HospitalProtocolVo> hospitalProtocolIds) {
        this.hospitalProtocolIds = hospitalProtocolIds;
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
}
