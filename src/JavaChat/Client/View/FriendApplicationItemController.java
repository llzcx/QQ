package JavaChat.Client.View;


import JavaChat.Client.Service.FriendApplicationService;
import JavaChat.Common.Pojo.FriendApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class FriendApplicationItemController {
    @FXML
    private ImageView HeadPortrait;
    @FXML
    private Button Agree_Button;
    @FXML
    private Button Reject_Button;
    @FXML
    private Label State_Label;
    @FXML
    private Label AccountNumber_Label;
    @FXML
    private Rectangle Cover_Rectangle;


    private FriendApplication friendApplication;

    public void Agree_ButtonColorEnter(){
        Agree_Button.setStyle(
                "-fx-background-color: #63eab4;"
        );
    }
    public void Agree_ButtonColorExited(){
        Agree_Button.setStyle(
                "-fx-background-color: #37ff00;"
        );
    }
    public void Reject_ButtonColorEnter(){
        Reject_Button.setStyle(
                "-fx-background-color: #63eab4;"
        );
    }
    public void Reject_ButtonColorExited(){
        Reject_Button.setStyle(
                "-fx-background-color: #ec521e;"
        );
    }
   public void AgreeFriendApplication(){
       System.out.println("同意");
       FriendApplicationService.AgreeFriendApplication(friendApplication.getFriendApplicationSender(),friendApplication.getFriendApplicationReceiver());
       Agree_Button.setDisable(true);
       Reject_Button.setDisable(true);
   }
   public void RejectFriendApplication(){
       FriendApplicationService.RejectFriendApplication(friendApplication.getFriendApplicationSender(),friendApplication.getFriendApplicationReceiver());
       System.out.println("拒绝");
       Agree_Button.setDisable(true);
       Reject_Button.setDisable(true);
   }
    public Image getHeadPortrait() {
        return HeadPortrait.getImage();
    }

    public void setHeadPortrait(Image headPortrait) {
        HeadPortrait.setImage(headPortrait);
    }

    public String getState() {
        return State_Label.getText();
    }

    public void setState(String state) {
        State_Label.setText(state);
    }

    public String getAccountNumber_Label() {
        return AccountNumber_Label.getText();
    }

    public void setAccountNumber_Label(String name) {
        AccountNumber_Label.setText(name);
    }

    public FriendApplication getFriendApplication() {
        return friendApplication;
    }

    public void setFriendApplication(FriendApplication friendApplication) {
        this.friendApplication = friendApplication;
    }

    public void SetCoverRectangleVisibility(boolean flag){
        Cover_Rectangle.setVisible(flag);
    }


}
