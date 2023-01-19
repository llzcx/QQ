package JavaChat.Client.View;

import JavaChat.Client.Service.FriendApplicationService;
import JavaChat.Client.Service.MessageService;
import JavaChat.Common.Pojo.Friend;
import JavaChat.Common.Pojo.FriendApplication;
import JavaChat.Common.Pojo.Message;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.FriendApplicationState;
import JavaChat.Common.Utils.FileUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Vector;

public class FriendApplicationController {
    @FXML
    private Button Close_Button;
    @FXML
    private ListView<FriendApplication> FriendApplication_ListView;


    public static Vector<FriendApplication> FriendApplication_Vector = new Vector<>();
    public static ObservableList<FriendApplication> FriendApplication_ObservableList = FXCollections.observableArrayList();

    public void CloseFriendApplication(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        FriendApplicationView.loginStage = null;
    }
    public void initialize(){
        FriendApplication_Vector.clear();//防止第二次加载的时候多出来
        FriendApplication_ListView.setFixedCellSize(129);
        FriendApplication_ListView.setItems(FriendApplication_ObservableList);
        FriendApplication_ListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FriendApplication>() {
            @Override
            public void changed(ObservableValue<? extends FriendApplication> observable, FriendApplication oldValue, FriendApplication newValue) {
            }
        });
        FriendApplication_ListView.setCellFactory(new Callback<ListView<FriendApplication>, ListCell<FriendApplication>>() {
            @Override
            public ListCell<FriendApplication> call(ListView<FriendApplication> param) {
                ListCell listCell = new ListCell<FriendApplication>(){
                    @Override
                    protected void updateItem(FriendApplication item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            setStyle("-fx-background-color:rgba(0,0,0,0)");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("fxml/FriendApplicationItem.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            FriendApplicationItemController friendApplicationItemController = fxmlLoader.getController();
                            String UserAccountNumber = MainCoreController.getAccountNumber();
                            String  Result = "";
                            friendApplicationItemController.setFriendApplication(item);
                            //如果这个好友申请的发出者是自己
                            if(UserAccountNumber.equals(item.getFriendApplicationSender())){
                                friendApplicationItemController.setAccountNumber_Label(item.getFriendApplicationReceiver());
                                friendApplicationItemController.SetCoverRectangleVisibility(false);
                                switch((FriendApplicationState)item.getFriendApplicationState()){
                                    case AGREE:
                                        friendApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "好友申请已经被同意了";
                                        break;
                                    case UNTREATED:
                                        friendApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "好友申请对方还未处理";
                                        break;
                                    case REJECT:
                                        friendApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "好友申请被拒绝";
                                        break;
                                }
                            }else{
                                friendApplicationItemController.setAccountNumber_Label(item.getFriendApplicationSender());
                                switch((FriendApplicationState)item.getFriendApplicationState()){
                                    case AGREE:
                                        friendApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "您已同意该用户的好友申请";
                                        break;
                                    case UNTREATED:
                                        friendApplicationItemController.SetCoverRectangleVisibility(false);
                                        Result = "等待您处理该好友申请";
                                        break;
                                    case REJECT:
                                        friendApplicationItemController.SetCoverRectangleVisibility(true);
                                        Result = "您已拒绝该好友申请";
                                        break;
                                }
                            }
                            friendApplicationItemController.setState(Result);
                            //得到对方的Account
                            String acc = MainCoreController.getAccountNumber().equals(item.getFriendApplicationSender())?item.getFriendApplicationReceiver():item.getFriendApplicationSender();
                            friendApplicationItemController.setHeadPortrait(FileUtils.File_Image(MainCoreController.FriendHeadPortrait.get(acc)));
//                            hBox.setPadding(new Insets(20,0,0,0));
//                            hBox.setPrefHeight();
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
        FriendApplication_ObservableList.clear();
        FriendApplicationService.PullFriendApplicationList(MainCoreController.getAccountNumber());
//        try {
//            Thread.currentThread().sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        FriendApplication_ObservableList.addAll(FriendApplication_Vector);

    }



}
