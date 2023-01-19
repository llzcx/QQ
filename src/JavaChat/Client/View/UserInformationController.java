package JavaChat.Client.View;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.ClientFileAddress;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.GetTimeUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;


public class UserInformationController {
    @FXML
    private TextField PersonalSignature;
    @FXML
    private TextField Name;
    @FXML
    private TextField AccountNumber;
    @FXML
    private TextField Birthday;
    @FXML
    private TextField Email;
    @FXML
    private TextField School;
    @FXML
    private Button Close;
    @FXML
    private ImageView Head;
    @FXML
    private Label Age;

    public static User user;

    public UserInformationController(){}
    public UserInformationController(User user){
        UserInformationController.user = user;
    }


    @FXML
    public void initialize(){
        System.out.println(user.getHeadPortrait_byte());
        System.out.println(user.getAccountNumber());
        MainCoreController.FriendHeadPortrait.put(user.getAccountNumber(),FileUtils.ByteToFile(user.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName()));
        Head.setImage(FileUtils.File_Image(MainCoreController.FriendHeadPortrait.get(user.getAccountNumber())));
        Rectangle rectangle = new Rectangle(Head.prefWidth(-1),Head.prefHeight(-1));
        rectangle.setArcWidth(500);
        rectangle.setArcHeight(500);
        Head.setClip(rectangle);
        PersonalSignature.setText(user.getPersonalSignature());
        if(Name==null){
            System.out.println("name");
        }
        if(user==null){
            System.out.println("user");
        }
        Name.setText(user.getName());
        AccountNumber.setText(user.getAccountNumber());
        String gender = user.getGender().equals("男")?"Boy":"Girl";
        String age = null;
        try {
            age = ((Integer)( GetTimeUtils.getAge(user.getBirthday()))).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Age.setText("A "+age+"-year-old "+gender+" From "+user.getSchool());
        School.setText(user.getSchool());
        Email.setText(user.getEmail());

    }


    @FXML
    public void Closely(){
        Stage stage =(Stage) Close.getScene().getWindow();
        stage.close();
    }

    public void OpenStage(){
        UserInformationView userInformationView = new UserInformationView();
        try {
            Platform.runLater(() -> {
                try {
                    userInformationView.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
