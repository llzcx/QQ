package JavaChat.Client.Service;

import JavaChat.Client.View.ForgetView;
import JavaChat.Client.View.LoginView;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.MessageType;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Transfer.TransferMessage;
import JavaChat.Common.Utils.JavaFxUtils;
import JavaChat.Common.Utils.TransferUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ForgetService {
    Socket socket = null;
    /**
     * 此方法为忘记密码业务,客户端外部消息传输
     * @param user
     * @return
     * @throws Exception
     */
    public static boolean Forget(User user) throws Exception{
        boolean flag = false;
        TransferMessage transferMessage = TransferUtil.ClientOutsideTransfer(user, ClientRequest.FORGET);
        //拿实体类,再拿消息类型
        MessageType messageType = transferMessage.getMessageType();

        if(messageType == ServerRespond.FORGET_SUCCEED){
            User user1 = (User) transferMessage.getJavabean();
            ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("重置密码成功");
            JavaFxUtils.ShowTipDialogStage("重置密码","您的密码已经重置!(账号:"+user1.getAccountNumber()+")");
            LoginView.start(new Stage());
            ForgetView.loginStage.close();
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("ResetErrorTip_Label")!=null)) {
                                ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            //让改变javafx控件的代码在主线程中运行
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //先将view层的按钮设置一下
                    ((Button)ForgetView.$("SendVerificationCode_Button")).setStyle(
                            "-fx-background-color: #847d7d"
                    );
                    //按钮不可用
                    ((Button)ForgetView.$("SendVerificationCode_Button")).setDisable(true);
                }
            });
            flag = true;
        }else if(messageType == ServerRespond.FORGET_VERIFICATION_CODE_ERROR) {
            ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("验证码错误");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("ResetErrorTip_Label")!=null)) {
                                ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;

        }else if(messageType == ServerRespond.FORGET_VERIFICATION_CODE_NOT_SEND) {
            ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("验证码未发送");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("ResetErrorTip_Label")!=null)) {
                                ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else if(messageType == ServerRespond.FORGET_EMAIL_IS_NOT_RIGHT) {
            ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("账号或邮箱错误");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("ResetErrorTip_Label")!=null)) {
                                ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("");
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
            System.out.println("忘记密码的其他消息暂时不处理");
        }
        return flag;
    }

    //发送忘记密码邮件
    public static boolean SendForgetEmail(User user) throws Exception {
        boolean flag = false;
        TransferMessage transferMessage = TransferUtil.ClientOutsideTransfer(user, ClientRequest.SEND_FORGET_EMAIL);
        MessageType messageType = transferMessage.getMessageType();
        if(messageType==ServerRespond.SEND_FORGET_EMAIL_REPEAT){
            ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("您的验证码依然有效,请勿重复发送验证码");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("ResetErrorTip_Label")!=null)) {
                                ((Label)(ForgetView.$("ResetErrorTip_Label"))).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else if(messageType==ServerRespond.SEND_FORGET_EMAIL_SUCCEED){
            //让改变javafx控件的代码在主线程中运行
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //先将view层的按钮设置一下
                    ((Button) (ForgetView.$("SendVerificationCode_Button"))).setStyle(
                            "-fx-background-color: #847d7d"
                    );
                    //按钮不可用
                    ((Button)ForgetView.$("SendVerificationCode_Button")).setDisable(true);
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
                                ((Button)ForgetView.$("SendVerificationCode_Button")).setText("重新发送("+i[0].toString()+")");
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
                        ((Button)ForgetView.$("SendVerificationCode_Button")).setStyle(
                                "-fx-background-color: #17e7f2"
                        );
                        //按钮还原
                        ((Button)ForgetView.$("SendVerificationCode_Button")).setDisable(false);
                        ((Button)ForgetView.$("SendVerificationCode_Button")).setText("重新发送验证码");
                    }
                });


                Socket socket1 = null;

                TransferMessage SendMessage = new TransferMessage();
                //让服务器删除这个验证码
                SendMessage.setJavabean(user);
                SendMessage.setMessageType(ClientRequest.FORGET_VERIFICATION_OBSOLETE);
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
        }else if(messageType == ServerRespond.FORGET_EMAIL_IS_NOT_RIGHT){
            ((Label)(ForgetView.$("SendErrorTip"))).setText("请检查邮箱是否正确");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //防止登录页面被关闭了
                            if ((ForgetView.$("SendErrorTip")!=null)) {
                                ((Label)(ForgetView.$("SendErrorTip"))).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
        //关闭资源
        return flag;
    }


}
