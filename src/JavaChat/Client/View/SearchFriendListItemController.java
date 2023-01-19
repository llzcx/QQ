package JavaChat.Client.View;

import JavaChat.Client.Service.FriendApplicationService;
import JavaChat.Client.Service.FriendListService;
import JavaChat.Client.Service.GroupApplicationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchFriendListItemController {
    @FXML
    private ImageView HeadPortrait;
    @FXML
    private Label Name;
    @FXML
    private Label AccountNumber;
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

    public void setHeadPortrait(Image image) {
        HeadPortrait.setImage(image);
    }

    public String getName() {
        return Name.getText();
    }

    public void setName(String name) {
        Name.setText(name);
    }

    public String getAccountNumber() {
        return AccountNumber.getText();
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber.setText(accountNumber);
    }

    public void AddThisFriend(){
        System.out.println("sender"+MainCoreController.getAccountNumber()+" Receiver"+AccountNumber.getText());
        FriendApplicationService.AddFriend(MainCoreController.getAccountNumber(),AccountNumber.getText());
    }
}
