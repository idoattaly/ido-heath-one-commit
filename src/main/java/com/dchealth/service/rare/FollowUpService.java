package com.dchealth.service.rare;

import com.dchealth.VO.FollowRecordData;
import com.dchealth.VO.FollowUpPlainVo;
import com.dchealth.VO.RemindInfo;
import com.dchealth.entity.rare.YunFolder;
import com.dchealth.entity.rare.YunFollowUp;
import com.dchealth.entity.rare.YunRecordDocment;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.DateUtils;
import com.dchealth.util.IDUtils;
import com.dchealth.util.JSONUtil;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
@Produces("application/json")
@Controller
@Path("follow")
public class FollowUpService {

    @Autowired
    private BaseFacade baseFacade;

    /**
     * 根据病人id获取随访计划
     * @param sickId
     * @return
     */
    @GET
    @Path("follow-up-plain")
    public FollowUpPlainVo getFollowUpPlains(@QueryParam("sickId") String sickId){
       String Hql = "select r from YunPatient as p,YunFolder as f,YunRecordDocment as r where r.folderId = f.id " +
                    " and f.patientId = p.id and r.category = 'S' and p.id = '"+sickId+"'";
       List<YunRecordDocment> yunRecordDocments = baseFacade.createQuery(YunRecordDocment.class,Hql,new ArrayList<Object>()).getResultList();
        FollowUpPlainVo followUpPlainVo = null;
        if(yunRecordDocments!=null && !yunRecordDocments.isEmpty()){
            followUpPlainVo = (FollowUpPlainVo)JSONUtil.JSONToObj(yunRecordDocments.get(0).getContent(),FollowUpPlainVo.class);
        }else {
            followUpPlainVo = new FollowUpPlainVo();
        }
       return followUpPlainVo;
    }

    /**
     *根据病人id 间隔时间，提前醒时间，是否按当前时间计算获取病人随访提醒时间信息
     * @param sickId 病人id
     * @param followIntervalTime  间隔时间 单位为月
     * @param followAdvanceTime 提醒时间 单位为天
     * @param isNow 是否按系统当前时间计算
     * @return
     */
    @GET
    @Path("follow-up-remind")
    public List<RemindInfo> getFollowUpRemindInfo(@QueryParam("sickId") String sickId,@QueryParam("followIntervalTime") String followIntervalTime,
                                       @QueryParam("followAdvanceTime") String followAdvanceTime,@QueryParam("isNow") String isNow){
        List<RemindInfo> remindInfos = new ArrayList<>();
        Timestamp startDate = new Timestamp(System.currentTimeMillis());
        if(isNow!=null && !"".equals(isNow)){
            String hql = "select p.createDate from YunPatient as p where p.id = '"+sickId+"'";
            startDate = baseFacade.createQuery(Timestamp.class,hql,new ArrayList<Object>()).getSingleResult();
        }
        List<Timestamp> followDays = getNeedDays(startDate,followIntervalTime);
        List<Timestamp> remindDays = getRemindDays(followDays,followAdvanceTime);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<6;i++){
            RemindInfo remindInfo = new RemindInfo();
            remindInfo.setSerial((i+1)+"");
            remindInfo.setFollow(sdf.format(followDays.get(i)));
            remindInfo.setRemind(sdf.format(remindDays.get(i)));
            remindInfos.add(remindInfo);
        }
        return remindInfos;
    }

    /**
     * 根据开始时间和时间间隔获取随访时间记录（6条）
     * @param startDate
     * @param followIntervalTime
     * @return
     */
    public List<Timestamp> getNeedDays(Timestamp startDate,String followIntervalTime){
        List<Timestamp> list = new ArrayList<>();
        Integer monthInterval = Integer.valueOf(followIntervalTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        for(int i=0;i<6;i++){
            calendar.add(Calendar.MONTH,monthInterval);
            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
            list.add(timestamp);
        }
        return list;
    }
    /**
     * 根据随访时间算出其提醒时间
     * @param followDays
     * @param followAdvanceTime
     * @return
     */
    public List<Timestamp> getRemindDays(List<Timestamp> followDays,String followAdvanceTime){
        List<Timestamp> list = new ArrayList<>();
        Integer dayInterval = Integer.valueOf(followAdvanceTime);
        for(int i=0;i<followDays.size();i++){
            Timestamp timestamp = followDays.get(i);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(timestamp);
            calendar1.add(Calendar.DAY_OF_MONTH,-dayInterval);
            list.add(new Timestamp(calendar1.getTimeInMillis()));
        }
        return list;
    }
    /**
     * 根据病人id更新或保存提交的随访计划
     * @param sickId
     * @param followUpPlainVo
     * @return
     */
    @POST
    @Path("merge-follow-up")
    @Transactional
    public Response mergeFollowUpPlain(@QueryParam("sickId")String sickId,FollowUpPlainVo followUpPlainVo){
        String Hql = "select r from YunPatient as p,YunFolder as f,YunRecordDocment as r where r.folderId = f.id " +
                " and f.patientId = p.id and r.category = 'S' and p.id = '"+sickId+"'";
        try {
        List<YunRecordDocment> yunRecordDocments = baseFacade.createQuery(YunRecordDocment.class,Hql,new ArrayList<Object>()).getResultList();
        String dcode = followUpPlainVo.getData().isEmpty()?"":followUpPlainVo.getData().get(0).getDcode();
        String jsonContent = JSONUtil.objectToJsonString(followUpPlainVo);
        if(yunRecordDocments!=null && !yunRecordDocments.isEmpty()){
            YunRecordDocment yunRecordDocment = yunRecordDocments.get(0);
            String content = yunRecordDocment.getContent();
            if(!"".equals(jsonContent) && !jsonContent.equals(content)){//随访计划不一致则保存更新
                yunRecordDocment.setContent(jsonContent);
                yunRecordDocment.setModifyDate(new Timestamp(new Date().getTime()));
                baseFacade.merge(yunRecordDocment);
                addFollowUp(followUpPlainVo,sickId);
             }
           }else{
                String folderHql = " from YunFolder as f where f.patientId = '"+sickId+"' and f.diagnosisCode = '"+dcode+"'";
                List<YunFolder> yunFolders = baseFacade.createQuery(YunFolder.class,folderHql,new ArrayList<Object>()).getResultList();
                String folderId = "";
                if(yunFolders!=null && !yunFolders.isEmpty()){
                    folderId = yunFolders.get(0).getId();
                }else{
                    YunFolder yunFolder = new YunFolder();
                    yunFolder = mergeYunFolder(yunFolder,sickId,dcode);
                    folderId = yunFolder.getId();
                }
                YunRecordDocment yunRecordDocment = new YunRecordDocment();
                mergeYunRecordDocment(yunRecordDocment,folderId,jsonContent);
                addFollowUp(followUpPlainVo,sickId);
           }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(followUpPlainVo).build();
    }
    /**
     * 生成病历夹信息
     * @param yunFolder
     * @param sickId
     * @param dcode
     * @return
     */
    public YunFolder mergeYunFolder(YunFolder yunFolder,String sickId,String dcode){
        yunFolder.setCreateDate(new Timestamp(new Date().getTime()));
        yunFolder.setActioncode("病例研究");
        yunFolder.setActionid(String.valueOf(IDUtils.genItemId()));
        yunFolder.setDiagnosisCode(dcode);
        yunFolder.setPatientId(sickId);
        return yunFolder = baseFacade.merge(yunFolder);
    }
    /**
     * 生成随访计划
     * @param yunRecordDocment
     * @param folderId
     * @param jsonContent
     * @return
     */
    public YunRecordDocment mergeYunRecordDocment(YunRecordDocment yunRecordDocment,String folderId,String jsonContent){
        yunRecordDocment.setFolderId(folderId);
        yunRecordDocment.setCategory("S");
        yunRecordDocment.setTitle("随访计划");
        yunRecordDocment.setTypecode1("JH");
        yunRecordDocment.setTypecode2("CONFIG");
        yunRecordDocment.setContent(jsonContent);
        yunRecordDocment.setCreateDate(new Timestamp(new Date().getTime()));
        yunRecordDocment.setModifyDate(new Timestamp(new Date().getTime()));
        return yunRecordDocment = baseFacade.merge(yunRecordDocment);
    }
    /**
     * 生成随访记录
     * @param followUpPlainVo
     * @param sickId
     */
    public void addFollowUp(FollowUpPlainVo followUpPlainVo,String sickId){
        List<FollowRecordData> followRecordDatas = followUpPlainVo.getData();
            for(FollowRecordData followRecordData:followRecordDatas){
                YunFollowUp yunFollowUp = new YunFollowUp();
                yunFollowUp.setPatientId(sickId);
                yunFollowUp.setSerialNumber(followRecordData.getSerial());
                yunFollowUp.setFollowDate(DateUtils.convertStringToDate(followRecordData.getFollow()));
                yunFollowUp.setRemindDate(DateUtils.convertStringToDate(followRecordData.getRemind()));
                yunFollowUp.setHstatus("S");
                yunFollowUp.setDcode(followRecordData.getDcode());
                yunFollowUp.setTitle(followRecordData.getTitle());
                baseFacade.merge(yunFollowUp);
            }
    }
}
