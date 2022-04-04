package com.dchealth.VO;

import com.dchealth.entity.rare.YunDisTemplet;
import com.dchealth.entity.rare.YunDiseaseList;

/**
 *
 * 首页疾病统计信息
 * Created by Administrator on 2017/6/23.
 */
public class DiseasePatInfo {
    private YunDiseaseList yunDiseaseList ;
    private Long patNumber;
    private Long followNumber ;

    public DiseasePatInfo() {
    }

    public DiseasePatInfo(YunDiseaseList yunDiseaseList, Long patNumber, Long followNumber) {
        this.yunDiseaseList = yunDiseaseList;
        this.patNumber = patNumber;
        this.followNumber = followNumber;
    }

    public YunDiseaseList getYunDiseaseList() {
        return yunDiseaseList;
    }

    public void setYunDiseaseList(YunDiseaseList yunDiseaseList) {
        this.yunDiseaseList = yunDiseaseList;
    }

    public Long getPatNumber() {
        return patNumber;
    }

    public void setPatNumber(Long patNumber) {
        this.patNumber = patNumber;
    }

    public Long getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(Long followNumber) {
        this.followNumber = followNumber;
    }
}
