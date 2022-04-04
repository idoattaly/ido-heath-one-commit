package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/26.
 */
@Entity
@Table(name = "yun_chpo", schema = "emhbase", catalog = "")
public class YunChpo {
    private String id;
    private String patientId;
    private String definitionEn;
    private String definitionCn;
    private String hpoId;
    private String nameEn;
    private String nameCn;
    private String hpoUrl;
    private String chpoUrl;
    private String typeName;
    private Timestamp createDate;
    private Timestamp modifyDate;

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
    @Column(name = "definition_en")
    public String getDefinitionEn() {
        return definitionEn;
    }

    public void setDefinitionEn(String definitionEn) {
        this.definitionEn = definitionEn;
    }

    @Basic
    @Column(name = "definition_cn")
    public String getDefinitionCn() {
        return definitionCn;
    }

    public void setDefinitionCn(String definitionCn) {
        this.definitionCn = definitionCn;
    }

    @Basic
    @Column(name = "hpo_id")
    public String getHpoId() {
        return hpoId;
    }

    public void setHpoId(String hpoId) {
        this.hpoId = hpoId;
    }

    @Basic
    @Column(name = "name_en")
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Basic
    @Column(name = "name_cn")
    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Basic
    @Column(name = "hpo_url")
    public String getHpoUrl() {
        return hpoUrl;
    }

    public void setHpoUrl(String hpoUrl) {
        this.hpoUrl = hpoUrl;
    }

    @Basic
    @Column(name = "chpo_url")
    public String getChpoUrl() {
        return chpoUrl;
    }

    public void setChpoUrl(String chpoUrl) {
        this.chpoUrl = chpoUrl;
    }

    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        if (createDate==null){
            return new Timestamp(new Date().getTime());
        }else{
            return createDate;
        }
    }

    public void setCreateDate(Timestamp createDate) {
        if (createDate==null){
            this.createDate=new  Timestamp(new Date().getTime());
        }else{
            this.createDate = createDate;
        }
    }

    @Basic
    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunChpo yunChpo = (YunChpo) o;

        if (id != null ? !id.equals(yunChpo.id) : yunChpo.id != null) return false;
        if (patientId != null ? !patientId.equals(yunChpo.patientId) : yunChpo.patientId != null) return false;
        if (definitionEn != null ? !definitionEn.equals(yunChpo.definitionEn) : yunChpo.definitionEn != null)
            return false;
        if (definitionCn != null ? !definitionCn.equals(yunChpo.definitionCn) : yunChpo.definitionCn != null)
            return false;
        if (hpoId != null ? !hpoId.equals(yunChpo.hpoId) : yunChpo.hpoId != null) return false;
        if (nameEn != null ? !nameEn.equals(yunChpo.nameEn) : yunChpo.nameEn != null) return false;
        if (nameCn != null ? !nameCn.equals(yunChpo.nameCn) : yunChpo.nameCn != null) return false;
        if (hpoUrl != null ? !hpoUrl.equals(yunChpo.hpoUrl) : yunChpo.hpoUrl != null) return false;
        if (chpoUrl != null ? !chpoUrl.equals(yunChpo.chpoUrl) : yunChpo.chpoUrl != null) return false;
        if (typeName != null ? !typeName.equals(yunChpo.typeName) : yunChpo.typeName != null) return false;
        if (createDate != null ? !createDate.equals(yunChpo.createDate) : yunChpo.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(yunChpo.modifyDate) : yunChpo.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (definitionEn != null ? definitionEn.hashCode() : 0);
        result = 31 * result + (definitionCn != null ? definitionCn.hashCode() : 0);
        result = 31 * result + (hpoId != null ? hpoId.hashCode() : 0);
        result = 31 * result + (nameEn != null ? nameEn.hashCode() : 0);
        result = 31 * result + (nameCn != null ? nameCn.hashCode() : 0);
        result = 31 * result + (hpoUrl != null ? hpoUrl.hashCode() : 0);
        result = 31 * result + (chpoUrl != null ? chpoUrl.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}
