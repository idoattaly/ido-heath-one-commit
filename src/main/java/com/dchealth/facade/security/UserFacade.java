package com.dchealth.facade.security;

import com.dchealth.entity.common.ResearchAssistant;
import com.dchealth.entity.common.RoleVsUser;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.security.PasswordAndSalt;
import com.dchealth.security.SystemPasswordService;
import com.dchealth.util.SmsSendUtil;
import com.dchealth.util.StringUtils;
import com.dchealth.util.UserUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
@Component
public class UserFacade extends BaseFacade {

    public YunUsers getYunUsersByUserId(String userName) throws Exception {
        String hql = "from YunUsers as user where user.userId='"+userName+"' and user.status<>'-1'";
        List<YunUsers> resultList = createQuery(YunUsers.class, hql, new ArrayList<>()).getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }else{
            throw new Exception("不存在的用户名");
        }
    }


    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public YunUsers getYunUserById(String id) throws Exception {
        String hql = "from YunUsers as user where user.id='"+id+"' and user.status<>'-1'";
        List<YunUsers> resultList = createQuery(YunUsers.class, hql, new ArrayList<>()).getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }else{
            throw new Exception("不存在的用户名");
        }
    }


    public YunUsers getYunUsersByLoginName(String username) throws Exception {
        String hql = "from YunUsers as user where user.userId='"+username+"' and user.status<>'-1'";
        List<YunUsers> resultList = createQuery(YunUsers.class, hql, new ArrayList<>()).getResultList();
        if(resultList.size()>0){
            return resultList.get(0);
        }else{
            throw new Exception("不存在的用户名");
        }
    }

    /**
     * 用户注册，保存用户信息
     * @param yunUsers
     * @return
     * @throws Exception
     */
    @POST
    @Transactional
    public Response mergeYunUsers(YunUsers yunUsers) throws Exception{
        return Response.status(Response.Status.OK).entity(merge(yunUsers)).build();
    }

    @Transactional
    public Response mergeAssistant(YunUsers yunUsers) throws Exception{
        YunUsers merge = null;
        String hql = "select userId from YunUsers where id<>'"+yunUsers.getId()+"' and (userId = '"+yunUsers.getUserId()+"' or userName = '"+yunUsers.getUserName()+"')";
        List<String> userList = createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(userList!=null && !userList.isEmpty()){
            throw new Exception("登录名或用户名已存在，请重新填写");
        }
        if(StringUtils.isEmpty(yunUsers.getId())){
            YunUsers currentUser = UserUtils.getYunUsers();
            PasswordAndSalt passwordAndSalt = SystemPasswordService.enscriptPassword(yunUsers.getUserId(), yunUsers.getPassword());
            yunUsers.setPassword(passwordAndSalt.getPassword());
            yunUsers.setSalt(passwordAndSalt.getSalt());
            yunUsers.setRolename(SmsSendUtil.getStringByKey("roleCode"));
            merge = merge(yunUsers);
            ResearchAssistant researchAssistant = new ResearchAssistant();
            researchAssistant.setStatus("0");
            researchAssistant.setAssistant(merge.getId());
            researchAssistant.setUserId(currentUser.getId());
            merge(researchAssistant);
            String rhql = "select id from RoleDict where code = '"+yunUsers.getRolename()+"'";
            List<String> roleIds = createQuery(String.class,rhql,new ArrayList<Object>()).getResultList();
            if(roleIds!=null && !roleIds.isEmpty()){
                RoleVsUser roleVsUser = new RoleVsUser();
                roleVsUser.setRoleId(roleIds.get(0));
                roleVsUser.setUserId(merge.getId());
                merge(roleVsUser);
            }
        }else{
            merge = merge(yunUsers);
        }
        return Response.status(Response.Status.OK).entity(merge).build();
    }
}
