package JavaChat.Client.Service;

import JavaChat.Client.View.AlterPasswordView;
import JavaChat.Client.View.ForgetView;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Transfer.TransferMessage;
import JavaChat.Common.Utils.TransferUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.Socket;

public class AlterPasswordService {
    //发送忘记密码邮件
    public static void SendAlterPasswordEmail(User user) {
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.SEND_ALTER_PASSWORD_EMAIL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void AlterPassword(User user){
        try {
            TransferUtil.ClientInsideTransfer(user,ClientRequest.Alter_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
