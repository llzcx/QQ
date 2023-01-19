package JavaChat.Common.Pojo;

import java.io.Serializable;

public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer Id;
    private String User_AccountNumber;
    private String Friend_AccountNumber;

    public Friend(){

    }

    public Friend(String user_AccountNumber, String friend_AccountNumber) {
        User_AccountNumber = user_AccountNumber;
        Friend_AccountNumber = friend_AccountNumber;
    }

    public String getUser_AccountNumber() {
        return User_AccountNumber;
    }

    public void setUser_AccountNumber(String user_AccountNumber) {
        User_AccountNumber = user_AccountNumber;
    }

    public String getFriend_AccountNumber() {
        return Friend_AccountNumber;
    }

    public void setFriend_AccountNumber(String friend_AccountNumber) {
        Friend_AccountNumber = friend_AccountNumber;
    }
}
