package JavaChat.Common.Pojo;

import JavaChat.Common.Transfer.GroupApplicationState;

import java.io.Serializable;

public class GroupApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    private int Id;
    private String GroupApplicationSender;
    private String GroupApplicationGroupName;
    private GroupApplicationState GroupApplicationState;
    private String Time;
    private String GroupNumber;
    private String Processor;
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getGroupApplicationSender() {
        return GroupApplicationSender;
    }

    public void setGroupApplicationSender(String groupApplicationSender) {
        GroupApplicationSender = groupApplicationSender;
    }

    public String getGroupApplicationGroupName() {
        return GroupApplicationGroupName;
    }

    public void setGroupApplicationGroupName(String groupApplicationGroupName) {
        GroupApplicationGroupName = groupApplicationGroupName;
    }

    public JavaChat.Common.Transfer.GroupApplicationState getGroupApplicationState() {
        return GroupApplicationState;
    }

    public void setGroupApplicationState(JavaChat.Common.Transfer.GroupApplicationState groupApplicationState) {
        GroupApplicationState = groupApplicationState;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber = groupNumber;
    }

    public String getProcessor() {
        return Processor;
    }

    public void setProcessor(String processor) {
        Processor = processor;
    }
}
