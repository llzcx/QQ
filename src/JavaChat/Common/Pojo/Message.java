package JavaChat.Common.Pojo;

import JavaChat.Common.Transfer.FileMessageState;

import java.io.File;
import java.io.Serializable;

/**
 * 该类为用户消息实体类
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer Id;
    private String Send_User_Id;
    private String Receive_User_Id;
    private String Content;
    private String Send_Time;
    private String MesType;//消息类型
    private String ChatTarget;
    private byte[] bytes;
    private File file;//这个用来存file名字的,,其实可以用String
    private String MsgType;
    private String FileName;
    //可以考虑加密
    private String FilePathInServer;
    FileMessageState FileMessageState;

    public Message(){}

    public Message(String send_User_Id, String receive_User_Id) {
        Send_User_Id = send_User_Id;
        Receive_User_Id = receive_User_Id;
    }

    public Message(String send_User_Id, String receive_User_Id, String content, String send_Time) {
        Send_User_Id = send_User_Id;
        Receive_User_Id = receive_User_Id;
        Content = content;
        Send_Time = send_Time;
    }

    public String getChatTarget() {
        return ChatTarget;
    }

    public void setChatTarget(String chatTarget) {
        ChatTarget = chatTarget;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getSend_User_Id() {
        return Send_User_Id;
    }

    public void setSend_User_Id(String send_User_Id) {
        Send_User_Id = send_User_Id;
    }

    public String getReceive_User_Id() {
        return Receive_User_Id;
    }

    public void setReceive_User_Id(String receive_User_Id) {
        Receive_User_Id = receive_User_Id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSend_Time() {
        return Send_Time;
    }

    public void setSend_Time(String send_Time) {
        Send_Time = send_Time;
    }

    public String getMesType() {
        return MesType;
    }

    public void setMesType(String mesType) {
        MesType = mesType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFilePathInServer() {
        return FilePathInServer;
    }

    public void setFilePathInServer(String filePathInServer) {
        FilePathInServer = filePathInServer;
    }

    public FileMessageState getFileMessageState() {
        return FileMessageState;
    }

    public void setFileMessageState(FileMessageState fileMessageState) {
        this.FileMessageState = fileMessageState;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
