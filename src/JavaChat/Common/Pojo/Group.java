package JavaChat.Common.Pojo;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private int Id;
    private String GroupName;
    private int GroupMemberNum;
    private String GroupNumber;
    private String GroupAnnouncement;
    private File GroupHeadPortraitFile;
    private byte[] GroupHeadPortraitFileBytes;
    private String GroupHeadPortraitFilePath;

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

    public int getGroupMemberNum() {
        return GroupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        GroupMemberNum = groupMemberNum;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber = groupNumber;
    }

    public String getGroupAnnouncement() {
        return GroupAnnouncement;
    }

    public void setGroupAnnouncement(String groupAnnouncement) {
        GroupAnnouncement = groupAnnouncement;
    }

    public File getGroupHeadPortraitFile() {
        return GroupHeadPortraitFile;
    }

    public void setGroupHeadPortraitFile(File groupHeadPortraitFile) {
        GroupHeadPortraitFile = groupHeadPortraitFile;
    }

    public byte[] getGroupHeadPortraitFileBytes() {
        return GroupHeadPortraitFileBytes;
    }

    public void setGroupHeadPortraitFileBytes(byte[] groupHeadPortraitFileBytes) {
        GroupHeadPortraitFileBytes = groupHeadPortraitFileBytes;
    }

    public String getGroupHeadPortraitFilePath() {
        return GroupHeadPortraitFilePath;
    }

    public void setGroupHeadPortraitFilePath(String groupHeadPortraitFilePath) {
        GroupHeadPortraitFilePath = groupHeadPortraitFilePath;
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));

    }

}
