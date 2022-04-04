package com.dchealth.VO;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/26.
 */
public class InviteUserVo {

    private String id;
    private String userName;
    private String sex;
    private String nation;
    private String mobile;
    private String tel;
    private String email;
    private Date birthDate;
    private String title;
    private String hospitalName;
    private String groupName;
    private String diseaseName;
    private String recordId;
    private String status;

    public InviteUserVo(String id, String userName, String sex, String nation, String mobile, String tel,
                        String email, Date birthDate, String title, String hospitalName, String groupName,
                        String diseaseName,String recordId,String status) {
        this.id = id;
        this.userName = userName;
        this.sex = sex;
        this.nation = nation;
        this.mobile = mobile;
        this.tel = tel;
        this.email = email;
        this.birthDate = birthDate;
        this.title = title;
        this.hospitalName = hospitalName;
        this.groupName = groupName;
        this.diseaseName = diseaseName;
        this.recordId = recordId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
