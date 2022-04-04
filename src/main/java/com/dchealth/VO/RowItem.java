package com.dchealth.VO;

/**
 *
 * 用于模板数据显示的数据
 * Created by Administrator on 2017/6/27.
 */
public class RowItem {
    private String name ;
    private String value;
    private String text ;
    private String inputCode ;
    private String loincCode;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
