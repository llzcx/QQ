package JavaChat.Client.Service;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.TransferUtil;

public class UserInformationService {
    /**
     * 拉取user信息
     * @param AccountNumber
     */
    public static void PullUser(String AccountNumber){
        User user = new User();
        user.setAccountNumber(AccountNumber);
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.PULL_USER_FROM_ACCOUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
