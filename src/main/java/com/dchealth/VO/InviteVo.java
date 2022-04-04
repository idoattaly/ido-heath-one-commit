package com.dchealth.VO;

import java.util.List;

public class InviteVo {
    private String groupId;
    private List<String> userIds;

    public InviteVo(String groupId, List<String> userIds) {
        this.groupId = groupId;
        this.userIds = userIds;
    }

    public InviteVo() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
