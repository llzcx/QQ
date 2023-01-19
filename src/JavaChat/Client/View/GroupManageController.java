package JavaChat.Client.View;

import JavaChat.Client.Service.GroupManageService;
import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupMember;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.GroupIdentity;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.JavaFxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Handler;

public class GroupManageController {
    @FXML
    private ListView GroupMemberLIst_ListView;
    public static ObservableList<GroupMember> GroupMemberLIst_ObservableList;
    @FXML
    private TextField GroupName_TextField;
    private boolean NameKey;
    @FXML
    private TextArea GroupAnnouncement_TextArea;
    @FXML
    private Button Return_Button;
    @FXML
    private Label Numbers_Label;
    @FXML
    private Button Save_Button;
    @FXML
    private Button Alter_Button;
    @FXML
    private Label ErrorTip_Label;
    @FXML
    private Label GroupNumber_Label;
    @FXML
    private ImageView HeadPortrait_ImageView;
    @FXML
    private Button AlterHead_Button;
    private Boolean flag1 = false;
    public void SaveEnterColor(){
        Save_Button.setStyle(
                "-fx-background-color: #57ffec"
        );
    }

    public void SaveExitColor(){
        Save_Button.setStyle(
                "-fx-background-color: #1cb7f1"
        );
    }
    public void AlterEnterColor(){
        Alter_Button.setStyle(
                "-fx-background-color: #57ffec"
        );
    }
    public void AlterExitColor(){
        Alter_Button.setStyle(
                "-fx-background-color: #1cb7f1"
        );
    }
    public void Alter(){
        if(!flag1){
            GroupName_TextField.setEditable(true);
            GroupAnnouncement_TextArea.setEditable(true);
            AlterHead_Button.setVisible(true);
            Save_Button.setVisible(true);
            Alter_Button.setText("取消修改");
            flag1 = true;
        }else{
            GroupName_TextField.setEditable(false);
            GroupAnnouncement_TextArea.setEditable(false);
            Save_Button.setVisible(false);
            AlterHead_Button.setVisible(false);
            Alter_Button.setText("修改资料");
            flag1 = false;
        }
    }
    public void Save(){
        if("".equals(GroupName_TextField.getText())){
            JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip"),"群名字还没有设置");
            return;
        }
        Group group = new Group();
        group.setGroupNumber(GroupManageController.group.getGroupNumber());
        group.setGroupAnnouncement(GroupAnnouncement_TextArea.getText());
        group.setGroupName(GroupName_TextField.getText());
        if(selectedFile!=null){
            try {
                group.setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(selectedFile));
                group.setGroupHeadPortraitFile(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GroupManageService.AlterGroupInformation(group);
        GroupName_TextField.setEditable(false);
        GroupAnnouncement_TextArea.setEditable(false);
        Save_Button.setVisible(false);
        Alter_Button.setText("修改资料");
        flag1 = false;
        AlterHead_Button.setVisible(false);
    }
    public void Return(){
        Stage stage =(Stage) Return_Button.getScene().getWindow();
        stage.close();
        GroupManageView.loginStage = null;
    }
    File selectedFile = null;
    public void AlterHead(){
        FileChooser fileChooser = new FileChooser();
        //selectedFile返回一个File对象
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile==null) return;
        if(!FileUtils.CheckFileIs_png_jpeg(selectedFile)){
            JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"请选择jpeg文件或者png文件");
            return;
        }
        System.out.println(selectedFile);
        if(selectedFile==null){
            return; }
        Image image = null;
        image = FileUtils.File_Image(selectedFile);
        System.out.println(image);
        HeadPortrait_ImageView.setImage(image);
    }

    public static GroupMember GroupMember_Myself = new GroupMember();
    public static GroupMember GroupMember_LEADER = new GroupMember();
    public static Vector<GroupMember> GroupMember_ADMINISTRATORS = new Vector<>();
    public static Vector<GroupMember> GroupMember_ORDINARY = new Vector<>();
    public static HashMap<String, User> UserHp = new HashMap<>();

    public static Group group = new Group();
    @FXML
    public void initialize(){
        //错误信息
        ErrorTip_Label.setText("");
        //初始化不可编辑
        GroupName_TextField.setEditable(false);
        GroupAnnouncement_TextArea.setEditable(false);
        AlterHead_Button.setVisible(false);
        AlterExitColor();
        SaveExitColor();
        flag1 = false;
        Save_Button.setVisible(false);
        GroupMemberLIst_ObservableList = FXCollections.observableArrayList();
        GroupMemberLIst_ListView.setItems(GroupMemberLIst_ObservableList);
        GroupMemberLIst_ListView.setCellFactory(new Callback<ListView<GroupMember>, ListCell<GroupMember>>() {
            @Override
            public ListCell<GroupMember> call(ListView<GroupMember> param) {
                ListCell<GroupMember> listCell = new ListCell<GroupMember>(){
                    @Override
                    protected void updateItem(GroupMember item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            setStyle("-fx-background-color:TRANSPARENT");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("Fxml/GroupMemberItem.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            GroupMemberItemController groupMemberItemController = fxmlLoader.getController();
                            groupMemberItemController.setGroupIdentity_Label(item.getMemberIdentity().toChinese());
                            if(item.getMemberAccountNumber().equals(MainCoreController.getAccountNumber())){
                                groupMemberItemController.setAccountNumber_Label(item.getMemberAccountNumber()+"                   我");
                            }else{
                                groupMemberItemController.setAccountNumber_Label(item.getMemberAccountNumber());
                            }
                            groupMemberItemController.setHead_ImageView(FileUtils.File_Image(MainCoreController.FriendHeadPortrait.get(item.getMemberAccountNumber())));
                            groupMemberItemController.setName_Label(UserHp.get(item.getMemberAccountNumber()).getName());
                            if(GroupMember_Myself.getMemberIdentity()==GroupIdentity.GROUP_LEADER){
                                if(item.getMemberIdentity()==GroupIdentity.GROUP_LEADER){
                                    groupMemberItemController.BanSetADMINISTRATORSButton();
                                    groupMemberItemController.getSetADMINISTRATORS_Button().setVisible(false);
                                    groupMemberItemController.getMoveOutGroup_Button().setText("解散群聊");
                                }
                                if(item.getMemberIdentity()==GroupIdentity.ADMINISTRATORS){
                                    groupMemberItemController.getSetADMINISTRATORS_Button().setText("取消管理员");
                                }
                            }else if(GroupMember_Myself.getMemberIdentity()==GroupIdentity.ADMINISTRATORS){
                                groupMemberItemController.getSetADMINISTRATORS_Button().setVisible(false);
                                groupMemberItemController.getMoveOutGroup_Button().setVisible(false);
                                if(item.getMemberIdentity()==GroupIdentity.ADMINISTRATORS){
                                    if(item.getMemberAccountNumber().equals(MainCoreController.getAccountNumber())){
                                        groupMemberItemController.getMoveOutGroup_Button().setVisible(true);
                                        groupMemberItemController.getMoveOutGroup_Button().setText("退出群聊");
                                    }
                                }else if(item.getMemberIdentity()==GroupIdentity.GROUP_LEADER){
                                    groupMemberItemController.getMoveOutGroup_Button().setVisible(false);
                                }
                            }else if(GroupMember_Myself.getMemberIdentity()==GroupIdentity.ORDINARY_GROUP_MEMBERS){
                                JavaFxUtils.HideOrShowButton(GroupManageView.$("Alter_Button"),false);
                                groupMemberItemController.getMoveOutGroup_Button().setVisible(false);
                                if(item.getMemberIdentity()==GroupIdentity.ORDINARY_GROUP_MEMBERS){
                                    groupMemberItemController.getMoveOutGroup_Button().setVisible(true);
                                    groupMemberItemController.getMoveOutGroup_Button().setText("退出群聊");
                                }
                                    groupMemberItemController.BanSetADMINISTRATORSButton();
                                    groupMemberItemController.getSetADMINISTRATORS_Button().setVisible(false);
                            }
                            //设置颜色
                            if(item.getMemberIdentity()==GroupIdentity.GROUP_LEADER){
                                groupMemberItemController.SetLEADERColor();
                            }else if(item.getMemberIdentity()==GroupIdentity.ADMINISTRATORS){
                                groupMemberItemController.SetADMINISTRATORColor();
                            }else if(item.getMemberIdentity()==GroupIdentity.ORDINARY_GROUP_MEMBERS){
                                groupMemberItemController.SetODINARYColor();
                            }
                            this.setGraphic(hBox);
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteItem = new MenuItem("查看个人资料");
                deleteItem.setOnAction(event -> {
//                    FriendApplicationService.DeleteFriend(AccountNumber,ChatTarget);
//                    FriendList_ListView.getItems().remove(listCell.getItem());
                });
                contextMenu.getItems().addAll(deleteItem);
                listCell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        listCell.setContextMenu(null);
                    } else {
                        listCell.setContextMenu(contextMenu);
                    }
                });
                return listCell;
            }
        });
        GroupManageService.getGroupMangeInformation(MainCoreController.GroupChatTarget);
    }

}
