package com.dchealth.VO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */
public class MessageRecVo {
    private String id;
    private String title;
    private String content;
    //private String userName;
    //private String status;
    private List<RecUserInfo> recUserInfos;
    private Timestamp createDate;

    public MessageRecVo(String id, String title, String content, List<RecUserInfo> recUserInfos,Timestamp createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
//        this.userName = userName;
//        this.status = status;
        this.recUserInfos = recUserInfos;
        this.recUserInfos = recUserInfos;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<RecUserInfo> getRecUserInfos() {
        return recUserInfos;
    }

    public void setRecUserInfos(List<RecUserInfo> recUserInfos) {
        this.recUserInfos = recUserInfos;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
