package com.dchealth.service.common;

import com.dchealth.entity.common.MenuDict;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
@Controller
@Produces("application/json")
@Path("menu")
public class MenuService {

    @Autowired
    private BaseFacade baseFacade ;

    /**
     * 获取用户的菜单
     * @param userId
     * @return
     */
    @GET
    @Path("list-menu-by-user")
    public List<MenuDict> getMenuByUserId(@QueryParam("userId") String userId){
        String hql = "select distinct md from MenuDict as md ,RoleVsMenus as rvm,RoleVsUser as rvu where " +
                "md.id=rvm.menuId and rvm.roleId=rvu.roleId and rvu.userId='"+userId+"'" +
                " and md.status='1'" ;
        return baseFacade.createQuery(MenuDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    /**
     * 获取角色菜单
     * @param roleId
     * @return
     */
    @GET
    @Path("list-menu-by-role")
    public List<MenuDict> getMenuByRoleId(@QueryParam("roleId")String roleId){
        String hql = "select md from MenuDict md ,RoleVsMenus rvm where " +
                "md.id = rvm.menuId and rvm.roleId='"+roleId+"' and md.status='1'" ;
        return baseFacade.createQuery(MenuDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    /**
     * 逻辑删除
     * 新增、修改、删除菜单
     * 删除菜单status=-1
     * 新增或者修改status=1
     * @param menuDict
     * @return
     */
    @POST
    @Transactional
    @Path("merge")
    public Response mergeMenuDict(MenuDict menuDict){
        MenuDict dict = baseFacade.merge(menuDict);
        return Response.status(Response.Status.OK).entity(dict).build();
    }


    /**
     * 获取所有菜单数据
     * @return
     */
    @GET
    @Path("list-menu-all")
    public List<MenuDict> listMenuDict(){
        String hql = "from MenuDict as md where md.status='1'" ;
        return  baseFacade.createQuery(MenuDict.class,hql,new ArrayList<Object>()).getResultList();
    }

}
