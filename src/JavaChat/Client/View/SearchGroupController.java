package JavaChat.Client.View;

import JavaChat.Client.Service.SearchGroupService;
import JavaChat.Common.Pojo.Group;
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

public class SearchGroupController {
    @FXML
    private Button Search_Button;
    @FXML
    private Label SearchErrorTip_Label;
    @FXML
    private ListView SearchGroupList_ListView;
    @FXML
    private TextField GroupNumber_TextField;
    @FXML
    private Button Close_Button;
    public ObservableList<Group> SearchGroup_ObservableList = FXCollections.observableArrayList();
    public static Vector<Group> SearchGroup_Vector = new Vector<>();

    public void initialize(){
        SearchGroupList_ListView.setEditable(false);
        SearchGroup_Vector.clear();
        SearchGroupList_ListView.setItems(SearchGroup_ObservableList);
        SearchGroupList_ListView.setCellFactory(new Callback<ListView<Group>, ListCell<Group>>() {
            @Override
            public ListCell<Group> call(ListView<Group> param) {
                return new ListCell<Group>(){
                    @Override
                    protected void updateItem(Group item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            //setStyle("-fx-background-color:rgba(0,0,0,0)");
                            //setStyle("-fx-background-color:#d2cece");
                            HBox hBox=new HBox();
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("fxml/SearchGroupListItem.fxml"));
                            try {
                                hBox.getChildren().add(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("列表加载错误");
                            }
                            SearchGroupListItemController searchGroupListItemController = fxmlLoader.getController();
                            searchGroupListItemController.setGroupNumber(item.getGroupNumber());
                            searchGroupListItemController.setName(item.getGroupName());
                            String path = FileUtils.saveFile(item.getGroupHeadPortraitFileBytes(), ClientFileAddress.GroupHeadPortrait,item.getGroupHeadPortraitFile().getName());
                            File file = new File(path);
                            searchGroupListItemController.setHeadPortrait(FileUtils.File_Image(file));
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
            }
        });
    }
    /**
     * 查询点击事件
     */
    public void Query(){
        SearchGroup_Vector.clear();
        SearchGroup_ObservableList.clear();
        if(GroupNumber_TextField.getText().equals("")){
            SearchErrorTip_Label.setText("请输入查询的关键字");
            return;
        }
        SearchGroupService.SearchGroupFromKey(GroupNumber_TextField.getText());
        try {
            Thread.currentThread().sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SearchGroup_ObservableList.addAll(SearchGroup_Vector);
    }

    public void Closely(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        SearchGroupView.loginStage = null;
    }


}
