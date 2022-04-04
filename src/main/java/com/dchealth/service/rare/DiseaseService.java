package com.dchealth.service.rare;

import com.dchealth.VO.DiseasePatInfo;
import com.dchealth.entity.rare.YunDiseaseList;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.GroupQuerySqlUtil;
import com.dchealth.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by Administrator on 2017/6/16.
 */
@Controller
@Produces("application/json")
@Path("disease")
public class DiseaseService {

    @Autowired
    private BaseFacade baseFacade ;


    /**
     * 获取疾病列表
     * @param name
     * @param dcode
     * @param bw
     * @param xt
     * @return
     */
    @GET
    @Path("list")
    public List<YunDiseaseList> listAllYunDiseaseList(@QueryParam("name") String name,@QueryParam("dcode")String dcode, @QueryParam("bw") String bw, @QueryParam("xt") String xt){

        String hql = "from YunDiseaseList as list where 1=1" ;
        if(null!=name&&!"".equals(name)){
            hql+=" and list.name like '%"+name+"%'" ;
        }
        if(null!=dcode&&!"".equals(dcode)){
            hql+=" and list.name = '"+dcode+"'" ;
        }
        if(null!=bw&&!"".equals(bw)){
            hql+=" and list.bw = '"+bw+"'" ;
        }
        if(null!=xt&&!"".equals(xt)){
            hql+=" and list.bw = '"+xt+"'" ;
        }

        return baseFacade.createQuery(YunDiseaseList.class,hql,new ArrayList<Object>()).getResultList() ;
    }


    /**
     * 创建新的疾病信息
     * @param yunDiseaseList
     * @return
     */
    @POST
    @Path("add-new")
    @Transactional
    public Response mergeYunDiseaseList(YunDiseaseList yunDiseaseList){
        try {
            YunDiseaseList merge = baseFacade.merge(yunDiseaseList);
            Response build = Response.status(Response.Status.OK).entity(merge).build();
            return build;
        } catch (Exception e) {
            e.printStackTrace();
            throw e ;
        }
    }

    /**
     * 修改疾病列表
     * @param yunDiseaseList
     * @return
     */
    @Transactional
    @Path("update")
    @POST
    public Response updateYunDiseaseList(YunDiseaseList yunDiseaseList){
        YunDiseaseList merge = baseFacade.merge(yunDiseaseList);
        return Response.status(Response.Status.OK).entity(merge).build();
    }


    /**
     * 获取当前医生研究疾病的病例信息
     * @param doctorId
     * @return
     */
    @GET
    @Path("list-doctor-pat-info")
    public List<DiseasePatInfo> listDiseasePatInfo(@QueryParam("doctorId") String doctorId){
        List<DiseasePatInfo> diseasePatInfos = new ArrayList<>() ;
        Set<String> diseaseSet = getManageDisease(doctorId);
        String hql="select distinct ydl from YunUserDisease yud ,YunDiseaseList ydl where ydl.dcode=yud.dcode ";
        String userIds = GroupQuerySqlUtil.getUserIds(doctorId,baseFacade);
        if(!StringUtils.isEmpty(userIds)){//大于0说明该用户有创建的群组 或者 参加的群组级别为A
            hql += " and yud.userId in ("+userIds+")";
        }else {
            hql += " and yud.userId = '"+doctorId+"'" ;
        }
        List<YunDiseaseList>  yunDiseaseLists = baseFacade.createQuery(YunDiseaseList.class, hql, new ArrayList<Object>()).getResultList();
        Map<String,Long>  discussPatMap = getPatNumberByType(doctorId,userIds,"0");//
        if(diseaseSet!=null && !diseaseSet.isEmpty()){
            Map<String,Long>  managePatMap = getPatNumberByType(doctorId,userIds,"1");//
            discussPatMap.putAll(managePatMap);
        }
        Map<String,Long>  discussfollowMap = getFollowNumberByType(doctorId,userIds,"0");//
        if(diseaseSet!=null && !diseaseSet.isEmpty()){
            Map<String,Long>  managefollowMap = getFollowNumberByType(doctorId,userIds,"1");//
            discussfollowMap.putAll(managefollowMap);
        }
        for(YunDiseaseList list:yunDiseaseLists){
            DiseasePatInfo diseasePatInfo = new DiseasePatInfo(list,Long.parseLong("0"),Long.parseLong("0"));
            //Long patNumber = getPatNumber(doctorId, list.getDcode(),diseaseSet);
            diseasePatInfo.setPatNumber(discussPatMap.get(list.getDcode())==null?0:discussPatMap.get(list.getDcode()));
            diseasePatInfo.setFollowNumber(discussfollowMap.get(list.getDcode())==null?0:discussfollowMap.get(list.getDcode()));//getPatFollowUp(doctorId,list.getDcode(),diseaseSet)
            diseasePatInfos.add(diseasePatInfo);
        }
        return diseasePatInfos;
    }


    public Map<String,Long> getPatNumberByType(String doctorId,String userIds,String type){
        Map<String,Long> retMap = new HashMap<String,Long>();
        String hql = "select count(*) as ct,yf.diagnosis_code from yun_folder as yf ,yun_patient as yp where yf.patient_id=yp.id";
        if("0".equals(type)){//
            if(StringUtils.isEmpty(userIds)){
                hql += " and yp.doctor_id = '"+doctorId+"'";
            }else{
                hql += " and yp.doctor_id in ("+userIds+")";
            }
        }else if("1".equals(type)){
            if(StringUtils.isEmpty(userIds)){
                hql += " and exists(select 1 from yun_user_disease_manager ym where ym.dcode = yf.diagnosis_code and ym.user_id = '"+doctorId+"')";
            }else {
                hql += " and exists(select 1 from yun_user_disease_manager ym where ym.dcode = yf.diagnosis_code and ym.user_id in ("+userIds+"))";
            }
        }
        hql +=" group by yf.diagnosis_code";
        List list = baseFacade.createNativeQuery(hql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                Object[] params = (Object[])list.get(i);
                retMap.put(params[1]+"",Long.parseLong(params[0].toString()));
            }
        }
        return retMap;
    }

    public Map<String,Long> getFollowNumberByType(String doctorId,String inSql,String type){
        Map<String,Long> retMap = new HashMap<String,Long>();
        String hql = "select count(*) CT,f.dcode from yun_follow_up as f,yun_patient as p  where YEAR(f.follow_date)=YEAR(current_date()) " +
                     "and MONTH(f.follow_date)=MONTH(current_date()) and f.hstatus='R' and f.patient_id=p.id ";
        if("0".equals(type)){//
            if(StringUtils.isEmpty(inSql)){
                hql += " and p.doctor_id = '"+doctorId+"'";
            }else{
                hql += " and p.doctor_id in ("+inSql+")";
            }
        }else if("1".equals(type)){
            if(StringUtils.isEmpty(inSql)){
                hql += " and exists(select 1 from yun_user_disease_manager ym where ym.dcode = f.dcode and ym.user_id = '"+doctorId+"')";
            }else{
                hql += " and exists(select 1 from yun_user_disease_manager ym where ym.dcode = f.dcode and ym.user_id in ("+inSql+"))";
            }
        }
        hql +=" GROUP BY f.dcode";
        List list = baseFacade.createNativeQuery(hql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                Object[] params = (Object[])list.get(i);
                retMap.put(params[1]+"",Long.parseLong(params[0].toString()));
            }
        }
        return retMap;
    }
    /**
     * 获取某个疾病当月的随访数
     * @param doctorId
     * @param dcode
     * @return
     */
    private Long getPatFollowUp(String doctorId, String dcode,Set diseaseSet) {
        String hql = "select count(*) from YunFollowUp as f,YunPatient as p  where YEAR(f.followDate)=YEAR(current_date()) and " +
                " MONTH(f.followDate)=MONTH(current_date()) and f.hstatus='R' and f.patientId=p.id" +
                " and f.dcode='"+dcode+"' ";
        if(diseaseSet==null || !diseaseSet.contains(dcode)){
            hql += " and p.doctorId='"+doctorId+"'" ;
        }
        return baseFacade.createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
    }

    /**
     * 获取某个研究院的某个疾病病人
     * @param doctorId
     * @param dcode
     * @return
     */
    private Long getPatNumber(String doctorId, String dcode,Set diseaseSet) {
        String hql = "select count(*) from YunFolder as yf ,YunPatient as yp where yf.patientId=yp.id " +
                     " and yf.diagnosisCode='"+dcode+"' " ;
        if(diseaseSet==null || !diseaseSet.contains(dcode)){
            hql += " and yp.doctorId='"+doctorId+"'";
        }
        return baseFacade.createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
    }

    public Set getManageDisease(String doctorId){
        Set<String> set = new HashSet<>();
        String hql = "select ydl from YunUserDiseaseManager yud ,YunDiseaseList ydl where ydl.dcode=yud.dcode "+
                " and yud.userId='"+doctorId+"'" ;
        List<YunDiseaseList> yunDiseaseLists = baseFacade.createQuery(YunDiseaseList.class, hql, new ArrayList<Object>()).getResultList();
        for(YunDiseaseList yunDiseaseList:yunDiseaseLists){
            set.add(yunDiseaseList.getDcode());
        }
        return set;
    }
}
