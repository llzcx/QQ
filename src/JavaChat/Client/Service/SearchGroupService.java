package JavaChat.Client.Service;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.TransferUtil;

public class SearchGroupService {
    /**
     * 根据用户输入的群名字或者群号搜索用户请求
     * @param KeyWord
     */
    public static void SearchGroupFromKey(String KeyWord){
        Group group = new Group();
        group.setGroupNumber(KeyWord);
        group.setGroupName(KeyWord);
        try {
            TransferUtil.ClientInsideTransfer(group, ClientRequest.SEARCH_GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
