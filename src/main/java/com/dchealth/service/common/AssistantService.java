package com.dchealth.service.common;

import com.dchealth.VO.Page;
import com.dchealth.entity.common.ResearchAssistant;
import com.dchealth.entity.common.RoleVsUser;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.facade.security.UserFacade;
import com.dchealth.security.PasswordAndSalt;
import com.dchealth.security.SystemPasswordService;
import com.dchealth.util.SmsSendUtil;
import com.dchealth.util.StringUtils;
import com.dchealth.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces("application/json")
@Path("assistant")
@Controller
public class AssistantService {

    @Autowired
    private BaseFacade baseFacade;

    @Autowired
    private UserFacade userFacade;

    /**
     * 添加一个人成为研究助手
     * @param researchAssistant
     * @return
     */
    @POST
    @Path("add-assistant")
    @Transactional
    public Response addAssistant(ResearchAssistant researchAssistant) throws Exception{
        String hql=" from ResearchAssistant where userId='"+researchAssistant.getUserId()+"' and assistant='"+researchAssistant.getAssistant()+"'";
        List<ResearchAssistant> assistantList = baseFacade.createQuery(ResearchAssistant.class, hql, new ArrayList<>()).getResultList();
        if(assistantList!=null&&assistantList.size()>0){
            throw new Exception("该用户已经是您的研究助手！");
        }
        YunUsers yunUsers = baseFacade.get(YunUsers.class, researchAssistant.getAssistant());
        if(!"DOCTOR_ASSISTANT".equals(yunUsers.getRolename())){
            throw new Exception("该用户还未开通研究助手的权限！");
        }
        ResearchAssistant merge = baseFacade.merge(researchAssistant);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 移除一个研究助手
     * @param researchAssistant
     * @return
     */
    @POST
    @Path("remove-assistant")
    @Transactional
    public Response removeAssistant(ResearchAssistant researchAssistant) throws Exception{
        String upAHql = " update ResearchAssistant set status = '-1' where userId = '"+researchAssistant.getUserId()+"' and assistant = '"+researchAssistant.getAssistant()+"'";
        baseFacade.excHql(upAHql);
        String upHql = " update YunUsers set status = '-1' where id = '"+researchAssistant.getAssistant()+"'";
        baseFacade.excHql(upHql);
        String delHql = " delete from RoleVsUser where userId = '"+researchAssistant.getAssistant()+"'";
        baseFacade.excHql(delHql);
        return Response.status(Response.Status.OK).entity(researchAssistant).build();
    }

    /**
     * 获取研究助手列表
     * @param userId
     * @return
     */
    @GET
    @Path("get-assistants")
    public Page<YunUsers> getAssistants(@QueryParam("userId") String userId,@QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        String hql=" from YunUsers where status<>'-1' and id in " +
                "(select assistant from ResearchAssistant where status<>'-1' and userId='"+userId+"')";
        Page<YunUsers> yunUsersList = baseFacade.getPageResult(YunUsers.class,hql,perPage,currentPage);
        return yunUsersList;
    }

    /**
     * 添加，修改研究助手
     * @param yunUsers
     * @return
     */
    @POST
    @Path("merge-assistant")
    public Response mergeAssistant(YunUsers yunUsers) throws Exception{
        try{
            return userFacade.mergeAssistant(yunUsers);
        }catch (DataIntegrityViolationException e){
            throw new Exception("该手机号已被注册，请重新填写");
        }catch (Exception e){
            throw e;
        }
    }


    /**
     *获取历史研究助手列表 带分页
     * @param userName 研究助手名称
     * @param userId 研究助手登录名
     * @param perPage 每页条数
     * @param currentPage 当前页
     * @return
     */
    @GET
    @Path("get-his-assistant")
    public Page<YunUsers> getHisAssistant(@QueryParam("userName")String userName,@QueryParam("userId")String userId,
                                          @QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage)throws Exception{
        YunUsers yunUsers = UserUtils.getYunUsers();
        String hql = "select u from YunUsers as u,ResearchAssistant as r where u.id = r.assistant and r.userId = " +
                     "'"+yunUsers.getId()+"' and u.status = '-1' and r.status='-1'";
        if(!StringUtils.isEmpty(userId)){
            hql += " and u.userId = '"+userId+"'";
        }
        if(!StringUtils.isEmpty(userName)){
            hql += " and u.userName like '%"+userName+"'%";
        }
        return baseFacade.getPageResult(YunUsers.class,hql,perPage,currentPage);
    }

    /**
     * 重新启用该研究助手
     * @param assistant
     * @return
     * @throws Exception
     */
    @POST
    @Path("restart-assistant")
    @Transactional
    public Response restartAssistant(@QueryParam("assistant")String assistant) throws Exception{
        if(StringUtils.isEmpty(assistant)){
            throw new Exception("研究助手id不能为空");
        }
        String upYunsersHql = " update YunUsers set status = '0' where id = '"+assistant+"'";
        String upAssistantHql = " update ResearchAssistant set status = '0' where assistant = '"+assistant+"'";
        baseFacade.excHql(upYunsersHql);
        baseFacade.excHql(upAssistantHql);
        String rhql = "select id from RoleDict where code = '"+SmsSendUtil.getStringByKey("roleCode")+"'";
        List<String> roleIds = baseFacade.createQuery(String.class,rhql,new ArrayList<Object>()).getResultList();
        if(roleIds!=null && !roleIds.isEmpty()){
            RoleVsUser roleVsUser = new RoleVsUser();
            roleVsUser.setRoleId(roleIds.get(0));
            roleVsUser.setUserId(assistant);
            baseFacade.merge(roleVsUser);
        }
        List list = new ArrayList();
        list.add(assistant);
        return Response.status(Response.Status.OK).entity(list).build();
    }
}
