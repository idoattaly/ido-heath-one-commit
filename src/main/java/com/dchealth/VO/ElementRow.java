package com.dchealth.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 单行数据
 * Created by Administrator on 2017/6/27.
 */
public class ElementRow implements Serializable {

    private String name ;
    private String type ;
    private Extend extend ;
    private List<RowItem> items= new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Extend getExtend() {
        return extend;
    }

    public void setExtend(Extend extend) {
        this.extend = extend;
    }

    public List<RowItem> getItems() {
        return items;
    }

    public void setItems(List<RowItem> items) {
        this.items = items;
    }
}
