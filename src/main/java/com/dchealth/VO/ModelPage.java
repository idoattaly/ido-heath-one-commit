package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 模板格式页面
 * Created by Administrator on 2017/6/27.
 */
public class ModelPage {

    private String questionDocDesc ;
    private String questionSubTitle ;
    private String title ;
    private String value ;

    private List<RowObject> rowssubjects = new ArrayList<>();

    public String getQuestionDocDesc() {
        return questionDocDesc;
    }

    public void setQuestionDocDesc(String questionDocDesc) {
        this.questionDocDesc = questionDocDesc;
    }

    public String getQuestionSubTitle() {
        return questionSubTitle;
    }

    public void setQuestionSubTitle(String questionSubTitle) {
        this.questionSubTitle = questionSubTitle;
    }

    public List<RowObject> getRowssubjects() {
        return rowssubjects;
    }

    public void setRowssubjects(List<RowObject> rowssubjects) {
        this.rowssubjects = rowssubjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
