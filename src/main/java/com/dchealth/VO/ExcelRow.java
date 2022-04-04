package com.dchealth.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */
public class ExcelRow {
    private List<ExcelCell> excelCells = new ArrayList<>();

    public List<ExcelCell> getExcelCells() {
        return excelCells;
    }

    public void setExcelCells(List<ExcelCell> excelCells) {
        this.excelCells = excelCells;
    }
}
