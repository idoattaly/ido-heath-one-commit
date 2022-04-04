package com.dchealth.service.rare;

import com.dchealth.VO.Page;
import com.dchealth.entity.common.HospitalDict;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */

@Produces("application/json")
@Path("hospital")
@Controller
public class HospitalService {


    @Autowired
    private BaseFacade baseFacade;

    /**
     * 获取所有的医院
     *
     * @return
     */
    @GET
    public List<HospitalDict> listAllHospitalDict() {
        List<HospitalDict> hospitalDicts = baseFacade.findAll(HospitalDict.class);
        return hospitalDicts;

    }

    /**
     * 获取医院信息
     * @param hospitalName
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-hospital-dicts")
    public Page<HospitalDict> getAllHospitalDicts(@QueryParam("hospitalName")String hospitalName, @QueryParam("perPage") int perPage,
                                                  @QueryParam("currentPage") int currentPage){
        String hql = " from HospitalDict where status<>'-1'";
        if(!StringUtils.isEmpty(hospitalName)){
            hql += " and hospitalName like '"+hospitalName+"%'";
        }
        hql += " ORDER BY length(id) asc ";
        return baseFacade.getPageResult(HospitalDict.class,hql,perPage,currentPage);
    }

    /**
     * 添加,修改,删除医院信息
     *
     * @param hospitalDict
     * @return
     */
    @POST
    @Transactional
    @Path("merge")
    public Response mergeHospitalDict(HospitalDict hospitalDict) throws Exception{
        if(!"-1".equals(hospitalDict.getStatus())){
            Boolean isHaveSameHs = judgeIfHaveSameHospital(hospitalDict);
            if(isHaveSameHs){
                throw new Exception("该医院信息已存在，请勿重复添加");
            }
        }
        if(StringUtils.isEmpty(hospitalDict.getId())){//新增
            String hospitalCode = getMaxHospitalCode();
            if(!StringUtils.isEmpty(hospitalCode)){
                hospitalDict.setHospitalCode(hospitalCode);
            }else{
                throw new Exception("获取医院编码失败");
            }
        }
        HospitalDict mergeHospitalDict = baseFacade.merge(hospitalDict);
        return Response.status(Response.Status.OK).entity(mergeHospitalDict).build();
    }

    /**
     * 判断是否有重复医院
     * @param hospitalDict
     * @return
     */
    public Boolean judgeIfHaveSameHospital(HospitalDict hospitalDict){
        String hql = "select hospitalName from HospitalDict where status<>'-1' and hospitalName = '"+hospitalDict.getHospitalName()+"'" +
                     " and id <>'"+hospitalDict.getId()+"'";
        List<String> nameList = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        Boolean isHave = false;
        if(nameList!=null && !nameList.isEmpty()){
            isHave = true;
        }
        return isHave;
    }
    /**
     * 获取医院编码最大值,并对最大值+1
     * @return
     */
    public String getMaxHospitalCode(){
        String hql = " select max(hospitalCode)+1 from HospitalDict where status<>'-1' and hospitalCode>999";
        List<Integer> hospitalCodes = baseFacade.createQuery(Integer.class,hql,new ArrayList<Object>()).getResultList();
        if(hospitalCodes!=null && !hospitalCodes.isEmpty()){
            return hospitalCodes.get(0)+"";
        }else{
            return "";
        }
    }
}

