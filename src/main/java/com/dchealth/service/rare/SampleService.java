package com.dchealth.service.rare;

import com.dchealth.entity.rare.YunSample;
import com.dchealth.entity.rare.YunSampleType;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */
@Controller
@Produces("application/json")
@Path("sample")
public class SampleService {

    @Autowired
    private BaseFacade baseFacade;

    /**
     * 根据id或样例类型获取样例类型信息,如果不填写查询全部信息
     * @param id
     * @param type
     * @return
     */
    @GET
    @Path("list-sample-type")
    public List<YunSampleType> getSampleTypes(@QueryParam("id") String id, @QueryParam("type") String type){
        String hql = " from YunSampleType as t where 1=1 ";
        if(id!=null && !"".equals(id)){
            hql += " and t.id = '"+id+"'";
        }
        if(type!=null && !"".equals(type)){
            hql += " and t.sampleType = '"+type+"'";
        }
        return baseFacade.createQuery(YunSampleType.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 添加或修改样本生物信息
     * @param yunSample
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response mergeYunSample(YunSample yunSample){
        YunSample merge = baseFacade.merge(yunSample);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据样本类型查询样本信息列表
     * @param sampleType
     * @return
     */
    @GET
    @Path("list-sample")
    public List<YunSample> getYunSamples(@QueryParam("sampleType") String sampleType,@QueryParam("patientId") String patientId){
       String hql = " from YunSample as s where 1=1 ";
       if(sampleType!=null && !"".equals(sampleType)){
           hql += " and s.sampleType = '"+sampleType+"'";
       }
        if(patientId!=null && !"".equals(patientId)){
            hql += " and s.patientId = '"+patientId+"'";
        }
       return baseFacade.createQuery(YunSample.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据id获取生物样本信息
     * @param id
     * @return
     */
    @GET
    @Path("get-sample")
    public YunSample getYunSample(@QueryParam("id") String id) throws Exception{
        String hql = " from YunSample as s where s.id = '"+id+"'";
        List<YunSample> yunSamples = baseFacade.createQuery(YunSample.class,hql,new ArrayList<Object>()).getResultList();
        if(yunSamples!=null && !yunSamples.isEmpty()){
            return yunSamples.get(0);
        }else{
            throw new Exception("生物样本信息不存在");
        }
    }
}
