package JavaChat.Client.View;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.RegularExpression;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

import static JavaChat.Client.Service.AlterPersonService.UpdatePersonInformation;

public class AlterPersonController {
    @FXML
    private ImageView HeadPortrait_ImageView;
    private User MyInformation = new User();
    ToggleGroup toggleGroup = new ToggleGroup();//单选组
    @FXML
    private Label AccountNumber_Label;
    @FXML
    private Label Gender_Label;
    @FXML
    private Button BackGround_Button;
    @FXML
    private Button HeadPortrait_Button;
    @FXML
    private TextArea PersonalSignature_TextArea;
    @FXML
    private Label PersonalSignatureErrorTip_Label;
    @FXML
    private TextField Name_TextField;
    @FXML
    private Label NameErrorTip_Label;
    private boolean NameKey;
    @FXML
    private DatePicker Birthday_DatePicker;
    @FXML
    private Label BirthdayErrorTip_Label;
    private boolean BirthdayKey;
    @FXML
    private RadioButton Man_RadioButton;
    @FXML
    private RadioButton Women_RadioButton;
    @FXML
    private TextField School_TextField;
    @FXML
    private Button Close_Button;
    @FXML
    private Button Save_Button;
    @FXML
    private Button Change_Button;
    @FXML
    private Label AlterPersonTip_Label;

    private File selectedFile;
    @FXML
    private String Gender;
    @FXML
    private Label HeadErrorTip_Label;

    String Path = null;

    public void initialize(){
        /*------头像----*/
        HeadPortrait_ImageView.setImage(FileUtils.File_Image(MainCoreController.FriendHeadPortrait.get(MainCoreController.PersonInformation.getAccountNumber())));
        /*------初始化-----*/
        NameKey = false;
        BirthdayKey =false;
        //把MainCore里面的user信息拿过来
        MyInformation = MainCoreController.PersonInformation;


        /*--------------------------------性别设置--------------------------------------*/
        Gender = "男";
        Man_RadioButton.setToggleGroup(toggleGroup);
        Women_RadioButton.setToggleGroup(toggleGroup);

        Man_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    Gender = Man_RadioButton.getText();
                }
            }
        });
        Women_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    Gender = Women_RadioButton.getText();
                }
            }
        });
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton r = (RadioButton)newValue;
            }
        });
        //日期选择框不能输入
        Birthday_DatePicker.setEditable(false);
        //不能选择未来日期
        Birthday_DatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
            }
        });
        if(MyInformation.getAccountNumber()!=null){
            AccountNumber_Label.setText(MyInformation.getAccountNumber());
        }
        //设置原始信息

        if (MyInformation.getPersonalSignature()!=null) {
            PersonalSignature_TextArea.setText(MyInformation.getPersonalSignature());
        }

        if (MyInformation.getName()!=null) {
            Name_TextField.setText(MyInformation.getName());
            NameKey = true;
        }
        if("男".equals(MyInformation.getGender())){
            toggleGroup.selectToggle(Man_RadioButton);
        }else{
            toggleGroup.selectToggle(Women_RadioButton);
        }
        School_TextField.setText(MyInformation.getSchool());


    }

    //关闭事件
    public void CloseAlterPerson(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        AlterPersonView.loginStage = null;
    }

   //匿名输入框监听事件
    public void NameReleasedEmail_TextField(){
        if(RegularExpression.CheckName(Name_TextField.getText())){
            NameErrorTip_Label.setText("");
            NameKey = true;
        }else{
            NameKey = false;
            NameErrorTip_Label.setText("您的匿名格式有误");
        }
    }

    //生日输入框监听事件
    public void BirthdayMouseEnterEmail_TextField(){
        if(Birthday_DatePicker.getValue()!=null){
            BirthdayKey = true;
            BirthdayErrorTip_Label.setText("");
        }else{
            BirthdayKey = false;
            BirthdayErrorTip_Label.setText("生日不能为空");
        }
    }


    //保存信息事件
    public void SaveInformation() throws Exception{
        System.out.println(NameKey + " " +BirthdayKey);
        if(!NameKey || !BirthdayKey){
            return;
        }
        MainCoreController.getPersonInformation();
        MainCoreController.getPersonInformation().setName(Name_TextField.getText());
        MainCoreController.getPersonInformation().setSchool(School_TextField.getText());
        MainCoreController.getPersonInformation().setPersonalSignature(PersonalSignature_TextArea.getText());
        MainCoreController.getPersonInformation().setGender(Gender);
        MainCoreController.getPersonInformation().setBirthday(Birthday_DatePicker.getValue().toString());
        MainCoreController.getPersonInformation().setFileSavePath(Path);
        MainCoreController.getPersonInformation().setHeadPortraitPath("");
        if (selectedFile!=null) {
            MainCoreController.getPersonInformation().setHeadPortrait_byte(FileUtils.FileChangeToByte(selectedFile));
            MainCoreController.getPersonInformation().setHeadPortraitFile(selectedFile);
        }
        UpdatePersonInformation(MainCoreController.getPersonInformation());
    }



    public static class View extends Application {
        public static void main(String[] args){
            launch(args);
        }
        @Override
        public void start(Stage primaryStage) throws Exception{
            AlterPersonView.start(primaryStage);
        }
    }

    //点击事件(上传头像)
    public void UploadAvatar(){
        FileChooser fileChooser = new FileChooser();
        //selectedFile返回一个File对象
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile==null) return;
        if(!FileUtils.CheckFileIs_png_jpeg(selectedFile)){
            JavaFxUtils.ShowStringOnLabel(AlterPersonView.$("HeadErrorTip_Label"),"请选择jpeg文件或者png文件");
            return;
        }
        System.out.println(selectedFile);
        if(selectedFile==null){
            return;
        }
        Image image = null;
        image = FileUtils.File_Image(selectedFile);
        System.out.println(image);
        HeadPortrait_ImageView.setImage(image);
    }
}
