package com.dchealth.VO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sunkqa on 2017/12/11.
 */
public class PatientVo {
    private String id;
    private String doctorId;
    private String owerId;
    private String doctorName;//医生名称
    private String hospitalName;//医院名称
    private String deptId;
    private String pid;
    private String mid;
    private String nc;
    private String ne;
    private String sx;
    private Timestamp br;
    private String lxfs;
    private String tel1;
    private String tel2;
    private String yzbm;
    private String email;
    private Timestamp createDate;
    private Timestamp modifyDate;
    //private String extkey;
    private String bz;
    private String status;//病人状态 0：已故,1:健在

    public PatientVo() {
    }

    public PatientVo(String id, String doctorId, String owerId, String doctorName, String hospitalName, String deptId, String pid, String mid, String nc, String ne, String sx, Date br, String lxfs, String tel1, String tel2, String yzbm, String email, Date createDate, Date modifyDate, String bz, String status) {
        this.id = id;
        this.doctorId = doctorId;
        this.owerId = owerId;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
        this.deptId = deptId;
        this.pid = pid;
        this.mid = mid;
        this.nc = nc;
        this.ne = ne;
        this.sx = sx;
        this.br = (br==null?null:new Timestamp(br.getTime()));
        this.lxfs = lxfs;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.yzbm = yzbm;
        this.email = email;
        this.createDate = (createDate==null?null:new Timestamp(createDate.getTime()));
        this.modifyDate = (modifyDate==null?null:new Timestamp(modifyDate.getTime()));
        this.bz = bz;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getOwerId() {
        return owerId;
    }

    public void setOwerId(String owerId) {
        this.owerId = owerId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getNe() {
        return ne;
    }

    public void setNe(String ne) {
        this.ne = ne;
    }

    public String getSx() {
        return sx;
    }

    public void setSx(String sx) {
        this.sx = sx;
    }

    public Timestamp getBr() {
        return br;
    }

    public void setBr(Timestamp br) {
        this.br = br;
    }

    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
