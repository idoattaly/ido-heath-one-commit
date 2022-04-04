package com.dchealth.service.rare;

import com.dchealth.entity.rare.YunDiseaseKnowledge;
import com.dchealth.entity.rare.YunDiseaseType;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/18.
 */
@Controller
@Produces("application/json")
@Path("knowledge")
public class DiseaseKnowledgeService {

    @Autowired
    private BaseFacade baseFacade;

    /**
     * 根据病种类型id或名称,类型查询病种信息，不填写则默认查询全部病种信息
     * @param id
     * @param name
     * @param type
     * @return
     */
    @GET
    @Path("list-disease-type")
    public List<YunDiseaseType> getDiseaseTypes(@QueryParam("id") String id,@QueryParam("name") String name,@QueryParam("type") String type){
        String hql = " from YunDiseaseType as t where 1=1 ";
        if(id!=null && !"".equals(id)){
            hql += " and t.id = '"+id+"'";
        }
        if(name!=null && !"".equals(name)){
            hql += " and t.name = '"+name+"'";
        }
        if(type!=null && !"".equals(type)){
            hql += " and t.type = '"+type+"'";
        }
        return baseFacade.createQuery(YunDiseaseType.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *根据病种类型,病种名称,标题,编码获取疾病知识信息
     * @param type 病种类型
     * @param typeName 病种名称
     * @param title 疾病知识-标题
     * @param dcode 疾病知识-编码
     * @return
     */
    @GET
    @Path("list-disease-knowledge")
    public List<YunDiseaseKnowledge> getDiseaseKnowledges(@QueryParam("type") String type, @QueryParam("typeName") String typeName,
                                                          @QueryParam("title") String title, @QueryParam("dcode") String dcode){
        String hql = "select dk from YunDiseaseKnowledge as dk,YunDiseaseType as t where dk.diseaseType = t.type";
        if(type!=null && !"".equals(type)){
            hql += " and t.type = '"+type+"'";
        }
        if(typeName!=null && !"".equals(typeName)){
            hql += " and t.name = '"+typeName+"'";
        }
        if(title!=null && !"".equals(title)){
            hql += " and dk.title = '"+title+"'";
        }
        if(dcode!=null && !"".equals(dcode)){
            hql += " and dk.diseaseCode = '"+dcode+"'";
        }
        hql +=" order by dk.createDate asc";
        return baseFacade.createQuery(YunDiseaseKnowledge.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据id查询疾病知识信息
     * @param id 疾病知识id
     * @return
     * @throws Exception
     */
    @GET
    @Path("find-knowledge")
    public YunDiseaseKnowledge getYunDiseaseKnowledge(@QueryParam("id") String id) throws Exception{
        return baseFacade.get(YunDiseaseKnowledge.class,id);
    }

    /**
     * 添加或修改疾病知识信息
     * @param yunDiseaseKnowledge
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response mergeYunDiseaseKnowledge(YunDiseaseKnowledge yunDiseaseKnowledge){
        String content = yunDiseaseKnowledge.getContent();
        String contentPath = getFilePath(content);
        yunDiseaseKnowledge.setPath(contentPath);
        yunDiseaseKnowledge.setModifyDate(new Timestamp(new Date().getTime()));
        YunDiseaseKnowledge merge = baseFacade.merge(yunDiseaseKnowledge);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据疾病知识信息id删除疾病知识
     * @param id 疾病知识id
     * @return
     */
    @POST
    @Transactional
    @Path("delete")
    public Response delYunDiseaseKnowledge(@QueryParam("id") String id){
        List<String> ids = new ArrayList<String>();
        ids.add(id);
        baseFacade.removeByStringIds(YunDiseaseKnowledge.class,ids);
        return Response.status(Response.Status.OK).entity(ids).build();
    }

    public  String getFilePath(String content){
        String path = "";
        if(content!=null && !"".equals(content)){
            List<String> filePaths = getImageSrc(content);
            path = filePaths.isEmpty()?"":filePaths.get(0);
        }
        return path;
    }
    public  List<String> getImageSrc(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
            imageSrcList.add(src);

        }
        return imageSrcList;
    }
}
