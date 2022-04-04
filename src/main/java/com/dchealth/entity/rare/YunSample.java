package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/17.
 */
@Entity
@Table(name = "yun_sample",schema = "emhbase",catalog = "")
public class YunSample {
    private String id;
    private String patientId;
    private String sampleCode;
    private String sampleType;
    private String storageDate;
    private String storageTemperature;
    private String chargeNumber;
    private String infectivity;
    private String storageLocation;
    private String anticoagulant;
    private String acquisitionSite;
    private String pathologicDiagnosis;
    private String note;
    private Timestamp createDate;

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
    @Column(name = "sample_code")
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
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
    @Column(name = "storage_date")
    public String getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    @Basic
    @Column(name = "storage_temperature")
    public String getStorageTemperature() {
        return storageTemperature;
    }

    public void setStorageTemperature(String storageTemperature) {
        this.storageTemperature = storageTemperature;
    }

    @Basic
    @Column(name = "charge_number")
    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    @Basic
    @Column(name = "Infectivity")
    public String getInfectivity() {
        return infectivity;
    }

    public void setInfectivity(String infectivity) {
        this.infectivity = infectivity;
    }

    @Basic
    @Column(name = "storage_location")
    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Basic
    @Column(name = "anticoagulant")
    public String getAnticoagulant() {
        return anticoagulant;
    }

    public void setAnticoagulant(String anticoagulant) {
        this.anticoagulant = anticoagulant;
    }

    @Basic
    @Column(name = "acquisition_site")
    public String getAcquisitionSite() {
        return acquisitionSite;
    }

    public void setAcquisitionSite(String acquisitionSite) {
        this.acquisitionSite = acquisitionSite;
    }

    @Basic
    @Column(name = "pathologic_diagnosis")
    public String getPathologicDiagnosis() {
        return pathologicDiagnosis;
    }

    public void setPathologicDiagnosis(String pathologicDiagnosis) {
        this.pathologicDiagnosis = pathologicDiagnosis;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Basic
    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        if (createDate==null){
            this.createDate=new  Timestamp(new Date().getTime());
        }else{
            this.createDate = createDate;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunSample yunSample = (YunSample)o;

        if (id != null ? !id.equals(yunSample.id) : yunSample.id != null) return false;
        if (patientId != null ? !patientId.equals(yunSample.patientId) : yunSample.patientId != null) return false;
        if (sampleCode != null ? !sampleCode.equals(yunSample.sampleCode) : yunSample.sampleCode != null) return false;
        if (sampleType != null ? !sampleType.equals(yunSample.sampleType) : yunSample.sampleType != null) return false;
        if (storageDate != null ? !storageDate.equals(yunSample.storageDate) : yunSample.storageDate != null) return false;
        if (storageTemperature != null ? !storageTemperature.equals(yunSample.storageTemperature) : yunSample.storageTemperature != null) return false;
        if (chargeNumber != null ? !chargeNumber.equals(yunSample.chargeNumber) : yunSample.chargeNumber != null) return false;
        if (infectivity != null ? !infectivity.equals(yunSample.infectivity) : yunSample.infectivity != null) return false;
        if (storageLocation != null ? !storageLocation.equals(yunSample.storageLocation) : yunSample.storageLocation != null) return false;
        if (anticoagulant != null ? !anticoagulant.equals(yunSample.anticoagulant) : yunSample.anticoagulant != null) return false;
        if (acquisitionSite != null ? !acquisitionSite.equals(yunSample.acquisitionSite) : yunSample.acquisitionSite != null) return false;
        if (pathologicDiagnosis != null ? !pathologicDiagnosis.equals(yunSample.pathologicDiagnosis) : yunSample.pathologicDiagnosis != null) return false;
        if (note != null ? !note.equals(yunSample.note) : yunSample.note != null) return false;
        if (createDate != null ? !createDate.equals(yunSample.createDate) : yunSample.createDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (sampleCode != null ? sampleCode.hashCode() : 0);
        result = 31 * result + (sampleType != null ? sampleType.hashCode() : 0);
        result = 31 * result + (storageDate != null ? storageDate.hashCode() : 0);
        result = 31 * result + (storageTemperature != null ? storageTemperature.hashCode() : 0);
        result = 31 * result + (chargeNumber != null ? chargeNumber.hashCode() : 0);
        result = 31 * result + (infectivity != null ? infectivity.hashCode() : 0);
        result = 31 * result + (storageLocation != null ? storageLocation.hashCode() : 0);
        result = 31 * result + (anticoagulant != null ? anticoagulant.hashCode() : 0);
        result = 31 * result + (acquisitionSite != null ? acquisitionSite.hashCode() : 0);
        result = 31 * result + (pathologicDiagnosis != null ? pathologicDiagnosis.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }


}
