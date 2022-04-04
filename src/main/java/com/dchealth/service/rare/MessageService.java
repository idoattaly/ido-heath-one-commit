package com.dchealth.service.rare;

import com.dchealth.VO.*;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.entity.rare.Message;
import com.dchealth.entity.rare.MessageText;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Administrator on 2017/11/9.
 */
@Produces("application/json")
@Path("message")
@Controller
public class MessageService {
    @Autowired
    private BaseFacade baseFacade;

    /**
     * 添加，修改站内信
     * @param messageVo
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response mergeMessage(MessageVo messageVo){
        String messageId = "";
        List<String> recIds = messageVo.getRecIds();
        if(StringUtils.isEmpty(messageVo.getId())){//新增
            MessageText messageText = new MessageText();
            messageText.setTitle(messageVo.getTitle());
            messageText.setContent(messageVo.getContent());
            messageText.setStatus("0");
            messageText.setSendDate(new  Timestamp(new Date().getTime()));
            messageText.setCreateBy(messageVo.getSendId());
            messageText.setModifyBy(messageVo.getSendId());
            messageText.setCreateDate(new Timestamp(new Date().getTime()));
            messageText.setModifyDate(new  Timestamp(new Date().getTime()));
            MessageText merge = baseFacade.merge(messageText);
            messageId = merge.getId();
        }else{//修改
            messageId = messageVo.getId();
            MessageText messageText = baseFacade.get(MessageText.class,messageVo.getId());
            messageText.setTitle(messageVo.getTitle());
            messageText.setContent(messageVo.getContent());
            messageText.setSendDate(new  Timestamp(new Date().getTime()));
            messageText.setModifyBy(messageVo.getSendId());
            messageText.setModifyDate(new  Timestamp(new Date().getTime()));
            baseFacade.merge(messageText);
            String hql = " delete from Message where messageId = '"+messageText.getId()+"'";
            baseFacade.excHql(hql);

        }
        if(recIds!=null && !recIds.isEmpty()){
            List<Message> messageList = new ArrayList<>();
            if(recIds.size()==1 && "0".equals(recIds.get(0))){//所有人
                List<YunUsers> userList = baseFacade.findAll(YunUsers.class);
                for(YunUsers yunUsers:userList){
                    if(!yunUsers.getId().equals(messageVo.getSendId())){
                        Message message = new Message();
                        message.setSendId(messageVo.getSendId());
                        message.setRecId(yunUsers.getId());
                        message.setMessageId(messageId);
                        message.setStatus("0");
                        message.setCreateDate(new  Timestamp(new Date().getTime()));
                        message.setModifyDate(new  Timestamp(new Date().getTime()));
                        messageList.add(message);
                    }
                }
            }else{
                for(String recId:recIds){
                    Message message = new Message();
                    message.setSendId(messageVo.getSendId());
                    message.setRecId(recId);
                    message.setMessageId(messageId);
                    message.setStatus("0");
                    message.setCreateDate(new  Timestamp(new Date().getTime()));
                    message.setModifyDate(new  Timestamp(new Date().getTime()));
                    messageList.add(message);
                }
            }
            baseFacade.batchInsert(messageList);
            messageVo.setId(messageId);
        }
        return Response.status(Response.Status.OK).entity(messageVo).build();
    }

    /**
     * 根据站内信id删除站内信 为逻辑删除 status标记为-1
     * @param id
     * @return
     * @throws Exception
     */
    @POST
    @Path("delete")
    @Transactional
    public Response deleteMessageText(@QueryParam("id")String id) throws Exception{
        if(StringUtils.isEmpty(id)){
            throw new Exception("站内信id参数不能为空");
        }
        MessageText messageText = baseFacade.get(MessageText.class,id);
        messageText.setStatus("-1");
        MessageText merge = baseFacade.merge(messageText);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据用户id获取站内信
     * @param userId
     * @param title 站内信标题
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-self-create-messages")
    public Page<MessageVo> getCreateMessagesBySelf(@QueryParam("userId") String userId,@QueryParam("title")String title,@QueryParam("perPage")int perPage,@QueryParam("currentPage")
                                                   int currentPage) throws Exception{
        if(StringUtils.isEmpty(userId)){
            throw new Exception("用户id参数不能为空");
        }
        String hql = "select new com.dchealth.VO.MessageVo(t.id,'"+userId+"',t.title,t.content,t.sendDate,t.createDate) from MessageText as t where t.status = '0' " +
                " and t.createBy = '"+userId+"'";
        if(!StringUtils.isEmpty(title)){
            hql += " and t.title like '%"+title+"%'";
        }
        Page<MessageVo> messageVoPage = baseFacade.getPageResult(MessageVo.class,hql,perPage,currentPage);
        List<MessageVo> messageVos = messageVoPage.getData();
        String messageIds = getMessageIds(messageVos);
        if(!StringUtils.isEmpty(messageIds)){
            String reHql = " from Message where sendId = '"+userId+"' and messageId in("+messageIds+")";
            List<Message> messageList = baseFacade.createQuery(Message.class,reHql,new ArrayList<Object>()).getResultList();
            Map<String,List<String>> listMap = getMessageRecIdsMap(messageList);
            messageVos = setRecIdsByMap(listMap,messageVos);
            messageVoPage.setData(messageVos);
        }
        return messageVoPage;
    }

    /**
     * 根据站内信id获取收信人员信息
     * @param messageId
     * @return
     */
    @GET
    @Path("get-self-messageRecVos")
    public List<MessageRecVo> getSelfMessageRecVos(@QueryParam("messageId")String messageId, @QueryParam("perPage")
                                                    int perPage, @QueryParam("currentPage")int currentPage){
        List<MessageRecVo> messageRecVos = new ArrayList<>();
        String hql = " from MessageText where status<>'-1' and  id = '"+messageId+"'";
        List<MessageText> messageTexts = baseFacade.createQuery(MessageText.class,hql,new ArrayList<Object>()).getResultList();
        if(messageTexts!=null && !messageTexts.isEmpty()){
            MessageText messageText = messageTexts.get(0);
            String userHql = "select u.user_name,e.status from Message as e,yun_users as u where " +
                    " e.recId = u.id and e.status<>'-1' and e.messageId = '"+messageId+"'";
            List list = baseFacade.createNativeQuery(userHql).getResultList();
            List<RecUserInfo> recUserInfos = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                RecUserInfo recUserInfo = new RecUserInfo();
                Object[] params = (Object[])list.get(i);
                String userName = params[0]==null?"":(params[0]+"");
                String status = params[1]==null?"":(params[1]+"");
                recUserInfo.setUserName(userName);
                recUserInfo.setStatus(status);
                recUserInfos.add(recUserInfo);
            }
            MessageRecVo  messageRecVo = new MessageRecVo(messageText.getId(),messageText.getTitle(),messageText.getContent(),recUserInfos,messageText.getCreateDate());
            messageRecVos.add(messageRecVo);
        }
        return messageRecVos;
    }
    /**
     * 登录用户获取自己收到的站内信通知
     * @param userId
     * @param title
     * @return
     */
    @GET
    @Path("get-my-message-texts")
    public Page<MessageSendDetail> getMyMessageTexts(@QueryParam("userId")String userId, @QueryParam("title")String title, @QueryParam("perPage")
                                               int perPage, @QueryParam("currentPage")int currentPage) throws Exception{
        Page<MessageSendDetail> resultPage = new Page<>();
        String hql = "select new com.dchealth.VO.MessageSendDetail(m.id,m.title,m.content,(select id from YunUsers where id = m.createBy) as sendId," +
                " (select userName from YunUsers where id = m.createBy) as userName,e.status,m.createDate) from MessageText as m,Message as e where m.status <>'-1'" +
                " and m.id = e.messageId and e.status<>'-1' and e.recId = '"+userId+"'";
        String hqlCount = "select count(*) from MessageText as m,Message as e where m.status <>'-1'" +
                " and m.id = e.messageId and e.status<>'-1' and e.recId = '"+userId+"'";
        TypedQuery<MessageSendDetail> typedQuery = baseFacade.createQuery(MessageSendDetail.class,hql,new ArrayList<Object>());
        Long counts =  baseFacade.createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        resultPage.setCounts(counts);
        if(perPage<=0){
            perPage =15;
        }
        if(currentPage<=0){
            currentPage=1;
        }
        typedQuery.setFirstResult((currentPage-1)*perPage);
        typedQuery.setMaxResults(perPage);
        resultPage.setPerPage((long)perPage);
        List<MessageSendDetail> resultList = typedQuery.getResultList();
        resultPage.setData(resultList);
        return resultPage;
    }

    /**
     * 根据站内信id，用户id，状态标识站内信为已读或删除状态
     * @param id
     * @param userId
     * @param status 0：未读 1:已读 -1：删除
     * @return
     */
    @POST
    @Path("change-message-status")
    @Transactional
    public Response changeMessage(@QueryParam("id")String id,@QueryParam("userId")String userId,@QueryParam("status")String status){
        List<String> ids = new ArrayList<>();
        String hql = " update Message set status = '"+status+"' where recId = '"+userId+"' and messageId = '"+id+"'";
        int result = baseFacade.excHql(hql);
        ids.add(result+"");
        return Response.status(Response.Status.OK).entity(ids).build();
    }
    /**
     * 对站内信接收者赋值
     * @param listMap
     * @param messageVos
     * @return
     */
    public List<MessageVo> setRecIdsByMap(Map<String,List<String>> listMap,List<MessageVo> messageVos){
        if(listMap!=null && !listMap.isEmpty()){
            for(MessageVo messageVo:messageVos){
                if(listMap.get(messageVo.getId())!=null){
                    messageVo.setRecIds(listMap.get(messageVo.getId()));
                }
            }
        }
        return messageVos;
    }

    public Map<String,List<String>> getMessageRecIdsMap(List<Message> messageList){
        Map<String,List<String>> listMap = new HashMap<>();
        if(messageList!=null && !messageList.isEmpty()){
            for(Message message:messageList){
                    if(listMap.get(message.getMessageId())!=null){
                        List<String> recIds = listMap.get(message.getMessageId());
                        recIds.add(message.getRecId());
                    }else{
                        List<String> recIds = new ArrayList<>();
                        recIds.add(message.getRecId());
                        listMap.put(message.getMessageId(),recIds);
                    }
            }
        }
        return listMap;
    }
    /**
     * 获取站内信id串
     * @param messageVos
     * @return
     */
    public String getMessageIds(List<MessageVo> messageVos){
        StringBuffer sb = new StringBuffer("");
        if(messageVos!=null && !messageVos.isEmpty()){
            for(MessageVo messageVo:messageVos){
                sb.append("'").append(messageVo.getId()).append("',");
            }
        }
        String messageIds = sb.toString();
        if(!StringUtils.isEmpty(messageIds)){
            messageIds = messageIds.substring(0,messageIds.length()-1);
        }
        return messageIds;
    }
}

