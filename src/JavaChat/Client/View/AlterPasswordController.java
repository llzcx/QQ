package JavaChat.Client.View;

import JavaChat.Client.Service.AlterPasswordService;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.RegularExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class AlterPasswordController {
    //关闭
    @FXML
    private Button Close_Button;

    @FXML
    private Label EmailTip_Label;

    //第一次输入密码
    @FXML
    private PasswordField NewPassword_PasswordField;
    @FXML
    private Label NewPasswordErrorTip_Label;
    private Boolean NewPasswordKey;



    //第二次输入密码
    @FXML
    private PasswordField  NewPasswordAgain_PasswordField;
    @FXML
    private Label NewPasswordAgainErrorTip_Label;
    private Boolean NewPasswordAgainKey;

    //验证码输入
    @FXML
    private TextField EmailVerificationCode_TextField;//验证码输入
    @FXML
    private Label EmailVerificationCodeErrorTip_Label;
    private Boolean EmailVerificationCodeKey;
    //修改密码
    @FXML
    private Button AlterPassword_Button;
    @FXML
    private Label AlterPasswordErrorTip_Label;
    private Boolean AlterPasswordKey;

    //发送验证码
    @FXML
    private Button SendVerificationCode_Button;
    @FXML
    private Label SendVerificationCodeErrorTip_Label;

    public void initialize(){
        User user = MainCoreController.getPersonInformation();
        EmailTip_Label.setText("我们将发送一封包含验证码的重置密码邮件至:"+user.getEmail());
        NewPasswordKey = false;
        NewPasswordAgainKey = false;
        EmailVerificationCodeKey = false;
        AlterPasswordKey = false;
//        NewPasswordErrorTip_Label.setText("您的密码格式有误");
//        NewPasswordAgainErrorTip_Label.setText("您的密码与您前面输入不一致");
//        EmailVerificationCodeErrorTip_Label.setText("您的验证码不能为空");
    }

    //密码
    public void KeyReleasedAccountPasswordReleased_PasswordField() {
        if(RegularExpression.CheckAccountPassword(NewPassword_PasswordField.getText())){
            NewPasswordKey = true;
            NewPasswordErrorTip_Label.setText("");
        }else{
            NewPasswordKey = false;
            NewPasswordErrorTip_Label.setText("您的密码格式有误");
        }

    }

    //再次输入密码监听
    public void KeyReleasedAccountPasswordAgain_PasswordField(){
        if(NewPasswordAgain_PasswordField.getText().equals(NewPassword_PasswordField.getText())){
            NewPasswordAgainKey = true;
            NewPasswordAgainErrorTip_Label.setText("");
        }else{
            NewPasswordAgainKey = false;
            NewPasswordAgainErrorTip_Label.setText("您的密码与您前面输入不一致");
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


    //修改密码监听
    public void EnterRegister_Button(){
        AlterPassword_Button.setStyle(
                "-fx-background-color: #00f1ea;"
        );
        System.out.println(NewPasswordAgainKey+" "+NewPasswordAgainKey+" "+EmailVerificationCodeKey);
        //最终检查
        if(RegularExpression.CheckAccountPassword(NewPassword_PasswordField.getText())&&NewPassword_PasswordField.getText().equals(NewPasswordAgain_PasswordField.getText())&&!RegularExpression.CheckStringIsEmpty(EmailVerificationCode_TextField.getText())){
            NewPasswordErrorTip_Label.setText("");
            NewPasswordKey = true;
            NewPasswordAgainErrorTip_Label.setText("");
            NewPasswordAgainKey = true;
            EmailVerificationCodeErrorTip_Label.setText("");
            EmailVerificationCodeKey = true;
        }
        if(NewPasswordKey&&NewPasswordAgainKey&&EmailVerificationCodeKey){
            AlterPasswordErrorTip_Label.setText("");
            AlterPasswordKey = true;
        }else{
            AlterPasswordKey = false;
            AlterPasswordErrorTip_Label.setText("请务必先将修改密码所需的信息输入完整");
        }
    }
    public void But_ColorExited(){
        AlterPassword_Button.setStyle(
                "-fx-background-color: #48bcff;"
        );
    }

    //修改密码
    public void AlterPassword(){
        if(!AlterPasswordKey){
            JavaFxUtils.ShowStringOnLabel(NewPasswordErrorTip_Label,"您的密码格式有误");
            return;
        }
        User user = MainCoreController.getPersonInformation();
        user.setAccountPassword(NewPassword_PasswordField.getText());
        user.setVerificationCode(EmailVerificationCode_TextField.getText());
        AlterPasswordService.AlterPassword(user);
    }

    public void CloseLy(){
        Stage stage =(Stage) Close_Button.getScene().getWindow();
        stage.close();
        AlterPasswordView.loginStage = null;
    }

    public void Send_VerificationCode(){
        if(!NewPasswordKey || !NewPasswordAgainKey){
            JavaFxUtils.ShowStringOnLabel(NewPasswordErrorTip_Label,"您的密码格式有误");
            return;
        }
        User user = MainCoreController.getPersonInformation();
        user.setAccountPassword(NewPassword_PasswordField.getText());
        //申请发送邮件
        AlterPasswordService.SendAlterPasswordEmail(user);
    }

}
