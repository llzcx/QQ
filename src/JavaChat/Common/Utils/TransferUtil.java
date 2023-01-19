package JavaChat.Common.Utils;

import JavaChat.Client.Thread.ClientConnectServerThread;
import JavaChat.Client.Thread.ManageClientConnectServerThread;
import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Transfer.TransferMessage;
import com.sun.security.ntlm.Server;

import javax.jws.soap.SOAPBinding;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 此类为方便写客户端发送请求的代码的工具
 */
public class TransferUtil {
    /**
     * 该方法实现将某个数据发送到指定服务器端口,并接收服务器传
     * 来的内容并返回一个传输类TransferMessage
     * 准确的来说既负责发送又负责接受
     * @param SendPojo 传过去的数据
     * @param clientRequest 请求样式
     * @return 返回一个传输交互类
     */
    public static TransferMessage ClientOutsideTransfer(Object SendPojo, ClientRequest clientRequest) throws Exception{
        //通信
        Socket socket = null;
        //传输用的
        TransferMessage SendMessage = new TransferMessage<>();
        //接受
        TransferMessage ReceiveMessage = null;

        SendMessage.setJavabean(SendPojo);
        SendMessage.setMessageType(clientRequest);
        System.out.println("发送到服务器的请求为:"+SendMessage.getMessageType());
        //send
        socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(SendMessage);

        //read
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ReceiveMessage = (TransferMessage) ois.readObject();
        System.out.println("服务器的回应为:"+ReceiveMessage.getMessageType());
        //对于登录单独处理
        if(ReceiveMessage.getMessageType()== ServerRespond.LOGIN_SUCCEED){
            User user = (User) ReceiveMessage.getJavabean();
            ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
            //启动线程
            ccst.start();
            //放入到线程集合方便管理
            System.out.println(user.getAccountNumber());
            System.out.println(ccst.getSocket());
            ManageClientConnectServerThread.addClientConnectServerThread(user.getAccountNumber(),ccst);
        }

        if(ReceiveMessage.getMessageType() != ServerRespond.LOGIN_SUCCEED){
            socket.close();
            oos.close();
            ois.close();
        }
        return ReceiveMessage;
    }

    /**
     * 此方法为登录操作以后的消息传输,
     * 将消息发过去以后要求服务器在通信线程那边接受
     * 返回是否发送成功即可
     * 准确的来说只是负责发送,接受在另外的线程接受
     * @param SendPojo
     * @param clientRequest
     * @throws Exception
     */
    public static void ClientInsideTransfer(Object SendPojo, ClientRequest clientRequest) throws Exception {
        //在这个客户端专属的主界面有一个专属的账号,这个必然是发送者的账号
        String UserAccount = MainCoreController.getAccountNumber();
        ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(UserAccount);
        Socket socket = clientConnectServerThread.getSocket();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        TransferMessage SendMessage = new TransferMessage();
        SendMessage.setJavabean(SendPojo);
        SendMessage.setMessageType(clientRequest);
        oos.writeObject(SendMessage);
    }

    public static void main(String[] args) {
        //通信
        Socket socket = null;
        //传输用的
        TransferMessage<Object> SendMessage = new TransferMessage<>();
        //接受
        TransferMessage transferMessage = null;

        SendMessage.setJavabean(new String("112"));
        SendMessage.setMessageType(ClientRequest.SEND_TEXT_MESSAGE);
        System.out.println(SendMessage.getMessageType());
    }
}
