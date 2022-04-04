package com.dchealth.util;

import com.dchealth.VO.ExcelCell;
import com.dchealth.VO.ExcelRow;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.applet.Main;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/26.
 */
public class ExcelUtil {
    //默认单元格内容为数字时格式
    private static DecimalFormat df = new DecimalFormat("0");
    // 默认单元格格式化日期字符串
    private static SimpleDateFormat sdf = new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss");
    // 格式化数字
    private static DecimalFormat nf = new DecimalFormat("0.00");
    public static ArrayList<Map> readExcel(File file) throws Exception{
        try {
            if(file == null){
                return null;
            }
            if(file.getName().endsWith("xlsx")){
                //处理ecxel2007
                return readExcel2007(file);
            }else{
                //处理ecxel2003
                return readExcel2003(file);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /*
     * @return 将返回结果存储在ArrayList内，存储结构与二位数组类似
     * lists.get(0).get(0)表示过去Excel中0行0列单元格
     */
    private static ArrayList<Map> readExcel2003(File file) throws Exception{
        try{
            //ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            //ArrayList<Object> colList;
            ArrayList<Map> allDataList = new ArrayList<Map>();
            ArrayList<ExcelRow> rowList = new ArrayList<ExcelRow>();
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
            int sheetNum = wb.getActiveSheetIndex();
            for(int k=0;k<=sheetNum;k++){
                ArrayList<Map> dataList = new ArrayList<Map>();
                HSSFSheet sheet = wb.getSheetAt(k);
                HSSFRow row;
                HSSFCell cell;
                Object value;
                String [] heads = getHeads(sheet.getRow(0));//获取头部信息
                for(int i = sheet.getFirstRowNum()+1 , rowCount = 1; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
                    ExcelRow excelRow = new ExcelRow();
                    List<ExcelCell> excelCells = new ArrayList<>();
                    Map map = new LinkedHashMap();
                    row = sheet.getRow(i);
                    //colList = new ArrayList<Object>();
                    if(row == null){
                        //当读取行为空时
                        if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                            //rowList.add(colList);
                        }
                        continue;
                    }else{
                        rowCount++;
                    }
                    for( int j = 0 ; j < heads.length ;j++){
                        ExcelCell excelCell = new ExcelCell();
                        cell = row.getCell(j);
                        if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                            //当该单元格为空
                            excelCell.setName(heads[j]);
                            excelCell.setValue("");
                            map.put(heads[j],"");

                        }else{
                            switch(cell.getCellType()){
                                case XSSFCell.CELL_TYPE_STRING:
                                    System.out.println(i + "行" + j + " 列 is String type");
                                    value = cell.getStringCellValue();
                                    break;
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                        value = df.format(cell.getNumericCellValue());
                                    } else if ("General".equals(cell.getCellStyle()
                                            .getDataFormatString())) {
                                        value = nf.format(cell.getNumericCellValue());
                                    } else {
                                        value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                                .getNumericCellValue()));
                                    }
                                    System.out.println(i + "行" + j
                                            + " 列 is Number type ; DateFormt:"
                                            + value.toString());
                                    break;
                                case XSSFCell.CELL_TYPE_BOOLEAN:
                                    System.out.println(i + "行" + j + " 列 is Boolean type");
                                    value = Boolean.valueOf(cell.getBooleanCellValue());
                                    break;
                                case XSSFCell.CELL_TYPE_BLANK:
                                    System.out.println(i + "行" + j + " 列 is Blank type");
                                    value = "";
                                    break;
                                default:
                                    System.out.println(i + "行" + j + " 列 is default type");
                                    value = cell.toString();
                            }// end switch
                            excelCell.setName(heads[j]);
                            excelCell.setValue(value);
                            map.put(heads[j],value);
                        }
                        //colList.add(value);
                        excelCells.add(excelCell);
                    }//end for j
                    //rowList.add(colList);
                    excelRow.setExcelCells(excelCells);
                    rowList.add(excelRow);
                    dataList.add(map);
                }//end for i
                allDataList.addAll(dataList);
            }
            return allDataList;
        }catch(Exception e){
            return null;
        }
    }

    private static ArrayList<Map> readExcel2007(File file) throws Exception{
        try{
            //ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            //ArrayList<Object> colList;
            ArrayList<Map> allDataList = new ArrayList<Map>();
            ArrayList<ExcelRow> rowList = new ArrayList<ExcelRow>();
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            int sheetNum = wb.getActiveSheetIndex();
            for(int k=0;k<=sheetNum;k++){
                ArrayList<Map> dataList = new ArrayList<Map>();
                XSSFSheet sheet = wb.getSheetAt(k);
                XSSFRow row;
                XSSFCell cell;
                Object value;
                String [] heads = getHeads(sheet.getRow(0));//获取头部信息
                for(int i = sheet.getFirstRowNum()+1 , rowCount = 1; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
                    ExcelRow excelRow = new ExcelRow();
                    List<ExcelCell> excelCells = new ArrayList<>();
                    Map map = new LinkedHashMap();
                    row = sheet.getRow(i);
                    //colList = new ArrayList<Object>();
                    if(row == null){
                        //当读取行为空时
                        if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                            //rowList.add(colList);
                        }
                        continue;
                    }else{
                        rowCount++;
                        System.out.println(rowCount);
                    }
                    System.out.println("row.getLastCellNum()"+row.getLastCellNum());
                    for( int j = 0 ; j < heads.length ;j++){
                        ExcelCell excelCell = new ExcelCell();
                        cell = row.getCell(j);
                        if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                            //当该单元格为空
                            //colList.add("");
                            excelCell.setName(heads[j]);
                            excelCell.setValue("");
                            map.put(heads[j],"");
                        }else{
                            switch(cell.getCellType()){
                                case XSSFCell.CELL_TYPE_STRING:
                                    System.out.println(i + "行" + j + " 列 is String type");
                                    value = cell.getStringCellValue();
                                    break;
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                        value = df.format(cell.getNumericCellValue());
                                    } else if ("General".equals(cell.getCellStyle()
                                            .getDataFormatString())) {
                                        value = nf.format(cell.getNumericCellValue());
                                    } else {
                                        value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                                .getNumericCellValue()));
                                    }
                                    System.out.println(i + "行" + j
                                            + " 列 is Number type ; DateFormt:"
                                            + value.toString());
                                    break;
                                case XSSFCell.CELL_TYPE_BOOLEAN:
                                    System.out.println(i + "行" + j + " 列 is Boolean type");
                                    value = Boolean.valueOf(cell.getBooleanCellValue());
                                    break;
                                case XSSFCell.CELL_TYPE_BLANK:
                                    System.out.println(i + "行" + j + " 列 is Blank type");
                                    value = "";
                                    break;
                                default:
                                    System.out.println(i + "行" + j + " 列 is default type");
                                    value = cell.toString();
                            }// end switch
                            excelCell.setName(heads[j]);
                            excelCell.setValue(value);
                            map.put(heads[j],value);
                        }
                        //colList.add(value);
                        excelCells.add(excelCell);
                    }//end for j
                    excelRow.setExcelCells(excelCells);
                    rowList.add(excelRow);
                    dataList.add(map);
                    // rowList.add(colList);
                }//end for i
                allDataList.addAll(dataList);
            }
            return allDataList;
        }catch(Exception e){
            System.out.println("exception");
            return null;
        }
    }

    public static String[] getHeads(Object row) throws Exception{
        //XSSFRow
        String [] heads = null;
        if(row==null){
            throw new Exception("表头信息不能为空");
        }
        if(row instanceof HSSFRow){
            HSSFRow  hssfRow = (HSSFRow)row;
            heads = new String[hssfRow.getPhysicalNumberOfCells()];
            for( int j = hssfRow.getFirstCellNum() ; j < hssfRow.getLastCellNum() ;j++) {
                HSSFCell cell = hssfRow.getCell(j);
                heads[j] = cell.getStringCellValue();
            }
        }else if(row instanceof XSSFRow){
            XSSFRow xssfRow = (XSSFRow)row;
            heads = new String[xssfRow.getPhysicalNumberOfCells()];
            for( int j = xssfRow.getFirstCellNum() ; j < xssfRow.getLastCellNum() ;j++) {
                XSSFCell cell = xssfRow.getCell(j);
                heads[j] = cell.getStringCellValue();
            }
        }
        return heads;
    }
    public static DecimalFormat getDf() {
        return df;
    }
    public static void setDf(DecimalFormat df) {
        ExcelUtil.df = df;
    }
    public static SimpleDateFormat getSdf() {
        return sdf;
    }
    public static void setSdf(SimpleDateFormat sdf) {
        ExcelUtil.sdf = sdf;
    }
    public static DecimalFormat getNf() {
        return nf;
    }
    public static void setNf(DecimalFormat nf) {
        ExcelUtil.nf = nf;
    }
}
