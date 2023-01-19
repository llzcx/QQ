package JavaChat.Client.Service;

import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.TransferUtil;

/**
 * 该类处理群列表业务
 */
public class GroupListService {
    public static void PullGroupList(String AccountNumber){
        JavaFxUtils.ClearObservableList(MainCoreController.Group_ObservableList);
        User user = new User();
        user.setAccountNumber(AccountNumber);
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.PULL_GROUP_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
