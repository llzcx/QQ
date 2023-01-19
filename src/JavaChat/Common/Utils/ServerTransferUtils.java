package JavaChat.Common.Utils;

import JavaChat.Common.Transfer.TransferMessage;
import JavaChat.Server.Thread.ManageClientThreads;
import JavaChat.Server.Thread.ServerConnectClientThread;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 该类
 */
public class ServerTransferUtils {
    /**
     * 将实体类发送给某个客户端的方法
     * 如果对方在线则返回true
     * 不在线则返回false
     * @param Account
     * @param SendMessage
     */
    public static boolean SendToActiveAndPassive(String Account, TransferMessage SendMessage){
        boolean flag = true;
        ServerConnectClientThread serverConnectClientThread =
                ManageClientThreads.getServerConnectClientThread(Account);
        if(serverConnectClientThread!=null){
            ObjectOutputStream oos =
                    null;
            try {

                oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                oos.writeObject(SendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            flag = false;
        }
        return false;
    }

}
