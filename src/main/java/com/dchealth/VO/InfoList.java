package com.dchealth.VO;

import java.io.Serializable;

/**
 *
 * 用于展示根据添加疾病的时候获取工作流或者表单信息
 * Created by Administrator on 2017/7/6.
 */
public class InfoList implements Serializable{

    private String name ;
    private String value ;
    private String id ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
