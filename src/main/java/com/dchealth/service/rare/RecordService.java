package com.dchealth.service.rare;

import com.dchealth.entity.rare.YunReleaseTemplet;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单填写服务
 * Created by Administrator on 2017/6/26.
 */
@Controller
@Produces("application/json")
@Path("record")
public class RecordService {

    @Autowired
    private BaseFacade baseFacade ;

}
