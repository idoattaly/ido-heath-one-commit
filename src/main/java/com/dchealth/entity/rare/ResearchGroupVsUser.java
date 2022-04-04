package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/10/19.
 */
@Entity
@Table(name = "research_group_vs_user", schema = "emhbase", catalog = "")
public class ResearchGroupVsUser {
    private String id;
    private String groupId;
    private String userId;
    private String learderFlag;
    private String createrFlag;

    @Column(name = "id")
    @Id
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "group_id", nullable = true, length = 64)
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "user_id", nullable = true, length = 64)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "learder_flag", nullable = true, length = 2)
    public String getLearderFlag() {
        return learderFlag;
    }

    public void setLearderFlag(String learderFlag) {
        this.learderFlag = learderFlag;
    }

    @Basic
    @Column(name = "creater_flag", nullable = true, length = 2)
    public String getCreaterFlag() {
        return createrFlag;
    }

    public void setCreaterFlag(String createrFlag) {
        this.createrFlag = createrFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResearchGroupVsUser that = (ResearchGroupVsUser) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (learderFlag != null ? !learderFlag.equals(that.learderFlag) : that.learderFlag != null) return false;
        if (createrFlag != null ? !createrFlag.equals(that.createrFlag) : that.createrFlag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (learderFlag != null ? learderFlag.hashCode() : 0);
        result = 31 * result + (createrFlag != null ? createrFlag.hashCode() : 0);
        return result;
    }
}
