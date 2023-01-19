package JavaChat.Client.Service;

import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.*;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.GroupApplicationState;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Utils.GetTimeUtils;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.TransferUtil;

import java.io.File;
import java.util.Vector;

/**
 * 群聊相关业务在此类中
 */
public class GroupApplicationService {

    /**
     * 创建群
     * @param AccountNumber
     * @param GroupName
     */
    public static void CreatGroupChat(String AccountNumber,String GroupName,File file,byte[] bytes){
      User user = new User();
      user.setAccountPassword(AccountNumber);
      Group group = new Group();
      group.setGroupName(GroupName);
      group.setGroupHeadPortraitFile(file);
      group.setGroupHeadPortraitFileBytes(bytes);
      Vector<Object> vector = new Vector<>();
      vector.add(user);
      vector.add(group);
        try {
            TransferUtil.ClientInsideTransfer(vector,ClientRequest.CREAT_GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 某张号要退出群号为GroupNumber的群聊
     * @param AccountNumber
     * @param GroupNumber
     */
    public static void ExitGroupChat(String AccountNumber,String GroupNumber){
        User user = new User();
        user.setAccountPassword(AccountNumber);
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        Vector<Object> vector = new Vector<>();
        vector.add(user);
        vector.add(group);
        try {
            TransferUtil.ClientInsideTransfer(vector, ClientRequest.EXIT_GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加入群聊
     * @param AccountNumber
     * @param GroupNumber
     */
    public static void EnterGroupChat(String AccountNumber,String GroupNumber,String GroupName){
      GroupApplication groupApplication = new GroupApplication();
      groupApplication.setGroupApplicationSender(AccountNumber);
      groupApplication.setTime(GetTimeUtils.GetNowTime());
      groupApplication.setGroupNumber(GroupNumber);
      groupApplication.setGroupApplicationGroupName(GroupName);
        try {
            TransferUtil.ClientInsideTransfer(groupApplication, ClientRequest.ENTER_GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除某人在某个群当中
     * @param GroupNumber
     */
    public static void DeleteGroupMember(String PassiveAccountNumber,String GroupNumber){
        User user1 = new User();
        User user2 = new User();
        Group group = new Group();
        user1.setAccountNumber(MainCoreController.getAccountNumber());
        user2.setAccountNumber(PassiveAccountNumber);
        group.setGroupNumber(GroupNumber);
        Vector<Object> vector = new Vector<>();
        vector.add(user1);
        vector.add(user2);
        vector.add(group);
        try {
            TransferUtil.ClientInsideTransfer(vector, ClientRequest.DELETE_GROUP_MEMBER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解散群
     * @param AccountNumber
     * @param GroupNumber
     */
    public static void Dissolution_Group(String AccountNumber,String GroupNumber){
        GroupMember groupMember = new GroupMember();
        groupMember.setMemberAccountNumber(AccountNumber);
        groupMember.setGroupNumber(GroupNumber);
        try {
            TransferUtil.ClientInsideTransfer(groupMember, ClientRequest.DISSOLUTION_GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 某账号要求拉取群列表
     * @param AccountNumber
     */
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


    /**
     * 某账号要拉取群聊申请列表
     * @param AccountNumber
     */
    public static  void PullGroupApplicationList(String AccountNumber){
        User user = new User();
        user.setAccountNumber(AccountNumber);
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.PULL_GROUP_APPLICATION_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *在群里发了一条文本消息
     * @param SenderAccountNumber
     * @param GroupNumber
     */
    public static void SendGroupTextMessage(String SenderAccountNumber,String GroupNumber){
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setSenderAccountNumber(SenderAccountNumber);
        groupMessage.setGroupNumber(GroupNumber);
        try {
            TransferUtil.ClientInsideTransfer(groupMessage, ClientRequest.SEND_GROUP_TEXT_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 拉取群消息
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
     * 同意某人进群
     * @param GroupApplicationSender
     * @param GroupNumber
     */
    public static void AgreeGroupApplication(String GroupApplicationSender, String GroupNumber){
        GroupApplication groupApplication = new GroupApplication();
        groupApplication.setGroupApplicationSender(GroupApplicationSender);
        groupApplication.setGroupNumber(GroupNumber);
        groupApplication.setTime(GetTimeUtils.GetNowTime());
        groupApplication.setProcessor(MainCoreController.getAccountNumber());
        try {
            TransferUtil.ClientInsideTransfer(groupApplication, ClientRequest.AGREE_GROUP_APPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *拒绝某人入群
     * @param GroupApplicationSender
     * @param GroupNumber
     */
    public static void RejectGroupApplication(String GroupApplicationSender,String GroupNumber){
        GroupApplication groupApplication = new GroupApplication();
        groupApplication.setGroupApplicationSender(GroupApplicationSender);
        groupApplication.setGroupNumber(GroupNumber);
        groupApplication.setTime(GetTimeUtils.GetNowTime());
        groupApplication.setProcessor(MainCoreController.getAccountNumber());
        try {
            TransferUtil.ClientInsideTransfer(groupApplication, ClientRequest.REJECT_GROUP_APPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
