package JavaChat.Common.Pojo;

import JavaChat.Common.Transfer.FileMessageState;

import java.io.File;
import java.io.Serializable;

public class GroupMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private int Id;
    private String GroupName;
    private String SenderAccountNumber;
    private String SendTime;
    private String GroupNumber;
    private String Content;

    public GroupMessage(){}

    /**
     *
     * @param senderAccountNumber
     * @param sendTime
     * @param groupNumber
     * @param content
     */
    public GroupMessage(String senderAccountNumber, String sendTime, String groupNumber, String content) {
        SenderAccountNumber = senderAccountNumber;
        SendTime = sendTime;
        GroupNumber = groupNumber;
        Content = content;
    }

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

    public String getSenderAccountNumber() {
        return SenderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        SenderAccountNumber = senderAccountNumber;
    }


    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber = groupNumber;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}
