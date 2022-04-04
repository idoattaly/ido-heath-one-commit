package com.dchealth.VO;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/18.
 */
public class YunDictitemVo implements Serializable{
    private String id;
    private String typeName;
    private String itemCode;
    private String itemName ;
    private String inputCode ;
    private String loincCode;

    public YunDictitemVo(String id, String typeName, String itemCode, String itemName, String inputCode, String loincCode) {
        this.id = id;
        this.typeName = typeName;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.inputCode = inputCode;
        this.loincCode = loincCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }
}
