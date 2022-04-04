package com.dchealth.VO;

public class HospitalDictVo {
    private String id;
    private String hospitalName;
    private String protocolId;

    public HospitalDictVo(String id, String hospitalName, String protocolId) {
        this.id = id;
        this.hospitalName = hospitalName;
        this.protocolId = protocolId;
    }

    public HospitalDictVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }
}
