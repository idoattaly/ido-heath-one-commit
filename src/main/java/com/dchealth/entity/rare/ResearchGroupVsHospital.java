package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/10/19.
 */
@Entity
@Table(name = "research_group_vs_hospital", schema = "emhbase", catalog = "")
public class ResearchGroupVsHospital {
    private String id;
    private String groupId;
    private String hospitalId;
    private String protocolId;

    @Basic
    @Column(name = "protocol_id", nullable = true, length = 64)
    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

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
    @Column(name = "group_id", nullable = true, length = 64)
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "hospital_id", nullable = true, length = 64)
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResearchGroupVsHospital that = (ResearchGroupVsHospital) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (hospitalId != null ? !hospitalId.equals(that.hospitalId) : that.hospitalId != null) return false;
        if (protocolId != null ? !protocolId.equals(that.protocolId) : that.protocolId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (hospitalId != null ? hospitalId.hashCode() : 0);
        result = 31 * result + (protocolId != null ? protocolId.hashCode() : 0);
        return result;
    }
}
