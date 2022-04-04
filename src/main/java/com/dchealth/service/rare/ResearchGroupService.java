package com.dchealth.service.rare;

import com.aliyun.mns.client.impl.queue.CreateQueueAction;
import com.dchealth.VO.*;
import com.dchealth.entity.common.HospitalDict;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.entity.rare.*;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.StringUtils;
import com.dchealth.util.UserUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by Administrator on 2017/10/19.
 */
@Controller
@Produces("application/json")
@Path("research-group")
public class ResearchGroupService {

    @Autowired
    private BaseFacade baseFacade ;


    /**
     * 添加，修改，删除群组，删除为逻辑删除 status = -1
     * @param researchGroupVo
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response mergeResearchGroup(ResearchGroupVo researchGroupVo) throws Exception{
        if("-1".equals(researchGroupVo.getStatus())){
            String delHospitalHql = " delete from ResearchGroupVsHospital where groupId = '"+researchGroupVo.getId()+"'";
            baseFacade.excHql(delHospitalHql);
            String delUser = "delete from ResearchGroupVsUser where groupId = '"+researchGroupVo.getId()+"'";
            baseFacade.excHql(delUser);
            ResearchGroup researchGroup = baseFacade.get(ResearchGroup.class,researchGroupVo.getId());
            researchGroup.setStatus(researchGroupVo.getStatus());
            ResearchGroup merge = baseFacade.merge(researchGroup);
            return Response.status(Response.Status.OK).entity(merge).build();
        }
        String sameHql = "select researchGroupName from ResearchGroup where status<>'-1' and researchGroupName = '"+researchGroupVo.getResearchGroupName()+"' and id<>'"+researchGroupVo.getId()+"'";
        List<String> stringList = baseFacade.createQuery(String.class,sameHql,new ArrayList<Object>()).getResultList();
        if(stringList!=null && !stringList.isEmpty()){
            throw new Exception("该群组名称已存在，请勿重新添加");
        }
        if(StringUtils.isEmpty(researchGroupVo.getId())){
            YunUsers yunUsers = UserUtils.getYunUsers();
            List<ResearchGroup> researchGroups = getResearchGroupByUserAndDiseaseId(yunUsers.getId(), researchGroupVo.getResearchDiseaseId());
            if(researchGroups!=null && !researchGroups.isEmpty()){
                throw new Exception("您已经在研究该疾病的群组！");
            }
            ResearchGroup researchGroup = new ResearchGroup();
            researchGroup.setDataShareLevel(researchGroupVo.getDataShareLevel());
            researchGroup.setGroupDesc(researchGroupVo.getGroupDesc());
            researchGroup.setGroupInInfo(researchGroupVo.getGroupInInfo());
            researchGroup.setManyHospitalFlag(researchGroupVo.getManyHospitalFlag());
            researchGroup.setResearchDiseaseId(researchGroupVo.getResearchDiseaseId());
            researchGroup.setResearchGroupName(researchGroupVo.getResearchGroupName());
            researchGroup.setStatus(researchGroupVo.getStatus());
            //researchGroup.setStatus("0");//0为正常申请，1为通过
            ResearchGroup merge = baseFacade.merge(researchGroup);
            List<String> hospitalDicts = new ArrayList<>();
            List<HospitalProtocolVo> hospitalProtocolIds =new ArrayList<>();
            if("0".equals(researchGroupVo.getManyHospitalFlag())){//单中心医院默认是自己的医院
                hospitalDicts = getHospitalDictByCode(yunUsers.getHospitalCode());
                if(hospitalDicts==null || hospitalDicts.isEmpty()){
                    throw new Exception("请填写您的医院信息！");
                }
                HospitalProtocolVo vo=new HospitalProtocolVo();
                vo.setHospitalId(hospitalDicts.get(0));
                hospitalProtocolIds.add(vo);
            }else{//多中心医院获取页面传的医院信息
                hospitalDicts = researchGroupVo.getHospitals();
                hospitalProtocolIds = researchGroupVo.getHospitalProtocolIds();
                List<String> myHospitalDict = getHospitalDictByCode(yunUsers.getHospitalCode());
                if(hospitalProtocolIds!=null){
                    if(myHospitalDict!=null){
                        hospitalProtocolIds = getNonRepeatHospitals(hospitalProtocolIds, myHospitalDict);
                    }
                }else{
                    throw new Exception("多中心医院选择不能为空");
                }
            }
            if(hospitalProtocolIds!=null && !hospitalProtocolIds.isEmpty()){
                List<ResearchGroupVsHospital> researchGroupVsHospitals = new ArrayList<>();
                for(HospitalProtocolVo hospitalProtocolId:hospitalProtocolIds){
                    ResearchGroupVsHospital researchGroupVsHospital = new ResearchGroupVsHospital();
                    researchGroupVsHospital.setGroupId(merge.getId());
                    researchGroupVsHospital.setHospitalId(hospitalProtocolId.getHospitalId());
                    researchGroupVsHospital.setProtocolId(hospitalProtocolId.getProtocolId());
                    researchGroupVsHospitals.add(researchGroupVsHospital);
                    //ResearchGroupVsHospital mergeHospital = baseFacade.merge(researchGroupVsHospital);
                }
                baseFacade.batchInsert(researchGroupVsHospitals);
            }
            ResearchGroupVsUser researchGroupVsUser = new ResearchGroupVsUser();
            researchGroupVsUser.setLearderFlag("1");
            researchGroupVsUser.setCreaterFlag("1");
            researchGroupVsUser.setUserId(yunUsers.getId());
            researchGroupVsUser.setGroupId(merge.getId());
            baseFacade.merge(researchGroupVsUser);
            return Response.status(Response.Status.OK).entity(merge).build();
        }else{
            YunUsers yunUsers = UserUtils.getYunUsers();
            String delHospitalHql = " delete from ResearchGroupVsHospital where groupId = '"+researchGroupVo.getId()+"'";
            baseFacade.excHql(delHospitalHql);
            ResearchGroup researchGroup = baseFacade.get(ResearchGroup.class,researchGroupVo.getId());
            researchGroup.setDataShareLevel(researchGroupVo.getDataShareLevel());
            researchGroup.setGroupDesc(researchGroupVo.getGroupDesc());
            researchGroup.setGroupInInfo(researchGroupVo.getGroupInInfo());
            researchGroup.setManyHospitalFlag(researchGroupVo.getManyHospitalFlag());
            researchGroup.setResearchDiseaseId(researchGroupVo.getResearchDiseaseId());
            researchGroup.setResearchGroupName(researchGroupVo.getResearchGroupName());
            researchGroup.setStatus(researchGroupVo.getStatus());
            ResearchGroup merge = baseFacade.merge(researchGroup);

            List<String> hospitalDicts = new ArrayList<>();
            List<HospitalProtocolVo> hospitalProtocolIds= new ArrayList<>();
            List<String> myHospitalDict = getHospitalDictByCode(yunUsers.getHospitalCode());
            if("0".equals(researchGroupVo.getManyHospitalFlag())){//修改为单中心医院
                HospitalProtocolVo vo=new HospitalProtocolVo();
                vo.setHospitalId(myHospitalDict.get(0));
                hospitalProtocolIds.add(vo);
            }else{
                /*if(researchGroupVo.getHospitals()!=null && !researchGroupVo.getHospitals().isEmpty()){
                    hospitalDicts = getNonRepeatHospitals(researchGroupVo.getHospitals(),myHospitalDict);
                }else{
                    hospitalDicts = myHospitalDict;
                }*/

               if(researchGroupVo.getHospitalProtocolIds()!=null && !researchGroupVo.getHospitalProtocolIds().isEmpty()){
                   hospitalProtocolIds=getNonRepeatHospitals(researchGroupVo.getHospitalProtocolIds(),myHospitalDict);
               }else{
                   HospitalProtocolVo vo=new HospitalProtocolVo();
                   vo.setHospitalId(myHospitalDict.get(0));
                   hospitalProtocolIds.add(vo);
               }
            }
            if(hospitalProtocolIds!=null && !hospitalProtocolIds.isEmpty()){
                List<ResearchGroupVsHospital> researchGroupVsHospitals = new ArrayList<>();
                for(HospitalProtocolVo hospitalProtocol:hospitalProtocolIds){
                    ResearchGroupVsHospital researchGroupVsHospital = new ResearchGroupVsHospital();
                    researchGroupVsHospital.setGroupId(merge.getId());
                    researchGroupVsHospital.setHospitalId(hospitalProtocol.getHospitalId());
                    researchGroupVsHospital.setProtocolId(hospitalProtocol.getProtocolId());
                    //ResearchGroupVsHospital mergeHospital = baseFacade.merge(researchGroupVsHospital);
                    researchGroupVsHospitals.add(researchGroupVsHospital);
                }
                baseFacade.batchInsert(researchGroupVsHospitals);
            }
            return Response.status(Response.Status.OK).entity(merge).build();
        }
    }

    /**
     * 获取不重复的医院信息
     * @param hospitalProtocolIds
     * @param myHospitalDict
     * @return
     */
    private List<HospitalProtocolVo> getNonRepeatHospitals(List<HospitalProtocolVo> hospitalProtocolIds, List<String> myHospitalDict) {
        String hospitalDict = null;
        Boolean isHave = false;
        if(myHospitalDict!=null && !myHospitalDict.isEmpty()){
            hospitalDict = myHospitalDict.get(0);

            for(HospitalProtocolVo hospitalDict1:hospitalProtocolIds){
                if(hospitalDict1.getHospitalId().equals(hospitalDict)){
                    isHave = true;
                }
            }
        }
        if(!isHave && hospitalDict!=null){
            HospitalProtocolVo vo=new HospitalProtocolVo();
            vo.setHospitalId(hospitalDict);
            hospitalProtocolIds.add(vo);
        }
        return hospitalProtocolIds;
    }

    /**
     * 根据医院编码查询医院信息
     * @param hospitalCode
     * @return
     */
    private List<String> getHospitalDictByCode(String hospitalCode) {
        String hql = "select id from HospitalDict where hospitalCode = '"+hospitalCode+"'";
        return baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据用户id和疾病信息查询群组信息
     * @param userId
     * @param diseaseId
     * @return
     */
    private List<ResearchGroup> getResearchGroupByUserAndDiseaseId(String userId,String diseaseId){
        String hql="select g from ResearchGroup g,ResearchGroupVsUser vs where vs.groupId=g.id and " +
                "vs.userId='"+userId+"' and g.researchDiseaseId='"+diseaseId+"'";
        return baseFacade.createQuery(ResearchGroup.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     *根据传入条件查询研究群组信息
     * @param groupName 研究组名称
     * @param diseaseId 多中心标志
     * @param userId 级别范围A，B两个级别
     * @param status 0表示正常申请，1表示审批通过 -1表示删除
     * @param perPage 每页条数
     * @param currentPage 当前页数
     * @return
     */
    @GET
    @Path("get-research-groups")
    public Page<ResearchGroupVo> getResearchGroups(@QueryParam("groupName")String groupName,@QueryParam("diseaseId")String diseaseId,
                                                 @QueryParam("userId")String userId,@QueryParam("status")String status,@QueryParam("userType")String userType,
                                                 @QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        String hql = "select new com.dchealth.VO.ResearchGroupVo(r.id,r.researchGroupName,r.researchDiseaseId,r.groupDesc,r.groupInInfo," +
                "r.manyHospitalFlag,r.dataShareLevel,r.status)  from ResearchGroup as r ";
        if("2".equals(userType)){
            hql += " ,YunUserDisease as u,YunDiseaseList as d where r.status ='1' and u.dcode = d.dcode and d.id = r.researchDiseaseId" +
                    " and  u.userId = '"+userId+"'";
        }else{
            hql += " where r.status <>'-1'";
        }
        if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(userType)){
            if("0".equals(userType)){//userType=0表示查询出该用户创建的科研项目
                hql += " and exists(select 1 from ResearchGroupVsUser where groupId = r.id and userId = '"+userId+"' and createrFlag = '1')";
            }else if("1".equals(userType)){//该用户参与的群组
                hql += " and exists(select 1 from ResearchGroupVsUser where groupId = r.id and userId = '"+userId+"' and createrFlag = '0')";
            }else if("2".equals(userType)){//查询出该用户未参加的，切与本医院有关的群组
                hql += " and not exists(select 1 from ResearchGroupVsUser where groupId = r.id  and userId = '"+userId+"') " +
                        "and not exists(select 1 from InviteApplyRecord where groupId = r.id and status in ('0','1') and flag ='0' and userId = '"+userId+"')" +
                        " and r.researchDiseaseId not in (select b.researchDiseaseId from ResearchGroupVsUser a,ResearchGroup b where a.groupId=b.id and a.userId='"+userId+"')" +
                        " and r.id in (select groupId from ResearchGroupVsHospital where hospitalId = (select h.id from HospitalDict as h,YunUsers as u where " +
                        " u.hospitalCode = h.hospitalCode and u.id = '"+userId+"'))";//to_do
            }else if("3".equals(userType)){//查询出该用户参加待审核的，切与本医院有关的群组
                hql += " and exists(select 1 from InviteApplyRecord where groupId = r.id and status = '0' and flag ='0' and userId = '"+userId+"') and " +
                        " r.id in (select groupId from ResearchGroupVsHospital where hospitalId = (select h.id from HospitalDict as h,YunUsers as u where " +
                        " u.hospitalCode = h.hospitalCode and u.id = '"+userId+"'))";
            }
        }
        if(!StringUtils.isEmpty(groupName)){
            hql += " and r.researchGroupName like '%"+groupName+"%'";
        }
        if(!StringUtils.isEmpty(diseaseId)){
            hql += " and r.researchDiseaseId = '"+diseaseId+"'";
        }
        if(!StringUtils.isEmpty(status)){
            hql += " and r.status = '"+status+"'";
        }
        Page<ResearchGroupVo> researchGroupPage = baseFacade.getPageResult(ResearchGroupVo.class,hql,perPage,currentPage);
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        StringBuffer groupIds = new StringBuffer("");
        for(ResearchGroupVo researchGroupVo:researchGroupPage.getData()){
            groupIds.append("'").append(researchGroupVo.getId()).append("',");
            if(map.get(researchGroupVo.getId())==null){
                List<String> hospitalDicts = new ArrayList<>();
                map.put(researchGroupVo.getId(),hospitalDicts);
            }
        }
        String groupIdsTo = groupIds.toString();
        if(!StringUtils.isEmpty(groupIdsTo)){
            groupIdsTo = groupIdsTo.substring(0,groupIdsTo.length()-1);
            String hospitalSql = "select h.id,r.group_id from hospital_dict as h,research_group_vs_hospital as r where h.status<>'-1' and r.hospital_id = h.id and r.group_id in ("+groupIdsTo+")";
            List list = baseFacade.createNativeQuery(hospitalSql).getResultList();
            if(list!=null && !list.isEmpty()){
                int size = list.size();
                for(int i=0;i<size;i++){
                    Object[] params = (Object[])list.get(i);
                    String groupId = (String)params[1];
                    if(map.get(groupId)!=null){
                        List<String> hospitalDicts = map.get(groupId);
                        hospitalDicts.add((String)params[0]);
                    }
                }
            }
            for(ResearchGroupVo researchGroupVo:researchGroupPage.getData()){
                researchGroupVo.setHospitals(map.get(researchGroupVo.getId()));
            }
        }
        return researchGroupPage;
    }

    /**
     *根据群组id获取群组信息
     * @param groupId 群组id
     * @return
     */
    @GET
    @Path("get-research-group")
    public ResearchGroupHospitalVo getResearchGroupById(@QueryParam("groupId")String groupId) throws Exception{
        String hql = " from ResearchGroup where id = '"+groupId+"'";
        List<ResearchGroup> researchGroups = baseFacade.createQuery(ResearchGroup.class,hql,new ArrayList<Object>()).getResultList();
        String hospitalSql = "select new com.dchealth.VO.HospitalDictVo(h.id,h.hospitalName,r.protocolId) from HospitalDict as h,ResearchGroupVsHospital as r where h.status<>'-1' and r.hospitalId = h.id and r.groupId  ='"+groupId+"'";
        List<HospitalDictVo> hospitalDicts = baseFacade.createQuery(HospitalDictVo.class,hospitalSql,new ArrayList<Object>()).getResultList();
        if(researchGroups!=null && !researchGroups.isEmpty()){
            ResearchGroupHospitalVo researchGroupHospitalVo = new ResearchGroupHospitalVo();
            researchGroupHospitalVo.setId(researchGroups.get(0).getId());
            researchGroupHospitalVo.setDataShareLevel(researchGroups.get(0).getDataShareLevel());
            researchGroupHospitalVo.setGroupDesc(researchGroups.get(0).getGroupDesc());
            researchGroupHospitalVo.setGroupInInfo(researchGroups.get(0).getGroupInInfo());
            researchGroupHospitalVo.setManyHospitalFlag(researchGroups.get(0).getManyHospitalFlag());
            researchGroupHospitalVo.setResearchGroupName(researchGroups.get(0).getResearchGroupName());
            researchGroupHospitalVo.setResearchDiseaseId(researchGroups.get(0).getResearchDiseaseId());
            researchGroupHospitalVo.setStatus(researchGroups.get(0).getStatus());
            researchGroupHospitalVo.setHospitals(hospitalDicts);
            return researchGroupHospitalVo;
        }else{
            throw new Exception("该群组信息不存在");
        }
    }

    /**
     *移除一个群组成员
     * @param groupId 群组id
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    @POST
    @Path("remove-user")
    @Transactional
    public Response removeResearchGroupUser(@QueryParam("groupId")String groupId,@QueryParam("userId")String userId) throws Exception{
        if(StringUtils.isEmpty(groupId)){
            throw new Exception("群组id不能为空");
        }
        if(StringUtils.isEmpty(userId)){
            throw new Exception("用户id不能为空");
        }
        String delHql = "delete from ResearchGroupVsUser where groupId = '"+groupId+"' and userId = '"+userId+"'";
        baseFacade.excHql(delHql);
        //异常群组成员时更新申请记录为已移除
        String upHql = "update InviteApplyRecord set status = '2' where groupId = '"+groupId+"' and userId = '"+userId+"'";
        baseFacade.excHql(upHql);
        List<String> resultList = new ArrayList<>();
        resultList.add(groupId);
        return Response.status(Response.Status.OK).entity(resultList).build();
    }

    /**
     * 邀请一个人人员入组
     * @param groupId
     * @param userId
     * @return
     */
    @POST
    @Path("invite-research-user")
    @Transactional
    public Response inviteResearchUser(@QueryParam("groupId")String groupId,@QueryParam("userId")String userId) throws Exception{
        if(StringUtils.isEmpty(groupId)){
            throw new Exception("群组id不能为空");
        }
        if(StringUtils.isEmpty(userId)){
            throw new Exception("用户id不能为空");
        }
        String hql = "select userId from InviteApplyRecord where groupId = '"+groupId+"' and userId = '"+userId+"' and flag = '1'" +
                " and status in ('0','1') ";
        List list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
        if(list!=null && !list.isEmpty()){
            throw new Exception("该用户已邀请，请勿重复邀请");
        }
        InviteApplyRecord inviteApplyRecord = new InviteApplyRecord();
        inviteApplyRecord.setGroupId(groupId);
        inviteApplyRecord.setUserId(userId);
        inviteApplyRecord.setFlag("1");
        inviteApplyRecord.setStatus("0");
        inviteApplyRecord.setCreateDate(new Date());
        InviteApplyRecord merge = baseFacade.merge(inviteApplyRecord);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 邀请一个人人员入组
     * @param inviteVo
     * @return
     */
    @POST
    @Path("invite-research-users")
    @Transactional
    public Response inviteResearchUser(InviteVo inviteVo) throws Exception{
        List<InviteApplyRecord> inviteApplyRecords = new ArrayList<>();
        if(StringUtils.isEmpty(inviteVo.getGroupId())){
            throw new Exception("群组id不能为空");
        }
        if(inviteVo.getUserIds()==null || inviteVo.getUserIds().isEmpty()){
            throw new Exception("用户id不能为空");
        }
        for(String userId:inviteVo.getUserIds()){
            String hql = "select userId from InviteApplyRecord where groupId = '"+inviteVo.getGroupId()+"' and userId = '"+userId+"' and flag = '1'" +
                    " and status in ('0','1') ";
            List list = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
            if(list!=null && !list.isEmpty()){
                throw new Exception("其中有用户已邀请，请勿重复邀请");
            }
            InviteApplyRecord inviteApplyRecord = new InviteApplyRecord();
            inviteApplyRecord.setGroupId(inviteVo.getGroupId());
            inviteApplyRecord.setUserId(userId);
            inviteApplyRecord.setFlag("1");
            inviteApplyRecord.setStatus("0");
            inviteApplyRecord.setCreateDate(new Date());
            InviteApplyRecord merge = baseFacade.merge(inviteApplyRecord);
            inviteApplyRecords.add(merge);
        }
        return Response.status(Response.Status.OK).entity(inviteApplyRecords).build();
    }
    /**
     * 申请一个群组加入
     * @param groupId
     * @param userId
     * @return
     */
    @POST
    @Path("apply-research-group")
    @Transactional
    public Response applyResearchGroup(@QueryParam("groupId")String groupId,@QueryParam("userId")String userId) throws Exception{
        if(StringUtils.isEmpty(groupId)){
            throw new Exception("群组id不能为空");
        }
        if(StringUtils.isEmpty(userId)){
            throw new Exception("用户id不能为空");
        }
        InviteApplyRecord inviteApplyRecord = new InviteApplyRecord();
        inviteApplyRecord.setGroupId(groupId);
        inviteApplyRecord.setUserId(userId);
        inviteApplyRecord.setFlag("0");
        inviteApplyRecord.setStatus("0");
        inviteApplyRecord.setCreateDate(new Date());
        InviteApplyRecord merge = baseFacade.merge(inviteApplyRecord);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     *审批或者同意个人入组
     * @param recordId 申请或者邀请记录Id
     * @return
     */
    @POST
    @Path("agree-group-user")
    @Transactional
    public Response agreeGroupUser(@QueryParam("recordId") String recordId) throws Exception{
        String hql = "from InviteApplyRecord where id = '"+recordId+"'";
        List<InviteApplyRecord> researchGroups = baseFacade.createQuery(InviteApplyRecord.class,hql,new ArrayList<Object>()).getResultList();
        if(researchGroups!=null && !researchGroups.isEmpty()){
            InviteApplyRecord inviteApplyRecord = researchGroups.get(0);
            inviteApplyRecord.setStatus("1");
            ResearchGroupVsUser researchGroupVsUser = new ResearchGroupVsUser();
            researchGroupVsUser.setGroupId(inviteApplyRecord.getGroupId());
            researchGroupVsUser.setUserId(inviteApplyRecord.getUserId());
            researchGroupVsUser.setCreaterFlag("0");
            researchGroupVsUser.setLearderFlag("0");
            baseFacade.merge(inviteApplyRecord);
            ResearchGroupVsUser merge = baseFacade.merge(researchGroupVsUser);
            return Response.status(Response.Status.OK).entity(merge).build();
        }else{
            throw new Exception("申请记录信息不存在");
        }
    }

    /**
     * 登录用户查看自己创建的组已邀请人员信息
     * @param userID 用户id 对应id 非userId
     * @param status 0表示待处理、1表示邀请人同意 -1表示拒绝
     * @param groupId   群组id
     * @param perPage 每页条数
     * @param currentPage 当前页
     *                    flag    1表示邀请   0表示申请
     * @return
     */
    @GET
    @Path("get-already-invite-users")
    public Page<InviteUserVo> getInviteUsers(@QueryParam("userID")String userID,@QueryParam("status")String status,@QueryParam("groupId")String groupId,
                                         @QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        return getInviteOrApplyUserVos(userID,status,"1",groupId,perPage,currentPage);
    }

    /**
     *根据用户id获取用户创建组申请人员信息
     * @param userID 用户id 对应id 非userId
     * @param status 0表示待处理、1表示邀请人同意 -1表示拒绝
     * @param groupId  群组id
     * @param perPage 每页条数
     * @param currentPage 当前页
     *                    flag    1表示邀请   0表示申请
     * @return
     */
    @GET
    @Path("get-already-apply-users")
    public Page<InviteUserVo> getApplyUsers(@QueryParam("userID")String userID,@QueryParam("status")String status,@QueryParam("groupId") String groupId,
                                             @QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        return getInviteOrApplyUserVos(userID,status,"0",groupId,perPage,currentPage);
    }
    /**
     *根据flag值判断是邀请或者申请，如果是邀请 获取当前用户邀请人员信息，如果为申请获取该用户创建组的申请人员信息
     * @param userID 用户id 对应id 非userId
     * @param status 0表示待处理、1表示邀请人同意 -1表示拒绝
     * @param flag 1表示邀请   0表示申请
     * @param groupId 群组id
     * @param perPage 每页条数
     * @param currentPage 当前页
     * @return
     */
    public Page<InviteUserVo> getInviteOrApplyUserVos(String userID,String status,String flag,String groupId,int perPage,int currentPage){
        String hql = "select new com.dchealth.VO.InviteUserVo(u.id,u.userName,u.sex,u.nation,u.mobile,u.tel,u.email,u.birthDate," +
                "u.title,u.hospitalName,g.researchGroupName as groupName,(select d.name from YunDiseaseList as d where d.id = g.researchDiseaseId) as diseaseName,p.id as recordId,p.status)" +
                " from YunUsers as u,ResearchGroup as g,InviteApplyRecord as p,ResearchGroupVsUser as c" +
                " where g.id = p.groupId and g.id = c.groupId and u.id = p.userId and p.flag = '"+flag+"' and " +
                " c.userId = '"+userID+"' and g.id = '"+groupId+"' ";
        String hqlCount = "select count(*) from YunUsers as u,ResearchGroup as g,InviteApplyRecord as p,ResearchGroupVsUser as c" +
                " where g.id = p.groupId and g.id = c.groupId and u.id = p.userId and p.flag = '"+flag+"' and " +
                " c.userId = '"+userID+"' and g.id = '"+groupId+"' ";
        if(!StringUtils.isEmpty(status)){
            hql += " and p.status = '"+status+"'";
            hqlCount += " and p.status = '"+status+"'";
        }

        Page<InviteUserVo> resultPage = new Page<>();
        TypedQuery<InviteUserVo> typedQuery = baseFacade.createQuery(InviteUserVo.class,hql,new ArrayList<Object>());
        Long counts =  baseFacade.createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        resultPage.setCounts(counts);
        if(perPage<=0){
            perPage =1;
        }
        if(currentPage<=0){
            currentPage=1;
        }
        typedQuery.setFirstResult((currentPage-1)*perPage);
        typedQuery.setMaxResults(perPage);
        resultPage.setPerPage((long)perPage);
        List<InviteUserVo> resultList = typedQuery.getResultList();
        resultPage.setData(resultList);
        return resultPage;
    }
    /**
     *flag=1表示获取邀请自己的记录 包含同意的，未同意的，待处理的，flag=0表示自己申请的记录信息
     * @param userID 用户id 对应id 非userId
     * @param status 0表示待处理、1表示邀请人同意 -1表示拒绝
     * flag    1表示邀请   0表示申请
     * @return
     */
    @GET
    @Path("get-invite-or-apply-records")
    public Page<InviteUserVo> getMyInviteApplyRecords(@QueryParam("userID")String userID,@QueryParam("status")String status,@QueryParam("flag")String flag,
                                                      @QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        String hql = "select new com.dchealth.VO.InviteUserVo(u.id,u.userName,u.sex,u.nation,u.mobile,u.tel,u.email,u.birthDate," +
                "u.title,u.hospitalName,g.researchGroupName as groupName,(select d.name from YunDiseaseList as d where d.id = g.researchDiseaseId) as diseaseName,p.id as recordId,p.status)" +
                " from YunUsers as u,ResearchGroup as g,InviteApplyRecord as p,ResearchGroupVsUser as c" +
                " where g.id = p.groupId and g.id = c.groupId  " ;
        String hqlCount = "select count(*) from YunUsers as u,ResearchGroup as g,InviteApplyRecord as p,ResearchGroupVsUser as c" +
                " where g.id = p.groupId and g.id = c.groupId  ";
        if("1".equals(flag)){//邀请的信息
            hql += " and u.id = c.userId and p.flag = '"+flag+"' and p.userId = '"+userID+"' and c.createrFlag = '1' ";
            hqlCount += " and u.id = c.userId and p.flag = '"+flag+"' and p.userId = '"+userID+"' and c.createrFlag = '1' ";
        }else{
            hql += " and u.id = p.userId and p.flag = '"+flag+"' and c.userId != '"+userID+"' and p.userId = '"+userID+"' ";
            hqlCount += " and u.id = p.userId and p.flag = '"+flag+"' and c.userId != '"+userID+"' and p.userId = '"+userID+"' ";
        }
        if(!StringUtils.isEmpty(status)){
            hql += " and p.status = '"+status+"'";
            hqlCount += " and p.status = '"+status+"'";
        }
        Page<InviteUserVo> resultPage = new Page<>();
        TypedQuery<InviteUserVo> typedQuery = baseFacade.createQuery(InviteUserVo.class,hql,new ArrayList<Object>());
        Long counts =  baseFacade.createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        resultPage.setCounts(counts);
        if(perPage<=0){
            perPage =1;
        }
        if(currentPage<=0){
            currentPage=1;
        }
        typedQuery.setFirstResult((currentPage-1)*perPage);
        typedQuery.setMaxResults(perPage);
        resultPage.setPerPage((long)perPage);
        List<InviteUserVo> resultList = typedQuery.getResultList();
        resultPage.setData(resultList);
        return resultPage;
    }

    /**
     *对群组邀请进行操作，同意或拒绝，或者申请进行操作，同意或拒绝
     * @param recordId 邀请记录id
     * @param status 状态值 0表示待处理、1表示同意-1表示为拒绝
     * @return
     */
    @POST
    @Path("confirm-invite-record")
    @Transactional
    public Response confirmInviteRecord(@QueryParam("recordId")String recordId,@QueryParam("status")String status) throws Exception{
        InviteApplyRecord inviteApplyRecord = null;
        try {
            inviteApplyRecord = baseFacade.get(InviteApplyRecord.class,recordId);
            if("0".equals(inviteApplyRecord.getStatus())){
                inviteApplyRecord.setStatus(status);
                inviteApplyRecord = baseFacade.merge(inviteApplyRecord);
                if("1".equals(status)){
                    ResearchGroupVsUser researchGroupVsUser = new ResearchGroupVsUser();
                    researchGroupVsUser.setLearderFlag("0");
                    researchGroupVsUser.setCreaterFlag("0");
                    researchGroupVsUser.setUserId(inviteApplyRecord.getUserId());
                    researchGroupVsUser.setGroupId(inviteApplyRecord.getGroupId());
                    baseFacade.merge(researchGroupVsUser);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("操作异常，请联系管理员");
        }
        return Response.status(Response.Status.OK).entity(inviteApplyRecord).build();
    }

    /**
     *获取群组下的成员信息
     * @param groupId 群组id
     * @param perPage 每页条数
     * @param currentPage 当前页
     * @return
     */
    @GET
    @Path("get-research-group-users")
    public Page<YunUsers> getResearchGroupUsers(@QueryParam("groupId")String groupId,@QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        String hql = "select u from YunUsers as u,ResearchGroupVsUser as v where u.id = v.userId and v.groupId = '"+groupId+"'";
        return baseFacade.getPageResult(YunUsers.class,hql,perPage,currentPage);
    }

    /**
     * 根据群组id和研究疾病获取相关的医生信息
     * @param groupId
     * @param researchDiseaseId
     * @return
     */
    @GET
    @Path("get-group-invite-users")
    public Page<YunUsers> getGroupInviteUsers(@QueryParam("groupId")String groupId,@QueryParam("researchDiseaseId")String researchDiseaseId,
                                         @QueryParam("userName")String userName,@QueryParam("hospitalGroupId") String hospitalGroupId,@QueryParam("hospitalId") String hospitalId,@QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage) throws Exception{
        if(StringUtils.isEmpty(researchDiseaseId)){
            throw new Exception("疾病信息不能为空");
        }
        if(StringUtils.isEmpty(groupId)){
            throw new Exception("群组信息不能为空");
        }
        Page<YunUsers> yunUsersPage=new Page<>();
        YunDiseaseList yunDiseaseList = baseFacade.get(YunDiseaseList.class,researchDiseaseId);
        ResearchGroup researchGroup = baseFacade.get(ResearchGroup.class,groupId);
        YunUsers user=new YunUsers();
        String hql2="select h from ResearchGroupVsHospital vs,HospitalDict h where vs.hospitalId = h.id and vs.groupId='"+groupId+"'";
        List<HospitalDict> hospitalDictList = baseFacade.createQuery(HospitalDict.class, hql2, new ArrayList<>()).getResultList();
        String hql3=" from YunUserDisease where dcode = '"+yunDiseaseList.getDcode()+"'";
        List<YunUserDisease> yunUserDiseaseList = baseFacade.createQuery(YunUserDisease.class, hql3, new ArrayList<>()).getResultList();
        YunUserDisease yunUserDisease=new YunUserDisease();
        if(yunUserDiseaseList!=null&&yunUserDiseaseList.size()>0){
            yunUserDisease=yunUserDiseaseList.get(0);
        }
        if(!StringUtils.isEmpty(userName)){
           /* String hql1=" from YunUsers where userName='"+userName+"' or userId='"+userName+"'";
            List<YunUsers> yunUsersList = baseFacade.createQuery(YunUsers.class, hql1, new ArrayList<>()).getResultList();
            if(yunUsersList!=null&&yunUsersList.size()>0){
                user=yunUsersList.get(0);
            }else {
                throw new Exception("不存在的用户名！");
            }
            if(hospitalDictList!=null&&hospitalDictList.size()>0){
                boolean flag=false;
                for(HospitalDict hospitalDict:hospitalDictList){
                    if(user.getHospitalName().equals(hospitalDict.getHospitalName())){
                        flag=true;
                    }
                }
                if(!flag){
                    throw new Exception("您不是该医院的医生！");
                }
            }
            if(!user.getId().equals(yunUserDisease.getUserId())){
                throw new Exception("该用户没有研究此疾病！");
            }*/
            String hql = "select u from YunUsers as u,ResearchGroupVsHospital as vs,ResearchGroup as g,HospitalDict as h" +
                    " where u.rolename not in ('FORM_USER','DOCTOR_ASSISTANT') and g.status<>'-1' and g.id = vs.groupId and u.hospitalCode = h.hospitalCode and vs.hospitalId = h.id " +
                    " and exists(select 1 from YunUserDisease where dcode = '"+yunDiseaseList.getDcode()+"' and userId = u.id)" +
                    " and u.id not in(select a.userId from ResearchGroupVsUser a,ResearchGroup t where a.groupId=t.id and t.researchDiseaseId='"+researchDiseaseId+"')" +
                    " and g.researchDiseaseId = '"+researchDiseaseId+"' and g.id = '"+groupId+"'";
            if(!StringUtils.isEmpty(userName)){
                hql += " and u.userName like '"+userName+"%'";
            }
            yunUsersPage = baseFacade.getPageResult(YunUsers.class, hql, perPage, currentPage);
        }
        if(!StringUtils.isEmpty(hospitalId)){
            String hql="select u from YunUsers u,HospitalDict h,YunUserDisease d,YunDiseaseList l where " +
                    "u.rolename not in ('FORM_USER','DOCTOR_ASSISTANT') and u.id=d.userId and d.dcode=l.dcode and l.id='"+researchDiseaseId+"' and u.hospitalCode=h.hospitalCode and h.id='"+hospitalId+"'" +
                    " and u.id not in(select a.userId from ResearchGroupVsUser a,ResearchGroup g where a.groupId=g.id and g.researchDiseaseId='"+researchDiseaseId+"')";
            yunUsersPage = baseFacade.getPageResult(YunUsers.class, hql, perPage, currentPage);
        }
        return yunUsersPage;
    }

    /**
     * 多中心审核待审核
     * @param status
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-group-invites")
    public Page<ResearchGroupsVo> getGroupsInvites(@QueryParam("status") String status, @QueryParam("perPage") int perPage, @QueryParam("currentPage") int currentPage ){
        String hql = "select new com.dchealth.VO.ResearchGroupsVo(r.id,r.researchGroupName,r.researchDiseaseId,r.groupDesc,r.groupInInfo," +
                "r.manyHospitalFlag,r.dataShareLevel,r.status,u.userName)  from ResearchGroup as r,YunUsers u,ResearchGroupVsUser vs where" +
                " r.status='"+status+"' and r.manyHospitalFlag='1' and u.id=vs.userId and vs.groupId=r.id and vs.createrFlag='1' ";
        return baseFacade.getPageResult(ResearchGroupsVo.class,hql,perPage,currentPage);
    }

    /**
     * 获取群组的医院信息
     * @param groupId
     * @return
     */
    @GET
    @Path("get-group-invite-hospitals")
    public List<HospitalDict> getGroupHospitals(@QueryParam("groupId") String groupId){
        String hql="select d from HospitalDict d,ResearchGroupVsHospital h where d.id=h.hospitalId and h.groupId='"+groupId+"'";
        return baseFacade.createQuery(HospitalDict.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 获取某个医院下的所有研究此疾病的单中心群组
     * @param hospitalId
     * @param researchDiseaseId
     * @return
     */
    @GET
    @Path("get-hospital-groups")
    public List<ResearchGroup> getHospitalGroups(@QueryParam("hospitalId") String hospitalId,@QueryParam("researchDiseaseId") String researchDiseaseId){
        String hql="select g from ResearchGroup g,ResearchGroupVsHospital h where g.id=h.groupId and " +
                "h.hospitalId='"+hospitalId+"' and g.researchDiseaseId='"+researchDiseaseId+"' and g.manyHospitalFlag='0'and status='1'";
        return baseFacade.createQuery(ResearchGroup.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 获取组内医院的病例数
     * @param groupId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-group-hospitals-folders")
    public List<HospitalsFoldersVo> getGroupHospitalsFolders(@QueryParam("groupId") String groupId) throws Exception {
        if(StringUtils.isEmpty(groupId)){
            throw new Exception("群组id不能为空！");
        }
        String hql="select new com.dchealth.VO.HospitalsFoldersVo(t.hospitalName,(select count(DISTINCT f.id) from " +
                "ResearchGroup a,ResearchGroupVsHospital h,ResearchGroupVsUser c,HospitalDict d,YunUsers u,YunPatient p,YunFolder f,YunDiseaseList l " +
                "where a.id=h.groupId and a.researchDiseaseId = l.id and a.id=c.groupId and h.hospitalId = d.id and " +
                "c.userId=u.id and d.hospitalName = u.hospitalName and u.hospitalName = t.hospitalName and u.rolename <> 'FORM_USER' and u.id=p.doctorId and p.id = f.patientId and f.diagnosisCode = l.dcode and a.id='"+groupId+"') as num) from " +
                "ResearchGroup g,ResearchGroupVsHospital vs,HospitalDict t where g.id=vs.groupId and vs.hospitalId= t.id and " +
                "g.id='"+groupId+"' group by t.hospitalName order by num desc";
        return baseFacade.createQuery(HospitalsFoldersVo.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     *根据组id和医院名字查询成员详情和病例数
     * @param groupId
     * @param hospitalName
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-group-hospitals-users-folders")
    public List<HospitalsUsersFoldersVo> getGroupHospitalsUsersFolders(@QueryParam("groupId") String groupId,@QueryParam("hospitalName") String hospitalName) throws Exception {
        if(StringUtils.isEmpty(groupId)){
            throw new Exception("群组id不能为空！");
        }
        if(StringUtils.isEmpty(hospitalName)){
            throw new Exception("医院名称不能为空！");
        }
        String hql="select new com.dchealth.VO.HospitalsUsersFoldersVo(c.id,c.userId,c.userName,l.name,l.dcode,(select count(DISTINCT f.id) from " +
                "ResearchGroup a,ResearchGroupVsUser b,YunUsers u,YunPatient p,YunFolder f,YunDiseaseList l " +
                "where a.id=b.groupId and b.userId=u.id and u.userName=c.userName and a.researchDiseaseId = l.id and u.hospitalName = '"+hospitalName+"' and " +
                "u.id=p.doctorId and p.id = f.patientId and f.diagnosisCode = l.dcode and a.id='"+groupId+"') as num) from " +
                "ResearchGroup g,ResearchGroupVsUser vs,YunUsers c,YunDiseaseList l where g.id=vs.groupId and g.researchDiseaseId=l.id and vs.userId=c.id and c.rolename <> 'FORM_USER' and c.hospitalName ='"+hospitalName+"'and " +
                "g.id='"+groupId+"' group by c.userName";
        return baseFacade.createQuery(HospitalsUsersFoldersVo.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 根据疾病获取群组以及病例数量
     * @param diagnosisCode
     * @return
     */
    @GET
    @Path("get-groups-folders")
    public List<GroupsFoldersVo> getGroupsFolders(@QueryParam("diagnosisCode") String diagnosisCode){
        String hql="select new com.dchealth.VO.GroupsFoldersVo(g.id,g.researchGroupName, (select count(f.id) from ResearchGroup r,ResearchGroupVsUser u,YunUsers a,YunPatient p,YunFolder f where" +
                " r.researchGroupName= g.researchGroupName and r.id=u.groupId and u.userId=a.id and a.rolename <> 'FORM_USER' and a.id=p.doctorId and p.id=f.patientId and f.diagnosisCode='"+diagnosisCode+"') as num) " +
                "from ResearchGroup g,YunDiseaseList l where g.researchDiseaseId=l.id and l.dcode='"+diagnosisCode+"'  order by num desc";
        return baseFacade.createQuery(GroupsFoldersVo.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 根据群组id获取医生的详情病例数
     * @param groupId
     * @return
     */
    @GET
    @Path("get-groups-users-folders")
    public List<GroupUsersFoldersVo> getGroupsUsersFolders(@QueryParam("groupId") String groupId){
        String hql="select new com.dchealth.VO.GroupUsersFoldersVo(c.id,c.userId,c.userName,l.name,l.dcode,c.hospitalName,(select count(DISTINCT f.id) from " +
                "ResearchGroup a,ResearchGroupVsUser b,YunUsers u,YunPatient p,YunFolder f,YunDiseaseList l " +
                "where a.id=b.groupId and b.userId=u.id and u.userName=c.userName and a.researchDiseaseId = l.id and " +
                "u.id=p.doctorId and p.id = f.patientId and f.diagnosisCode = l.dcode and a.id='"+groupId+"') as num) from " +
                "ResearchGroup g,ResearchGroupVsUser vs,YunUsers c,YunDiseaseList l where c.rolename <> 'FORM_USER' and g.id=vs.groupId and g.researchDiseaseId=l.id and vs.userId=c.id and " +
                "g.id='"+groupId+"' group by c.userName order by num desc";
        return baseFacade.createQuery(GroupUsersFoldersVo.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     * 根据疾病获取所有医院以及录入病例数
     * @param diagnosisCode
     * @return
     */
    @GET
    @Path("get-hospitals-folders")
    public List<HospitalDisFoldersVo> gethHospitalsFolders(@QueryParam("diagnosisCode") String diagnosisCode){
        String hql="select new com.dchealth.VO.HospitalDisFoldersVo(u.hospitalName,count(f.id) as num) " +
                "from YunUsers u,YunPatient p,YunFolder f where " +
                "u.rolename <> 'FORM_USER' and u.id=p.doctorId and p.id=f.patientId and " +
                "f.diagnosisCode = '"+diagnosisCode+"' group by u.hospitalName order by num desc";
        return baseFacade.createQuery(HospitalDisFoldersVo.class, hql, new ArrayList<>()).getResultList();
    }

    /**
     * 根据医院名称获取医生详情和病例数
     * @param diagnosisCode
     * @param hospitalName
     * @return
     */
    @GET
    @Path("get-hospitals-users-folders")
    public List<GroupUsersFoldersVo> gethHospitalsUsersFolders(@QueryParam("diagnosisCode") String diagnosisCode,@QueryParam("hospitalName") String hospitalName){
        String hql="select new com.dchealth.VO.GroupUsersFoldersVo(u.id,u.userId,u.userName,l.name,l.dcode,u.hospitalName,count(DISTINCT f.id) as num) " +
                "from YunUsers u,YunPatient p,YunFolder f,YunDiseaseList l where " +
                "u.rolename <> 'FORM_USER' and u.id=p.doctorId and u.hospitalName='"+hospitalName+"' and p.id=f.patientId and f.diagnosisCode=l.dcode and " +
                "f.diagnosisCode ='"+diagnosisCode+"' group by u.userName order by num desc";
        return baseFacade.createQuery(GroupUsersFoldersVo.class, hql, new ArrayList<>()).getResultList();
    }

    /**
     * 多中心群组通过审核
     * @param groupId
     * @return
     */
    @POST
    @Path("audit-status")
    @Transactional
    public Response updateStatus(@QueryParam("groupId") String groupId){
        ResearchGroup researchGroup = baseFacade.get(ResearchGroup.class, groupId);
        researchGroup.setStatus("1");
        ResearchGroup merge = baseFacade.merge(researchGroup);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

}
