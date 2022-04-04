package com.dchealth.service.rare;

import com.dchealth.VO.*;
import com.dchealth.entity.common.RoleVsUser;
import com.dchealth.entity.rare.YunDiseaseList;
import com.dchealth.entity.rare.YunFolder;
import com.dchealth.entity.rare.YunFollowUp;
import com.dchealth.entity.rare.YunPatient;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.GroupQuerySqlUtil;
import com.dchealth.util.JSONUtil;
import com.dchealth.util.SmsSendUtil;
import com.dchealth.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import sun.applet.Main;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by Administrator on 2017/6/23.
 */
@Controller
@Path("pat")
@Produces("application/json")
public class PatientService {

    @Autowired
    private BaseFacade baseFacade ;

    /**
     * 获取某个疾病的、某个医生的病人列表
     * @param doctorId
     * @param dcode
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("list-pat")
    public Page<PatientVo> listYunPatient(@QueryParam("doctorId") String doctorId, @QueryParam("dcode") String dcode,@QueryParam("flag")boolean flag,
                                           @QueryParam("nc") String nc,@QueryParam("tel2") String tel2,@QueryParam("pid") String pid,
                                           @QueryParam("email")String email,@QueryParam("perPage")int perPage,@QueryParam("currentPage")int currentPage){
        String hql = "select new com.dchealth.VO.PatientVo(p.id,p.doctorId,p.owerId,(select userName from YunUsers where status<>'-1' and id = p.doctorId) as " +
                "doctorName,(select hospitalName from YunUsers where status<>'-1' and id = p.doctorId) as hospitalName,p.deptId,p.pid,p.mid," +
                "p.nc,p.ne,p.sx,p.br,p.lxfs,p.tel1,p.tel2,p.yzbm,p.email,p.createDate,p.modifyDate,p.bz,p.status) from YunPatient as p ,YunFolder as f where f.patientId =p.id " ;
        String hqlCount = "select count(p.id) from YunPatient as p ,YunFolder as f where f.patientId =p.id " ;
        if(!"".equals(dcode)&&null!=dcode){
            hql +=" and f.diagnosisCode='"+dcode+"'" ;
            hqlCount +=" and f.diagnosisCode='"+dcode+"'" ;
        }
        if(!"".equals(doctorId)&&null!=doctorId){
            if(flag){
                hql+=" and p.doctorId = '"+doctorId+"'" ;
                hqlCount+=" and p.doctorId = '"+doctorId+"'" ;
            }else{
                String userIds = GroupQuerySqlUtil.getUserIds(doctorId,baseFacade);
//            String doctorIds = "";
//            if(StringUtils.isEmpty(userIds)){
//                doctorIds = "'"+doctorId+"'";
//            }else{
//                doctorIds = userIds;
//            }
//            String manageDiseaseCodes = GroupQuerySqlUtil.getManageDiseaseCode(doctorIds,baseFacade);
                if(StringUtils.isEmpty(userIds)){
                    hql+=" and p.doctorId = '"+doctorId+"'" ;
                    hqlCount+=" and p.doctorId = '"+doctorId+"'" ;
//                hql += " or exists(select 1 from YunUserDiseaseManager ym where ym.dcode = f.diagnosisCode and ym.userId = '"+doctorId+"'))";
//                hqlCount += " or exists(select 1 from YunUserDiseaseManager ym where ym.dcode = f.diagnosisCode and ym.userId = '"+doctorId+"'))";
                }else{
                    hql+=" and p.doctorId in ("+userIds+")" ;
                    hqlCount+=" and p.doctorId in ("+userIds+")" ;
//                hql += " or exists(select 1 from YunUserDiseaseManager ym where ym.dcode = f.diagnosisCode and ym.userId in ("+userIds+")))";
//                hqlCount += " or exists(select 1 from YunUserDiseaseManager ym where ym.dcode = f.diagnosisCode and ym.userId in ("+userIds+")))";

                }
            }
        }
        if(!"".equals(nc)&&null!=nc){
            hql+=" and p.nc = '"+nc+"'" ;
            hqlCount+=" and p.nc = '"+nc+"'" ;
        }
        if(!"".equals(tel2)&&null!=tel2){
            hql+=" and p.tel2 = '"+tel2+"'" ;
            hqlCount+=" and p.tel2 = '"+tel2+"'" ;
        }
        if(!"".equals(pid)&&null!=pid){
            hql+=" and p.pid = '"+pid+"'" ;
            hqlCount+=" and p.pid = '"+pid+"'" ;
        }
        if(!"".equals(email)&&null!=email){
            hql+=" and p.email = '"+email+"'" ;
            hqlCount+=" and p.email = '"+email+"'" ;
        }
        hql+=" order by p.createDate desc";
        hqlCount+=" order by p.createDate desc";
        TypedQuery<PatientVo> baseFacadeQuery = baseFacade.createQuery(PatientVo.class, hql, new ArrayList<Object>());
        Long count = baseFacade.createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();
        Page<PatientVo> patientPage= new Page<>();
        patientPage.setCounts(count);
        patientPage.setPerPage((long)perPage);
        if(currentPage<=0){
            currentPage = 1;
        }
        if(perPage>0){
            baseFacadeQuery.setFirstResult((currentPage-1)*perPage) ;
            baseFacadeQuery.setMaxResults(perPage);
        }
        List<PatientVo> yunPatients = baseFacadeQuery.getResultList();
        patientPage.setData(yunPatients);
        return patientPage;
    }

    /**
     * 获取随诊病人列表
     * @param doctorId 医生ID信息
     * @param followDateBegin 随诊时间范围之开始时间
     * @param followDateEnd   随诊时间范围之结束时间
     * @param remindDateBegin 随诊提醒时间范围之开始时间
     * @param remindDateEnd   随诊提醒时间范围之结束时间
     * @param dcode           疾病代码
     * @param hstatus         随诊状态
     * @param perPage         每页显示数目
     * @param currentPage     当前页
     * @return
     */
    @GET
    @Path("list-follow-pat")
    public Page<PatientFollowUpVo> listFollowPatient(@QueryParam("doctorId") String doctorId, @QueryParam("followDateBegin") String followDateBegin,
                                              @QueryParam("followDateEnd") String followDateEnd,@QueryParam("remindDateBegin") String remindDateBegin,
                                              @QueryParam("remindDateEnd")String remindDateEnd,@QueryParam("dcode")String dcode,
                                              @QueryParam("hstatus")String hstatus,@QueryParam("perPage")int perPage,@QueryParam("currentPage")int currentPage){
        Page<PatientFollowUpVo> yunPatientPage = new Page<>();
        String hqlCount = "select count(p) from YunPatient as p ,YunFollowUp as yf where p.id=yf.patientId" ;
        String pfHql = "select new com.dchealth.VO.PatientFollowUpVo(p.id,p.doctorId,p.deptId,p.pid,p.mid,p.nc,p.ne,p.sx,p.br,p.lxfs,p.tel1,p.tel2,p.yzbm,p.email,p.createDate, yf.id, yf.title, yf.dcode, yf.followDate, yf.remindDate) from YunPatient as p ,YunFollowUp as yf where p.id=yf.patientId";
        if(!"".equals(doctorId)&null!=doctorId){
            String userIds = GroupQuerySqlUtil.getUserIds(doctorId,baseFacade);
            if(StringUtils.isEmpty(userIds)){
                pfHql += " and p.doctorId='"+doctorId+"'" ;
                hqlCount += " and p.doctorId='"+doctorId+"'" ;
            }else{
                pfHql += " and p.doctorId in ("+userIds+")" ;
                hqlCount += " and p.doctorId in ("+userIds+")" ;
            }
        }
        if(!"".equals(followDateBegin)&null!=followDateBegin){
            pfHql +=" and to_days(yf.followDate)>=to_days('"+followDateBegin+"')" ;
            hqlCount += " and to_days(yf.followDate)>=to_days('"+followDateBegin+"')" ;
        }
        if(!"".equals(followDateEnd)&null!=followDateEnd){
            pfHql += " and to_days(yf.followDate)<=to_days('"+followDateEnd+"')" ;
            hqlCount += " and to_days(yf.followDate)<=to_days('"+followDateEnd+"')" ;
        }
        if(!"".equals(remindDateBegin)&null!=remindDateBegin){
            pfHql += " and to_days(yf.remindDate)>=to_days('"+remindDateBegin+"')" ;
            hqlCount += " and to_days(yf.remindDate)>=to_days('"+remindDateBegin+"')" ;
        }
        if(!"".equals(remindDateEnd)&null!=remindDateEnd){
            pfHql += " and to_days(yf.remindDate)<=to_days('"+remindDateEnd+"')" ;
            hqlCount += " and to_days(yf.remindDate)<=to_days('"+remindDateEnd+"')" ;
        }

        if(!"".equals(dcode)&null!=dcode){
            pfHql += " and yf.dcode='"+dcode+"'" ;
            hqlCount += " and yf.dcode='"+dcode+"'" ;
        }

        if(!"".equals(hstatus)&null!=hstatus){
            pfHql += " and yf.hstatus='"+hstatus+"'" ;
            hqlCount += " and yf.hstatus='"+hstatus+"'" ;
        }
        Long aLong = baseFacade.createQuery(Long.class, hqlCount, new ArrayList<Object>()).getSingleResult();
        TypedQuery<PatientFollowUpVo> pfBaseTypedQuery = baseFacade.createQuery(PatientFollowUpVo.class, pfHql, new ArrayList<Object>());
        yunPatientPage.setCounts(aLong);
        yunPatientPage.setPerPage((long)perPage);
        if(currentPage<=0){
            currentPage = 1;
        }
        if(perPage>0){
            pfBaseTypedQuery.setFirstResult((currentPage-1)*perPage) ;
            pfBaseTypedQuery.setMaxResults(perPage);
        }
        List<PatientFollowUpVo> patientFollowUpVos = pfBaseTypedQuery.getResultList();
        //yunPatientPage.setData(baseFacadeQuery.getResultList());
        yunPatientPage.setData(patientFollowUpVos);
        return yunPatientPage;
    }

    /**
     * 根据病人id删除病人相关信息
     * @param id
     * @return
     */
    @POST
    @Path("del-patient")
    @Transactional
    public Response delPatientInfo(@QueryParam("id") String id,@QueryParam("veryCode") String veryCode, @Context HttpServletRequest request) throws Exception{
        if(StringUtils.isEmpty(veryCode)){
            throw new Exception("验证码不能为空，请重新输入");
        }
        String sessionVeryCode = request==null?"":(String) request.getSession().getAttribute(request.getSession().getId()+ SmsSendUtil.delPationt);
        if(StringUtils.isEmpty(sessionVeryCode)){
            throw new Exception("验证码已失效，请重新输入");
        }
        if(!veryCode.equals(sessionVeryCode)){
            throw new Exception("验证码不正确，请重新输入");
        }
        List<String> ids = new ArrayList<>();
        List<String> yunflupIds = new ArrayList<>();
        List<String> yunFolderIds = new ArrayList<>();
        ids.add(id);
        String flupHql = " from YunFollowUp as yf where yf.patientId = '"+id+"'";
        List<YunFollowUp> yunFollowUps = baseFacade.createQuery(YunFollowUp.class,flupHql,new ArrayList<Object>()).getResultList();
        for(YunFollowUp yunFollowUp:yunFollowUps){
            yunflupIds.add(yunFollowUp.getId());
        }
        String folderHql = " from YunFolder as f where f.patientId = '"+id+"'";
        List<YunFolder> yunFolders = baseFacade.createQuery(YunFolder.class,folderHql,new ArrayList<Object>()).getResultList();
        for(YunFolder yunFolder:yunFolders){
            yunFolderIds.add(yunFolder.getId());
        }
        baseFacade.removeByStringIds(YunFollowUp.class,yunflupIds);
        baseFacade.removeByStringIds(YunFolder.class,yunFolderIds);
        baseFacade.removeByStringIds(YunPatient.class,ids);
        request.getSession().removeAttribute(request.getSession().getId()+SmsSendUtil.delPationt);
        return Response.status(Response.Status.OK).entity(ids).build();
    }

    /**
     * 根据病人id设置病人状态
     * @param patientId
     * @param status 0:代表已故,1:代表健在,后续有其他值再次添加
     * @return
     * @throws Exception
     */
    @POST
    @Transactional
    @Path("set-pat-status")
    public Response setPatientStatus(@QueryParam("patientId") String patientId,@QueryParam("status") String status) throws Exception{
        YunPatient yunPatient = baseFacade.get(YunPatient.class,patientId);
        if(yunPatient==null){
            throw new Exception("病人信息不存在");
        }
        yunPatient.setStatus(status);
        return Response.status(Response.Status.OK).entity(baseFacade.merge(yunPatient)).build();
    }

    /**
     * 根据医生id和疾病编码判断医生是否加入群组
     * @param doctorId
     * @param diseaseCode
     * @return
     */
    @GET
    @Path("judge-doctor-if-join-group")
    public String judgeDoctorIfJoinGroup(@QueryParam("doctorId")String doctorId,@QueryParam("diseaseCode")String diseaseCode) throws Exception{
        String hql = "select v.user_id from research_group_vs_user as v,(select g.id,g.research_disease_id from research_group as g,research_group_vs_user as v" +
                " where g.id = v.group_id and g.status<>'-1' and ((v.user_id = '"+doctorId+"' and v.creater_flag ='1') or (v.user_id = '"+doctorId+"' and " +
                "g.data_share_level = 'A'))) as b,yun_disease_list as d where v.group_id = b.id and b.research_disease_id = d.id and d.dcode = '"+diseaseCode+"'";
        List list = baseFacade.createNativeQuery(hql).getResultList();
        Map map = new HashMap();
        if(list!=null && !list.isEmpty()){
            map.put("isShare","true");
        }else {
            map.put("isShare","false");
        }
        return JSONUtil.objectToJsonString(map);
    }
}
