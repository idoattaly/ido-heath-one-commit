package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/23.
 */
@Entity
@Table(name = "yun_patient", schema = "emhbase", catalog = "")
public class YunPatient {
    private String id;
    private String doctorId;
    private String owerId;
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
    private String extkey;
    private String bz;
    private String status;//病人状态 0：已故,1:健在

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
    @Column(name = "doctor_id")
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Basic
    @Column(name = "ower_id")
    public String getOwerId() {
        return owerId;
    }

    public void setOwerId(String owerId) {
        this.owerId = owerId;
    }

    @Basic
    @Column(name = "dept_id")
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @Basic
    @Column(name = "pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "mid")
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Basic
    @Column(name = "nc")
    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    @Basic
    @Column(name = "ne")
    public String getNe() {
        return ne;
    }

    public void setNe(String ne) {
        this.ne = ne;
    }

    @Basic
    @Column(name = "sx")
    public String getSx() {
        return sx;
    }

    public void setSx(String sx) {
        this.sx = sx;
    }

    @Basic
    @Column(name = "br")
    public Timestamp getBr() {
        return br;
    }

    public void setBr(Timestamp br) {
        this.br = br;
    }

    @Basic
    @Column(name = "lxfs")
    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }

    @Basic
    @Column(name = "tel1")
    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    @Basic
    @Column(name = "tel2")
    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    @Basic
    @Column(name = "yzbm")
    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Basic
    @Column(name = "extkey")
    public String getExtkey() {
        return extkey;
    }

    public void setExtkey(String extkey) {
        this.extkey = extkey;
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
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status==null||"".equals(status)){
            this.status = "1";
        }else{
            this.status = status;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunPatient that = (YunPatient) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (owerId != null ? !owerId.equals(that.owerId) : that.owerId != null) return false;
        if (deptId != null ? !deptId.equals(that.deptId) : that.deptId != null) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (mid != null ? !mid.equals(that.mid) : that.mid != null) return false;
        if (nc != null ? !nc.equals(that.nc) : that.nc != null) return false;
        if (ne != null ? !ne.equals(that.ne) : that.ne != null) return false;
        if (sx != null ? !sx.equals(that.sx) : that.sx != null) return false;
        if (br != null ? !br.equals(that.br) : that.br != null) return false;
        if (lxfs != null ? !lxfs.equals(that.lxfs) : that.lxfs != null) return false;
        if (tel1 != null ? !tel1.equals(that.tel1) : that.tel1 != null) return false;
        if (tel2 != null ? !tel2.equals(that.tel2) : that.tel2 != null) return false;
        if (yzbm != null ? !yzbm.equals(that.yzbm) : that.yzbm != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(that.modifyDate) : that.modifyDate != null) return false;
        if (extkey != null ? !extkey.equals(that.extkey) : that.extkey != null) return false;
        if (bz != null ? !bz.equals(that.bz) : that.bz != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (owerId != null ? owerId.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (mid != null ? mid.hashCode() : 0);
        result = 31 * result + (nc != null ? nc.hashCode() : 0);
        result = 31 * result + (ne != null ? ne.hashCode() : 0);
        result = 31 * result + (sx != null ? sx.hashCode() : 0);
        result = 31 * result + (br != null ? br.hashCode() : 0);
        result = 31 * result + (lxfs != null ? lxfs.hashCode() : 0);
        result = 31 * result + (tel1 != null ? tel1.hashCode() : 0);
        result = 31 * result + (tel2 != null ? tel2.hashCode() : 0);
        result = 31 * result + (yzbm != null ? yzbm.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (extkey != null ? extkey.hashCode() : 0);
        result = 31 * result + (bz != null ? bz.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
