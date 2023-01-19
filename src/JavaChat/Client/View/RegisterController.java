package JavaChat.Client.View;
import java.io.*;
import java.time.LocalDate;

import JavaChat.Client.Service.RegisterService;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.RegularExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegisterController {
    public static String MyAccount;
    ToggleGroup toggleGroup = new ToggleGroup();//单选组
    @FXML
    private TextField anonymousName_TextField;
    @FXML
    private Label anonymousNameErrorTip_Label;
    private boolean anonymousNameKey;
    @FXML
    private PasswordField AccountPassword_PasswordField;
    @FXML
    private Label AccountPasswordErrorTip_Label;
    private boolean AccountPasswordKey;
    @FXML
    private PasswordField AccountPasswordAgain_PasswordField;
    @FXML
    private Label AccountPasswordAgainErrorTip_Label;
    private boolean AccountPasswordAgainKey;
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
    private TextField Email_TextField;//邮箱输入
    @FXML
    private Label EmailErrorTip_Label;
    private boolean EmailKey;
    @FXML
    private TextField EmailVerificationCode_TextField;//验证码输入
    @FXML
    private Label EmailVerificationCodeErrorTip_Label;
    private boolean EmailVerificationCodeKey;
    @FXML
    private Button FinishedRegister_Button;
    @FXML
    private Label RegisterErrorTip_Label;
    private Boolean RegisterKey;
    @FXML
    private Button SendVerificationCode_Button;
    @FXML
    private Label VerificationCode_ErrorTip;
    @FXML
    private Button Return_Button;
    @FXML
    private Button UploadAvatar_Button;//上传头像
    @FXML
    private Button HeadPortrait_Button;
    @FXML
    private ImageView HeadPortrait_ImageView = null;
    private Boolean HeadPortraitKey;
    @FXML
    private Label HeadPortraitErrorTip_Label;
    //选择头像
    private File selectedFile;

    private String Gender = null;

    public void initialize() {
        //不能选择未来日期
        Birthday_DatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
            }
        });
        this.Gender = "男";
        Man_RadioButton.setToggleGroup(toggleGroup);
        Women_RadioButton.setToggleGroup(toggleGroup);

        Man_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Gender = Man_RadioButton.getText();
                }
            }
        });
        Women_RadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Gender = Women_RadioButton.getText();
                }
            }
        });
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton r = (RadioButton) newValue;
            }
        });
        toggleGroup.selectToggle(Man_RadioButton);

        anonymousNameKey = false;
        AccountPasswordKey = false;
        AccountPasswordAgainKey =false;
        BirthdayKey = false;
        EmailKey = false;
        EmailVerificationCodeKey =false;
        HeadPortraitKey = false;
        HeadPortraitErrorTip_Label.setText("请上传您的头像");
    }
    public void But_ColorExited(){
        FinishedRegister_Button.setStyle(
                "-fx-background-color: #48bcff;"
        );
    }

    //匿名输入框监听事件
    public void KeyReleasedAnonymousName_TextField() {
        if (RegularExpression.CheckName(anonymousName_TextField.getText())) {
            anonymousNameKey = true;
            anonymousNameErrorTip_Label.setText("");
        } else {
            anonymousNameKey = false;
            anonymousNameErrorTip_Label.setText("您的匿名格式有误");
        }
    }

    //密码
    public void KeyReleasedAccountPasswordReleased_PasswordField() {
        if(RegularExpression.CheckAccountPassword(AccountPassword_PasswordField.getText())){
            AccountPasswordKey = true;
            AccountPasswordErrorTip_Label.setText("");
        }else{
            AccountPasswordKey = false;
            AccountPasswordErrorTip_Label.setText("您的密码格式有误");
        }

    }

    //再次输入密码监听
    public void KeyReleasedAccountPasswordAgain_PasswordField(){
        if(AccountPasswordAgain_PasswordField.getText().equals(AccountPassword_PasswordField.getText())){
            AccountPasswordAgainKey = true;
            AccountPasswordAgainErrorTip_Label.setText("");
        }else{
            AccountPasswordAgainKey = false;
            AccountPasswordAgainErrorTip_Label.setText("您的密码与您前面输入不一致");
        }
    }

    //生日输入框监听事件鼠标进入
    public void MouseMoveBirthday_TextField(){
        if(Birthday_DatePicker.getValue()!=null){
            BirthdayKey = true;
            BirthdayErrorTip_Label.setText("");
            System.out.println("选择的时间为:"+Birthday_DatePicker.getValue().toString());
        }else{
            BirthdayKey = false;
            BirthdayErrorTip_Label.setText("生日不能为空");
        }
    }
    //生日鼠标离开
    public void MouseExitBirthday_TextField(){
        if(Birthday_DatePicker.getValue()!=null){
            BirthdayKey = true;
            BirthdayErrorTip_Label.setText("");
            System.out.println("选择的时间为:"+Birthday_DatePicker.getValue().toString());
        }else{
            BirthdayKey = false;
            BirthdayErrorTip_Label.setText("生日不能为空");
        }
    }

    //邮箱输入框监听事件
    public void KeyReleasedEmail_TextField(){
        if(RegularExpression.CheckEmail(Email_TextField.getText())){
            EmailKey = true;
            EmailErrorTip_Label.setText("");
        }else{
            EmailKey = false;
            EmailErrorTip_Label.setText("您的邮箱有误");
        }
    }

    //验证码监听
    public void KeyReleasedEmailVerificationCode_TextField(){
        if(!RegularExpression.CheckStringIsEmpty(EmailVerificationCode_TextField.getText())){
            EmailVerificationCodeKey = true;
            EmailVerificationCodeErrorTip_Label.setText("");
        }else{
            EmailVerificationCodeKey = false;
            EmailVerificationCodeErrorTip_Label.setText("您的验证码不能为空");
        }
    }

    //注册监听
    public void EnterRegister_Button(){
        FinishedRegister_Button.setStyle(
                "-fx-background-color: #00f1ea;"
        );
        System.out.println(anonymousNameKey+" "+AccountPasswordKey+" "+AccountPasswordAgainKey+" "+EmailKey+" "+BirthdayKey+" "+EmailVerificationCodeKey);
        //最终检查
        if(HeadPortrait_ImageView!=null&&RegularExpression.CheckName(anonymousName_TextField.getText())&&RegularExpression.CheckAccountPassword(AccountPassword_PasswordField.getText())&&AccountPassword_PasswordField.getText().equals(AccountPasswordAgain_PasswordField.getText())&&RegularExpression.CheckEmail(Email_TextField.getText())&&!RegularExpression.CheckStringIsEmpty(EmailVerificationCode_TextField.getText())&&Birthday_DatePicker.getValue()!=null){
            anonymousNameErrorTip_Label.setText("");
            anonymousNameKey = true;
            AccountPasswordErrorTip_Label.setText("");
            AccountPasswordKey = true;
            AccountPasswordAgainErrorTip_Label.setText("");
            AccountPasswordAgainKey = true;
            EmailErrorTip_Label.setText("");
            EmailKey = true;
           // VerificationCode_ErrorTip.setText("");
            EmailVerificationCodeKey = true;
            BirthdayKey = true;
            HeadPortraitKey = true;
        }
        if((HeadPortraitKey&&anonymousNameKey&&AccountPasswordKey&&AccountPasswordAgainKey&&EmailKey&&BirthdayKey&&EmailVerificationCodeKey)){
            RegisterErrorTip_Label.setText("");
            RegisterKey = true;
        }else{
            RegisterKey = false;
            RegisterErrorTip_Label.setText("请务必先将注册所需的信息输入完整");
        }
    }

    public void ReturnLogin(){
        System.out.println("返回");
        Stage stage =(Stage) Return_Button.getScene().getWindow();
        stage.close();
        try {
            LoginView.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //点击事件(上传头像)
    public void UploadAvatar() throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        //selectedFile返回一个File对象
        selectedFile = fileChooser.showOpenDialog(new Stage());
        System.out.println(selectedFile);
        if(selectedFile==null){
            HeadPortraitErrorTip_Label.setText("请上传您的头像");
            HeadPortraitKey =false;
            return; }
        HeadPortraitErrorTip_Label.setText("");
        HeadPortraitKey = true;
        Image image = null;
        image = FileUtils.File_Image(selectedFile);
        System.out.println(image);
        HeadPortrait_ImageView.setImage(image);
    }

    //发送验证码
    public void Send_VerificationCode() {
        //正则判断
        if(!EmailKey){
            System.out.println("邮箱未输入");
            return;
        }
        User user = new User();
        user.setEmail(Email_TextField.getText());

        //申请发送邮件
        try {
            RegisterService.SendRegisterEmail(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        //注册事件
        public void RegisterAccount () throws Exception {
            if(!RegisterKey){
                return;
            }
            User user = new User();
            user.setName(anonymousName_TextField.getText());
            user.setAccountNumber("");
            user.setHeadPortraitFile(selectedFile);
            user.setHeadPortrait_byte(FileUtils.FileChangeToByte(selectedFile));
            user.setAccountPassword(AccountPassword_PasswordField.getText());
            user.setEmail(Email_TextField.getText());
            user.setHeadPortrait_byte(FileUtils.FileChangeToByte(selectedFile));
            user.setVerificationCode(EmailVerificationCode_TextField.getText());
            if (RegisterService.Register(user)){
                System.out.println("注册成功!");
                ReturnLogin();
                JavaFxUtils.ShowStringOnTextField(LoginView.$("AccountNumber_TextField"),MyAccount);
                JavaFxUtils.ShowStringOnLabel(LoginView.$("LoginErrorTip_Label"),"您已经注册成功!");
            } else {
                System.out.println("注册失败!");
            }
        }


}
