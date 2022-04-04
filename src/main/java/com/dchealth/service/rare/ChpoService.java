package com.dchealth.service.rare;

import com.dchealth.entity.rare.YunChpo;
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
 * Created by Administrator on 2017/7/26.
 */
@Controller
@Produces("application/json")
@Path("chpo")
public class ChpoService {

    @Autowired
    private BaseFacade baseFacade;

    /**
     *获取chpo列表信息
     * @param typeName 类型名称
     * @param nameEn 英文名
     * @param nameCn 中文名
     * @return
     */
    @GET
    @Path("get-chpo-list")
    public List<YunChpo> getYunChpoList(@QueryParam("typeName") String typeName,@QueryParam("nameEn") String nameEn,@QueryParam("nameCn") String nameCn,
                                         @QueryParam("patientId") String patientId){
        String hql = " from YunChpo where 1=1";
        if(!StringUtils.isEmpty(patientId)){
            hql += " and patientId = '"+patientId+"'";
        }
        if(!StringUtils.isEmpty(typeName)){
            hql += " and typeName = '"+typeName+"'";
        }
        if(!StringUtils.isEmpty(nameEn)){
            hql += " and nameEn = '"+nameEn+"'";
        }
        if(!StringUtils.isEmpty(nameCn)){
            hql += " and nameCn = '"+nameCn+"'";
        }
        hql += "order by createDate desc,typeName asc";
        return baseFacade.createQuery(YunChpo.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 保存Chpo信息
     * @param yunChpo
     * @return
     */
    @POST
    @Transactional
    @Path("merge")
    public Response mergeYunChpo(YunChpo yunChpo) throws Exception{
        if(StringUtils.isEmpty(yunChpo.getPatientId())){
            throw new Exception("病人信息不能为空");
        }
        String hql = " from YunChpo where hpoId = '"+yunChpo.getHpoId()+"' and patientId = '"+yunChpo.getPatientId()+"'" +
                     " and typeName = '"+yunChpo.getTypeName()+"'";
        List<YunChpo> yunChpoList = baseFacade.createQuery(YunChpo.class,hql,new ArrayList<Object>()).getResultList();
        YunChpo merge = null;
        if(yunChpoList==null || yunChpoList.isEmpty()){
            merge = baseFacade.merge(yunChpo);
        }else{
            merge = yunChpo;
        }
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 更新chpo信息
     * @param yunChpo
     * @return
     */
    @POST
    @Transactional
    @Path("modify")
    public Response modifyYunChpo(YunChpo yunChpo) throws Exception{
        String hql = " from YunChpo where hpoId = '"+yunChpo.getHpoId()+"' and patientId = '"+yunChpo.getPatientId()+"'" +
                " and typeName = '"+yunChpo.getTypeName()+"' and id <> '"+yunChpo.getId()+"'";
        List<YunChpo> yunChpoList = baseFacade.createQuery(YunChpo.class,hql,new ArrayList<Object>()).getResultList();
        if(yunChpoList!=null && !yunChpoList.isEmpty()){
            throw new Exception("该分类下已存在该chpo信息");
        }
        YunChpo merge = baseFacade.merge(yunChpo);
        return Response.status(Response.Status.OK).entity(merge).build();
    }
    /**
     * 根据id删除chpo信息
     * @param id
     * @return
     */
    @POST
    @Transactional
    @Path("del")
    public Response delYunChpo(@QueryParam("id") String id){
        List<String> ids = new ArrayList<>();
        if(!StringUtils.isEmpty(id)){
            ids.add(id);
            baseFacade.removeByStringIds(YunChpo.class,ids);
        }
       return Response.status(Response.Status.OK).entity(ids).build();
    }

    /**
     * 根据id获取chpo信息
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-chpo-by-id")
    public YunChpo getYunChpoById(@QueryParam("id") String id) throws Exception{
        String hql = " from YunChpo where id = '"+id+"'";
        List<YunChpo> yunChpoList = baseFacade.createQuery(YunChpo.class,hql,new ArrayList<Object>()).getResultList();
        if(yunChpoList!=null && !yunChpoList.isEmpty()){
            return yunChpoList.get(0);
        }else{
            throw new Exception("该chpo信息不存在");
        }
    }
}
