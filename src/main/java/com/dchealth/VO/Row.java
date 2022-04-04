package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */
public class Row {
    private List<Col> cols = new ArrayList<>();

    public List<Col> getCols() {
        return cols;
    }

    public void setCols(List<Col> cols) {
        this.cols = cols;
    }
}
