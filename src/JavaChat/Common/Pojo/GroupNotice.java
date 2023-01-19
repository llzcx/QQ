package JavaChat.Common.Pojo;

import JavaChat.Common.Transfer.GroupNoticeType;

import java.io.Serializable;

public class GroupNotice implements Serializable {
    private static final long serialVersionUID = 1L;
    private int Id;
    private String ActiveUser;
    private String PassiveUser;
    private String groupNumber;
    private String Time;
     private GroupNoticeType groupNoticeType;
    public GroupNotice(){

    }

    public String getActiveUser() {
        return ActiveUser;
    }

    public void setActiveUser(String activeUser) {
        ActiveUser = activeUser;
    }

    public void setPassiveUser(String passiveUser) {
        PassiveUser = passiveUser;
    }

    public void setgroupNumber(String group) {
        this.groupNumber = group;
    }

    public String getPassiveUser() {
        return PassiveUser;
    }

    public String getgroupNumber() {
        return groupNumber;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public GroupNoticeType getGroupNoticeType() {
        return groupNoticeType;
    }

    public void setGroupNoticeType(GroupNoticeType groupNoticeType) {
        this.groupNoticeType = groupNoticeType;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
