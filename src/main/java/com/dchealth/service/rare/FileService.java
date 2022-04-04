package com.dchealth.service.rare;

import com.dchealth.VO.Col;
import com.dchealth.entity.rare.QxUploadFile;
import com.dchealth.facade.common.BaseFacade;
import com.dchealth.util.IDUtils;
import com.dchealth.util.JSONUtil;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/3.
 */
@Produces("application/json")
@Path("file")
@Controller
public class FileService {



    @Autowired
    private BaseFacade baseFacade ;

    @GET
    @Path("get-file-by-id")
    public String getFilePathById(@QueryParam("id") String id) throws Exception {

        QxUploadFile qxUploadFile = baseFacade.get(QxUploadFile.class,id);
        if(qxUploadFile==null){
            throw new Exception("没有找到文件上传记录！");
        }
        Col col = new Col();
        String path = qxUploadFile.getFilePath();
        System.out.println("数据库存储路径："+path);
        col.setValue(path);
        path = JSONUtil.objectToJsonString(col);
        return path;
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("upload")
    @Transactional
    public Response saveFile(@FormDataParam( "file") InputStream uploadedInputStream,
                             @FormDataParam( "file") FormDataContentDisposition fileDetail,@Context HttpServletRequest httpServletRequest) throws Exception {
        String filename =fileDetail.getFileName();
        String path = httpServletRequest.getServletContext().getRealPath("/");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        long time = IDUtils.genItemId() ;
        String exName = filename.substring(filename.lastIndexOf('.'));
        String filePath = File.separator+"upload";
        if(month>=10){
            //path = path+"\\upload"+"\\"+year+month+day+"\\"+hour+"\\"+time+exName;
            path = path+File.separator+"upload"+File.separator+""+year+month+day+File.separator+hour+File.separator+time+exName;
            filePath =filePath+"/"+year+month+day+"/"+hour+"/"+time+exName;
        }else{
            //path = path+"\\upload"+"\\"+year+"0"+month+day+"\\"+hour+"\\"+time+exName;
            path = path+File.separator+"upload"+File.separator+year+"0"+month+day+File.separator+hour+File.separator+time+exName;
            filePath =filePath+"/"+year+"0"+month+day+"/"+hour+"/"+time+exName;
        }

        QxUploadFile qxUploadFile = new QxUploadFile();
        qxUploadFile.setFileName(filename);
        qxUploadFile.setFilePath(filePath);

        File file= new File(path);
        if(!file.exists()){
            File fileParent = file.getParentFile() ;
            if(!fileParent.exists()){
                if(!fileParent.mkdirs()){
                    throw new Exception("创建文件路径失败");
                };
            }
            if(!file.createNewFile()){
                throw new Exception("创建文件失败!");
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int length = 0 ;
        while(-1 !=(length=uploadedInputStream.read(bytes))){
            fileOutputStream.write(bytes);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        QxUploadFile merge = baseFacade.merge(qxUploadFile);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

}
