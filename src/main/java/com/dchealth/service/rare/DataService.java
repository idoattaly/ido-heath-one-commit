package com.dchealth.service.rare;

import com.dchealth.VO.DataElementFormat;
import com.dchealth.VO.YunDataFormatVo;
import com.dchealth.VO.YunValueFormatVo;
import com.dchealth.entity.rare.YunValue;
import com.dchealth.entity.rare.YunValueFormat;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.JSONUtil;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 元数据管理服务
 * Created by Administrator on 2017/6/19.
 */
@Controller
@Produces("application/json")
@Path("data")
public class DataService {

    @Autowired
    private BaseFacade baseFacade ;

    /**
     * 获取元数据列表
     * @param idcode
     * @param doctorId
     * @param name
     * @param zflags
     * @return
     */
    @GET
    @Path("yun-value-list")
    public List<YunValue> getYunValues(@QueryParam("idcode") String idcode,@QueryParam("doctorId") String doctorId,
                                       @QueryParam("name")String name,@QueryParam("zflags")String zflags,@QueryParam("deptId")String deptId,
                                       @QueryParam("pubFlag") String pubFlag) throws Exception {
        String hql = "from YunValue as v where 1=1 " ;
        if(idcode!=null&&!"".equals(idcode)){
            hql+=" and v.idcode='"+idcode+"'" ;
        }

        if ("未分组".equals(zflags)){
            hql+=" and  (v.zflags is null or v.zflags = '')";
        }

        if(name!=null&&!"".equals(name)){
            hql+=" and v.name='"+name+"'" ;
        }

        if(zflags!=null&&!"".equals(zflags)&&!"未分组".equals(zflags)){
            hql +=" and v.zflags='"+zflags+"'" ;
        }

        if("".equals(pubFlag)||pubFlag==null){
            throw  new Exception("缺少pubFlag，公共私有数据标识。0表示私有数据，1表示公共数据");
        }

        if("1".equals(pubFlag)){
            hql += " and v.doctorId='0'" ;
        }

        if("0".equals(pubFlag)){
            if(doctorId==null||"".equals(doctorId)){
                throw  new Exception("缺少doctorId，用户标识");
            }
            if(deptId==null||"".equals(deptId)){
                throw  new Exception("缺少deptId，科室标识 ");
            }
            hql+=" and (v.doctorId='"+doctorId+"' or (v.deptId='"+deptId+"' and v.deptId <>'0'))" ;
        }
        return baseFacade.createQuery(YunValue.class,hql,new ArrayList<Object>()).getResultList();
    }


    /**
     * 新增修改元数据
     * @param yunValue
     * @return
     */
    @POST
    @Path("merge-value")
    @Transactional
    public Response addYunValue(YunValue yunValue) throws Exception{
        String hql = " from YunValue where name = '"+yunValue.getName()+"' and id <>'"+yunValue.getId()+"'";
        List<YunValue> yunValueList = baseFacade.createQuery(YunValue.class,hql,new ArrayList<Object>()).getResultList();
        if(yunValueList!=null && !yunValueList.isEmpty()){
            throw new Exception("元数据名称已存在，请重新修改");
        }
        return Response.status(Response.Status.OK).entity(baseFacade.merge(yunValue)).build();
    }

    /**
     * 删除元数据
     * @param id
     * @return
     */
    @Transactional
    @POST
    @Path("del-value")
    public Response delYunValue(@QueryParam("id") String id){
        //删除元数据格式化
        //删除元数据
        List<String> ids = new ArrayList<>() ;
        ids.add(id);
        baseFacade.removeByStringIds(YunValueFormat.class,ids);
        baseFacade.removeByStringIds(YunValue.class,ids);
        return Response.status(Response.Status.OK).entity(ids).build();
    }

    /**
     * 获取格式化项目
     * @param dataId
     * @return
     * @throws IOException
     * @throws JSONException
     */
    @GET
    @Path("value-data-format")
    public YunDataFormatVo getYunDataFormatVO(@QueryParam("dataId") String dataId) throws IOException, JSONException {
        String id = dataId;
        YunValueFormat yunValueFormat = baseFacade.get(YunValueFormat.class, id);
        DataElementFormat dataElement = (DataElementFormat) JSONUtil.JSONToObj(yunValueFormat.getFormat(), DataElementFormat.class);
        YunDataFormatVo yunDataFormatVo = new YunDataFormatVo() ;
        yunDataFormatVo.setId(String.valueOf(yunValueFormat.getId()));
        yunDataFormatVo.setDict(yunValueFormat.getDict());
        yunDataFormatVo.setExtend(dataElement.getExtend());
        yunDataFormatVo.setTitle(yunValueFormat.getTitle());
        yunDataFormatVo.setHead(dataElement.getHead());
        yunDataFormatVo.setLevel(yunValueFormat.getLevel());
        yunDataFormatVo.setModif_date(yunValueFormat.getModifyDate());
        yunDataFormatVo.setOlddata(yunValueFormat.getOlddata());
        yunDataFormatVo.setPart(dataElement.getPart());
        yunDataFormatVo.setPlac(dataElement.getPlac());
        yunDataFormatVo.setRelyon(yunValueFormat.getRelyon());
        yunDataFormatVo.setRelyonvalue(dataElement.getRelyonvalue());
        yunDataFormatVo.setTail(dataElement.getTail());
        yunDataFormatVo.setTemplet(dataElement.getTemplet());
        return yunDataFormatVo ;
    }

    /**
     * 修改格式化项目
     * @param yunDataFormatVo
     * @return
     */
    @POST
    @Transactional
    @Path("merge-value-data-format")
    public Response mergeDataFormat(YunDataFormatVo yunDataFormatVo) throws IOException, JSONException {
        YunValueFormat yunValueFormat = baseFacade.get(YunValueFormat.class,yunDataFormatVo.getId());
        if(yunValueFormat==null){
            yunValueFormat = new YunValueFormat() ;
            yunValueFormat.setId(yunDataFormatVo.getId());
        }
        yunValueFormat.setDict(yunDataFormatVo.getDict());
        yunValueFormat.setLevel(yunDataFormatVo.getLevel());
        yunValueFormat.setModifyDate(new Timestamp(new Date().getTime()));
        yunValueFormat.setOlddata(yunDataFormatVo.getOlddata());
        yunValueFormat.setRelyon(yunDataFormatVo.getRelyon());
        yunValueFormat.setTitle(yunDataFormatVo.getTitle());

        DataElementFormat dataElementFormat = new DataElementFormat(yunDataFormatVo.getHead(),
                yunDataFormatVo.getExtend(),yunDataFormatVo.getRelyonvalue(),yunDataFormatVo.getTail(),
                yunDataFormatVo.getTemplet(),yunDataFormatVo.getPart(),yunDataFormatVo.getPlac());
        String format = JSONUtil.objectToJson(dataElementFormat).toString();
        yunValueFormat.setFormat(format);
        YunValueFormat merge = baseFacade.merge(yunValueFormat);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取元数据分组
     * @param doctorId
     * @return
     */
    @GET
    @Path("list-value-group")
    public List<String> getDiseaseGroupName(@QueryParam("doctorId") String doctorId){
        String hql = "select distinct (case when y.zflags is null then '' else y.zflags end) from YunValue as y where y.doctorId='"+doctorId+"' " +
                     " or exists(select 1 from YunUsers as u where u.id = '"+doctorId+"' and u.deptId = y.deptId and u.status<>'-1' and u.deptId<>'0')" ;
        return baseFacade.createQuery(String.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    /**
     * 获取元数据及其格式信息（导出）
     * @param doctorId
     * @return
     */
    @GET
    @Path("list-all-value")
    public YunValueFormatVo getAllYunValueAndFormat(@QueryParam("doctorId") String doctorId){
        String hql = " from YunValue as y where y.doctorId='"+doctorId+"'" ;
        List<YunValue> yunValues = baseFacade.createQuery(YunValue.class,hql,new ArrayList<Object>()).getResultList();
        hql = "select f from YunValue as y,YunValueFormat as f where y.id = f.id and y.doctorId='"+doctorId+"'" ;
        List<YunValueFormat> yunValueFormats = baseFacade.createQuery(YunValueFormat.class,hql,new ArrayList<Object>()).getResultList();
        YunValueFormatVo yunValueFormatVo = new YunValueFormatVo();
        yunValueFormatVo.setDoctorId(doctorId);
        yunValueFormatVo.setYunValues(yunValues);
        yunValueFormatVo.setYunValueFormats(yunValueFormats);
        return yunValueFormatVo;
    }

    /**
     * 导入该医生下的元数据及其格式信息（导入）
     * @param yunValueFormatVo
     * @return
     */
    @POST
    @Path("import-data-value")
    @Transactional
    public Response importYunValueAndFormat(YunValueFormatVo yunValueFormatVo){
        String doctorId = yunValueFormatVo.getDoctorId();
        List<YunValue> yunValues = yunValueFormatVo.getYunValues();
        List<YunValueFormat> yunValueFormats = yunValueFormatVo.getYunValueFormats();
        Map<String,String> yunValueIdsMap = new HashMap<>();
        removeYunValueByDoctorId(doctorId);
        for(YunValue yunValue:yunValues){
            yunValue.setModifyDate(new Timestamp(new Date().getTime()));
            YunValue merge = baseFacade.merge(yunValue);
            yunValueIdsMap.put(yunValue.getId(),merge.getId());
        }
        for(YunValueFormat yunValueFormat:yunValueFormats){
            yunValueFormat.setModifyDate(new Timestamp(new Date().getTime()));
            yunValueFormat.setId(yunValueIdsMap.get(yunValueFormat.getId()));
            baseFacade.merge(yunValueFormat);
        }
        return Response.status(Response.Status.OK).entity(yunValues).build();
    }

    /**
     * 根据医生id删除元数据信息
     * @param doctorId
     */
    public void removeYunValueByDoctorId(String doctorId){
        String hql = "delete from YunValue where doctorId = '"+doctorId+"'";
        baseFacade.excHql(hql);
    }
}
