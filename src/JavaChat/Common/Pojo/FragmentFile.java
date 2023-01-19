package JavaChat.Common.Pojo;

import java.io.Serializable;

public class FragmentFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] bytes;
    private int current;
    private int total;
    private int lastBytesLength;
    private String name;
    private String Time;
    private String receiver;
    private String sender;
    private int Id;
    public FragmentFile(){

    }

    public FragmentFile(byte[] bytes, int current, int total, int lastBytesLength,String name,String time,String receiver) {
        this.bytes = bytes;
        this.current = current;
        this.total = total;
        this.lastBytesLength = lastBytesLength;
        this.name = name;
        this.Time = time;
        this.receiver = receiver;
    }

    public FragmentFile(byte[] bytes, int current, int total, int lastBytesLength,String name,String time,String Sender,String receiver) {
        this.bytes = bytes;
        this.current = current;
        this.total = total;
        this.lastBytesLength = lastBytesLength;
        this.name = name;
        this.Time = time;
        this.receiver = receiver;
        this.sender = Sender;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLastBytesLength() {
        return lastBytesLength;
    }

    public void setLastBytesLength(int lastBytesLength) {
        this.lastBytesLength = lastBytesLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
