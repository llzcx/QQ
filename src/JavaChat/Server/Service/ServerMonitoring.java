package JavaChat.Server.Service;

import JavaChat.Client.Service.RegisterService;
import JavaChat.Client.View.ForgetView;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.MessageType;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Transfer.TransferMessage;
import JavaChat.Common.Utils.*;
import JavaChat.Server.Dao.DatabaseOperation_Login;
import JavaChat.Server.Dao.DatabaseOperation_Register;
import JavaChat.Server.Dao.DatabaseOperation_Forget;
import JavaChat.Server.Thread.ManageClientThreads;
import JavaChat.Server.Thread.ServerConnectClientThread;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import static JavaChat.Server.Dao.DatabaseOperation_Forget.UserAccountNumberAndEmailIsRight;

/**
 * 该类实现接受客户端外层业务消息
 */
public class ServerMonitoring {
    //Key = Email, Value = VerificationCode
    private static HashMap<String,String> Register_VerificationCode = new HashMap<>();
    private static HashMap<String,String> Forget_VerificationCode = new HashMap<>();
    //public static HashMap<String,Vector<String>> VerificationCodeSet = new HashMap<>();
    public static HashMap<String,String> AlterPassword = new HashMap<>();
    private ServerSocket ss = null;
    //该类的一个构造器
    public ServerMonitoring(){
        try {
            //服务期正在监听
            ss = new ServerSocket(9999);
            //循环接受客户端发来的消息
            while (true) {
                //如果没有客户端连接,则会阻塞在accept
                //System.out.println("服务器正在监听");
                Socket socket = ss.accept();
                //ystem.out.println("监听完毕");
                //得到socket的输入流(等客户端发来消息)
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                TransferMessage ReceiveMessage  = (TransferMessage) ois.readObject();
                System.out.println("服务器获得外部指令:"+ReceiveMessage.getMessageType());
                MessageType messageType = ReceiveMessage.getMessageType();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //创建一个Message对象,回复给客户端
                TransferMessage SendMessage = new TransferMessage();

                if(false){}

                /**
                 * 登录
                 */
                else if(messageType==ClientRequest.LOGIN){
                    User user = (User)ReceiveMessage.getJavabean();
                    //在数据库检查账号密码是否匹配
                    if (!DatabaseOperation_Login.CheckAccountNumberIsRight(user.getAccountNumber(),user.getAccountPassword())) {
                        SendMessage.setMessageType(ServerRespond.LOGIN_FAIL_ERROR);
                    }else if(ManageClientThreads.getServerConnectClientThread(user.getAccountNumber())!=null){
                        SendMessage.setMessageType(ServerRespond.LOGIN_FAIL_REPEAT_LOGIN);
                    }else{
                        //在数据库拿到user的所有信息
                        User user1 = DatabaseOperation_Login.GetUserInformation(user.getAccountNumber(),user.getAccountPassword());
                        //拿到头像
                        File file = new File(user1.getHeadPortraitPath());
                        user1.setHeadPortraitFile(file);
                        user1.setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
                        System.out.println("用户:" + user.getAccountNumber()+"已经登录!");
                        ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getAccountNumber(),socket);
                        ManageClientThreads.addServerConnectClientThread(user.getAccountNumber(),serverConnectClientThread);
                        serverConnectClientThread.start();
                        SendMessage.setJavabean(user1);
                        SendMessage.setMessageType(ServerRespond.LOGIN_SUCCEED);

                    }
                    oos.writeObject(SendMessage);

                }

                /**
                 * 注册
                 */
                else if(messageType==ClientRequest.REGISTER){
                    User user = (User) ReceiveMessage.getJavabean();
                    String VerificationCode = Register_VerificationCode.get(user.getEmail());

                    //先检查验证码
                    if(VerificationCode==null){
                        SendMessage.setMessageType(ServerRespond.REGISTER_VERIFICATION_CODE_NOT_SEND);
                    }
                    else if(!user.getVerificationCode().equals(VerificationCode)){
                        SendMessage.setMessageType(ServerRespond.REGISTER_VERIFICATION_CODE_ERROR);
                    }
                    //去数据库查重
                    else if(DatabaseOperation_Register.EmailIsEmpty(user)){
                        //在磁盘中写入头像
                        if(user.getHeadPortrait_byte()!=null && user.getHeadPortraitPath()!=null && user.getHeadPortraitFile()!=null)
                        user.setHeadPortraitPath(
                                FileUtils.saveFile(user.getHeadPortrait_byte(),ServerFileAddress.HeadPortrait,user.getHeadPortraitFile().getName())
                        );
                        //在数据库创建新用户
                        String acc = "";
                        do{
                            acc = SnowFlake.GetAccountNumber();
                        }while (DatabaseOperation_Login.CheckAccountNumberIsExist(acc));
                        user.setAccountNumber(acc);
                        DatabaseOperation_Register.CreatNewUser(user);
                        SendMessage.setJavabean(user);
                        SendMessage.setMessageType(ServerRespond.REGISTER_SUCCEED);
                    }else{
                        SendMessage.setMessageType(ServerRespond.REGISTER_EMAIL_OCCUPIED);
                    }
                    oos.writeObject(SendMessage);
                }

                /**
                 * 忘记密码
                 */
                else if(messageType == ClientRequest.FORGET){
                    User user = (User)ReceiveMessage.getJavabean();
                    String VerificationCode = Forget_VerificationCode.get(user.getEmail());
                    SendMessage.setJavabean(user);
                    //检查验证码
                    if(VerificationCode==null){
                        SendMessage.setMessageType(ServerRespond.FORGET_VERIFICATION_CODE_NOT_SEND);
                    }
                    else if(!VerificationCode.equals(user.getVerificationCode())){
                        SendMessage.setMessageType(ServerRespond.FORGET_VERIFICATION_CODE_ERROR);
                    }
                    //去数据库查询账号与邮箱是否匹配
                    else if(UserAccountNumberAndEmailIsRight(user)){
                        //修改此账号在数据库中密码
                        DatabaseOperation_Forget.ResetAccountPassword(user);
                        System.out.println("账号:" + user.getAccountNumber() + "已经修改密码!");
                        SendMessage.setMessageType(ServerRespond.FORGET_SUCCEED);
                    }else{
                        SendMessage.setMessageType(ServerRespond.FORGET_EMAIL_IS_NOT_RIGHT);
                        System.out.println("账号:" + user.getAccountNumber() + "尝试修改密码但失败了!(原因:user中账号与email不匹配)");
                    }
                    //传给客户端
                    oos.writeObject(SendMessage);
                }


                /**
                 * 发送注册验证码
                 */
                else if(messageType == ClientRequest.SEND_REGISTER_EMAIL){
                    User user = (User) ReceiveMessage.getJavabean();
                    if(Register_VerificationCode.get(user.getEmail())!=null){
                        SendMessage.setMessageType(ServerRespond.SEND_REGISTER_EMAIL_REPEAT);
                    }else {
                        String VerificationCode = CreatString.getCharAndNum(5);
                        SendEmailAlterPassword.sendEmail(user.getEmail(), VerificationCode);
                        Register_VerificationCode.put(user.getEmail(), VerificationCode);
                        SendMessage.setMessageType(ServerRespond.SEND_REGISTER_EMAIL_SUCCEED);
                    }
                    oos.writeObject(SendMessage);
                    System.out.println(user.getEmail()+"de验证码为:"+Register_VerificationCode.get(user.getEmail()));
                }


                /**
                 * 发送忘记密码验证码
                 */
                else if(messageType == ClientRequest.SEND_FORGET_EMAIL){
                    User user = (User) ReceiveMessage.getJavabean();
                    if(!UserAccountNumberAndEmailIsRight(user)){
                        SendMessage.setMessageType(ServerRespond.FORGET_EMAIL_IS_NOT_RIGHT);
                    }else if(Forget_VerificationCode.get(user.getEmail())!=null){
                        SendMessage.setMessageType(ServerRespond.SEND_FORGET_EMAIL_REPEAT);
                    }else {
                        String VerificationCode = CreatString.getCharAndNum(5);
                        SendEmailForget.sendEmail(user.getEmail(), VerificationCode);
                        Forget_VerificationCode.put(user.getEmail(), VerificationCode);
                        SendMessage.setMessageType(ServerRespond.SEND_FORGET_EMAIL_SUCCEED);
                        System.out.println(user.getEmail()+"de验证码为:"+ Forget_VerificationCode.get(user.getEmail()));
                    }
                    oos.writeObject(SendMessage);
                }


                /**
                 * 注册验证码失效
                 */
                else if(messageType == ClientRequest.REGISTER_VERIFICATION_OBSOLETE){
                    User user = (User) ReceiveMessage.getJavabean();
                    Register_VerificationCode.remove(user.getEmail());
                }


                /**
                 * 忘记密码验证码失效
                 */
                else if(messageType == ClientRequest.FORGET_VERIFICATION_OBSOLETE){
                    User user = (User) ReceiveMessage.getJavabean();
                    Forget_VerificationCode.remove(user.getEmail());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //如果服务端退出了while循环,说明服务器端不再监听了,需要关闭资源
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
