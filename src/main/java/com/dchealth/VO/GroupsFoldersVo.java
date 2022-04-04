package com.dchealth.VO;

public class GroupsFoldersVo {
    private String id;
    private String researchGroupName;
    private long num;

    public GroupsFoldersVo(String id, String researchGroupName, long num) {
        this.id = id;
        this.researchGroupName = researchGroupName;
        this.num = num;
    }

    public GroupsFoldersVo() {
    }

    public String getResearchGroupName() {
        return researchGroupName;
    }

    public void setResearchGroupName(String researchGroupName) {
        this.researchGroupName = researchGroupName;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
