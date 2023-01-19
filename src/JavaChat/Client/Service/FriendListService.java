package JavaChat.Client.Service;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.TransferUtil;

public class FriendListService {
    /**
     * 该类为拉取好友列表业务操作,客户端内部消息传输
     * @param AccountNumber 需要拉取的账号
     */
    public static void PullFriendList(String AccountNumber){
        User user = new User();
        user.setAccountNumber(AccountNumber);
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.PULL_FRIEND_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
