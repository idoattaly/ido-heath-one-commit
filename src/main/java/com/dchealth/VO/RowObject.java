package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * 表单模板一个一个页面
 * Created by Administrator on 2017/6/27.
 */
public class RowObject {
    private String column ;
    private List<ElementRow> rows = new ArrayList<>();

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public List<ElementRow> getRows() {
        return rows;
    }

    public void setRows(List<ElementRow> rows) {
        this.rows = rows;
    }
}
