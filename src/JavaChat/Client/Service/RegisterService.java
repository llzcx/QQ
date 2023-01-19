package JavaChat.Client.Service;

import JavaChat.Client.View.LoginView;
import JavaChat.Client.View.RegisterController;
import JavaChat.Client.View.RegisterView;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.MessageType;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Transfer.TransferMessage;
import JavaChat.Common.Utils.TransferUtil;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static javafx.scene.paint.Color.TRANSPARENT;

public class RegisterService {

    /**
     * 此方法为注册业务类,客户端外部消息传输
     * @param user
     * @return
     * @throws Exception
     */
    public static boolean Register(User user) throws Exception{
        MessageType ReceiveMessage = null;
        User user1 = null;
        boolean flag = false;

        TransferMessage transferMessage = TransferUtil.ClientOutsideTransfer(user,ClientRequest.REGISTER);

        //拿实体类,再拿消息类型
        MessageType messageType =transferMessage.getMessageType();
        user1 = (User)transferMessage.getJavabean();

        if(messageType == ServerRespond.REGISTER_SUCCEED){
            Stage dialogStage = new Stage();
            DialogPane dialog = new DialogPane();
            dialog.setHeaderText("");
            dialog.setContentText("恭喜你!注册成功,您的账号为:"+user1.getAccountNumber());
            dialog.getButtonTypes().add(ButtonType.FINISH);
            Button close = (Button)dialog.lookupButton(ButtonType.FINISH);
            close.setText("复制账号");
            String Acc = user1.getAccountNumber();
            close.setOnAction(event -> {
                dialogStage.close();
                //放到粘贴板上去
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(Acc);
                clipboard.setContent(clipboardContent);
            });
            Scene dialogScene = new Scene(dialog,TRANSPARENT);
            dialogStage.setScene(dialogScene);
            dialogStage.setTitle("注册成功!");
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.show();
            RegisterController.MyAccount = user1.getAccountNumber();
            flag = true;
        }else if(messageType == ServerRespond.REGISTER_VERIFICATION_CODE_ERROR){
            ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("验证码错误");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止注册页面被关闭了
                            if ((RegisterView.$("RegisterErrorTip_Label")!=null)) {
                                ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else if(messageType == ServerRespond.REGISTER_EMAIL_OCCUPIED) {
            ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("邮箱已经被注册了");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((RegisterView.$("RegisterErrorTip_Label")!=null)) {
                                ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else if(messageType==ServerRespond.REGISTER_VERIFICATION_CODE_NOT_SEND){
            ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("邮箱未发送");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((RegisterView.$("RegisterErrorTip_Label")!=null)) {
                                ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else{
            System.out.println("注册其他消息暂时不处理");
        }
        return flag;
    }
    //发送注册邮件
    public static boolean SendRegisterEmail(User user) throws Exception {
        boolean flag = false;
        TransferMessage transferMessage = TransferUtil.ClientOutsideTransfer(user, ClientRequest.SEND_REGISTER_EMAIL);
        MessageType messageType = transferMessage.getMessageType();
        if(messageType==ServerRespond.SEND_REGISTER_EMAIL_REPEAT){
            ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("您的验证码依然有效,请勿重复发送验证码");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((RegisterView.$("RegisterErrorTip_Label")!=null)) {
                                ((Label)RegisterView.$("RegisterErrorTip_Label")).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else if(messageType==ServerRespond.SEND_REGISTER_EMAIL_SUCCEED){
            //让改变javafx控件的代码在主线程中运行
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //先将view层的按钮设置一下
                    ((Button)RegisterView.$("SendVerificationCode_Button")).setStyle(
                            "-fx-background-color: #847d7d"
                    );
                    //按钮不可用
                    ((Button)RegisterView.$("SendVerificationCode_Button")).setDisable(true);
                }
            });
            final Integer[] i = {60};
            //开启一个线程倒计时
            Thread t = new Thread(() -> {
                try {
                    //倒计时60秒,60秒以后验证码直接自动失效
                    for (; i[0] >= 1; i[0]--) {
                        Thread.sleep(1000);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                ((Button)RegisterView.$("SendVerificationCode_Button")).setText("重新发送("+i[0].toString()+")");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //颜色还原
                        ((Button)RegisterView.$("SendVerificationCode_Button")).setStyle(
                                "-fx-background-color: #17e7f2"
                        );
                        //按钮还原
                        ((Button)RegisterView.$("SendVerificationCode_Button")).setDisable(false);
                        ((Button)RegisterView.$("SendVerificationCode_Button")).setText("重新发送验证码");
                    }
                });


                Socket socket1 = null;

                TransferMessage SendMessage = new TransferMessage();
                //让服务器删除这个验证码
                SendMessage.setJavabean(user);
                SendMessage.setMessageType(ClientRequest.REGISTER_VERIFICATION_OBSOLETE);
                try {
                    socket1 = new Socket(InetAddress.getByName("127.0.0.1"),9999);
                    ObjectOutputStream oos1 = new ObjectOutputStream(socket1.getOutputStream());
                    oos1.writeObject(SendMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        socket1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start(); // 启动新线程
            flag = true;
        }
        //关闭资源
        return flag;
    }


    /**
     * 测试附线程的睡眠情况
     * @param args
     */
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                //倒计时60秒,60秒以后验证码直接自动失效
                Thread.sleep(1000*2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("附线程开始跑了");

        });
        System.out.println("主线程跑了"+"123");
        t.start(); // 启动新线程
    }
}
