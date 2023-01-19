package JavaChat.Client.View;

import JavaChat.Client.Service.GroupApplicationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchGroupListItemController {
    @FXML
    private ImageView HeadPortrait;
    @FXML
    private Label Name;
    @FXML
    private Label GroupNumber;
    @FXML
    private Button Add_Button;

    public void initialize(){
    }

    public void EnterColor_Add_Button(){
        Add_Button.setStyle(
                "-fx-background-color:  #37e01c"
        );
    }
    public void ExitColor_Add_Button(){
        Add_Button.setStyle(
                "-fx-background-color:  #48bcff"
        );
    }

    public Image getHeadPortrait() {
        return HeadPortrait.getImage();
    }

    public void setHeadPortrait(Image headPortrait) {
        HeadPortrait.setImage(headPortrait);
    }

    public String getName() {
        return Name.getText();
    }

    public void setName(String name) {
        Name.setText(name);
    }

    public String getGroupNumber() {
        return GroupNumber.getText();
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber.setText(groupNumber);
    }

    public void AddThisGroup(){
        GroupApplicationService.EnterGroupChat(MainCoreController.getAccountNumber(),GroupNumber.getText(),Name.getText());
    }
}
