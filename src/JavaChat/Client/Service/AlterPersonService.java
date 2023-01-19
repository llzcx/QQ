package JavaChat.Client.Service;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.TransferUtil;

public class AlterPersonService {
    public static void UpdatePersonInformation(User user){
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.ALTER_PERSON_INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
