package JavaChat.Client.View;

import JavaChat.Client.Service.FriendApplicationService;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.ClientFileAddress;
import JavaChat.Common.Utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class SearchFriendController {
    @FXML
    private Button Search_Button;
    @FXML
    private ListView SearchFriendList_ListView;
    @FXML
    private TextField AccountNumber_TextField;
    @FXML
    private Button Close_Button;
    public static ObservableList<User> SearchFriend_ObservableList = FXCollections.observableArrayList();
    public static Vector<User> SearchFriend_Vector = new Vector<>();
    @FXML
    private Label AddFriendErrorTip_Label;
    @FXML
    void initialize() {
        SearchFriend_Vector.clear();
        SearchFriendList_ListView.setItems(SearchFriend_ObservableList);
        SearchFriendList_ListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<User>(){
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            //setStyle("-fx-background-color:rgba(0,0,0,0)");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("fxml/SearchFriendListItem.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            SearchFriendListItemController searchFriendListItemController = fxmlLoader.getController();
//                            hBox.setPadding(new Insets(20,0,0,0));
//                            hBox.setPrefHeight();
                            searchFriendListItemController.setAccountNumber(item.getAccountNumber());
                            searchFriendListItemController.setName(item.getName());
                            if (item.getHeadPortrait_byte()!=null) {
                                String path = FileUtils.saveFile(item.getHeadPortrait_byte(), ClientFileAddress.GroupHeadPortrait,item.getHeadPortraitFile().getName());
                                File file = new File(path);
                                searchFriendListItemController.setHeadPortrait(FileUtils.File_Image(file));
                            }
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
    }

    /**
     * 查询点击事件
     */
    public void Query(){
        SearchFriend_Vector.clear();
        SearchFriend_ObservableList.clear();
        if(AccountNumber_TextField.getText().equals("")){
            System.out.println("请输入查询的关键字");
            return;
        }
        FriendApplicationService.SearchUserFromKey(AccountNumber_TextField.getText());
//        try {
//            Thread.currentThread().sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        SearchFriend_ObservableList.addAll(SearchFriend_Vector);
    }

    public void Closely(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        SearchFriendView.loginStage = null;
    }
}
