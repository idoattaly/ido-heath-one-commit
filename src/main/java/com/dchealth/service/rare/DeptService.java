package com.dchealth.service.rare;

import com.dchealth.entity.common.YunUsers;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/7/7.
 */
@Controller
@Produces("application/json")
@Path("dept")
public class DeptService {

    @Autowired
    private BaseFacade baseFacade ;


    /**
     * 重置科室
     * @param userId
     * @return
     */
    @POST
    @Transactional
    @Path("rest-dept")
    public Response restDept(@QueryParam("userId") String userId){
        YunUsers yunUsers = baseFacade.get(YunUsers.class,userId);
        String hqlYunValue = "update YunValue as y set y.deptId='"+yunUsers.getDeptId()+"' where y.doctorId='"+userId+"'";
        String hqlYunDisease = "update YunDiseaseList as y set y.deptId='"+yunUsers.getDeptId()+"' where y.doctorId='"+userId+"'";
        baseFacade.excHql(hqlYunDisease);
        baseFacade.excHql(hqlYunValue);
        return Response.status(Response.Status.OK).entity(yunUsers).build();
    }

}
