package com.dchealth.service.common;

import com.dchealth.VO.YunDictTypeAndItemVo;
import com.dchealth.entity.common.YunDictitem;
import com.dchealth.entity.common.YunDicttype;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.StringUtils;
import com.dchealth.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/16.
 */
@Controller
@Produces("application/json")
@Path("dict")
@Service
public class DictService {


    @Autowired
    private BaseFacade baseFacade;

    /**
     * 获取字典列表
     *
     * @param typeName 字典名称，如果为空或者不写则返回所有的值
     * @return
     */
    @GET
    @Path("list-type-by-name")
    public List<YunDicttype> listDictType(@QueryParam("typeName") String typeName,@QueryParam("userId")String userId) {
        YunUsers yunUsers = baseFacade.get(YunUsers.class,userId);
        String hql = "from YunDicttype where 1=1 ";
        if (typeName != null && !"".equals(typeName)) {
            hql += " and typeName like '%" + typeName + "%'";
        }
        if (userId != null && !"".equals(userId)) {
            hql += " and (userId='"+userId+"' or (deptId='"+yunUsers.getDeptId()+"' and deptId<>'0'))";
        }
        return baseFacade.createQuery(YunDicttype.class, hql, new ArrayList<Object>()).getResultList();
}

    /**
     * 获取所有的字典项目
     *
     * @param typeId
     * @return
     */
    @GET
    @Path("list-item-by-type")
    public List<YunDictitem> listDictItemByTypeId(@QueryParam("typeId") String typeId) {
        String hql = "from YunDictitem as item where item.typeIdDm='" + typeId + "'";
        return baseFacade.createQuery(YunDictitem.class, hql, new ArrayList<Object>()).getResultList();
    }


    /**
     * 添加新的字典
     *
     * @param yunDicttype
     * @return
     */
    @Path("add-new-type")
    @Transactional
    @POST
    public Response addDictType(YunDicttype yunDicttype) {
        YunDicttype dicttype = baseFacade.merge(yunDicttype);
        return Response.status(Response.Status.OK).entity(dicttype).build();
    }


    /**
     * 单个添加字典项目
     *
     * @param yunDictitem
     * @return
     */
    @POST
    @Transactional
    @Path("add-new-item")
    public Response addDictItem(YunDictitem yunDictitem) {
        YunDictitem merge = baseFacade.merge(yunDictitem);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 添加字典项目
     * @param yunDictitems
     * @return
     */
    @POST
    @Transactional
    @Path("add-new-items")
    public Response addDictItmes(List<YunDictitem> yunDictitems) {
        String typeIdDm = yunDictitems==null?"":(yunDictitems.isEmpty()?"":yunDictitems.get(0).getTypeIdDm());
        if(!StringUtils.isEmpty(typeIdDm)){
            String hql = " delete from YunDictitem where typeIdDm = '"+typeIdDm+"'";
            baseFacade.excHql(hql);
        }
        for (YunDictitem item : yunDictitems) {
            baseFacade.merge(item);
        }
        return Response.status(Response.Status.OK).entity(yunDictitems).build();
    }


    /**
     * 删除某一下字典
     * @param serialNo
     * @return
     */
    @POST
    @Transactional
    @Path("del-item")
    public Response removeDictItem(@QueryParam("serialNo") String serialNo){
        YunDictitem yunDictitem = baseFacade.get(YunDictitem.class, serialNo);
        baseFacade.remove(yunDictitem);
        return Response.status(Response.Status.OK).entity(yunDictitem).build();
    }

    /**
     * 删除所有的类别
     * @param typeId
     * @return
     */
    @POST
    @Transactional
    @Path("del-type")
    public Response removeDictType(@QueryParam("typeId")String typeId){
        try {
            String itemHql ="from YunDictitem as item where item.typeIdDm='"+typeId+"'";
            List<YunDictitem> yunDictitems = baseFacade.createQuery(YunDictitem.class,itemHql,new ArrayList<Object>()).getResultList();
            List<String> itemIds = new ArrayList<String>();
            for(YunDictitem dictitem:yunDictitems){
                itemIds.add(dictitem.getSerialNo());
            }

            String typeHql = "from YunDicttype as type where type.typeId='"+typeId+"'";
            List<YunDicttype> yunDicttypes = baseFacade.createQuery(YunDicttype.class,typeHql,new ArrayList<Object>()).getResultList();
            List<String> typeIds = new ArrayList<>() ;
            for (YunDicttype dicttype:yunDicttypes){
                typeIds.add(dicttype.getTypeId()) ;
            }
            baseFacade.removeByStringIds(YunDictitem.class,itemIds);
            baseFacade.removeByStringIds(YunDicttype.class,typeIds);
            return Response.status(Response.Status.OK).entity(yunDictitems).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 根据医生id获取其下的所有字典数据信息
     * @param doctorId
     * @return
     */
    @GET
    @Path("list-all-value")
    public YunDictTypeAndItemVo getAllDictAndItem (@QueryParam("doctorId") String doctorId){
        String hql = " from YunDicttype as type where type.userId = '"+doctorId+"'";
        List<YunDicttype> yunDicttypes = baseFacade.createQuery(YunDicttype.class,hql,new ArrayList<Object>()).getResultList();
        String itemHql = "select item from YunDicttype as type,YunDictitem as item where type.typeId = item.typeIdDm and type.userId = '"+doctorId+"'";
        List<YunDictitem> yunDictitems = baseFacade.createQuery(YunDictitem.class,itemHql,new ArrayList<Object>()).getResultList();
        YunDictTypeAndItemVo yunDictTypeAndItemVo = new YunDictTypeAndItemVo();
        yunDictTypeAndItemVo.setDoctorId(doctorId);
        yunDictTypeAndItemVo.setYunDicttypes(yunDicttypes);
        yunDictTypeAndItemVo.setYunDictitems(yunDictitems);
        return yunDictTypeAndItemVo;
    }

    /**
     * 导入该医生下的字典及项目信息
     * @param yunDictTypeAndItemVo
     * @return
     */
    @POST
    @Path("import-data-value")
    @Transactional
    public Response importDictAndItemValue(YunDictTypeAndItemVo yunDictTypeAndItemVo){
        String doctorId = yunDictTypeAndItemVo.getDoctorId();
        List<YunDicttype> yunDicttypes = yunDictTypeAndItemVo.getYunDicttypes();
        List<YunDictitem> yunDictitems = yunDictTypeAndItemVo.getYunDictitems();
        Map<String,String> newDictypeIdMap = new HashMap<>();
        Map<String,String> oldDictypeIdMap = new HashMap<>();
        for(YunDicttype yunDicttype:yunDicttypes){
            YunDicttype yunDicttype1 = getYunDicttypeByName(yunDicttype.getTypeName(),doctorId);
            if(yunDicttype1==null){
                YunDicttype merge = baseFacade.merge(yunDicttype);
                newDictypeIdMap.put(yunDicttype.getTypeId(),merge.getTypeId());
            }else{
                oldDictypeIdMap.put(yunDicttype.getTypeId(),yunDicttype1.getTypeId());
            }
        }
        dealOldYunDictItems(oldDictypeIdMap);
        for(YunDictitem yunDictitem:yunDictitems){
            YunDictitem yunDictitem1 = baseFacade.get(YunDictitem.class,yunDictitem.getSerialNo());
            if(yunDictitem1!=null){
                continue;
            }
            if(newDictypeIdMap.containsKey(yunDictitem.getTypeIdDm())){
                yunDictitem.setTypeIdDm(newDictypeIdMap.get(yunDictitem.getTypeIdDm()));
            }else if(oldDictypeIdMap.containsKey(yunDictitem.getTypeIdDm())){
                yunDictitem.setTypeIdDm(oldDictypeIdMap.get(yunDictitem.getTypeIdDm()));
            }
            baseFacade.merge(yunDictitem);
        }
        return Response.status(Response.Status.OK).entity(yunDicttypes).build();
    }

    /**
     * 删除字典项下的字典信息
     * @param oldDictypeIdMap
     */
    public void dealOldYunDictItems(Map<String,String> oldDictypeIdMap){
        if(oldDictypeIdMap!=null && !oldDictypeIdMap.isEmpty()){
            for(String key:oldDictypeIdMap.keySet()){
                delDictItemByTypeIdDm(oldDictypeIdMap.get(key));
            }
        }
    }

    /**
     * 通过字典项的id删除字典项下的字典信息
     * @param typeIdDm
     */
    public void delDictItemByTypeIdDm(String typeIdDm){
        String hql = "delete from YunDictitem where typeIdDm = '"+typeIdDm+"'";
        baseFacade.excHql(hql);
    }
    /**
     * 根据字典名称和医生id获取私有字典信息
     * @param typeName
     * @param doctorId
     * @return
     */
    public YunDicttype getYunDicttypeByName(String typeName,String doctorId){
        String hql = " from YunDicttype where typeName = '"+typeName+"' and userId = '"+doctorId+"'";
        List<YunDicttype> yunDicttypeList = baseFacade.createQuery(YunDicttype.class,hql,new ArrayList<Object>()).getResultList();
        if(yunDicttypeList!=null && !yunDicttypeList.isEmpty()){
            return yunDicttypeList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 根据字典名称和字典类型查询其字典项目信息
     * @param name
     * @param pubFlag
     * @return
     */
    @GET
    @Path("list-item-by-name")
    public List<YunDictitem> getYunDictitemsByTypeName(@QueryParam("name") String name,@QueryParam("pubFlag") String pubFlag) throws Exception{
        YunUsers yunUsers = UserUtils.getYunUsers();
        String hql = "select type.typeId from YunDicttype as type where 1=1 ";
        if("0".equals(pubFlag)){//0为私有字典
            hql += " and (type.userId='"+yunUsers.getId()+"' or (type.deptId='"+yunUsers.getDeptId()+"' and type.deptId<>'0')) " +
                    " and type.typeName = '"+name+"'";
        }else if("1".equals(pubFlag)){//1为共有字典
            hql += " and type.userId = '0' and type.typeName = '"+name+"'";
        }
        List<String> typeIds = baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList();
//        if("0".equals(pubFlag) && (typeIds==null || typeIds.isEmpty())){
//            String hql2 = "select type.typeId from YunDicttype as type " +
//                          " where type.userId <> '0' and (type.deptId = '0' or type.deptId is null) " +
//                          " and type.typeName = '"+name+"'";
//            typeIds = baseFacade.createQuery(String.class,hql2,new ArrayList<Object>()).getResultList();
//        }
        String typeId = typeIds.isEmpty()?"":typeIds.get(0);
        String itemHql = "select item from YunDictitem as item where item.typeIdDm = '"+typeId+"'";
        List<YunDictitem> yunDictitems = baseFacade.createQuery(YunDictitem.class,itemHql,new ArrayList<Object>()).getResultList();
        return yunDictitems;
    }

    /**
     * 根据医生ID,科室id和字典标识查询字典类型信息
     * @param doctorId
     * @param deptId
     * @param pubFlag
     * @return
     */
    @GET
    @Path("list-type-by-doctorId")
    public List<YunDicttype> listDictTypeByDoctorId(@QueryParam("doctorId") String doctorId,@QueryParam("deptId")String deptId,@QueryParam("pubFlag") String pubFlag) {
        String hql = "from YunDicttype as type where 1=1 ";
        if("0".equals(pubFlag)){//0为私有字典
            hql += " and ( type.userId='"+doctorId+"' or (type.deptId='"+deptId+"' and type.deptId<>'0')) ";
        }else{//1为共有字典
            hql += " and type.userId = '0' ";
        }
        return baseFacade.createQuery(YunDicttype.class, hql, new ArrayList<Object>()).getResultList();
    }
}
