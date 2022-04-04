package com.dchealth.VO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/14.
 */
public class PatientFollowUpVo  implements Serializable{
    private String id;
    private String doctorId;
    private String deptId;
    private String pid;
    private String mid;
    private String nc;
    private String ne;
    private String sx;
    private Date br;
    private String lxfs;
    private String tel1;
    private String tel2;
    private String yzbm;
    private String email;
    private Date createDate;
    private String followId;
    private String title;
    private String dcode;
    private Date followDate;
    private Date remindDate;

    public PatientFollowUpVo(String id, String doctorId, String deptId, String pid, String mid, String nc, String ne, String sx, Date br, String lxfs, String tel1, String tel2, String yzbm, String email,Date createDate, String followId, String title, String dcode, Date followDate, Date remindDate) {
        super();
        this.id = id;
        this.doctorId = doctorId;
        this.deptId = deptId;
        this.pid = pid;
        this.mid = mid;
        this.nc = nc;
        this.ne = ne;
        this.sx = sx;
        this.br = br;
        this.lxfs = lxfs;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.yzbm = yzbm;
        this.email = email;
        this.createDate = createDate;
        this.followId = followId;
        this.title = title;
        this.dcode = dcode;
        this.followDate = followDate;
        this.remindDate = remindDate;
    }
    public PatientFollowUpVo(){
     super();
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

    public Date getBr() {
        return br;
    }

    public void setBr(Date br) {
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

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
