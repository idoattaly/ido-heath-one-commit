package com.dchealth.VO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/19.
 */
public class YunDataFormatVo implements Serializable {

     private String id ;
     private String dict ;
     private String extend ;
     private String head ;
     private String level ;
     private Date modif_date ;
     private String olddata ;
     private String part ;
     private String plac ;
     private String relyon ;
     private String relyonvalue ;
     private String tail ;
     private String title ;
     private String templet ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getModif_date() {
        return modif_date;
    }

    public void setModif_date(Date modif_date) {
        this.modif_date = modif_date;
    }

    public String getOlddata() {
        return olddata;
    }

    public void setOlddata(String olddata) {
        this.olddata = olddata;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPlac() {
        return plac;
    }

    public void setPlac(String plac) {
        this.plac = plac;
    }

    public String getRelyon() {
        return relyon;
    }

    public void setRelyon(String relyon) {
        this.relyon = relyon;
    }

    public String getRelyonvalue() {
        return relyonvalue;
    }

    public void setRelyonvalue(String relyonvalue) {
        this.relyonvalue = relyonvalue;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplet() {
        return templet;
    }

    public void setTemplet(String templet) {
        this.templet = templet;
    }
}
