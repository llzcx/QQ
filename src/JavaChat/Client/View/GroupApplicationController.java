package JavaChat.Client.View;

import JavaChat.Client.Service.FriendApplicationService;
import JavaChat.Client.Service.GroupApplicationService;
import JavaChat.Common.Pojo.FriendApplication;
import JavaChat.Common.Pojo.GroupApplication;
import JavaChat.Common.Transfer.FriendApplicationState;
import JavaChat.Common.Transfer.GroupApplicationState;
import JavaChat.Common.Utils.FileUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Vector;

public class GroupApplicationController {
    @FXML
    private Button Close_Button;
    @FXML
    private ListView<GroupApplication> GroupApplication_ListView;
    @FXML
    private Label ErrorTip_Label;


    public static ObservableList<GroupApplication> GroupApplication_ObservableList = FXCollections.observableArrayList();

    public void CloseGroupApplication(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        GroupApplicationView.loginStage = null;
    }

    public void initialize(){
        GroupApplication_ListView.setFixedCellSize(129);
        GroupApplication_ListView.setItems(GroupApplication_ObservableList);
        GroupApplication_ListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GroupApplication>() {
            @Override
            public void changed(ObservableValue<? extends GroupApplication> observable, GroupApplication oldValue, GroupApplication newValue) {
            }
        });
        GroupApplication_ListView.setCellFactory(new Callback<ListView<GroupApplication>, ListCell<GroupApplication>>() {
            @Override
            public ListCell<GroupApplication> call(ListView<GroupApplication> param) {
                ListCell listCell = new ListCell<GroupApplication>(){
                    @Override
                    protected void updateItem(GroupApplication item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            //setStyle("-fx-background-color:rgba(0,0,0,0)");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("fxml/GroupApplicationItem.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            GroupApplicationItemController groupApplicationItemController = fxmlLoader.getController();
                            String UserAccountNumber = MainCoreController.getAccountNumber();
                            String  Result = "";
                            groupApplicationItemController.setGroupApplication(item);
                            groupApplicationItemController.setSender_Label("申请人:"+item.getGroupApplicationSender());
                            groupApplicationItemController.setGroupNumber_Label("群号:"+item.getGroupNumber());
                            //如果这个群申请的发出者是自己
                            if(UserAccountNumber.equals(item.getGroupApplicationSender())){
                                groupApplicationItemController.SetCoverRectangleVisibility(false);
                                switch(item.getGroupApplicationState()){
                                    case AGREE:
                                        groupApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "群申请已经被"+item.getProcessor()+"同意了";
                                        break;
                                    case UNTREATED:
                                        groupApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "群申请群主和管理还未处理"+item.getGroupApplicationSender()+"的群申请";
                                        break;
                                    case REJECT:
                                        groupApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "群申请被"+item.getProcessor()+"拒绝";
                                        break;
                                }
                            }else{
                                switch((GroupApplicationState)item.getGroupApplicationState()){
                                    case AGREE:
                                        groupApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = item.getProcessor()+"已经同意"+item.getGroupApplicationSender()+"的群申请";
                                        break;
                                    case UNTREATED:
                                        groupApplicationItemController.SetCoverRectangleVisibility(false);
                                        Result = "等待群主或者管理员处理"+item.getGroupApplicationSender()+"的群申请";
                                        break;
                                    case REJECT:
                                        groupApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = item.getProcessor()+"已拒绝"+item.getGroupApplicationSender()+"的群申请";
                                        break;
                                }
                            }
                            groupApplicationItemController.setState(Result);

                            String GroupNumber = item.getGroupNumber();
                            groupApplicationItemController.setHeadPortrait(FileUtils.File_Image(MainCoreController.GroupHeadPortrait.get(GroupNumber)));
                            this.setGraphic(hBox);
                        }
                        else
                        {
                            this.setGraphic(null);
                        }
                    }
                };
                return listCell;
            }
        });
        GroupApplication_ObservableList.clear();
        GroupApplicationService.PullGroupApplicationList(MainCoreController.getAccountNumber());

    }
}
