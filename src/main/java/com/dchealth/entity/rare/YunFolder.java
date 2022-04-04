package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/23.
 */
@Entity
@Table(name = "yun_folder", schema = "emhbase", catalog = "")
public class YunFolder {
    private String id;
    private String patientId;
    private String actioncode;
    private String actionid;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private String diagnosisCode;
    private String diagnosis;
    private String bz;
    private String relationId;
    private String relationFolder;

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
    @Column(name = "patient_id")
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "actioncode")
    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    @Basic
    @Column(name = "actionid")
    public String getActionid() {
        return actionid;
    }

    public void setActionid(String actionid) {
        this.actionid = actionid;
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
    @Column(name = "diagnosis_code")
    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    @Basic
    @Column(name = "diagnosis")
    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Basic
    @Column(name = "bz")
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Basic
    @Column(name = "relation_id")
    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    @Basic
    @Column(name = "relation_folder")
    public String getRelationFolder() {
        return relationFolder;
    }

    public void setRelationFolder(String relationFolder) {
        this.relationFolder = relationFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunFolder yunFolder = (YunFolder) o;

        if (id != null ? !id.equals(yunFolder.id) : yunFolder.id != null) return false;
        if (patientId != null ? !patientId.equals(yunFolder.patientId) : yunFolder.patientId != null) return false;
        if (actioncode != null ? !actioncode.equals(yunFolder.actioncode) : yunFolder.actioncode != null) return false;
        if (actionid != null ? !actionid.equals(yunFolder.actionid) : yunFolder.actionid != null) return false;
        if (createDate != null ? !createDate.equals(yunFolder.createDate) : yunFolder.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(yunFolder.modifyDate) : yunFolder.modifyDate != null) return false;
        if (diagnosisCode != null ? !diagnosisCode.equals(yunFolder.diagnosisCode) : yunFolder.diagnosisCode != null)
            return false;
        if (diagnosis != null ? !diagnosis.equals(yunFolder.diagnosis) : yunFolder.diagnosis != null) return false;
        if (bz != null ? !bz.equals(yunFolder.bz) : yunFolder.bz != null) return false;
        if (relationId != null ? !relationId.equals(yunFolder.relationId) : yunFolder.relationId != null) return false;
        if (relationFolder != null ? !relationFolder.equals(yunFolder.relationFolder) : yunFolder.relationFolder != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (actioncode != null ? actioncode.hashCode() : 0);
        result = 31 * result + (actionid != null ? actionid.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (diagnosisCode != null ? diagnosisCode.hashCode() : 0);
        result = 31 * result + (diagnosis != null ? diagnosis.hashCode() : 0);
        result = 31 * result + (bz != null ? bz.hashCode() : 0);
        result = 31 * result + (relationId != null ? relationId.hashCode() : 0);
        result = 31 * result + (relationFolder != null ? relationFolder.hashCode() : 0);
        return result;
    }
}
