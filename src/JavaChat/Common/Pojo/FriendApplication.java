package JavaChat.Common.Pojo;

import JavaChat.Common.Transfer.FriendApplicationState;
import JavaChat.Common.Transfer.StateType;

import java.io.Serializable;

public class FriendApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    private String FriendApplicationSender;
    private String FriendApplicationReceiver;
    private String Time;
    private StateType FriendApplicationState;
    public FriendApplication(){}

    public FriendApplication(String friendApplicationSender, String friendApplicationReceiver) {
        FriendApplicationSender = friendApplicationSender;
        FriendApplicationReceiver = friendApplicationReceiver;
    }

    public String getFriendApplicationSender() {
        return FriendApplicationSender;
    }

    public void setFriendApplicationSender(String friendApplicationSender) {
        FriendApplicationSender = friendApplicationSender;
    }

    public String getFriendApplicationReceiver() {
        return FriendApplicationReceiver;
    }

    public void setFriendApplicationReceiver(String friendApplicationReceiver) {
        FriendApplicationReceiver = friendApplicationReceiver;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public StateType getFriendApplicationState() {
        return FriendApplicationState;
    }

    public void setFriendApplicationState(FriendApplicationState friendApplicationState) {
        this.FriendApplicationState = friendApplicationState;
    }

    public void setFriendApplicationState(String friendApplicationState) {
        if("0".equals(friendApplicationState)){
            FriendApplicationState = JavaChat.Common.Transfer.FriendApplicationState.UNTREATED;
        }else if("1".equals(friendApplicationState)){
            FriendApplicationState = JavaChat.Common.Transfer.FriendApplicationState.AGREE;
        }else if("2".equals(friendApplicationState)){
            FriendApplicationState = JavaChat.Common.Transfer.FriendApplicationState.REJECT;
        }
    }
}
