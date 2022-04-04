package com.dchealth.VO;

/**
 * 元数据格式化
 * Created by Administrator on 2017/6/19.
 */
public class DataElementFormat {
    private String head ;
    private String extend ;
    private String relyonvalue;
    private String tail ;
    private String templet;
    private String part ;
    private String plac ;

    public DataElementFormat(String head, String extend, String relyonvalue, String tail, String template, String part, String plac) {
        this.head = head;
        this.extend = extend;
        this.relyonvalue = relyonvalue;
        this.tail = tail;
        this.templet = template;
        this.part = part;
        this.plac = plac;
    }

    public DataElementFormat() {
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
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

    public String getTemplet() {
        return templet;
    }

    public void setTemplet(String templet) {
        this.templet = templet;
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
}
