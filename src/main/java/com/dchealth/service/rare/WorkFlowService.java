package com.dchealth.service.rare;

import com.dchealth.VO.*;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.entity.rare.*;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.provider.FolderSaveException;
import com.dchealth.util.*;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
@Controller
@Produces("application/json")
@Path("work-flow")
public class WorkFlowService {

    @Autowired
    private BaseFacade baseFacade ;


    /**
     * 根据ID获取工作流信息
     * @param id
     * @return
     */
    @GET
    @Path("get-work-flow")
    public YunDisTemplateVo getWorkFlow(@QueryParam("id") String id){
        String hql ="from  YunDisTemplet as t where t.mblx='WORK' and t.id='"+id+"'";
        YunDisTemplet singleResult = baseFacade.createQuery(YunDisTemplet.class, hql, new ArrayList<Object>()).getSingleResult();
        YunDisTemplateVo vo = new YunDisTemplateVo();
        setYunDisTemplateVo(vo,singleResult);
        return vo ;
    }

    /**
     * 获取工作流模板
     * @param dcode 疾病编码
     * @param title 工作流标题
     * @param doctorId 医生ID
     * @return
     */
    @GET
    @Path("list-work-flow")
    public List<YunDisTemplateVo> listWorkFlow(@QueryParam("dcode") String dcode , @QueryParam("title") String title, @QueryParam("doctorId") String doctorId){
        String hql = "from YunDisTemplet as t where t.mblx='WORK'" ;
        List<YunDisTemplateVo> yunDisTemplateVos = new ArrayList<>() ;
        if(!"".equals(dcode)&&null!=dcode){
            hql +=" and t.dcode='"+dcode+"'";
        }
        if(!"".equals(title)&&null!=title){
            hql +=" and t.title='"+title+"'";
        }
        if(!"".equals(doctorId)&&null!=doctorId){
            hql +=" and t.doctorId='"+doctorId+"'";
        }
        List<YunDisTemplet> yunDisTemplets = baseFacade.createQuery(YunDisTemplet.class,hql,new ArrayList<Object>()).getResultList();
        for(YunDisTemplet templet:yunDisTemplets){
            YunDisTemplateVo vo = new YunDisTemplateVo();
            setYunDisTemplateVo(vo,templet);
            yunDisTemplateVos.add(vo);
        }
        return yunDisTemplateVos;

    }

    /**
     * 设置展示对象
     * @param vo
     * @param templet
     */
    private void setYunDisTemplateVo(YunDisTemplateVo vo ,YunDisTemplet templet){
        vo.setDcode(templet.getDcode());
        vo.setDeptId(templet.getDeptId());
        vo.setId(templet.getId());
        vo.setDoctorId(templet.getDoctorId());
        vo.setMblx(templet.getMblx());
        vo.setModifyDate(templet.getModifyDate());
        vo.setTitle(templet.getTitle());
        vo.setNote(templet.getNote());
        String mbsj = templet.getMbsj();
        if(!"".equals(mbsj)&&null!=mbsj){
            List<ModelTemplateVo> vos = (List<ModelTemplateVo>) JSONUtil.JSONToObj(templet.getMbsj(),new ArrayList<ModelTemplateVo>().getClass());
            vo.setMbsj(vos);
        }else{
            vo.setMbsj(new ArrayList<ModelTemplateVo>());
        }
    }


    /**
     * 修改工作流模板
     * @param yunDisTemplateVo
     * @return
     */
    @POST
    @Path("merge-work-flow")
    @Transactional
    public Response mergeWorkFlow(YunDisTemplateVo yunDisTemplateVo) throws IOException, JSONException {
        YunDisTemplet yunDisTemplet = new YunDisTemplet();
        yunDisTemplet.setDcode(yunDisTemplateVo.getDcode());
        yunDisTemplet.setDeptId(yunDisTemplateVo.getDeptId());
        yunDisTemplet.setId(yunDisTemplateVo.getId());
        yunDisTemplet.setDoctorId(yunDisTemplateVo.getDoctorId());
        yunDisTemplet.setMblx(yunDisTemplateVo.getMblx());
        yunDisTemplet.setModifyDate(yunDisTemplateVo.getModifyDate());
        yunDisTemplet.setTitle(yunDisTemplateVo.getTitle());
        yunDisTemplet.setNote(yunDisTemplateVo.getNote());
        String mbsj = JSONUtil.objectToJsonString(yunDisTemplateVo.getMbsj());
        yunDisTemplet.setMbsj(mbsj);
        YunDisTemplet merge = baseFacade.merge(yunDisTemplet);
        return  Response.status(Response.Status.OK).entity(merge).build();
    }

    /***
     * 新建病人获取工作流数据
     * @param dcode
     * @param title
     * @param doctorId
     * @return
     */
    @GET
    @Path("list-work-flow-item")
    public List<ModelTemplateVo> getWorkFlowDocumentElement(@QueryParam("dcode") String dcode,@QueryParam("title") String title,@QueryParam("doctorId") String doctorId) throws Exception {
        String hql = "from YunReleaseTemplet as t where t.hstatus='R' and t.dcode='"+dcode+"' and t.title='"+title+"'" ;
        List<ModelTemplateVo> documentDataElements = new ArrayList<>();
        List<YunReleaseTemplet> resultList = baseFacade.createQuery(YunReleaseTemplet.class, hql, new ArrayList<Object>()).getResultList();
        for(YunReleaseTemplet templet:resultList){
            Hversion hversion= (Hversion) JSONUtil.JSONToObj(templet.getHversion(),Hversion.class);
            if(templet.getMbsj()!=null&&!"".equals(templet.getMbsj())){
                List<ModelTemplateVo> mbsj = (List<ModelTemplateVo>) JSONUtil.JSONToObj(templet.getMbsj(),new ArrayList<ModelTemplateVo>().getClass());
                documentDataElements.addAll(mbsj);
            }
        }
        //如果没有发布数据，则用私有数据
        if(documentDataElements.size()==0){
            if(null==doctorId||"".equals(doctorId)){
                YunUsers yunUsers = UserUtils.getYunUsers();
                doctorId = yunUsers.getId();
            }
            List<YunDisTemplateVo> yunDisTemplateVos = listWorkFlow(dcode, title, doctorId);
            for (YunDisTemplateVo vo:yunDisTemplateVos){
                documentDataElements.addAll(vo.getMbsj());
            }
        }
        return documentDataElements;
    }


    /**
     * 根据病人的ID获取获取填写的表单信息
     * @param patientId
     * @return
     */
    @GET
    @Path("list-record-doc")
    public List<YunRecordDocment> getYunRecordDocument(@QueryParam("patientId") String patientId){
        String hql = "select d from YunFolder as f ,YunRecordDocment as d where f.id=d.folderId and d.category <> 'S'" +
                " and f.patientId='"+patientId+"'" ;
        return baseFacade.createQuery(YunRecordDocment.class,hql,new ArrayList<Object>()).getResultList();
    }


    /**
     * 修改保存信息
     * @return
     */
    @POST
    @Path("merge-record-doc")
    @Transactional
    public  Response addDocument(PostData postData) throws Exception {
        PostDocumentData postDocumentData = postData.getPostDocumentData();
        PostPara postPara = postData.getPostPara();
        String status = postDocumentData.getStatus();
        YunUsers yunUsers = UserUtils.getYunUsers();
        String docId = postPara.getDocId();
        String pid = postPara.getId();
        String isBaseInfo = postPara.getIsBaseInfo();
        String followId = postPara.getFollowId();
        modifyFollowUpStatus(followId);
        try{
            if("创建".equals(status)){
                YunPatient yunPatient = null ;
                if(null==docId||"".equals(docId)){
                    if(pid==null||"".equals(pid)){
                        yunPatient = new YunPatient();
                        yunPatient= mergePatient(yunPatient,postPara,yunUsers);
                        //创建病例夹
                        YunFolder yunFolder = new YunFolder() ;
                        yunFolder=mergeYunFloder(yunFolder,postDocumentData,yunPatient);
                        YunRecordDocment yunRecordDocment = new YunRecordDocment();
                        yunRecordDocment=mergeRecordCocument(yunRecordDocment,postPara,yunFolder,postDocumentData,yunUsers);
                        return Response.status(Response.Status.OK).entity(yunRecordDocment).build();
                    }else{
                        yunPatient = baseFacade.get(YunPatient.class,pid);
                        if("1".equals(isBaseInfo)){
                            yunPatient = mergePatient(yunPatient,postPara,yunUsers);
                        }
                        Boolean isSamePerson = JudgeIfSamePerson(yunPatient);
                        if(!isSamePerson){
                            throw new FolderSaveException("病人病历非本人或本人的研究助手创建，不能修改");
                        }
                        YunFolder yunFolder = getYunFloderByPatientId(yunPatient.getId());
                        yunFolder = mergeYunFloder(yunFolder,postDocumentData,yunPatient);
                        YunRecordDocment yunRecordDocment = new YunRecordDocment();
                        yunRecordDocment =mergeRecordCocument(yunRecordDocment,postPara,yunFolder,postDocumentData,yunUsers);
                        return Response.status(Response.Status.OK).entity(yunRecordDocment).build();
                    }
                }else{
                    YunRecordDocment yunRecordDocment = baseFacade.get(YunRecordDocment.class,docId);
                    String folderId = yunRecordDocment.getFolderId();
                    YunFolder yunFolder = baseFacade.get(YunFolder.class,folderId);
                    String patientId = yunFolder.getPatientId();
                    yunPatient = baseFacade.get(YunPatient.class,patientId);
                    if("1".equals(isBaseInfo)){
                        yunPatient = mergePatient(yunPatient,postPara,yunUsers);
                    }
                    Boolean isSamePerson = JudgeIfSamePerson(yunPatient);
                    if(!isSamePerson){
                        throw new FolderSaveException("病人病历非本人或本人的研究助手创建，不能修改");
                    }
                    yunFolder = mergeYunFloder(yunFolder,postDocumentData,yunPatient);
                    yunRecordDocment= mergeRecordCocument(yunRecordDocment,postPara,yunFolder,postDocumentData,yunUsers);
                    return Response.status(Response.Status.OK).entity(yunRecordDocment).build();
                }


            }

            if("填写".equals(status)||"完成".equals(status)){
                if(docId==null||"".equals(docId)){
                    if(pid==""||pid==null){
                        throw new Exception("传入的病人信息为空，请传递病人ID");
                    }else{
                        YunPatient yunPatient = baseFacade.get(YunPatient.class,pid);
                        Boolean isSamePerson = JudgeIfSamePerson(yunPatient);
                        if(!isSamePerson){
                            throw new FolderSaveException("病人病历非本人或本人的研究助手创建，不能修改");
                        }
                        YunFolder yunFolder = getYunFloderByPatientId(yunPatient.getId());;
                        YunRecordDocment yunRecordDocment = new YunRecordDocment();
                        yunRecordDocment= mergeRecordCocument(yunRecordDocment,postPara,yunFolder,postDocumentData,yunUsers);
                        return Response.status(Response.Status.OK).entity(yunRecordDocment).build();
                    }
                }else{
                    YunRecordDocment yunRecordDocment = baseFacade.get(YunRecordDocment.class,docId);
                    if(yunRecordDocment==null){
                        yunRecordDocment = getYunRecordDocmentByPid(pid);
                    }
                    String folderId = yunRecordDocment.getFolderId();
                    YunFolder yunFolder = baseFacade.get(YunFolder.class,folderId);
                    String patientId = yunFolder.getPatientId();
                    YunPatient yunPatient = baseFacade.get(YunPatient.class,patientId);
                    if("1".equals(isBaseInfo)){
                        yunPatient = mergePatient(yunPatient,postPara,yunUsers);
                    }
                    Boolean isSamePerson = JudgeIfSamePerson(yunPatient);
                    if(!isSamePerson){
                        throw new FolderSaveException("病人病历非本人或本人的研究助手创建，不能修改");
                    }
                    yunFolder = mergeYunFloder(yunFolder,postDocumentData,yunPatient);
                    yunRecordDocment= mergeRecordCocument(yunRecordDocment,postPara,yunFolder,postDocumentData,yunUsers);
                    return Response.status(Response.Status.OK).entity(yunRecordDocment).build();
                }
            }
        }catch (FolderSaveException f){
            throw new Exception("病人病历非本人或本人的研究助手创建，不能修改");
        }
        catch (Exception e){
            throw new Exception("数据保存失败，请刷新页面后重试");
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("未进入判断流程，请检查入参").build();
    }

    /**
     * 判断修改病历的是否是同一个人，如果不是则不能修改
     * @param yunPatient
     * @return
     * @throws Exception
     */
    private Boolean JudgeIfSamePerson(YunPatient yunPatient) throws Exception{
        YunUsers yunUsers = UserUtils.getYunUsers();
        Boolean isSamePerson = false;
        if(yunUsers.getId().equals(yunPatient.getDoctorId())|| yunUsers.getId().equals(yunPatient.getOwerId())){
            isSamePerson = true;
        }
        return isSamePerson;
    }

    /**
     * 根据病人id查询文档信息
     * @param pid
     * @return
     */
    public YunRecordDocment getYunRecordDocmentByPid(String pid){
        String hql = "select d from YunRecordDocment d,YunFolder f where d.folderId = f.id and f.patientId = '"+pid+"'";
        List<YunRecordDocment> yunRecordDocments = baseFacade.createQuery(YunRecordDocment.class,hql,new ArrayList<Object>()).getResultList();
        if(yunRecordDocments!=null && !yunRecordDocments.isEmpty()){
            return yunRecordDocments.get(0);
        }else{
            return null;
        }
    }
    public void modifyFollowUpStatus(String followId){
        if(followId!=null && !"".equals(followId)){
            String hql = " from YunFollowUp as f where f.hstatus = 'S' and f.id = '"+followId+"'";
            List<YunFollowUp> yunFollowUps = baseFacade.createQuery(YunFollowUp.class,hql,new ArrayList<Object>()).getResultList();
            if(yunFollowUps!=null && !yunFollowUps.isEmpty()){
                YunFollowUp yunFollowUp = yunFollowUps.get(0);
                yunFollowUp.setHstatus("R");
                yunFollowUp.setModifyDate(new Timestamp(new Date().getTime()));
                baseFacade.merge(yunFollowUp);
            }
        }
    }
    /**
     * 根据病人ID，获取病历夹
     * @param patientId
     * @return
     */
    private YunFolder getYunFloderByPatientId(String patientId) {
        String hql ="from YunFolder as f where f.patientId='"+patientId+"'";
        List<YunFolder> resultList = baseFacade.createQuery(YunFolder.class, hql, new ArrayList<Object>()).getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 设置记录信息
     * @param yunRecordDocment
     * @param postPara
     * @param yunFolder
     * @param postDocumentData
     * @param yunUsers
     * @throws IOException
     * @throws JSONException
     */
    private YunRecordDocment mergeRecordCocument(YunRecordDocment yunRecordDocment, PostPara postPara, YunFolder yunFolder, PostDocumentData postDocumentData, YunUsers yunUsers) throws IOException, JSONException {

        yunRecordDocment.setCategory(postPara.getCategory());
        yunRecordDocment.setFolderId(yunFolder.getId());
        yunRecordDocment.setCreateDate(new Timestamp(new Date().getTime()));
        yunRecordDocment.setTitle(postPara.getTitle());
        yunRecordDocment.setTypecode1(postPara.getCode());
        yunRecordDocment.setTypecode2(postPara.getCodeName());
        yunRecordDocment.setTempletname(postPara.getMbId());
        yunRecordDocment.setContent(JSONUtil.objectToJson(postDocumentData).toString());
        yunRecordDocment.setDoctorId(yunUsers.getId());
        return baseFacade.merge(yunRecordDocment);
    }

    /**
     * 设置病历夹信息
     * @param yunFolder
     * @param postDocumentData
     * @param patient
     */
    private YunFolder mergeYunFloder(YunFolder yunFolder, PostDocumentData postDocumentData, YunPatient patient) {
        yunFolder.setCreateDate(new Timestamp(new Date().getTime()));
        yunFolder.setActioncode("病例研究");
        yunFolder.setActionid(String.valueOf(IDUtils.genItemId()));
        yunFolder.setDiagnosisCode(postDocumentData.getDcode());
        yunFolder.setDiagnosis(postDocumentData.getDcodeName());
        yunFolder.setPatientId(patient.getId());
        return yunFolder = baseFacade.merge(yunFolder);

    }

    /**
     * 设置病人信息
     * @param patient
     * @param postPara
     * @param yunUsers
     */
    private YunPatient mergePatient(YunPatient patient, PostPara postPara, YunUsers yunUsers) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(!StringUtils.isEmpty(postPara.getBr())){
            Date date = simpleDateFormat.parse(postPara.getBr());
            patient.setBr(new Timestamp(date.getTime()));
        }
        String owerId = getOwerId(yunUsers,baseFacade);
        if(!StringUtils.isEmpty(owerId)){
            patient.setOwerId(owerId);
        }else{
            if(StringUtils.isEmpty(patient.getOwerId())){//第一次创建时赋值拥有者，其他人修改则不再赋值
                patient.setOwerId(yunUsers.getId());//如果该用户不是研究助手 那么拥有者是其本人
            }
        }
        if(StringUtils.isEmpty(patient.getDoctorId())){//研究助手创建的病历被修改 创作者还为原先的医生
            patient.setDoctorId(yunUsers.getId());
        }
        patient.setDeptId(yunUsers.getDeptId());
        patient.setEmail(postPara.getEmail());
        patient.setMid(postPara.getmId());
        patient.setLxfs(postPara.getLxfs());
        patient.setNc(postPara.getNc());
        patient.setNe(postPara.getNc());
        patient.setPid(postPara.getpId());
        patient.setTel1(postPara.getTel1());
        patient.setTel2(postPara.getTel2());
        patient.setSx(postPara.getSx());
        return patient = baseFacade.merge(patient);
    }

    /**
     * 根据用户信息判断其是否是管理助手 如果是管理助手查询其导师及拥有这
     * @param yunUsers
     * @param baseFacade
     * @return
     */
    public String getOwerId(YunUsers yunUsers,BaseFacade baseFacade){
        String owerId = "";
        if(yunUsers.getRolename()!=null && yunUsers.getRolename().equals(SmsSendUtil.getStringByKey("roleCode"))){
            List list = GroupQuerySqlUtil.getResearchAssistantSql(yunUsers.getId(),"1",baseFacade);
            if(list!=null && !list.isEmpty()){
                owerId = list.get(0)+"";
            }
        }
        return owerId;
    }
    /**
     * 获取新增病人随访或者表单内容
     * @param pubFlag 0位未发布的私有数据，1位发布的数据
     * @param dcode  疾病编码
     * @param mblx   模板类型工作流位“WORK”，非工作流为空
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-info-list")
    public List<InfoList> getInfoList(@QueryParam("pubFlag") String pubFlag,@QueryParam("dcode") String dcode,@QueryParam("mblx") String mblx) throws Exception {
        String hql = "";
        YunUsers yunUsers = UserUtils.getYunUsers();
        String doctorId = yunUsers.getId();
        String deptId = yunUsers.getDeptId();
        List<InfoList> infoLists = new ArrayList<>();
        if (null==pubFlag||"".equals(pubFlag)){
            throw  new Exception("参数pubFlag不能为空");
        }

        if (null==dcode||"".equals(dcode)){
            throw  new Exception("参数dcode不能为空");
        }
        if("0".equals(pubFlag)){
            hql="from YunDisTemplet as yd where yd.dcode='"+dcode+"' and (yd.doctorId='"+doctorId+"' or (yd.deptId='"+deptId+"' and yd.deptId<>'0' ))";
        }

        if("1".equals(pubFlag)){
            hql="from YunReleaseTemplet as yd where yd.dcode='"+dcode+"'";
        }
        if("WORK".equals(mblx)){
            hql+=" and yd.mblx='WORK'";
        }else{
            hql+=" and yd.mblx <> 'WORK'";
        }

        if("1".equals(pubFlag)){
            List<YunReleaseTemplet> yunReleaseTemplets = baseFacade.createQuery(YunReleaseTemplet.class,hql,new ArrayList<Object>()).getResultList();
            for(YunReleaseTemplet templet:yunReleaseTemplets){
                InfoList infoList = new InfoList();
                infoList.setId(templet.getId());
                infoList.setName(templet.getTitle());
                infoList.setValue(templet.getDcode());
                infoLists.add(infoList);
            }
        }
        if("0".equals(pubFlag)){
            List<YunDisTemplet> yunDiseaseLists = baseFacade.createQuery(YunDisTemplet.class,hql,new ArrayList<Object>()).getResultList();
            for(YunDisTemplet list:yunDiseaseLists){
                InfoList infoList = new InfoList();
                infoList.setId(list.getId());
                infoList.setName(list.getTitle());
                infoList.setValue(list.getDcode());
                infoLists.add(infoList);
            }
        }
        return infoLists;
    }
}
