package com.dchealth.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public class PostDocumentData implements Serializable {

    private String name ;
    private String dcodeName ;
    private String dcode ;
    private String status ;
    private List<PostElement> data = new ArrayList<>();
    private String current ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDcodeName() {
        return dcodeName;
    }

    public void setDcodeName(String dcodeName) {
        this.dcodeName = dcodeName;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostElement> getData() {
        return data;
    }

    public void setData(List<PostElement> data) {
        this.data = data;
    }

    public String getCurrent() {
        return current;
    }
}
