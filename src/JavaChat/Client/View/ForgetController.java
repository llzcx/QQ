package JavaChat.Client.View;

import JavaChat.Client.Service.ForgetService;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.RegularExpression;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ForgetController {
    //账号
    @FXML
    private TextField AccountNumber_TextField;
    @FXML
    private Label AccountNumberErrorTip_Label;
    private Boolean AccountNumberKey;
    //新密码
    @FXML
    private PasswordField NewAccountPassword_TextField;
    @FXML
    private Label NewAccountPasswordErrorTip_Label;
    private Boolean NewAccountPasswordKey;
    //再次输入新密码
    @FXML
    private PasswordField NewAccountPasswordAgain_TextField;
    @FXML
    private Label NewAccountPasswordAgainErrorTip_Label;
    private Boolean NewAccountPasswordAgainKey;
    //邮箱
    @FXML
    private TextField Email_TextField;//邮箱输入
    @FXML
    private Label EmailErrorTip_Label;
    private Boolean EmailKey;
    //验证码输入
    @FXML
    private TextField EmailVerificationCode_TextField;//验证码输入
    @FXML
    private Label EmailVerificationCodeErrorTip_Label;
    private Boolean EmailVerificationCodeKey;
    //重置密码
    @FXML
    private Button Reset_Button;
    @FXML
    private Label ResetErrorTip_Label;
    private Boolean ResetKey;
    //发送验证码按钮
    @FXML
    private Button SendVerificationCode_Button;
    @FXML
    private Label SendErrorTip;
    @FXML
    private Button Return_Button;
    public void initialize() {
        AccountNumberKey = false;
        NewAccountPasswordKey = false;
        NewAccountPasswordAgainKey = false;
        EmailVerificationCodeKey =false;
        EmailKey = false;
        EmailVerificationCodeKey =false;
        ResetKey = false;
    }

    //账号
    public void KeyReleasedAccountNumber_Field() {
        if(RegularExpression.CheckAccountNumber(AccountNumber_TextField.getText())){
            AccountNumberKey = true;
            AccountNumberErrorTip_Label.setText("");
        }else{
            AccountNumberKey = false;
            AccountNumberErrorTip_Label.setText("您的账号格式有误");
        }

    }
    //验证码
    public void KeyReleasedEmailVerificationCode_TextField() {
        if(!RegularExpression.CheckStringIsEmpty(EmailVerificationCode_TextField.getText())){
            EmailVerificationCodeKey = true;
            EmailVerificationCodeErrorTip_Label.setText("");
        }else{
            EmailVerificationCodeKey = false;
            EmailVerificationCodeErrorTip_Label.setText("您的验证码不能为空");
        }
    }


    //新密码
    public void KeyReleasedNewAccountPassword_TextField() {
        if(RegularExpression.CheckAccountPassword(NewAccountPassword_TextField.getText())){
            NewAccountPasswordKey = true;
            NewAccountPasswordErrorTip_Label.setText("");
        }else{
            NewAccountPasswordKey = false;
            NewAccountPasswordErrorTip_Label.setText("您的密码格式有误");
        }

    }

    //再次输入新的密码
    public void KeyReleasedNewAccountPasswordAgain_TextField() {
        if(NewAccountPassword_TextField.getText().equals(NewAccountPasswordAgain_TextField.getText())){
            NewAccountPasswordAgainKey = true;
            NewAccountPasswordAgainErrorTip_Label.setText("");
        }else{
            NewAccountPasswordAgainKey = false;
            NewAccountPasswordAgainErrorTip_Label.setText("您再次输入的密码与前一次不一致");
        }
    }

    //邮箱
    public void KeyReleasedEmail_TextField() {
        if(RegularExpression.CheckEmail(Email_TextField.getText())){
            EmailKey = true;
            EmailErrorTip_Label.setText("");
        }else{
            EmailKey = false;
            EmailErrorTip_Label.setText("邮箱格式不正确");
        }
    }
    public void ReturnLogin(){
        Stage stage =(Stage) Reset_Button.getScene().getWindow();
        stage.close();
        try {
            LoginView.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendVerificationCode() {
        if(!EmailKey || !AccountNumberKey){
            SendErrorTip.setText("请将邮箱和账号填写完整");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("")!=null)) {
                                ((Label)(ForgetView.$("SendErrorTip"))).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            return;
        }
        User user = new User();
        user.setAccountNumber(AccountNumber_TextField.getText());
        user.setAccountPassword(NewAccountPassword_TextField.getText());
        user.setEmail(Email_TextField.getText());
        user.setVerificationCode(EmailVerificationCode_TextField.getText());
        try {
            ForgetService.SendForgetEmail(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //鼠标进入
    public void EnterForget_Button(){
        Reset_Button.setStyle(
                "-fx-background-color: #48bcff;"
        );
        System.out.println(AccountNumberKey+" "+NewAccountPasswordKey+" "+NewAccountPasswordAgainKey+" "+EmailKey+" "+EmailVerificationCodeKey);
        //最终检查
        if(RegularExpression.CheckAccountNumber(AccountNumber_TextField.getText())&&RegularExpression.CheckAccountPassword(NewAccountPassword_TextField.getText())&&NewAccountPassword_TextField.getText().equals(NewAccountPasswordAgain_TextField.getText())&&RegularExpression.CheckEmail(Email_TextField.getText())&&!RegularExpression.CheckStringIsEmpty(EmailVerificationCode_TextField.getText())){
            AccountNumberErrorTip_Label.setText("");
            AccountNumberKey = true;
            NewAccountPasswordErrorTip_Label.setText("");
            NewAccountPasswordKey = true;
            NewAccountPasswordAgainErrorTip_Label.setText("");
            NewAccountPasswordAgainKey = true;
            EmailErrorTip_Label.setText("");
            EmailKey = true;
            EmailVerificationCodeErrorTip_Label.setText("");
            EmailVerificationCodeKey = true;
        }
        if((AccountNumberKey&&NewAccountPasswordKey&&NewAccountPasswordAgainKey&&EmailKey&&EmailVerificationCodeKey)){
            ResetKey = true;
            ResetErrorTip_Label.setText("");
        }else{
            ResetKey = false;
            ResetErrorTip_Label.setText("请务必先将重置密码所需的信息输入完整");
        }
    }

    public void ExitForget_Button(){
        Reset_Button.setStyle(
                "-fx-background-color: #00f1ea;"
        );
    }

    //设计点击事件
    public void ForgetAccount () throws Exception {
        if(AccountNumber_TextField.getText().equals("")||NewAccountPassword_TextField.getText().equals("")||Email_TextField.getText().equals("")|| EmailVerificationCode_TextField.getText().equals("")){
            System.out.println("请将信息输入完整!");
            return;
        }
        User user = new User();
        user.setAccountNumber(AccountNumber_TextField.getText());
        user.setEmail(Email_TextField.getText());
        user.setAccountPassword(NewAccountPassword_TextField.getText());
        user.setVerificationCode(EmailVerificationCode_TextField.getText());
        if (ForgetService.Forget(user)){
            System.out.println("重置成功!");
        } else {
            System.out.println("重置失败!");
        }
    }

}
