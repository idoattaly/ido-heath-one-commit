package com.dchealth.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 病人数据VO
 * Created by Administrator on 2017/6/27.
 */
public class DocumentData implements Serializable{
    private String name ;
    private String dcode ;
    private String dcodeName ;
    private String status ;
    private String current ;
    private List<DocumentDataElement> data= new ArrayList<>() ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getDcodeName() {
        return dcodeName;
    }

    public void setDcodeName(String dcodeName) {
        this.dcodeName = dcodeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public List<DocumentDataElement> getData() {
        return data;
    }

    public void setData(List<DocumentDataElement> data) {
        this.data = data;
    }
}
