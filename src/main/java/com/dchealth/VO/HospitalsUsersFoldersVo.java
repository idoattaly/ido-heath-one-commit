package com.dchealth.VO;

public class HospitalsUsersFoldersVo {

    private String id;
    private String userId;//用户名
    private String userName;//姓名
    private String name;
    private String dcode;
    private long num;

    public HospitalsUsersFoldersVo(String id, String userId, String userName, String name, String dcode, long num) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.dcode = dcode;
        this.num = num;
    }

    public HospitalsUsersFoldersVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
