package JavaChat.Common.Pojo;

import JavaChat.Common.Transfer.IdentityType;

import java.io.Serializable;

public class GroupMember implements Serializable {
    private static final long serialVersionUID = 1L;
    private int Id;
    private String GroupName;
    private String MemberAccountNumber;
    private IdentityType MemberIdentity;
    private String Link;
    private String EnterTime;
    private String GroupNumber;
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getMemberAccountNumber() {
        return MemberAccountNumber;
    }

    public void setMemberAccountNumber(String memberAccountNumber) {
        MemberAccountNumber = memberAccountNumber;
    }

    public IdentityType getMemberIdentity() {
        return MemberIdentity;
    }

    public void setMemberIdentity(IdentityType memberIdentity) {
        MemberIdentity = memberIdentity;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getEnterTime() {
        return EnterTime;
    }

    public void setEnterTime(String enterTime) {
        EnterTime = enterTime;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber = groupNumber;
    }
}
