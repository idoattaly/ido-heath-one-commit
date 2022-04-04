package com.dchealth.VO;

public class HospitalProtocolVo {

    private String hospitalId;
    private String protocolId;

    public HospitalProtocolVo() {
    }

    public HospitalProtocolVo(String hospitalId, String protocolId) {
        this.hospitalId = hospitalId;
        this.protocolId = protocolId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }
}
