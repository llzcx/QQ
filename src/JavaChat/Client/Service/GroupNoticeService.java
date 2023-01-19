package JavaChat.Client.Service;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.TransferUtil;

/**
 * 群通知客户端请求类
 */
public class GroupNoticeService {
    /**
     * 拉取某个群的群通知
     * @param GroupNumber
     */
    public static void PullGroupNoticeList(String GroupNumber){
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        try {
            TransferUtil.ClientInsideTransfer(group, ClientRequest.PULL_GROUP_NOTICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
