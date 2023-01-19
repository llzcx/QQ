package JavaChat.Client.Service;

import JavaChat.Client.View.GroupManageController;
import JavaChat.Client.View.GroupManageView;
import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.TransferUtil;
import javafx.stage.Stage;

import java.util.Vector;

/**
 * 管理群
 */
public class GroupManageService {

    public static void getGroupMangeInformation(String GroupNumber){
        JavaFxUtils.ClearObservableList(GroupManageController.GroupMemberLIst_ObservableList);
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        try {
            TransferUtil.ClientInsideTransfer(group, ClientRequest.PULL_MANAGE_GROUP_RESOURCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void AlterGroupInformation(Group group){
        try {
            TransferUtil.ClientInsideTransfer(group, ClientRequest.ALTER_GROUP_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置管理员
     * @param AccountNumberActive
     * @param AccountNumberPassive
     * @param GroupNumber
     */
    public static void SetAdministrators(String AccountNumberActive,String AccountNumberPassive,String GroupNumber){
        User userActive = new User();
        userActive.setAccountNumber(AccountNumberActive);
        User userPassive = new User();
        userPassive.setAccountNumber(AccountNumberPassive);
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        Vector<Object> vector = new Vector<>();
        //这个维克托表明1个人在某个群里面给另外一个人设置管理员
        vector.add(userActive);
        vector.add(userPassive);
        vector.add(group);
        try {
            TransferUtil.ClientInsideTransfer(vector,ClientRequest.SET_GROUP_ADMINISTRATORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 撤销管理员
     * @param AccountNumberActive
     * @param AccountNumberPassive
     * @param GroupNumber
     */
    public static void DismissalAdministrators(String AccountNumberActive,String AccountNumberPassive,String GroupNumber){
        User userActive = new User();
        userActive.setAccountNumber(AccountNumberActive);
        User userPassive = new User();
        userPassive.setAccountNumber(AccountNumberPassive);
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        Vector<Object> vector = new Vector<>();
        vector.add(userActive);
        vector.add(userPassive);
        vector.add(group);
        try {
            TransferUtil.ClientInsideTransfer(vector,ClientRequest.DISMISSAL_GROUP_ADMINISTRATORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 踢人
     * @param AccountNumberActive
     * @param AccountNumberPassive
     * @param GroupNumber
     */
    public static void DeleteGroupMember(String AccountNumberActive,String AccountNumberPassive,String GroupNumber){
        User userActive = new User();
        userActive.setAccountNumber(AccountNumberActive);
        User userPassive = new User();
        userPassive.setAccountNumber(AccountNumberPassive);
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        Vector<Object> vector = new Vector<>();
        vector.add(userActive);
        vector.add(userPassive);
        vector.add(group);
        try {
            TransferUtil.ClientInsideTransfer(vector,ClientRequest.DELETE_GROUP_MEMBER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解散群聊
     * @param AccountNumberActive
     * @param GroupNumber
     */
    public static void DissolutionGroup(String AccountNumberActive,String GroupNumber){
        User userActive = new User();
        userActive.setAccountNumber(AccountNumberActive);
        Group group = new Group();
        group.setGroupNumber(GroupNumber);
        Vector<Object> vector = new Vector<>();
        vector.add(userActive);
        vector.add(group);
        Stage stage =(Stage) GroupManageView.scene.getWindow();
        GroupManageView.loginStage = null;
        stage.close();
        try {
            TransferUtil.ClientInsideTransfer(vector,ClientRequest.DISSOLUTION_GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
