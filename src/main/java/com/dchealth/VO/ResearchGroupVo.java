package com.dchealth.VO;

import java.util.List;

/**
 * Created by Administrator on 2017/10/30.
 */
public class ResearchGroupVo {
    private String id;
    private String researchGroupName;
    private String researchDiseaseId;
    private String groupDesc;
    private String groupInInfo;
    private String manyHospitalFlag;
    private List<String> hospitals;
    private List<HospitalProtocolVo> hospitalProtocolIds;
    private String dataShareLevel;
    private String status;

    public ResearchGroupVo() {
    }

    public List<HospitalProtocolVo> getHospitalProtocolIds() {
        return hospitalProtocolIds;
    }

    public void setHospitalProtocolIds(List<HospitalProtocolVo> hospitalProtocolIds) {
        this.hospitalProtocolIds = hospitalProtocolIds;
    }

    public ResearchGroupVo(String id, String researchGroupName, String researchDiseaseId, String groupDesc, String groupInInfo, String manyHospitalFlag, String dataShareLevel, String status) {
        this.id = id;
        this.researchGroupName = researchGroupName;
        this.researchDiseaseId = researchDiseaseId;
        this.groupDesc = groupDesc;
        this.groupInInfo = groupInInfo;
        this.manyHospitalFlag = manyHospitalFlag;
        this.dataShareLevel = dataShareLevel;
        this.status = status;

    }

    public ResearchGroupVo(String id, String researchGroupName, String researchDiseaseId, String groupDesc, String groupInInfo, String manyHospitalFlag, List<String> hospitals, List<HospitalProtocolVo> hospitalProtocolIds, String dataShareLevel, String status) {
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

    public List<String> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<String> hospitals) {
        this.hospitals = hospitals;
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
