package com.dchealth.VO;

/**
 * Created by Administrator on 2017/6/28.
 */
public class PostElement {
    private String name ;
    private String value ;
    private Object data ;
    private String complete ;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
