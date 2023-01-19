package JavaChat.Client.View;

import JavaChat.Client.Service.GroupNoticeService;
import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupNotice;
import JavaChat.Common.Transfer.GroupNoticeType;
import JavaChat.Common.Utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class GroupNoticeController {
    @FXML
    private Label SearchErrorTip_Label;
    @FXML
    private ListView GroupNotice_ListView;
    @FXML
    private TextField GroupNumber_TextField;
    @FXML
    private Button Close_Button;
    public static ObservableList<Group> GroupNotice_ObservableList = FXCollections.observableArrayList();



    public void initialize(){
        GroupNotice_ListView.setFixedCellSize(120);
        GroupNotice_ListView.setItems(GroupNotice_ObservableList);
        GroupNotice_ListView.setCellFactory(new Callback<ListView<GroupNotice>, ListCell<GroupNotice>>() {
            @Override
            public ListCell<GroupNotice> call(ListView<GroupNotice> param) {
                return new ListCell<GroupNotice>(){
                    @Override
                    protected void updateItem(GroupNotice item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            //setPrefSize(523,129);
                            setStyle("-fx-background-color:rgba(0,0,0,0)");
                            //setStyle("-fx-background-color:#d2cece");
                            HBox hBox=new HBox();
                            //hBox.setPrefSize(376,129);
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("fxml/GroupNoticeItem.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            GroupNoticeItemController groupNoticeItemController = fxmlLoader.getController();
                            groupNoticeItemController.setGroupNotice(item);
                            groupNoticeItemController.setGroupNumber(item.getGroupNumber());
                            groupNoticeItemController.setActiveHeadPortrait(FileUtils.File_Image(MainCoreController.FriendHeadPortrait.get(item.getActiveUser())));
                            String event = "";
                            if(item.getGroupNoticeType()== GroupNoticeType.XX_ENTER){
                                event = item.getActiveUser()+"进入了本群";
                            }else if(item.getGroupNoticeType()==GroupNoticeType.XX_EXIT){
                                event = item.getActiveUser() + "退出了本群";
                            }else if(item.getGroupNoticeType()==GroupNoticeType.XX_BE_XX_SET_ADMINISTRATOR){
                                event = item.getPassiveUser() + "被" +item.getActiveUser() + "设置为管理员";
                            }else if(item.getGroupNoticeType() == GroupNoticeType.XX_BE_XX_DEMOTION){
                                event = item.getPassiveUser() + "被" + item.getActiveUser() + "撤职管理员";
                            }else if(item.getGroupNoticeType() == GroupNoticeType.XX_GROUP_ADMINISTRATOR_BE_DISSOLUTION){
                                event = item.getPassiveUser() + "被" + item.getActiveUser() + "撤职管理员";
                            }else if(item.getGroupNoticeType() == GroupNoticeType.XX_REMOVE_XX){
                                event = item.getPassiveUser() + "被" + item.getActiveUser() + "踢出本群";
                            }
                            groupNoticeItemController.setEvent(event);
                            this.setGraphic(hBox);
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
            }
        });
        GroupNotice_ObservableList.clear();
        GroupNoticeService.PullGroupNoticeList(MainCoreController.GroupChatTarget);
    }

    public void Closely(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        GroupNoticeView.loginStage = null;
    }


}
