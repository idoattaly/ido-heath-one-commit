package com.dchealth.service.rare;

import com.dchealth.VO.*;
import com.dchealth.entity.common.YunDictitem;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.entity.rare.*;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.security.PasswordAndSalt;
import com.dchealth.security.SystemPasswordService;
import com.dchealth.util.*;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import javafx.scene.control.cell.ComboBoxListCell;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/7/27.
 */
@Produces("application/json")
@Path("transfer")
@Controller
public class FileTransferService {

    @Autowired
    private BaseFacade baseFacade;
    private Map modelTempMap = new ConcurrentHashMap();
    private Map formMap = new ConcurrentHashMap();

    /**
     * 上传excel格式文件转换为json格式文件输出
     * @param uploadedInputStream
     * @param fileDetail
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("upload")
    @Transactional
    public Response saveFile(@FormDataParam( "file") InputStream uploadedInputStream,
                             @FormDataParam( "file") FormDataContentDisposition fileDetail, @Context HttpServletRequest httpServletRequest) throws Exception {
        String filename =fileDetail.getFileName();
        String path = httpServletRequest.getServletContext().getRealPath("/");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        long time = IDUtils.genItemId() ;
        String exName = filename.substring(filename.lastIndexOf('.'));
        String filePath = "/upload";
        if(month>=10){
            path = path+"\\upload"+"\\"+year+month+day+"\\"+hour+"\\"+time+exName;
        }else{
            path = path+"\\upload"+"\\"+year+"0"+month+day+"\\"+hour+"\\"+time+exName;
        }

        File file= new File(path);
        if(!file.exists()){
            File fileParent = file.getParentFile() ;
            if(!fileParent.exists()){
                if(!fileParent.mkdirs()){
                    throw new Exception("创建文件路径失败");
                };
            }
            if(!file.createNewFile()){
                throw new Exception("创建文件失败!");
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int length = 0 ;
        while(-1 !=(length=uploadedInputStream.read(bytes))){
            fileOutputStream.write(bytes);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        String jsonString = JSONUtil.objectToJsonString(ExcelUtil.readExcel(file));
        return Response.status(Response.Status.OK).entity(ExcelUtil.readExcel(file)).build();
    }


    /**
     * 根据传入的mode和医生id,病人id，疾病编码导出病人病历信息
     * @param patientId 病人id
     * @param doctorId 医生id
     * @param dcode 疾病编码
     * @param mode 0位医生下所有病人病历信息导出 1:位单个病人病历信息导出
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GET
    @Path("patient-info-check-out")
    public List<String> checkOutPatientInfo(@QueryParam("patientId")String patientId,@QueryParam("doctorId")String doctorId,@QueryParam("dcode")String dcode,
                                            @QueryParam("mode")String mode,@Context HttpServletResponse httpServletResponse,
                                            @Context HttpServletRequest httpServletRequest) throws Exception{
        List<YunRecordDocment> yunRecordDocments = null;
        List<String> list = new ArrayList<>();
        boolean isMany = false;
        if(StringUtils.isEmpty(mode)||"0".equals(mode)){//默认为医生下所有病人病历信息导出
            yunRecordDocments = getyunRecordDocmentsByDoctorId(doctorId,dcode);
            isMany = true;
            list.add(doctorId);
        }else if("1".equals(mode)){//单个病人病历信息导出
            yunRecordDocments = new ArrayList<>();
            YunRecordDocment yunRecordDocment = getYunRecordDocment(patientId);
            yunRecordDocments.add(yunRecordDocment);
            list.add(patientId);
        }
        String filePath =produceExcel(yunRecordDocments,isMany,httpServletRequest);
        String diseaseName = yunRecordDocments==null?"病历信息":yunRecordDocments.get(0).getTypecode2();
        String fileName = diseaseName+DateUtils.getFormatDate(new Date(),DateUtils.DATE_FORMAT)+(isMany?".xlsx":".xls");
        download(filePath,httpServletResponse,fileName);
        return list;
    }
    /**
     * 根据病人id获取病人病历信息
     * @param patientId
     * @return
     * @throws Exception
     */
    public YunRecordDocment getYunRecordDocment(String patientId) throws Exception{
        String hql = "select r from YunRecordDocment as r,YunFolder as f where f.id = r.folderId and r.category<>'S'" +
                     " and f.patientId = '"+patientId+"'";
        List<YunRecordDocment> recordDocments = baseFacade.createQuery(YunRecordDocment.class,hql,new ArrayList<Object>()).getResultList();
        if(recordDocments!=null && !recordDocments.isEmpty()){
            return recordDocments.get(0);
        }else{
            throw new Exception("病历信息不存在");
        }
    }

    /**
     * 根据医生ID和疾病编码获取病人病历信息
     * @param doctorId
     * @param dcode
     * @return
     */
    public List<YunRecordDocment> getyunRecordDocmentsByDoctorId(String doctorId,String dcode){
        String hql = "select r from YunRecordDocment as r,YunFolder as f,YunPatient as p where f.id = r.folderId and f.patientId = p.id and r.category<>'S'" +
                     " and f.diagnosisCode = '"+dcode+"' and p.doctorId = '"+doctorId+"'";
        return baseFacade.createQuery(YunRecordDocment.class,hql,new ArrayList<Object>()).getResultList();
    }
    public List<Map> getModelTemplateVoList(String dcode,String title) throws Exception{
        List<Map> modelTemplateVoList = null;
        if(modelTempMap.get(dcode)==null){
            YunReleaseTemplet yunReleaseTemplet = getYunReleaseTemplet(dcode,title);
            String mbsj = "";
            if(yunReleaseTemplet==null){
                YunDisTemplet yunDisTemplet = getYunDisTemplet(dcode,title);
                if(yunDisTemplet!=null){
                    mbsj = yunDisTemplet.getMbsj();
                }
            }else {
                mbsj = yunReleaseTemplet.getMbsj();
            }
            modelTemplateVoList = (List<Map>) JSONUtil.JSONToObj(mbsj,new ArrayList<Map>().getClass());
            modelTempMap.put(dcode+"@"+title,modelTemplateVoList);
        }else{
            modelTemplateVoList = (List<Map>)modelTempMap.get(dcode+"@"+title);
        }
        return modelTemplateVoList;
    }

    public void fillTemplateMbsjMap(List<Map> modelTemplateVoList) throws Exception{
        if(modelTemplateVoList==null && modelTemplateVoList.isEmpty()){
            return;
        }
        Map modelTemplateVo = modelTemplateVoList.get(0);
        String dcode = modelTemplateVo.get("value")+"";
        StringBuffer stringBuffer = new StringBuffer("");
        for(Map map:modelTemplateVoList){
            stringBuffer.append(",'").append(map.get("name")).append("'");
        }
        String titles = stringBuffer.toString().substring(1);
        YunUsers yunUsers = UserUtils.getYunUsers();
        String hqlPrivate = " from YunDisTemplet as t where t.dcode='"+dcode+"' and t.title in ("+titles+") and (t.doctorId='"+yunUsers.getId()+"'" +
                " or (t.deptId='"+yunUsers.getDeptId()+"' and t.deptId<>'0'))" ;
        List<YunDisTemplet> list = baseFacade.createQuery(YunDisTemplet.class,hqlPrivate,new ArrayList<Object>()).getResultList();
        for(YunDisTemplet yunDisTemplet:list){
            formMap.put(yunDisTemplet.getDcode()+"@"+yunDisTemplet.getTitle(),yunDisTemplet.getMbsj());
        }
        String pubHql = "from YunReleaseTemplet as r where r.hstatus='R' and  r.dcode='"+dcode+"' and r.title in ("+titles+")" ;
        List<YunReleaseTemplet> resultList = baseFacade.createQuery(YunReleaseTemplet.class, pubHql, new ArrayList<Object>()).getResultList();
        for(YunReleaseTemplet yunReleaseTemplet:resultList){
            formMap.put(yunReleaseTemplet.getDcode()+"@"+yunReleaseTemplet.getTitle(),yunReleaseTemplet.getMbsj());
        }
    }

    /**
     * 下载生成的excel病历信息
     * @param path
     * @param response
     * @param fileName
     */
    private void download(String path, HttpServletResponse response,String fileName) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
            response.setContentLength(buffer.length);
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 生成excel格式的病历
     * @param yunRecordDocments
     * @param isMany
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    public String produceExcel(List<YunRecordDocment> yunRecordDocments,boolean isMany,HttpServletRequest httpServletRequest) throws Exception{
        String tempFilePath = SmsSendUtil.getStringByKey("tempFilePath");
        String path = httpServletRequest.getServletContext().getRealPath("/");
        String downFilePath = path+File.separator+"upload";
        tempFilePath = tempFilePath==null?downFilePath:tempFilePath;
        String randomfm = UUID.randomUUID().toString();
        String filePath = tempFilePath + File.separator  + randomfm + ".xlsx";
        if(yunRecordDocments==null && yunRecordDocments.isEmpty()){
            return filePath;
        }
        if(isMany){
            File folder = new File(tempFilePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet();
            sheet.setDefaultRowHeight((short) 600);
            //sheet.setColumnWidth(0, 15000);
            //sheet.setDefaultColumnWidth(15000);
            sheet.autoSizeColumn(1, true);
            int p=0;
            XSSFRow row =  sheet.createRow(p);//第一行为表头
            for(YunRecordDocment yunRecordDocment:yunRecordDocments){
                XSSFRow row2 = sheet.createRow(p+1);
                int colIndex = 0;
                int colValueIndex = 0;
                String content = yunRecordDocment.getContent();
                DocumentData documentData = (DocumentData)JSONUtil.JSONToObj(content,DocumentData.class);
                Map<String,Object> map = new HashMap<>();
                for(DocumentDataElement documentDataElement:documentData.getData()){
                    map.put(documentDataElement.getName(),documentDataElement.getData());
                }
                List<Map> modelTemplateVoList = getModelTemplateVoList(yunRecordDocment.getTypecode1(),yunRecordDocment.getTitle());

                for(int i=0;i<modelTemplateVoList.size();i++){
                    Map modelTemplateVo = modelTemplateVoList.get(i);
                    String key = modelTemplateVo.get("value")+"@"+modelTemplateVo.get("name");
                    if(formMap.get(key)==null){
                        fillTemplateMbsjMap(modelTemplateVoList);
                    }
                    Form form = formMap.get(key)==null?null:getFormData(formMap.get(key)+"");
//                    Form form = getPubTemplateForm(modelTemplateVo.get("value")+"",modelTemplateVo.get("name")+"");
//                    if(form==null){
//                        form = getPrivateTemplateForm(modelTemplateVo.get("value")+"",modelTemplateVo.get("name")+"");
//                    }
                    Object data = map.get(modelTemplateVo.get("name"));
                    Map valueMap = (Map)JSONUtil.JSONToObj(JSONUtil.objectToJsonString(data),Map.class);
                    if(form!=null && !form.getForm_template().getPages().isEmpty()){
                        List<ModelPage> pages = form.getForm_template().getPages();
                        for(ModelPage modelPage:pages){
                            String modelTitle = modelPage.getTitle();//excel头部--基本情况
                            List<RowObject> rowObjects = modelPage.getRowssubjects();
                            colIndex = writeToFileByType(wb,row,row2,colIndex,colValueIndex,rowObjects,sheet,p,valueMap,isMany);
                            colValueIndex = colIndex;
                        }
                    }
                }
                p++;
            }
            FileOutputStream out = null;
            try {
                File file = new File(filePath);
                out  =  new FileOutputStream(file);
                wb.write(out);
                out.close();
            } catch (IOException e) {
                throw new RuntimeException("文件写入异常！" + e.getMessage());
            } finally {
                out = null;
            }
        }else{
            YunRecordDocment yunRecordDocment = yunRecordDocments.get(0);
            String content = yunRecordDocment.getContent();
            DocumentData documentData = (DocumentData)JSONUtil.JSONToObj(content,DocumentData.class);
            Map<String,Object> map = new HashMap<>();
            for(DocumentDataElement documentDataElement:documentData.getData()){
                map.put(documentDataElement.getName(),documentDataElement.getData());
            }
            List<Map> modelTemplateVoList = getModelTemplateVoList(yunRecordDocment.getTypecode1(),yunRecordDocment.getTitle());
            File folder = new File(tempFilePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            HSSFWorkbook wb = new HSSFWorkbook();
            for(int i=0;i<modelTemplateVoList.size();i++){
                File file = new File(filePath);
                FileOutputStream out =  new FileOutputStream(file);
                Map modelTemplateVo = modelTemplateVoList.get(i);
                Form form = getPubTemplateForm(modelTemplateVo.get("value")+"",modelTemplateVo.get("name")+"");
                if(form==null){
                    form = getPrivateTemplateForm(modelTemplateVo.get("value")+"",modelTemplateVo.get("name")+"");
                }
                Object data = map.get(modelTemplateVo.get("name"));
                Map valueMap = (Map)JSONUtil.JSONToObj(JSONUtil.objectToJsonString(data),Map.class);
                if(form!=null && !form.getForm_template().getPages().isEmpty()){
                    List<ModelPage> pages = form.getForm_template().getPages();
                    for(ModelPage modelPage:pages){
                        String modelTitle = modelPage.getTitle();//excel头部--基本情况
                        List<RowObject> rowObjects = modelPage.getRowssubjects();
                        writeToFile(wb,out,rowObjects,modelTitle,i,valueMap);
                    }
                }
            }
        }
        return filePath;
    }
    private int writeToFileByType(XSSFWorkbook wb,XSSFRow row,XSSFRow row2,int colIndex,int colValueIndex,List<RowObject> rowObjects,XSSFSheet sheet,int p,Map valueMap,boolean isMany) throws Exception{
        XSSFCellStyle style = createHSSFCellStyle(wb);
        XSSFCellStyle styleBolder = createBolderXSSFCellStyle(wb);
        for(RowObject rowObject:rowObjects){
            ElementRow elementRow = rowObject.getRows().get(0);
            String rowName = elementRow.getName();//EMAIL 此为key 通过key获取邮箱对应的值
            String type = elementRow.getType();//text
            String head = elementRow.getExtend().getHead();//邮箱
            String relyon = elementRow.getExtend().getRelyon();//数据依赖
            if(p==0 && elementRow.getItems().isEmpty()){
                XSSFCell noCell = row.createCell(colIndex++);
                noCell.setCellValue(removeHtmlToken(head));
                noCell.setCellStyle(styleBolder);
            }
            if(elementRow.getItems().isEmpty()){
                XSSFCell cell2 = row2.createCell(colValueIndex++);
                cell2.setCellStyle(style);
                    if("text".equals(type)){
                        if(valueMap!=null && valueMap.get(rowName)!=null){
                            cell2.setCellValue(removeHtmlToken(valueMap.get(rowName)+""));
                        }
                    }else if("time".equals(type)){
                        //String v = DateUtils.getFormatDate(new Date(),DateUtils.TIME_FORMAT_A);
                        if(valueMap!=null && valueMap.get(rowName)!=null){
                            cell2.setCellValue(removeHtmlToken(valueMap.get(rowName)+""));
                        }
                    }else{
                        if(valueMap!=null && valueMap.get(rowName)!=null){
                            cell2.setCellValue(removeHtmlToken(valueMap.get(rowName)+""));
                        }
                    }
                }else{
                    int indexItem = 0;
                    if(type.contains("select")){//select_radio,select_block,select
                        if(p==0){
                            XSSFCell noCell = row.createCell(colIndex++);
                            noCell.setCellValue(removeHtmlToken(head));
                            noCell.setCellStyle(styleBolder);
                        }
                        if(valueMap!=null && !StringUtils.isEmpty(valueMap.get(rowName)+"")){
                            String valueit = valueMap.get(rowName)+"";
                            XSSFCell cellItem = row2.createCell(colValueIndex++);
                            cellItem.setCellStyle(style);
                            StringBuffer cellValue=new StringBuffer("");
                            for(RowItem rowItem:elementRow.getItems()){
                                if(rowItem.getName().equals(rowName) && ifContainsValue(valueit,rowItem.getValue())){
                                    cellValue.append(",").append(removeHtmlToken(rowItem.getText()));
                                }
                            }
                            cellItem.setCellValue(cellValue.toString().length()>0?cellValue.toString().substring(1):"");
                        }
                    } else {
                        for(RowItem rowItem:elementRow.getItems()){//完成多条是否选择显示
                            if(p==0){
                                XSSFCell cellItem = row.createCell(colIndex++);
                                cellItem.setCellValue(head+"-"+removeHtmlToken(rowItem.getText()));
                                cellItem.setCellStyle(style);
                            }
                            XSSFCell cellItem1 = row2.createCell(colValueIndex++);
                            String key = rowItem.getName()+"_"+rowItem.getValue()+"_"+(indexItem++);
                            if(valueMap!=null && "1".equals(valueMap.get(key))){
                                cellItem1.setCellValue("是");
                                cellItem1.setCellStyle(style);
                            }else if(valueMap!=null && "0".equals(valueMap.get(key))){
                                cellItem1.setCellValue("否");
                                cellItem1.setCellStyle(style);
                            }else{
                                cellItem1.setCellValue("");
                                cellItem1.setCellStyle(style);
                            }
                        }
                    }
                }
        }
        return colIndex;
    }

    private void writeToFile(HSSFWorkbook wb,FileOutputStream out,List<RowObject> rowObjects,String sheetName,int i,Map valueMap) throws Exception{
        HSSFCellStyle style = createHSSFCellStyle(wb);
        HSSFCellStyle styleBolder = createBolderHSSFCellStyle(wb);
        HSSFSheet sheet = this.createGridSheet(wb,removeHtmlToken(sheetName));
        wb.setSheetName(i,removeHtmlToken(sheetName));
        sheet.setColumnWidth(0, 15000);
        int rowIndex = 1;
        for(RowObject rowObject:rowObjects){
            ElementRow elementRow = rowObject.getRows().get(0);
            String rowName = elementRow.getName();//EMAIL 此为key 通过key获取邮箱对应的值
            String type = elementRow.getType();//text
            String head = elementRow.getExtend().getHead();//邮箱
            String relyon = elementRow.getExtend().getRelyon();//数据依赖
            if(StringUtils.isEmpty(relyon) || (!StringUtils.isEmpty(relyon) && valueMap!=null && "1".equals(valueMap.get(relyon)))){//值为是
                HSSFRow row = sheet.createRow(rowIndex++);
                HSSFCell noCell = row.createCell(0);
                noCell.setCellValue(removeHtmlToken(head));
                noCell.setCellStyle(styleBolder);
                if(elementRow.getItems().isEmpty()){
                    HSSFRow row2 = sheet.createRow(rowIndex++);
                    row2.setHeightInPoints(20);
                    HSSFCell cell2 = row2.createCell(0);
                    cell2.setCellStyle(style);
                    if("text".equals(type)){
                        if(valueMap!=null && valueMap.get(rowName)!=null){
                            cell2.setCellValue(removeHtmlToken(valueMap.get(rowName)+""));
                        }
                    }else if("time".equals(type)){
                        //String v = DateUtils.getFormatDate(new Date(),DateUtils.TIME_FORMAT_A);
                        if(valueMap!=null && valueMap.get(rowName)!=null){
                            cell2.setCellValue(removeHtmlToken(valueMap.get(rowName)+""));
                        }
                    }else{
                        if(valueMap!=null && valueMap.get(rowName)!=null){
                            cell2.setCellValue(removeHtmlToken(valueMap.get(rowName)+""));
                        }
                    }
                }else{
                    int indexItem = 0;
                    if(type.contains("select")){//select_radio,select_block,select
                        if(valueMap!=null && !StringUtils.isEmpty(valueMap.get(rowName)+"")){
                            String valueit = valueMap.get(rowName)+"";
                            for(RowItem rowItem:elementRow.getItems()){
                                if(rowItem.getName().equals(rowName) && ifContainsValue(valueit,rowItem.getValue())){
                                    HSSFRow rowIt = sheet.createRow(rowIndex++);
                                    HSSFCell cellItem = rowIt.createCell(0);
                                    cellItem.setCellValue(removeHtmlToken(rowItem.getText()));
                                    cellItem.setCellStyle(style);
                                }
                            }
                        }
                    } else {
                        for(RowItem rowItem:elementRow.getItems()){//完成多条是否选择显示
                            HSSFRow rowIt = sheet.createRow(rowIndex++);
                            HSSFCell cellItem = rowIt.createCell(0);
                            cellItem.setCellValue(removeHtmlToken(rowItem.getText()));
                            cellItem.setCellStyle(style);
                            HSSFCell cellItem1 = rowIt.createCell(1);
                            String key = rowItem.getName()+"_"+rowItem.getValue()+"_"+(indexItem++);
                            if(valueMap!=null && "1".equals(valueMap.get(key))){
                                cellItem1.setCellValue("是");
                                cellItem1.setCellStyle(style);
                            }else if(valueMap!=null && "0".equals(valueMap.get(key))){
                                cellItem1.setCellValue("否");
                                cellItem1.setCellStyle(style);
                            }
                        }
                    }
                }
            }
        }
        wb.write(out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("文件写入异常！" + e.getMessage());
        } finally {
            out = null;
        }
    }

    private HSSFSheet createGridSheet(HSSFWorkbook wb,String sheetName) {
        HSSFCellStyle style = createHSSFCellStyle(wb);
        HSSFSheet sheet = wb.createSheet(sheetName);
        return sheet;
    }

    private HSSFCellStyle createHSSFCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle(); // 单元格格式
        style.setWrapText(true); // 设置自动换行
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        SetStyleBorder(style,(short)3);
        return style;
    }

    private XSSFCellStyle createHSSFCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle(); // 单元格格式
        style.setWrapText(true); // 设置自动换行
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setBorderBottom((short) 3);
        style.setBorderLeft((short) 3);
        style.setBorderRight((short) 3);
        style.setBorderTop((short) 3);
        return style;
    }

    private XSSFCellStyle createBolderXSSFCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle(); // 单元格格式
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 15);
        style.setWrapText(true); // 设置自动换行
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setBorderBottom((short) 3);
        style.setBorderLeft((short) 3);
        style.setBorderRight((short) 3);
        style.setBorderTop((short) 3);
        style.setFont(font);
        return style;
    }

    private HSSFCellStyle createBolderHSSFCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle(); // 单元格格式
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 15);
        style.setWrapText(true); // 设置自动换行
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        SetStyleBorder(style,(short)3);
        style.setFont(font);
        return style;
    }
    private void SetStyleBorder(HSSFCellStyle style,short k){
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setBorderBottom((short) k);
        style.setBorderLeft((short) k);
        style.setBorderRight((short) k);
        style.setBorderTop((short) k);
    }

    public boolean ifContainsValue(String arrayStr,String value){
        boolean isContain = false;
        if(!StringUtils.isEmpty(arrayStr)){
            if(arrayStr.equals(value)){
                isContain = true;
            }else if(arrayStr.indexOf("[")>-1 && arrayStr.indexOf("]")>-1 && arrayStr.contains(value)){
                isContain = true;
            }
        }
        return isContain;
    }

    /**
     * 去除html标签，后续可再优化下
     * @param htmlStr
     * @return
     */
    public String removeHtmlToken(String htmlStr){
        if(htmlStr==null||"".equals(htmlStr)){
            return htmlStr;
        }
        htmlStr = htmlStr.replace("（","(");
        htmlStr = htmlStr.replace("）",")");
        htmlStr = htmlStr.replace("&nbsp;","");
        htmlStr = htmlStr.replace("@","");
        htmlStr = htmlStr.replace("："," ");
        htmlStr = htmlStr.replace("&gt;","");
        htmlStr = htmlStr.replace("&le;","");
        return htmlStr.replaceAll("</?[^>]+>", "");
    }

    /**
     * 根据疾病编码和标题查询疾病模板
     * @param dcode
     * @param title
     * @return
     */
    public YunReleaseTemplet getYunReleaseTemplet(String dcode,String title){
        String hql = "from YunReleaseTemplet as r where r.hstatus='R' and  r.dcode='"+dcode+"' and r.title='"+title+"'" ;
        List<YunReleaseTemplet> resultList = baseFacade.createQuery(YunReleaseTemplet.class, hql, new ArrayList<Object>()).getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 获取公有的模板设计内容
     * @param dcode
     * @param title
     * @return
     * @throws Exception
     */
    public Form getPubTemplateForm(String dcode,String title) throws Exception {
        YunReleaseTemplet yunReleaseTemplet = getYunReleaseTemplet(dcode,title);
        if(yunReleaseTemplet!=null){
            String mbsj = yunReleaseTemplet.getMbsj();
            if(mbsj!=null&&!"".equals(mbsj)){
                return getFormData(mbsj);
            }else{
                return  null ;
            }
        }else{
            return null;
        }
    }

    /**
     * 获取私有的模板
     * @param dcode
     * @param title
     * @return
     * @throws Exception
     */
    public YunDisTemplet getYunDisTemplet(String dcode,String title) throws Exception{
        YunUsers yunUsers = UserUtils.getYunUsers();
        String hqlPrivate = "from YunDisTemplet as t where t.dcode='"+dcode+"' and t.title='"+title+"' and (t.doctorId='"+yunUsers.getId()+"'" +
                " or (t.deptId='"+yunUsers.getDeptId()+"' and t.deptId<>'0'))" ;
        List<YunDisTemplet> yunDisTemplets = baseFacade.createQuery(YunDisTemplet.class, hqlPrivate, new ArrayList<Object>()).getResultList();
        if(yunDisTemplets.size()>0){
            return yunDisTemplets.get(0);
        }else{
            return null;
        }
    }
    /**
     * 获取私有的模板设计内容
     * @param dcode
     * @param title
     * @return
     * @throws Exception
     */
    public Form getPrivateTemplateForm(String dcode,String title) throws Exception {
        YunDisTemplet tmplate = getYunDisTemplet(dcode,title);
        if(tmplate!=null){
            String mbsj = tmplate.getMbsj();
            if(mbsj!=null&&!"".equals(mbsj)){
                return getFormData(mbsj);
            }else{
                return  null ;
            }
        }else{
            return null ;
        }
    }
    /**
     * 根据模板数据设置Form
     * @param mbsj
     * @return
     * @throws Exception
     */
    private Form getFormData(String mbsj) throws Exception {

        Form o = (Form) JSONUtil.JSONToObj(mbsj, Form.class);

        List<FormData> form_datas = o.getForm_data();
        FormPage formPage = new FormPage();

        for(FormData formData :form_datas){
            ModelPage modelPage = new ModelPage();
            modelPage.setTitle(formData.getName());
            modelPage.setValue(formData.getValue());

            List<Row> rows = formData.getRows();
            List<RowObject> rowssubjects = modelPage.getRowssubjects();
            for (Row row:rows){
                RowObject rowObject = new RowObject();
                setRowObject(row,rowObject);
                rowssubjects.add(rowObject);
            }
            formPage.getPages().add(modelPage);
        }
        o.setForm_template(formPage);
        return  o;
    }
    /**
     * 根据formData Row 设置templateObject 的rowObject
     * @param row
     * @param rowObject
     */
    private void setRowObject(Row row, RowObject rowObject) throws Exception {

        List<Col> cols = row.getCols();
        if(cols.size()>0){
            rowObject.setColumn(String.valueOf(12/cols.size()));
        }

        List<ElementRow> rows = rowObject.getRows();
        for (Col col:cols){
            ElementRow elementRow = new ElementRow();
            elementRow.setName(col.getValue());
            setElementRow(elementRow,col);
            rows.add(elementRow);
        }

    }
    private void setElementRow(ElementRow elementRow, Col col) throws Exception {
        String value = col.getValue();
        YunUsers yunUsers = UserUtils.getYunUsers();
        String deptId = yunUsers.getDeptId();
        String id = yunUsers.getId();
        value = StringUtils.replaceBank(value);//元数据格式横杆线改成下划线即 '-'改为'_'
        String hql = "select t from YunValueFormat t,YunValue as v  where t.id=v.id and  v.name='"+value+"'";

        List<YunValueFormat> resultList = baseFacade.createQuery(YunValueFormat.class, hql, new ArrayList<Object>()).getResultList();
        if(resultList.size()<1){
            String hql2 = "select t from YunValueFormat t,YunValue as v  where t.id=v.id and  v.name like '"+value+"%'";
            resultList = baseFacade.createQuery(YunValueFormat.class, hql2, new ArrayList<Object>()).getResultList();
            if(resultList.isEmpty()){
                throw new Exception("获取名称为【"+value+"】的元数据格式失败！");
            }
        }
        YunValueFormat yunValueFormat = resultList.get(0);
        YunValue yunValue =baseFacade.get(YunValue.class,yunValueFormat.getId());
        String valueDoctorId = yunValue.getDoctorId();

        DataElementFormat dataElement = (DataElementFormat) JSONUtil.JSONToObj(yunValueFormat.getFormat(), DataElementFormat.class);
        Extend extend = new Extend();
        String extend1 = dataElement.getExtend();
        if(extend1!=null&&!"".equals(extend1)){
            Extend obj = (Extend) JSONUtil.JSONToObj(extend1,Extend.class);
            extend.setHead(obj.getHead());
            extend.setPlac(obj.getPlac());
            extend.setTail(obj.getTail());
        }else{
            extend.setHead(dataElement.getHead());
            extend.setPlac(dataElement.getPlac());
            extend.setTail(dataElement.getTail());
        }
        String relyonvalue = dataElement.getRelyonvalue();

        String relyon = yunValueFormat.getRelyon();
        if(!"".equals(relyon)&&null!=relyon&&!"".equals(relyonvalue)&&null!=relyonvalue){

        }
        extend.setRelyon(relyon);
        extend.setRelyonvalue(relyonvalue);

        extend.setTemplet(dataElement.getTemplet());
        elementRow.setExtend(extend);
        elementRow.setType(dataElement.getPart());
        elementRow.setName(StringUtils.replaceBank(value));//原先为带有'-'改为'_'
        String dict = yunValueFormat.getDict();

        if(dict!=null&&!"".equals(dict)){
            String hqlDict = "select yi from YunDicttype as yd,YunDictitem yi  where yd.id=yi.typeIdDm and yd.typeName='"+dict+"'" +
                    " and ((yd.deptId='0' and yd.userId='"+id+"') or (yd.deptId='"+deptId+"' and yd.userId='"+id+"'))" +
                    " order by yd.userId ,yd.deptId desc" ;
            List<YunDictitem> resultList1 = baseFacade.createQuery(YunDictitem.class, hqlDict, new ArrayList<Object>()).getResultList();
            if(resultList1.size()<1){
                String hqlPubDict = "select yi from YunDicttype as yd,YunDictitem yi  where yd.id=yi.typeIdDm and yd.typeName='"+dict+"' " +
                        " and yd.userId='0'" ;
                resultList1 = baseFacade.createQuery(YunDictitem.class, hqlPubDict, new ArrayList<Object>()).getResultList();
                if(resultList1.size()<1){

                    String hqlOther = "select yi from YunDicttype as yd,YunDictitem yi  where yd.id=yi.typeIdDm and yd.typeName='"+dict+"'" +
                            " and yd.userId='"+valueDoctorId+"' ";
                    resultList1 = baseFacade.createQuery(YunDictitem.class, hqlOther, new ArrayList<Object>()).getResultList();
                    if(resultList1.size()<1){
                        throw new Exception("获取名称为【"+dict+"】的字典失败");
                    }
                }
            }

            for(YunDictitem yunDictitem:resultList1){
                RowItem rowItem = new RowItem();
                rowItem.setInputCode(yunDictitem.getInputCode());
                rowItem.setName(StringUtils.replaceBank(value));
                rowItem.setText(yunDictitem.getItemName());
                rowItem.setValue(yunDictitem.getItemCode());
                elementRow.getItems().add(rowItem);
            }
        }
    }
}
