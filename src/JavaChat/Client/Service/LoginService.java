package JavaChat.Client.Service;

import JavaChat.Client.View.LoginView;
import JavaChat.Client.View.MainCoreController;
import JavaChat.Client.View.RegisterView;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.MessageType;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Transfer.TransferMessage;
import JavaChat.Common.Utils.ClientFileAddress;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.TransferUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginService {
    /**
     * 此方法为登录业务类,客户端外部消息传输
     * @param user
     * @return
     * @throws Exception
     */
    public static boolean Login(User user) throws Exception {
        User user1 = null;
        boolean flag = false;
        TransferMessage transferMessage = TransferUtil.ClientOutsideTransfer(user, ClientRequest.LOGIN);
        MessageType messageType = transferMessage.getMessageType();

        if(messageType == ServerRespond.LOGIN_SUCCEED){//登录成功
            MainCoreController.PersonInformation = (User)transferMessage.getJavabean();
            User user2 = MainCoreController.PersonInformation;
            MainCoreController.FriendHeadPortrait.put(user2.getAccountNumber(),FileUtils.ByteToFile(user2.getHeadPortrait_byte(), ClientFileAddress.HeadPortrait,user2.getHeadPortraitFile().getName()));
            flag = true;
        }else if(messageType == ServerRespond.LOGIN_FAIL_ERROR){//登录失败
            ((Label)LoginView.$("LoginErrorTip_Label")).setText("账号或密码错误");
            Thread t = new Thread(() -> {
                try {
                        Thread.sleep(3000);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //防止登录页面被关闭了
                                if ((LoginView.$("LoginErrorTip_Label")!=null)) {
                                    ((Label) LoginView.$("LoginErrorTip_Label")).setText("");
                                }
                            }
                        });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }else if(messageType == ServerRespond.LOGIN_FAIL_REPEAT_LOGIN){
            ((Label)LoginView.$("LoginErrorTip_Label")).setText("重复登录");
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if((LoginView.$("LoginErrorTip_Label")!=null)){
                                ((Label) LoginView.$("LoginErrorTip_Label")).setText("");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            flag = false;
        }
        return flag;
    }

    /**
     * 属于登录后的操作,客户端内部消息传输
     * @param user
     * @return
     * @throws Exception
     */
    public static void LoginOut(User user) throws Exception{
        boolean flag = false;
        TransferUtil.ClientInsideTransfer(user, ClientRequest.EXIT_LOGIN);
    }
}
