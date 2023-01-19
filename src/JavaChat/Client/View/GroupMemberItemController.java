package JavaChat.Client.View;

import JavaChat.Client.Service.GroupApplicationService;
import JavaChat.Client.Service.GroupManageService;
import JavaChat.Common.Pojo.Group;
import JavaChat.Server.ServerMain.ServerApplication;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.scene.paint.Color.TRANSPARENT;


public class GroupMemberItemController {
    @FXML
    private Label Name_Label;
    @FXML
    private Label AccountNumber_Label;
    @FXML
    private Label GroupIdentity_Label;
    @FXML
    private ImageView Head_ImageView;
    @FXML
    private Button SetADMINISTRATORS_Button;
    @FXML
    private Button MoveOutGroup_Button;
    public void Agree_ButtonColorEnter(){
        SetADMINISTRATORS_Button.setStyle(
                "-fx-background-color: #63eab4;"
        );
    }
    public void Agree_ButtonColorExited(){
        SetADMINISTRATORS_Button.setStyle(
                "-fx-background-color: #37ff00;"
        );
    }
    public void Reject_ButtonColorEnter(){
        MoveOutGroup_Button.setStyle(
                "-fx-background-color: #63eab4;"
        );
    }
    public void Reject_ButtonColorExited(){
        MoveOutGroup_Button.setStyle(
                "-fx-background-color: #ec521e;"
        );
    }

    public void BanMoveOutGroupButton(){
        MoveOutGroup_Button.setDisable(true);
    }


    public void BanSetADMINISTRATORSButton(){
        SetADMINISTRATORS_Button.setDisable(true);
    }

    public void SetADMINISTRATORColor(){
        GroupIdentity_Label.setStyle(
                "-fx-background-color: #72d715"
        );
    }

    public void SetLEADERColor(){
        GroupIdentity_Label.setStyle(
                "-fx-background-color: #ef9a0e"
        );
    }
    public void SetODINARYColor(){
        GroupIdentity_Label.setStyle(
                "-fx-background-color: #bab3aa"
        );
    }
    @FXML
    public void initialize(){
        GroupIdentity_Label.setAlignment(Pos.CENTER);
        GroupIdentity_Label.setStyle(
                "-fx-background-radius: 25"
        );
    }

    public void SetADMINISTRATORS(){
        if(SetADMINISTRATORS_Button.getText().equals("取消管理员")){
            GroupManageService.DismissalAdministrators(MainCoreController.getAccountNumber(),AccountNumber_Label.getText(),GroupManageController.group.getGroupNumber());
        }else if(SetADMINISTRATORS_Button.getText().equals("设为管理员")){

            GroupManageService.SetAdministrators(MainCoreController.getAccountNumber(),AccountNumber_Label.getText(),GroupManageController.group.getGroupNumber());
        }else{
            System.out.println("设置错误");
        }
    }


    public void MoveOutGroup(){
        if(MoveOutGroup_Button.getText().equals("解散群聊")){
            Stage dialogStage = new Stage();
            DialogPane dialog = new DialogPane();
            dialog.setHeaderText("");
            dialog.setContentText("确认解散群聊"+GroupManageController.group.getGroupName()+"("+GroupManageController.group.getGroupNumber()+")?");
            dialog.getButtonTypes().add(ButtonType.NO);
            dialog.getButtonTypes().add(ButtonType.OK);
            Button exit = (Button)dialog.lookupButton(ButtonType.OK);
            exit.setText("解散群聊");
            exit.setOnAction(event -> {
                GroupManageService.DissolutionGroup(MainCoreController.getAccountNumber(),GroupManageController.group.getGroupNumber());
                dialogStage.close();
            });
            Button close = (Button)dialog.lookupButton(ButtonType.NO);
            close.setText("取消");
            close.setOnAction(event -> {
                GroupManageService.DissolutionGroup(MainCoreController.getAccountNumber(),GroupManageController.group.getGroupNumber());
                dialogStage.close();
            });
            Scene dialogScene = new Scene(dialog,TRANSPARENT);
            dialogStage.setScene(dialogScene);

            dialogStage.setTitle("解散群聊");
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.show();
        }else if(MoveOutGroup_Button.getText().equals("退出群聊")){
            GroupApplicationService.ExitGroupChat(MainCoreController.getAccountNumber(),MainCoreController.GroupChatTarget);
        }else if(MoveOutGroup_Button.getText().equals("踢出群聊")){
            Stage dialogStage = new Stage();
            DialogPane dialog = new DialogPane();
            dialog.setHeaderText("");
            dialog.setContentText("是否要将"+Name_Label.getText()+"("+AccountNumber_Label.getText()+")移除群聊?");
            dialog.getButtonTypes().add(ButtonType.NO);
            dialog.getButtonTypes().add(ButtonType.OK);
            Button exit = (Button)dialog.lookupButton(ButtonType.OK);
            exit.setText("确认");
            exit.setOnAction(event -> {
                GroupApplicationService.DeleteGroupMember(AccountNumber_Label.getText(),MainCoreController.GroupChatTarget);
                dialogStage.close();
            });
            Button close = (Button)dialog.lookupButton(ButtonType.NO);
            close.setText("取消");
            close.setOnAction(event -> {
                dialogStage.close();
            });
            Scene dialogScene = new Scene(dialog,TRANSPARENT);
            dialogStage.setScene(dialogScene);

            dialogStage.setTitle("移出群成员");
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.show();
        }
    }


    public String getName_Label() {
        return Name_Label.getText();
    }

    public void setName_Label(String name) {
        Name_Label.setText(name);
    }

    public String getAccountNumber_Label() {
        return AccountNumber_Label.getText();
    }

    public void setAccountNumber_Label(String accountNumber) {
        AccountNumber_Label.setText(accountNumber);
    }

    public String getGroupIdentity_Label() {
        return GroupIdentity_Label.getText();
    }

    public void setGroupIdentity_Label(String Identity) {
        GroupIdentity_Label.setText(Identity);
    }

    public Image getHead_ImageView() {
        return Head_ImageView.getImage();
    }

    public void setHead_ImageView(Image head) {
        Head_ImageView.setImage(head);
    }

    public Button getSetADMINISTRATORS_Button() {
        return SetADMINISTRATORS_Button;
    }

    public void setSetADMINISTRATORS_Button(Button setADMINISTRATORS_Button) {
        SetADMINISTRATORS_Button = setADMINISTRATORS_Button;
    }

    public Button getMoveOutGroup_Button() {
        return MoveOutGroup_Button;
    }

    public void setMoveOutGroup_Button(Button moveOutGroup_Button) {
        MoveOutGroup_Button = moveOutGroup_Button;
    }
}
