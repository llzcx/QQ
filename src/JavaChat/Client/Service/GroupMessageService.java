package JavaChat.Client.Service;

import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupMessage;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.GetTimeUtils;
import JavaChat.Common.Utils.TransferUtil;

import java.util.HashMap;

public class GroupMessageService {
    /**
     * 拉取群聊消息
     * @param GroupNumber
     */
    public static void PullGroupMessage(String GroupNumber){
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        try {
            TransferUtil.ClientInsideTransfer(group, ClientRequest.PULL_GROUP_MESSAGE_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送群消息
     * @param SenderAccountNumber
     * @param Content
     * @param GroupNumber
     */
    public static void SendGroupMessageToGroup(String SenderAccountNumber, String Content,String GroupNumber){
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setSenderAccountNumber(SenderAccountNumber);
        groupMessage.setGroupNumber(GroupNumber);
        groupMessage.setContent(Content);
        groupMessage.setSendTime(GetTimeUtils.GetNowTime());
        try {
            TransferUtil.ClientInsideTransfer(groupMessage,ClientRequest.SEND_GROUP_TEXT_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
