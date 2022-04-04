package com.dchealth.service.common;

import com.dchealth.entity.common.ResourceDict;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */
@Produces("application/json")
@Path("resource")
@Controller
public class ResourceService {


    @Autowired
    private BaseFacade baseFacade ;

    /**
     * 获取所有的资源
     * @return
     */
    @GET
    @Path("list")
    public List<ResourceDict> listResource(){
        String hql = "from ResourceDict as rd where rd.status='1'";
        return baseFacade.createQuery(ResourceDict.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 修改资源信息
     * @param resourceDict
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response mergeResource(ResourceDict resourceDict){
        ResourceDict dict = baseFacade.merge(resourceDict);
        return Response.status(Response.Status.OK).entity(dict).build();
    }


    /**
     * 获取用户所有的资源
     * @param userId
     * @return
     */
    @GET
    @Path("list-resource-by-user")
    public List<ResourceDict> listResourceByUser(@QueryParam("userId") String userId){
        String hql = "select rd from RoleVsUser rvu ,RoleVsResource rvr ,ResourceDict rd,RoleDict rdi where " +
                " rdi.status='1' and rd.status='1' and rvu.roleId=rdi.id " +
                " and rvu.userId='"+userId+"' and rdi.id=rvr.roleId " +
                " and rvr.resourceId=rd.id";

        return baseFacade.createQuery(ResourceDict.class,hql,new ArrayList<Object>()).getResultList();
    }

}
