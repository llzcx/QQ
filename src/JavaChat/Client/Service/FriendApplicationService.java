package JavaChat.Client.Service;

import JavaChat.Common.Pojo.Friend;
import JavaChat.Common.Pojo.FriendApplication;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.FriendApplicationState;
import JavaChat.Common.Utils.TransferUtil;

/**
 * 该类为好友处理的业务,比如添加好友删除好友等
 */
public class FriendApplicationService {
    /**
     * 添加好友
     * @param FriendApplicationSender
     * @param FriendApplicationReceiver
     */
    public static void AddFriend(String FriendApplicationSender,String FriendApplicationReceiver){
        FriendApplication friendApplication = new FriendApplication();
        friendApplication.setFriendApplicationSender(FriendApplicationSender);
        friendApplication.setFriendApplicationReceiver(FriendApplicationReceiver);
        friendApplication.setFriendApplicationState(FriendApplicationState.UNTREATED);
        try {
            TransferUtil.ClientInsideTransfer(friendApplication, ClientRequest.ADD_FRIEND_APPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户输入的名字或者账号搜索用户请求
     * @param KeyWord
     */
    public static void SearchUserFromKey(String KeyWord){
        User user = new User();
        user.setName(KeyWord);
        user.setAccountNumber(KeyWord);

        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.SEARCH_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同意好友申请
     * @param FriendApplicationSender
     * @param FriendApplicationReceiver
     */
    public static void AgreeFriendApplication(String FriendApplicationSender,String FriendApplicationReceiver){
        FriendApplication friendApplication = new FriendApplication();
        friendApplication.setFriendApplicationSender(FriendApplicationSender);
        friendApplication.setFriendApplicationReceiver(FriendApplicationReceiver);

        try {
            TransferUtil.ClientInsideTransfer(friendApplication, ClientRequest.AGREE_FRIEND_APPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝好友申请
     * @param FriendApplicationSender
     * @param FriendApplicationReceiver
     */
    public static void RejectFriendApplication(String FriendApplicationSender,String FriendApplicationReceiver){
        FriendApplication friendApplication = new FriendApplication();
        friendApplication.setFriendApplicationSender(FriendApplicationSender);
        friendApplication.setFriendApplicationReceiver(FriendApplicationReceiver);

        try {
            TransferUtil.ClientInsideTransfer(friendApplication, ClientRequest.REJECT_FRIEND_APPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉取好友申请列表
     * @param AccountNumber
     */
    public static void PullFriendApplicationList(String AccountNumber){
        User user = new User();
        user.setAccountNumber(AccountNumber);
        try {
            TransferUtil.ClientInsideTransfer(user,ClientRequest.PULL_ADD_FRIEND_APPLICATION_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除好友
     * @param User_AccountNumber
     * @param Friend_AccountNumber
     */
    public static void DeleteFriend(String User_AccountNumber,String Friend_AccountNumber){
        Friend friend = new Friend();
        friend.setUser_AccountNumber(User_AccountNumber);
        friend.setFriend_AccountNumber(Friend_AccountNumber);
        try {
            TransferUtil.ClientInsideTransfer(friend,ClientRequest.DELETE_FRIEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
