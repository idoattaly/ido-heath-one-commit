package com.dchealth.VO;

import com.dchealth.entity.rare.YunDiseaseList;
import com.dchealth.entity.common.YunUsers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */
public class YunUserVO implements Serializable{

    private YunUsers yunUsers ;
    private List<YunDiseaseList> yunUserDisease = new ArrayList<>() ;
    private List<YunDiseaseList> yunUserDiseaseManager=new ArrayList<>() ;

    public YunUserVO(YunUsers yunUsers, List<YunDiseaseList> yunUserDisease, List<YunDiseaseList> yunUserDiseaseManager) {
        this.yunUsers = yunUsers;
        this.yunUserDisease = yunUserDisease;
        this.yunUserDiseaseManager = yunUserDiseaseManager;
    }

    public YunUserVO() {
    }

    public YunUsers getYunUsers() {
        return yunUsers;
    }

    public void setYunUsers(YunUsers yunUsers) {
        this.yunUsers = yunUsers;
    }

    public List<YunDiseaseList> getYunUserDisease() {
        return yunUserDisease;
    }

    public void setYunUserDisease(List<YunDiseaseList> yunUserDisease) {
        this.yunUserDisease = yunUserDisease;
    }

    public List<YunDiseaseList> getYunUserDiseaseManager() {
        return yunUserDiseaseManager;
    }

    public void setYunUserDiseaseManager(List<YunDiseaseList> yunUserDiseaseManager) {
        this.yunUserDiseaseManager = yunUserDiseaseManager;
    }
}


