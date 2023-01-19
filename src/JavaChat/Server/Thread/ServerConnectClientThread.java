package JavaChat.Server.Thread;

import JavaChat.Common.Pojo.*;
import JavaChat.Common.Transfer.*;
import JavaChat.Common.Utils.*;
import JavaChat.Server.Dao.*;
import JavaChat.Server.Service.ServerMonitoring;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import static JavaChat.Server.Dao.DatabaseOperation_Forget.UserAccountNumberAndEmailIsRight;


/*
 *此类为服务端与客户端保持连接的线程
 */
public class ServerConnectClientThread extends Thread{
    //该线程持有socket(账号:Accounumber)
    private String AccountNumber;
    private Socket socket;
    private TransferMessage SendMessage = null;
    private MessageType messageType = null;
    TransferMessage ReceiveMessage = null;
    //构造器
    public ServerConnectClientThread(String AccountNumber, Socket socket) {
        this.AccountNumber = AccountNumber;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public static ObjectOutputStream getUserObjectOutPutStream(String UserAccountNumber){
        ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(UserAccountNumber);
        if(serverConnectClientThread==null){
            return null;
        }
        try {
            return new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public void ForwardTransferMessageToAllGroupMember(Object JavaBean, MessageType messageType, String Account){
        ObjectOutputStream objectOutputStream = getUserObjectOutPutStream(Account);
        if(objectOutputStream==null){
            System.out.println(Account+"不在线");
            return;
        }
        try {
            SendMessage.setJavabean(JavaBean);
            SendMessage.setMessageType(messageType);
            objectOutputStream.writeObject(SendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ForwardTransferMessageToAllGroupMember(Object JavaBean, MessageType messageType, Vector<GroupMember> Account){
        for (GroupMember groupMember:Account) {
            if(groupMember.getMemberAccountNumber().equals(AccountNumber)){
                continue;
            }
            String temp = groupMember.getMemberAccountNumber();
            ObjectOutputStream objectOutputStream = getUserObjectOutPutStream(temp);
            if(objectOutputStream==null){
                System.out.println(groupMember.getMemberAccountNumber()+"不在线");
                continue;
            }
            try {
                SendMessage.setJavabean(JavaBean);
                SendMessage.setMessageType(messageType);
                objectOutputStream.writeObject(SendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ForwardTransferMessageToAllAccount(Object JavaBean, MessageType messageType, Vector<String> Account){
        for (String s:Account) {
            if(s.equals(AccountNumber)){
                continue;
            }
            ObjectOutputStream objectOutputStream = getUserObjectOutPutStream(s);
            if(objectOutputStream==null){
                System.out.println(Account+"不在线");
                continue;
            }
            try {
                SendMessage.setJavabean(JavaBean);
                SendMessage.setMessageType(messageType);
                objectOutputStream.writeObject(SendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {
       boolean flag = true;
        while(true){
            try {
                ObjectInputStream ois = null;
                try {
                    System.out.println("等待"+AccountNumber+"的请求中...");
                    ois = new ObjectInputStream(socket.getInputStream());
                    System.out.println("等待"+AccountNumber+"的请求完毕");
                }catch (IOException e){
                    socket.close();
                    System.out.println("用户:"+AccountNumber + "异常退出");
                    ManageClientThreads.RemoveServerConnectionThread(AccountNumber);
                    Vector<String> vec = DatabaseOperation_FriendList.PullAllFriend(AccountNumber);
                    ForwardTransferMessageToAllAccount(AccountNumber,ServerRespond.OFFLINE_NOTIFICATION,vec);
                    break;
                }



                ReceiveMessage  = (TransferMessage) ois.readObject();
                messageType = ReceiveMessage.getMessageType();

                SendMessage = new TransferMessage();

                System.out.println("服务器获得账号为: "+AccountNumber+" 的内部指令:" + messageType);

                if(flag){
                    //将上线的消息转发给好友,这个flag是让这个语句只执行一次
                    Vector<String> vec = DatabaseOperation_FriendList.PullAllFriend(AccountNumber);
                    //Vector<User> FriendVector = DatabaseOperation_FriendList.ReturnFriendListInformation(vec);
                    ForwardTransferMessageToAllAccount(AccountNumber,ServerRespond.ONLINE_NOTIFICATION,vec);
                    flag = false;
                }

                if(false){}
                /**
                 * 退出登录
                 */
                else if(messageType== ClientRequest.EXIT_LOGIN){
                    User user = (User) ReceiveMessage.getJavabean();
                    ManageClientThreads.RemoveServerConnectionThread(user.getAccountNumber());
                    SendMessage.setJavabean(user);
                    SendMessage.setMessageType(ServerRespond.EXIT_LOGIN_SUCCEED);
                    System.out.println("用户 " + AccountNumber+" 已经退出成功!");
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(SendMessage);
                    //将下线的消息转发给好友
                    Vector<String> vec = DatabaseOperation_FriendList.PullAllFriend(AccountNumber);
                    //Vector<User> FriendVector = DatabaseOperation_FriendList.ReturnFriendListInformation(vec);
                    ForwardTransferMessageToAllAccount(AccountNumber,ServerRespond.OFFLINE_NOTIFICATION,vec);
                    //在这里要跳出循环,在这个时候就可以安全的在客户端断开socket了
                    break;
                }


                /**
                 * 发送文本消息
                 */
                else if(messageType== ClientRequest.SEND_TEXT_MESSAGE){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    //保存到数据库中
                    message.setId(DatabaseOperation_Message.SaveMessage(message));
                    ForwardTransferMessageToAllGroupMember(message,ServerRespond.SEND_TEXT_MESSAGE_SUCCEED_TO_PASSIVE,message.getReceive_User_Id());
                    ForwardTransferMessageToAllGroupMember(message,ServerRespond.SEND_TEXT_MESSAGE_SUCCEED_TO_ACTIVE,message.getSend_User_Id());
                }

                //发送图片消息
                else if(messageType == ClientRequest.SEND_IMAGE_MESSAGE){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    byte[] bytes = message.getBytes();
                    //将字节数组读入到服务器的磁盘当中
                    String Path = FileUtils.saveFile(bytes, ServerFileAddress.File,message.getFile().getName());
                    //将文件路径设置在message实例当中
                    message.setFilePathInServer(Path);
                    //将消息记录保存在mysql当中
                    message.setId(DatabaseOperation_Message.SaveMessage(message));
                    ForwardTransferMessageToAllGroupMember(message,ServerRespond.SEND_IMAGE_MESSAGE_SUCCEED_ACTIVE,AccountNumber);
                    ForwardTransferMessageToAllGroupMember(message,ServerRespond.SEND_IMAGE_MESSAGE_SUCCEED_PASSIVE,message.getReceive_User_Id());
                }

                //发送文件消息
                else if(messageType == ClientRequest.SEND_FILE_MESSAGE){
                   FragmentFile fragmentFile = (FragmentFile) ReceiveMessage.getJavabean();
                    //在服务器的路径
                    String path = ServerFileAddress.File+"\\"+CreatString.RemoveOther(AccountNumber+fragmentFile.getTime()+fragmentFile.getName());
                   if(fragmentFile.getCurrent()==1){
                       //开始时
                       Message message = new Message();
                       message.setMesType("文件消息");
                       message.setSend_User_Id(AccountNumber);
                       message.setReceive_User_Id(fragmentFile.getReceiver());
                       message.setSend_Time(fragmentFile.getTime());
                       message.setFileMessageState(FileMessageState.UNTREATED);
                       message.setFilePathInServer(path);
                       message.setFileName(fragmentFile.getName());
                       message.setId(DatabaseOperation_Message.SaveMessage(message));
                       fragmentFile.setId(message.getId());
                   }else{
                       fragmentFile.setId(DatabaseOperation_Message.getIdFromPath(path));
                   }
                    //字节数组->文件保存
                    FileUtils.saveFile(fragmentFile.getBytes(),path,((Integer)fragmentFile.getCurrent()).toString());
                    //让客户端改变进度条
                    ForwardTransferMessageToAllGroupMember(fragmentFile,ServerRespond.SEND_FILE_MESSAGE_TO_ACTIVE,AccountNumber);
                    //发完
                    if(fragmentFile.getCurrent()==fragmentFile.getTotal()){
                        //创message
                       Message message = new Message();
                       message.setId(DatabaseOperation_Message.getIdFromPath(path));
                       message.setMesType("文件消息");
                       message.setSend_User_Id(AccountNumber);
                       message.setReceive_User_Id(fragmentFile.getReceiver());
                       message.setSend_Time(fragmentFile.getTime());
                       message.setFileMessageState(FileMessageState.UNTREATED);
                       message.setFilePathInServer(path);
                       message.setFileName(fragmentFile.getName());
                       //被动
                       ForwardTransferMessageToAllGroupMember(message,ServerRespond.SEND_FILE_MESSAGE_TO_PASSIVE,fragmentFile.getReceiver());
                    }
                }


                /**
                 * 同意接受文件消息
                 */
                else if (messageType == ClientRequest.AGREE_FILE_MESSAGE){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    //路径
                    String path = message.getFilePathInServer();
                    //文件数量
                    int maxnum = FileUtils.numberOfFiles(path);
                    //最后一个
                    File lastfile = new File(path+"\\"+((Integer)maxnum).toString());
                    int id = DatabaseOperation_Message.getIdFromPath(path);
                    String name = DatabaseOperation_Message.getFileNameFromId(id);
                    byte[] lastbytes = FileUtils.FileChangeToByte(lastfile);
                    for (int i = 1; i <= maxnum; i++) {
                        File file1 = new File(path+"\\"+((Integer)i).toString());
                        byte[] newbytes = FileUtils.FileChangeToByte(file1);
                        FragmentFile fragmentFile = new FragmentFile();
                        fragmentFile.setBytes(newbytes);
                        fragmentFile.setName(name);
                        fragmentFile.setTime(message.getSend_Time());
                        fragmentFile.setCurrent(i);
                        fragmentFile.setTotal(maxnum);
                        fragmentFile.setLastBytesLength(lastbytes.length);
                        fragmentFile.setId(id);
                        fragmentFile.setSender(message.getSend_User_Id());
                        fragmentFile.setReceiver(AccountNumber);
                        ForwardTransferMessageToAllGroupMember(fragmentFile,ServerRespond.AGREE_FILE_MESSAGE_TO_ACTIVE,AccountNumber);
                        if(i==maxnum){
                            //当发送完最后一个字节数组以后告诉消息的发送着
                            Message message1 = message;
                            message1.setFileMessageState(FileMessageState.AGREE);
                            ForwardTransferMessageToAllGroupMember(message1,ServerRespond.AGREE_FILE_MESSAGE_TO_PASSIVE,message.getSend_User_Id());
                        }
                    }
                }


                /**
                 * 拒绝接受文件消息
                 */
                else if(messageType == ClientRequest.REJECT_FILE_MESSAGE){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    message.setFileMessageState(FileMessageState.REJECT);
                    //改变数据库中内容
                    DatabaseOperation_Message.UpdateFileMessageState(message);
                    ForwardTransferMessageToAllGroupMember(message,ServerRespond.REJECT_FILE_MESSAGE_TO_ACTIVE,message.getReceive_User_Id());
                    ForwardTransferMessageToAllGroupMember(message,ServerRespond.REJECT_FILE_MESSAGE_TO_PASSIVE,message.getSend_User_Id());
                }


                //拉取好友列表
                else if(messageType==ClientRequest.PULL_FRIEND_LIST){
                    User user = (User) ReceiveMessage.getJavabean();
                    Vector<String> vec = new Vector<>();
                    try {
                        vec = DatabaseOperation_FriendList.PullAllFriend(user.getAccountNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Vector<User> v = new Vector<>();
                    try {
                        v = DatabaseOperation_FriendList.ReturnFriendListInformation(vec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < v.size(); i++) {
                        if(CheckOneIsOnline(v.get(i).getAccountNumber())){
                            v.get(i).setOnline_Offline_State("在线");
                        }else{
                            v.get(i).setOnline_Offline_State("离线");
                        }
                    }
                    //将头像的字节数组加载到user对象当中
                    for (int i = 0; i < v.size(); i++) {
                        File file = new File(v.get(i).getHeadPortraitPath());
                        v.get(i).setHeadPortraitFile(file);
                        v.get(i).setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
                    }
                        SendMessage.setMessageType(ServerRespond.PULL_FRIEND_LIST_SUCCEED);
                        SendMessage.setJavabean(v);

                    //先拿到接收者的线程
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getServerConnectClientThread(user.getAccountNumber());
                    //再来拿oos
                    ObjectOutputStream oos =
                            new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(SendMessage);
                }

                /**
                 * 拉取与某人的消息
                 */
                else if(messageType==ClientRequest.PULL_MESSAGE_LIST){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    Vector<Message> messages = new Vector<>();
                    try {
                        messages = DatabaseOperation_Message.ExtractMessage(message.getSend_User_Id(),message.getReceive_User_Id());
                        for (int i = 0; i < messages.size(); i++) {
                            Message message1 = messages.get(i);
                            if(message1.getMesType().equals("文本消息") || message1.getMesType().equals("文件消息")){
                                continue;
                            }
                            File file = new File(message1.getFilePathInServer());
                            messages.get(i).setBytes(FileUtils.FileChangeToByte(file));
                            messages.get(i).setFile(file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Vector<Object> AllVector = new Vector<>();
                    AllVector.add(messages);
                    AllVector.add(message);
                    ForwardTransferMessageToAllGroupMember(AllVector,ServerRespond.PULL_MESSAGE_LIST_SUCCEED,AccountNumber);
                }


                /**
                 * 添加好友申请
                 */
                else if(messageType == ClientRequest.ADD_FRIEND_APPLICATION){
                    FriendApplication friendApplication = (FriendApplication) ReceiveMessage.getJavabean();
                    //不能发送给自己
                    if(friendApplication.getFriendApplicationSender().equals(friendApplication.getFriendApplicationReceiver())){
                        ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.ADD_FRIEND_APPLICATION_SEND_FAIL_MYSELF,AccountNumber);
                    }
                    //非好友关系的人才能添加好友
                    else if(DatabaseOperation_FriendApplication.CheckTwoIsFriend(friendApplication.getFriendApplicationSender(),friendApplication.getFriendApplicationReceiver())){
                        ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.ADD_FRIEND_APPLICATION_SEND_FAIL_HAVE_BE_FRIEND,AccountNumber);
                    }
                    //重复发送了好友申请
                    else if(DatabaseOperation_FriendApplication.CheckFriendApplication(friendApplication)==FriendApplicationState.UNTREATED){
                        ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.ADD_FRIEND_APPLICATION_SEND_FAIL_REPEAT,AccountNumber);
                    //可以发送申请
                    }else{
                        //存在数据库当中
                        friendApplication.setFriendApplicationState(FriendApplicationState.UNTREATED);
                        DatabaseOperation_FriendApplication.CreatFriendApplication(friendApplication);
                        //返回给接收者的客户端
                        ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.TRANSMIT_APPLICATION_TO_PASSIVE_SUCCEED,friendApplication.getFriendApplicationReceiver());
                        ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.TRANSMIT_APPLICATION_TO_ACTIVE_SUCCEED,friendApplication.getFriendApplicationSender());
                    }
                }


                /**
                 * 同意好友申请
                 * 主动者是这个好友申请的接收者,我要转发给这个好友申请的发送者一个消息:好友申请被同意了
                 */
                else if(messageType == ClientRequest.AGREE_FRIEND_APPLICATION){
                    FriendApplication friendApplication = (FriendApplication) ReceiveMessage.getJavabean();
                    //改变数据库中申请的状态
                    friendApplication.setFriendApplicationState(FriendApplicationState.AGREE);
                    DatabaseOperation_FriendApplication.UpdateFriendApplicationState(friendApplication);
                    DatabaseOperation_FriendApplication.CreatFriend(friendApplication);
                    Vector<Object> vector = new Vector<>();
                    vector.add(friendApplication);
                    User user = getUserAndHeadByte(friendApplication.getFriendApplicationSender());
                    if(CheckOneIsOnline(user.getAccountNumber())){
                        user.setOnline_Offline_State("在线");
                    }else{
                        user.setOnline_Offline_State("离线");
                    }
                    vector.add(user);
                    //转发给主动者
                    ForwardTransferMessageToAllGroupMember(vector,ServerRespond.AGREE_FRIEND_APPLICATION_TO_ACTIVE_SUCCEED,friendApplication.getFriendApplicationReceiver());
                    //转发给被动者
                    User user1 = getUserAndHeadByte(friendApplication.getFriendApplicationReceiver());
                    if(CheckOneIsOnline(user1.getAccountNumber())){
                        user1.setOnline_Offline_State("在线");
                    }else{
                        user1.setOnline_Offline_State("离线");
                    }
                    vector.set(1,user1);
                    ForwardTransferMessageToAllGroupMember(vector,ServerRespond.AGREE_FRIEND_APPLICATION_TO_PASSIVE_SUCCEED,friendApplication.getFriendApplicationSender());
                }


                /**
                 * 拒绝好友申请
                 * 拒绝好友申请的接收者是这个客户端消息的发送者,所以在这里主动方是这个好友申请的接收者
                 */
                else if(messageType == ClientRequest.REJECT_FRIEND_APPLICATION){
                    FriendApplication friendApplication = (FriendApplication) ReceiveMessage.getJavabean();
                    //拒绝好友申请的人是这次请求的发送者,是这个好友申请的接收者,我要转发给这个好友申请的发送者一个消息:好友申请被拒绝了
                    //改变数据库中申请的状态
                    friendApplication.setFriendApplicationState(FriendApplicationState.REJECT);
                    DatabaseOperation_FriendApplication.UpdateFriendApplicationState(friendApplication);
                    SendMessage.setJavabean(friendApplication);
                    //转发给被动者
                    ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.REJECT_GROUP_APPLICATION_TO_ACTIVE,friendApplication.getFriendApplicationReceiver());
                    //转发给主动者
                    ForwardTransferMessageToAllGroupMember(friendApplication,ServerRespond.REJECT_GROUP_APPLICATION_TO_PASSIVE_ADMINISTRATORS,friendApplication.getFriendApplicationSender());
                }


                /**
                 * 拉取好友申请列表
                 */
                else if(messageType == ClientRequest.PULL_ADD_FRIEND_APPLICATION_LIST){
                    Vector<Object> AllVector = new Vector<>();
                    Vector<User> vectorUser = new Vector<>();
                    User user = (User) ReceiveMessage.getJavabean();
                    String AccountNumber = user.getAccountNumber();
                    //在数据库中拉取好友申请列表(只需要获取到最新的申请即可)
                    HashMap<String,FriendApplication> hp = DatabaseOperation_FriendApplication.GetFriendApplicationList(AccountNumber);
                    Vector<FriendApplication> vectorFriendApplication = new Vector<>();
                    Iterator<Map.Entry<String, FriendApplication>> iterator = hp.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, FriendApplication> entry = iterator.next();
                        vectorFriendApplication.add(entry.getValue());
                        vectorUser.add(getUserAndHeadByte(entry.getValue().getFriendApplicationSender()));
                        vectorUser.add(getUserAndHeadByte(entry.getValue().getFriendApplicationReceiver()));
                    }
                    AllVector.add(vectorFriendApplication);
                    AllVector.add(vectorUser);
                    ForwardTransferMessageToAllGroupMember(AllVector,ServerRespond.PULL_ADD_FRIEND_APPLICATION_LIST_SUCCEED,AccountNumber);
                }


                /**
                 * 删除好友
                 */
                else if(messageType == ClientRequest.DELETE_FRIEND){
                    Friend friend = (Friend) ReceiveMessage.getJavabean();
                    //在数据库操作
                    DatabaseOperation_FriendApplication.DeleteFriendShip(friend);
                    ForwardTransferMessageToAllGroupMember(friend,ServerRespond.DELETE_FRIEND_SUCCEED_TO_ACTIVE,AccountNumber);
                    ForwardTransferMessageToAllGroupMember(friend,ServerRespond.DELETE_FRIEND_SUCCEED_TO_PASSIVE,friend.getFriend_AccountNumber());

                }


                /**
                 * 搜索好友
                 */
                else if(messageType == ClientRequest.SEARCH_USER){
                    User user= (User) ReceiveMessage.getJavabean();
                    Vector<User> v = null;
                    if(RegularExpression.CheckAccountNumber(user.getAccountNumber())){
                        v = DatabaseOperation_FriendApplication.SearchFriendTwo(user.getAccountNumber());
                    }else{
                        v = DatabaseOperation_FriendApplication.SearchFriend(user.getAccountNumber());
                    }
                    for (int i = 0; i < v.size(); i++){
                        if(v.get(i).getHeadPortraitPath()!=null && !"".equals(v.get(i).getHeadPortraitPath())){
                            String path = v.get(i).getHeadPortraitPath();
                            v.get(i).setHeadPortrait_byte(FileUtils.FileChangeToByte(new File(path)));
                            v.get(i).setHeadPortraitFile(new File(path));
                        }
                    }
                    ForwardTransferMessageToAllGroupMember(v,ServerRespond.SEARCH_USER_SUCCEED,AccountNumber);
                }


                /**
                 *修改个人信息
                 */
                else if(messageType == ClientRequest.ALTER_PERSON_INFORMATION){
                    User user = (User) ReceiveMessage.getJavabean();

                    if (user.getHeadPortraitFile()!=null) {
                        String path = FileUtils.saveFile(user.getHeadPortrait_byte(),ServerFileAddress.HeadPortrait,user.getHeadPortraitFile().getName());
                        user.setHeadPortraitPath(path);
                        user.setHeadPortrait_byte(FileUtils.FileChangeToByte(new File(path)));
                    }
                    DatabaseOperation_AlterPerson.UpdatePersonInformation(user);
                    SendMessage.setMessageType(ServerRespond.ALTER_PERSON_INFORMATION_SUCCEED);
                    SendMessage.setJavabean(user);
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getServerConnectClientThread(AccountNumber);
                    ObjectOutputStream oos =
                            new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(SendMessage);
                }

                /**
                 * 修改密码验证码
                 */
                else if(messageType == ClientRequest.SEND_ALTER_PASSWORD_EMAIL){
                    User user = (User) ReceiveMessage.getJavabean();
                    if(!UserAccountNumberAndEmailIsRight(user)){
                        SendMessage.setMessageType(ServerRespond.ALTER_PASSWORD_EMAIL_IS_NOT_RIGHT);
                    }else if(ServerMonitoring.AlterPassword.get(user.getEmail())!=null){
                        SendMessage.setMessageType(ServerRespond.SEND_ALTER_PASSWORD_EMAIL_REPEAT);
                    }else {
                        String VerificationCode = CreatString.getCharAndNum(5);
                        SendEmailForget.sendEmail(user.getEmail(), VerificationCode);
                        ServerMonitoring.AlterPassword.put(user.getEmail(), VerificationCode);
                        SendMessage.setMessageType(ServerRespond.SEND_ALTER_PASSWORD_EMAIL_SUCCEED);
                        System.out.println(user.getEmail()+"de验证码为:"+ ServerMonitoring.AlterPassword.get(user.getEmail()));
                    }
                    ObjectOutputStream oos = getUserObjectOutPutStream(AccountNumber);
                    oos.writeObject(SendMessage);
                }


                /**
                 * 修改密码
                 */
                else if(messageType == ClientRequest.Alter_PASSWORD){
                    User user = (User)ReceiveMessage.getJavabean();
                    String VerificationCode = ServerMonitoring.AlterPassword.get(user.getEmail());
                    SendMessage.setJavabean(user);
                    //检查验证码
                    if(VerificationCode==null){
                        SendMessage.setMessageType(ServerRespond.ALTER_PASSWORD_VERIFICATION_CODE_NOT_SEND);
                    }
                    else if(!VerificationCode.equals(user.getVerificationCode())){
                        SendMessage.setMessageType(ServerRespond.ALTER_PASSWORD_VERIFICATION_CODE_ERROR);
                    }
                    //去数据库查询账号与邮箱是否匹配
                    else if(UserAccountNumberAndEmailIsRight(user)){
                        //修改此账号在数据库中密码
                        DatabaseOperation_Forget.ResetAccountPassword(user);
                        System.out.println("账号:" + user.getAccountNumber() + "已经修改密码!");
                        SendMessage.setMessageType(ServerRespond.ALTER_PASSWORD_SUCCEED);
                    }else{
                        SendMessage.setMessageType(ServerRespond.ALTER_PASSWORD_EMAIL_IS_NOT_RIGHT);
                        System.out.println("账号:" + user.getAccountNumber() + "尝试修改密码但失败了!(原因:user中账号与email不匹配)");
                    }
                    //传给客户端
                    ObjectOutputStream oos = getUserObjectOutPutStream(AccountNumber);
                    oos.writeObject(SendMessage);
                    ServerMonitoring.AlterPassword.put(user.getEmail(),null);
                }


                /**
                 * 修改密码验证码过时
                 */
                else if(messageType == ClientRequest.Alter_PASSWORD_OBSOLETE){
                    User user = (User)ReceiveMessage.getJavabean();
                    ServerMonitoring.AlterPassword.remove(user.getEmail());
                }


                /**
                 * 创建群
                 */
                else if(messageType == ClientRequest.CREAT_GROUP){
                    Vector<Object> vector = (Vector<Object>) ReceiveMessage.getJavabean();
                    User user = (User) vector.get(0);
                    Group group = (Group) vector.get(1);
                    GroupMember groupMember = new GroupMember();
                    //生成群号
                    String GroupNumber = SnowFlake.GetGroupNumber();
                    group.setGroupNumber(GroupNumber);
                    //将头像保存至磁盘
                    String path = FileUtils.saveFile(group.getGroupHeadPortraitFileBytes(),ServerFileAddress.GroupHeadPortrait,group.getGroupHeadPortraitFile().getName());
                    group.setGroupHeadPortraitFilePath(path);
                    //修改数据库内容
                    group.setGroupMemberNum(1);
                    DatabaseOperation_GroupApplication.CreatGroup(group);
                    //修改这个成员在群中的身份
                    groupMember.setMemberIdentity(GroupIdentity.GROUP_LEADER);
                    groupMember.setMemberAccountNumber(user.getAccountNumber());
                    groupMember.setGroupName(group.getGroupName());
                    groupMember.setGroupNumber(group.getGroupNumber());
                    groupMember.setEnterTime(GetTimeUtils.GetNowTime());
                    groupMember.setMemberAccountNumber(AccountNumber);
                    //修改数据库中内容
                    DatabaseOperation_GroupApplication.InsertGroupMember(groupMember);
                    //将群号返回给客户端
                    ForwardTransferMessageToAllGroupMember(group,ServerRespond.CREAT_GROUP_SUCCEED,AccountNumber);
                }


                /**
                 * 加入群
                 */
                else if(messageType == ClientRequest.ENTER_GROUP){
                    GroupApplication groupApplication = (GroupApplication) ReceiveMessage.getJavabean();
                    //在群内就不能加入了
                    if(DatabaseOperation_GroupApplication.CheckUserIsInGroup(groupApplication.getGroupApplicationSender(),groupApplication.getGroupNumber())){
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.ENTER_GROUP_FAIL_HAVE_IN_GROUP,AccountNumber);
                    }
                    //重复发送了群申请
                    else if(DatabaseOperation_GroupApplication.CheckGroupApplication(groupApplication.getGroupApplicationSender(),groupApplication.getGroupNumber())){
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.ENTER_GROUP_FAIL_REPEAT,AccountNumber);
                    }
                    else{
                        //在数据库创建一个入群申请
                        groupApplication.setGroupApplicationState(GroupApplicationState.UNTREATED);
                        DatabaseOperation_GroupApplication.CreateGroupApplication(groupApplication);
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.ENTER_GROUP_SUCCEED_TO_ACTIVE,AccountNumber);
                        Vector<GroupMember> vector = DatabaseOperation_GroupApplication.GetGroupAllADMINISTRATORS(groupApplication.getGroupNumber());
                        //告诉管理员,让管理员去处理这个申请
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.ENTER_GROUP_SUCCEED_TO_PASSIVE_ADMINISTRATOR,vector);
                    }
                }


                /**
                 * 搜索群聊
                 */
                else if(messageType == ClientRequest.SEARCH_GROUP){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    Vector<Group> v = DatabaseOperation_SearchGroup.SearchGroup(group.getGroupNumber());
                    for (int i = 0; i < v.size(); i++) {
                        File file = new File(v.get(i).getGroupHeadPortraitFilePath());
                        v.get(i).setGroupHeadPortraitFile(file);
                        v.get(i).setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
                    }
                    ForwardTransferMessageToAllGroupMember(v,ServerRespond.SEARCH_GROUP_SUCCEED,AccountNumber);
                }



                /**
                 * 发送群文本消息
                 */
                else if(messageType == ClientRequest.SEND_GROUP_TEXT_MESSAGE){
                    GroupMessage groupMessage = (GroupMessage)ReceiveMessage.getJavabean();
                    //将消息保存至数据库
                    groupMessage.setId(DatabaseOperation_GroupMessage.SaveGroupMessage(groupMessage));
                    //先获得这个群的所有成员
                    Vector<GroupMember> vector = DatabaseOperation_GroupApplication.GetGroupAllGroupMember(groupMessage.getGroupNumber());
                    //方法重载,加入参数vector
                    ForwardTransferMessageToAllGroupMember(groupMessage,ServerRespond.SEND_GROUP_TEXT_MESSAGE_SUCCEED_TO_PASSIVE,vector);
                    ForwardTransferMessageToAllGroupMember(groupMessage,ServerRespond.SEND_GROUP_TEXT_MESSAGE_SUCCEED_TO_ACTIVE,AccountNumber);
                }

                /**
                 * '拉取群消息
                 */
                else if(messageType == ClientRequest.PULL_GROUP_MESSAGE_LIST){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    Vector<Object> AllVector = new Vector<>();
                    Vector<GroupMessage> MsgVector = DatabaseOperation_GroupMessage.ExtractGroupMessage(group.getGroupNumber());
                    Vector<User> UserVector = new Vector<>();
                    for (int i = 0; i < MsgVector.size(); i++) {
                        GroupMessage groupMessage = MsgVector.get(i);
                        if(DatabaseOperation_GroupApplication.CheckUserPassIsInGroup(groupMessage.getSenderAccountNumber(),group.getGroupNumber())){
                            User user = DatabaseOperation_Login.GetUserInformation(groupMessage.getSenderAccountNumber(),"...");
                            File file = new File(user.getHeadPortraitPath());
                            user.setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
                            user.setHeadPortraitFile(file);
                           UserVector.add(user);
                        }
                    }
                    AllVector.add(MsgVector);
                    AllVector.add(UserVector);
                    AllVector.add(group);
                    ForwardTransferMessageToAllGroupMember(AllVector,ServerRespond.PULL_GROUP_MESSAGE_SUCCEED,AccountNumber);
                }


                /**
                 * 拉取群列表
                 */
                else if(messageType == ClientRequest.PULL_GROUP_LIST){
                    User user = (User) ReceiveMessage.getJavabean();
                    Vector<Object> AllSet_vector = new Vector<>();
                    Vector<String> AccountSet = DatabaseOperation_GroupList.PullAllGroup(user.getAccountNumber());
                    //获取这个账号的所有群
                    Vector<Group> Group_vector = DatabaseOperation_GroupList.ReturnGroupListInformation(AccountSet);
                    for (int i = 0; i < Group_vector.size(); i++) {
                        File file = new File(Group_vector.get(i).getGroupHeadPortraitFilePath());
                        Group_vector.get(i).setGroupHeadPortraitFile(file);
                        Group_vector.get(i).setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
                    }
                    //0
                    AllSet_vector.add(Group_vector);
                    Vector<User> User_vector = new Vector<>();
                    //获取所有群群所有人的user对象
                    Vector<GroupMember> vectorGroupMember = new Vector<>();
                    for (int i = 0; i < Group_vector.size(); i++) {
                        String number = Group_vector.get(i).getGroupNumber();
                        User_vector.addAll(DatabaseOperation_GroupApplication.GetGroupAllUser(number));
                        vectorGroupMember.addAll(DatabaseOperation_GroupApplication.GetGroupAllGroupMember(number));
                    }
                    //1
                    AllSet_vector.add(User_vector);
                    //头像
                    for (int i = 0; i < User_vector.size(); i++) {
                        File file = new File(User_vector.get(i).getHeadPortraitPath());
                        User_vector.get(i).setHeadPortraitFile(file);
                        User_vector.get(i).setHeadPortraitFile(file);
                        User_vector.get(i).setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
                    }
                    //2
                    AllSet_vector.add(vectorGroupMember);

                    ForwardTransferMessageToAllGroupMember(AllSet_vector,ServerRespond.PULL_GROUP_LIST_SUCCEED,AccountNumber);
                }



                /**
                 * 拉取群申请applicaiton列表
                 */
                else if(messageType == ClientRequest.PULL_GROUP_APPLICATION_LIST){
                    User user = (User) ReceiveMessage.getJavabean();
                    Vector<Object> AllVector = new Vector<>();
                    Vector<GroupApplication> vectorGroupApplication = DatabaseOperation_GroupApplication.GetFriendApplicationList(user.getAccountNumber());
                    AllVector.add(vectorGroupApplication);
                    //获取到这些群的群头像放在group当中
                    HashMap<String,Group> hp = new HashMap<>();
                    Vector<Group> groups = new Vector<>();
                    for (int i = 0; i < vectorGroupApplication.size(); i++) {
                        String GroupNumber = vectorGroupApplication.get(i).getGroupNumber();
                        if(hp.get(GroupNumber)!=null){
                            continue;
                        }
                        Vector<String> vString = new Vector<>();
                        vString.add(GroupNumber);
                        Group group = DatabaseOperation_GroupList.ReturnGroupListInformation(vString).get(0);
                        File file = new File(group.getGroupHeadPortraitFilePath());
                        group.setGroupHeadPortraitFile(file);
                        group.setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
                        hp.put(GroupNumber,group);
                    }
                    groups.addAll(hp.values());
                    AllVector.add(groups);
                    ForwardTransferMessageToAllGroupMember(AllVector,ServerRespond.PULL_GROUP_APPLICATION_LIST_SUCCEED,AccountNumber);
                }


                /**
                 * 修改群数据资料
                 */
                else if(messageType == ClientRequest.ALTER_GROUP_DATA){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    GroupIdentity groupIdentity = DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(AccountNumber,group.getGroupNumber());
                    if(groupIdentity==GroupIdentity.GROUP_LEADER || groupIdentity==GroupIdentity.ADMINISTRATORS){
                        if (group.getGroupHeadPortraitFileBytes()!=null) {
                            String path = FileUtils.saveFile(group.getGroupHeadPortraitFileBytes(), ServerFileAddress.GroupHeadPortrait,group.getGroupHeadPortraitFile().getName());
                            group.setGroupHeadPortraitFilePath(path);
                        }
                        DatabaseOperation_AlterGroup.UpdateGroupInformation(group);
                        ForwardTransferMessageToAllGroupMember(group,ServerRespond.ALTER_GROUP_DATA_SUCCEED,AccountNumber);
                    }else{
                        ForwardTransferMessageToAllGroupMember(group,ServerRespond.ALTER_GROUP_DATA_FAIL,AccountNumber);
                    }
                }


                /**
                 * 同意群申请
                 */
                else if(messageType == ClientRequest.AGREE_GROUP_APPLICATION){
                    GroupApplication groupApplication = (GroupApplication) ReceiveMessage.getJavabean();
                    if(!CheckIsADMINISTRATORSOrLEADER(AccountNumber,groupApplication.getGroupNumber())){
                        //权限不足
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.GROUP_APPLICATION_INSUFFICIENT_PERMISSIONS,AccountNumber);
                    }else if(DatabaseOperation_GroupApplication.GetAllGroupApplicationByGroupNumber(groupApplication.getGroupNumber()).get(groupApplication.getGroupApplicationSender()).getGroupApplicationState()!=GroupApplicationState.UNTREATED){
                        //已经被处理过了
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.GROUP_APPLICATION_HAD_BEEN_PROCESSED,AccountNumber);
                    }
                    else{
                        Vector<Object> Allvector = new Vector<>();
                        groupApplication.setGroupApplicationState(GroupApplicationState.AGREE);
                        System.out.println("737:"+groupApplication.getGroupNumber()+" "+groupApplication.getGroupApplicationSender() +" "+ groupApplication.getGroupApplicationState());

                        DatabaseOperation_GroupApplication.UpdateGroupApplicationState(groupApplication);
                        Allvector.add(groupApplication);

                        Vector<String> vString = new Vector<>();
                        vString.add(groupApplication.getGroupNumber());
                        Group group = DatabaseOperation_GroupList.ReturnGroupListInformation(vString).get(0);
                        File file = new File(group.getGroupHeadPortraitFilePath());
                        group.setGroupHeadPortraitFile(file);
                        group.setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
                        Allvector.add(group);

                        GroupNotice groupNotice = new GroupNotice();
                        groupNotice.setGroupNumber(group.getGroupNumber());
                        groupNotice.setGroupNoticeType(GroupNoticeType.XX_ENTER);
                        groupNotice.setActiveUser(groupApplication.getGroupApplicationSender());
                        groupNotice.setTime(GetTimeUtils.GetNowTime());
                        DatabaseOperation_GroupNotice.CreatGroupNotice(groupNotice);
                        //找到这个群所有人的member
                        Vector<GroupMember> vector = DatabaseOperation_GroupApplication.GetGroupAllGroupMember(groupApplication.getGroupNumber());
                        Vector<GroupMember> vector1 = new Vector<>();
                        for (int i = 0; i < vector.size(); i++) {
                            GroupMember groupMember = vector.get(i);
                            if(!CheckIsADMINISTRATORSOrLEADER(groupMember.getMemberAccountNumber(),groupMember.getGroupNumber()) || groupMember.getMemberAccountNumber().equals(AccountNumber)){
                                continue;
                            }
                            vector1.add(groupMember);
                        }
                        //在groupmember中创建
                        GroupMember groupMember = new GroupMember();
                        groupMember.setGroupNumber(groupApplication.getGroupNumber());
                        groupMember.setMemberIdentity(GroupIdentity.ORDINARY_GROUP_MEMBERS);
                        groupMember.setMemberAccountNumber(groupApplication.getGroupApplicationSender());
                        groupMember.setEnterTime(GetTimeUtils.GetNowTime());
                        groupMember.setGroupName(group.getGroupName());
                        groupMember.setLink("1");
                        DatabaseOperation_GroupApplication.InsertGroupMember(groupMember);
                        //发给所有管理员
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.AGREE_GROUP_APPLICATION_TO_PASSIVE_ADMINISTRATORS,vector1);
                        //自己
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.AGREE_GROUP_APPLICATION_TO_ACTIVE,AccountNumber);
                        //申请加群的人
                        ForwardTransferMessageToAllGroupMember(Allvector,ServerRespond.AGREE_GROUP_APPLICATION_TO_PASSIVE_SENDER,groupApplication.getGroupApplicationSender());
                    }
                }


                /**
                 * 拒绝群申请
                 */
                else if(messageType == ClientRequest.REJECT_GROUP_APPLICATION){
                    GroupApplication groupApplication = (GroupApplication) ReceiveMessage.getJavabean();
                    if(!CheckIsADMINISTRATORSOrLEADER(AccountNumber,groupApplication.getGroupNumber())){
                        //权限不足
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.GROUP_APPLICATION_INSUFFICIENT_PERMISSIONS,AccountNumber);
                    }else if(DatabaseOperation_GroupApplication.GetAllGroupApplicationByGroupNumber(groupApplication.getGroupNumber()).get(groupApplication.getGroupApplicationSender()).getGroupApplicationState()!=GroupApplicationState.UNTREATED){
                        //已经被处理过了
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.GROUP_APPLICATION_HAD_BEEN_PROCESSED,AccountNumber);
                    }
                    else{
                        groupApplication.setGroupApplicationState(GroupApplicationState.REJECT);
                        DatabaseOperation_GroupApplication.UpdateGroupApplicationState(groupApplication);
                        //找到这个群所有人的member
                        Vector<GroupMember> vector = DatabaseOperation_GroupApplication.GetGroupAllGroupMember(groupApplication.getGroupNumber());
                        Vector<GroupMember> vector1 = new Vector<>();
                        for (int i = 0; i < vector.size(); i++) {
                            GroupMember groupMember = vector.get(i);
                            if(!CheckIsADMINISTRATORSOrLEADER(groupMember.getMemberAccountNumber(),groupMember.getGroupNumber()) || groupMember.getMemberAccountNumber().equals(AccountNumber)){
                                continue;
                            }
                            vector1.add(groupMember);
                        }
                        //发给所有管理员
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.REJECT_GROUP_APPLICATION_TO_PASSIVE_ADMINISTRATORS,vector1);
                        //自己
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.REJECT_GROUP_APPLICATION_TO_ACTIVE,AccountNumber);
                        //申请加群的人
                        ForwardTransferMessageToAllGroupMember(groupApplication,ServerRespond.REJECT_GROUP_APPLICATION_TO_PASSIVE_SENDER,groupApplication.getGroupApplicationSender());
                    }
                }


                /**
                 *设置管理员
                 */
                else if(messageType == ClientRequest.SET_GROUP_ADMINISTRATORS){
                    Vector<Object>  vector= (Vector<Object>) ReceiveMessage.getJavabean();
                    //需要2个user信息和一个group信息
                    User userSetUser = (User) vector.get(0);
                    User userBeSetUser = (User) vector.get(1);
                    Group group = (Group) vector.get(2);
                    GroupNotice groupNotice = new GroupNotice();
                    //检查这个人是否权限足够
                    GroupIdentity groupIdentity = DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(userSetUser.getAccountNumber(),group.getGroupNumber());
                    if(groupIdentity==GroupIdentity.GROUP_LEADER){
                        groupNotice.setActiveUser(userSetUser.getAccountNumber());
                        groupNotice.setPassiveUser(userBeSetUser.getAccountNumber());
                        groupNotice.setTime(GetTimeUtils.GetNowTime());
                        groupNotice.setGroupNoticeType(GroupNoticeType.XX_BE_XX_SET_ADMINISTRATOR);
                        groupNotice.setGroupNumber(group.getGroupNumber());
                        DatabaseOperation_GroupNotice.CreatGroupNotice(groupNotice);
                        GroupMember groupMember = new GroupMember();
                        groupMember.setGroupNumber(group.getGroupNumber());
                        groupMember.setMemberAccountNumber(userBeSetUser.getAccountNumber());
                        groupMember.setMemberIdentity(GroupIdentity.ADMINISTRATORS);

                        //修改数据库
                        System.out.println("777 "+groupMember.getGroupNumber()+" "+groupMember.getMemberAccountNumber());
                        DatabaseOperation_GroupApplication.UpdateGroupMemberIdentity(groupMember);
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.SET_GROUP_ADMINISTRATORS_SUCCEED_ACTIVE,AccountNumber);
                        Vector<GroupMember> vector1 = DatabaseOperation_GroupApplication.GetGroupAllADMINISTRATORS(group.getGroupNumber());
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.SET_GROUP_ADMINISTRATORS_SUCCEED_PASSIVE,vector1);
                    }else{
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.SET_GROUP_ADMINISTRATORS_FAIL,AccountNumber);
                    }
                }


                //取消群管理
                else if(messageType == ClientRequest.DISMISSAL_GROUP_ADMINISTRATORS){
                    Vector<Object>  vector= (Vector<Object>) ReceiveMessage.getJavabean();
                    //需要2个user信息和一个group信息
                    User userSetUser = (User) vector.get(0);
                    User userBeSetUser = (User) vector.get(1);
                    Group group = (Group) vector.get(2);
                    GroupNotice groupNotice = new GroupNotice();
                    //检查这个人是否权限足够
                    GroupIdentity groupIdentity = DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(userSetUser.getAccountNumber(),group.getGroupNumber());
                    if(groupIdentity==GroupIdentity.GROUP_LEADER){
                        groupNotice.setActiveUser(userSetUser.getAccountNumber());
                        groupNotice.setPassiveUser(userBeSetUser.getAccountNumber());
                        groupNotice.setTime(GetTimeUtils.GetNowTime());
                        groupNotice.setGroupNoticeType(GroupNoticeType.XX_GROUP_ADMINISTRATOR_BE_DISSOLUTION);
                        groupNotice.setGroupNumber(group.getGroupNumber());
                        DatabaseOperation_GroupNotice.CreatGroupNotice(groupNotice);
                        GroupMember groupMember = new GroupMember();
                        groupMember.setGroupNumber(group.getGroupNumber());
                        groupMember.setMemberAccountNumber(userBeSetUser.getAccountNumber());
                        groupMember.setMemberIdentity(GroupIdentity.ORDINARY_GROUP_MEMBERS);
                        //修改数据库
                        DatabaseOperation_GroupApplication.UpdateGroupMemberIdentity(groupMember);
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_SUCCEED_ACTIVE,AccountNumber);
                        Vector<GroupMember> vector1 = DatabaseOperation_GroupApplication.GetGroupAllADMINISTRATORS(group.getGroupNumber());
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_SUCCEED_PASSIVE,vector1);
                    }else{
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_FAIL,AccountNumber);
                    }
                }


                //踢人
                else if(messageType == ClientRequest.DELETE_GROUP_MEMBER){
                    Vector<Object>  vector= (Vector<Object>) ReceiveMessage.getJavabean();
                    //需要2个user信息和一个group信息
                    User userSetUser = (User) vector.get(0);
                    User userBeSetUser = (User) vector.get(1);
                    Group group = (Group) vector.get(2);
                    GroupNotice groupNotice = new GroupNotice();
                    //检查这个人是否权限足够
                    GroupIdentity groupIdentity = DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(userSetUser.getAccountNumber(),group.getGroupNumber());
                    if(groupIdentity==GroupIdentity.GROUP_LEADER || groupIdentity==GroupIdentity.ADMINISTRATORS){
                        groupNotice.setActiveUser(userSetUser.getAccountNumber());
                        groupNotice.setPassiveUser(userBeSetUser.getAccountNumber());
                        groupNotice.setTime(GetTimeUtils.GetNowTime());
                        groupNotice.setGroupNoticeType(GroupNoticeType.XX_REMOVE_XX);
                        groupNotice.setGroupNumber(group.getGroupNumber());
                        DatabaseOperation_GroupNotice.CreatGroupNotice(groupNotice);
                        GroupMember groupMember = new GroupMember();
                        groupMember.setGroupNumber(group.getGroupNumber());
                        groupMember.setMemberAccountNumber(userBeSetUser.getAccountNumber());
                        groupMember.setMemberIdentity(GroupIdentity.ORDINARY_GROUP_MEMBERS);
                        groupMember.setLink("0");
                        //修改数据库
                        DatabaseOperation_GroupApplication.UpdateGroupMemberLink(groupMember);
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.DELETE_GROUP_MEMBER_SUCCEED_ACTIVE,AccountNumber);
                        //<GroupMember> vector1 = DatabaseOperation_GroupApplication.GetGroupAllADMINISTRATORS(group.getGroupNumber());
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.DELETE_GROUP_MEMBER_SUCCEED_PASSIVE,userBeSetUser.getAccountNumber());
                    }else{
                        ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_FAIL,AccountNumber);
                    }
                }

                //解散群
                else if(messageType == ClientRequest.DISSOLUTION_GROUP){
                    Vector<Object>  vector= (Vector<Object>) ReceiveMessage.getJavabean();
                    //需要2个user信息和一个group信息
                    User userSetUser = (User) vector.get(0);
                    Group group = (Group) vector.get(1);
                    //检查这个人是否权限足够
                    GroupIdentity groupIdentity = DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(userSetUser.getAccountNumber(),group.getGroupNumber());
                    if(groupIdentity==GroupIdentity.GROUP_LEADER){
                        //修改数据库
                        //删除群
                        //DatabaseOperation_GroupApplication.DeleteGroup(group);
                        //删除群申请
                        //DatabaseOperation_GroupApplication.DeleteGroupApplication(group);
                        //删除群通知
                        //DatabaseOperation_GroupNotice.DeleteAllGroupNotice(group);
                        //删除所有群成员(包括那些已经入群的)
                        Vector<GroupMember> vector1 = DatabaseOperation_GroupApplication.GetGroupAllGroupMember(group.getGroupNumber());
                        for (int i = 0; i < vector1.size(); i++) {
                            GroupMember groupMember = vector1.get(i);
                            groupMember.setMemberIdentity(GroupIdentity.ORDINARY_GROUP_MEMBERS);
                            groupMember.setLink("0");
                            //修改数据库
                            DatabaseOperation_GroupApplication.UpdateGroupMemberLink(groupMember);
                        }
                        ForwardTransferMessageToAllGroupMember(group,ServerRespond.DISSOLUTION_GROUP_SUCCEED_ACTIVE,AccountNumber);
                        ForwardTransferMessageToAllGroupMember(group,ServerRespond.DISSOLUTION_GROUP_SUCCEED_PASSIVE,vector1);
                    }else{
                        ForwardTransferMessageToAllGroupMember(group,ServerRespond.DISSOLUTION_GROUP_FAIL,AccountNumber);
                    }
                }



                /**
                 * 退出群
                 */
                else if(messageType == ClientRequest.EXIT_GROUP){
                    Vector<Object> vector = (Vector<Object>) ReceiveMessage.getJavabean();
                    User user = (User) vector.get(0);
                    Group group = (Group) vector.get(1);
                    GroupMember groupMember = new GroupMember();
                    groupMember.setMemberAccountNumber(AccountNumber);
                    groupMember.setGroupNumber(group.getGroupNumber());
                    //身份重置
                    groupMember.setMemberIdentity(GroupIdentity.ORDINARY_GROUP_MEMBERS);
                    groupMember.setLink("0");
                    //更新数据库中link的值
                    DatabaseOperation_GroupApplication.UpdateGroupMemberLink(groupMember);
                    ForwardTransferMessageToAllGroupMember(group,ServerRespond.EXIT_GROUP_SUCCEED,AccountNumber);
                    GroupNotice groupNotice = new GroupNotice();
                    groupNotice.setActiveUser(user.getAccountNumber());
                    groupNotice.setTime(GetTimeUtils.GetNowTime());
                    groupNotice.setGroupNoticeType(GroupNoticeType.XX_EXIT);
                    groupNotice.setGroupNumber(group.getGroupNumber());
                    DatabaseOperation_GroupNotice.CreatGroupNotice(groupNotice);
                    ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.EXIT_GROUP_SUCCEED_TO_ACTIVE,AccountNumber);
                    Vector<GroupMember> vector1 = DatabaseOperation_GroupApplication.GetGroupAllADMINISTRATORS(group.getGroupNumber());
                    ForwardTransferMessageToAllGroupMember(groupNotice,ServerRespond.EXIT_GROUP_SUCCEED_TO_PASSIVE,vector1);
                }

                //拉取管理群需要的资源
                else if(messageType == ClientRequest.PULL_MANAGE_GROUP_RESOURCE){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    Vector<Object> AllVector = new Vector<>();
                    for (int i = 0; i < 6; i++) {
                        AllVector.add(new Object());
                    }
                    Vector<GroupMember> vector = DatabaseOperation_GroupApplication.GetGroupAllGroupMember(group.getGroupNumber());
                    //群成员
                    GroupMember groupMember_LEADER = new GroupMember();
                    Vector<GroupMember> groupMember_ADMINISTRATORS = new Vector<>();
                    Vector<GroupMember> groupMember_ORDINARY = new Vector<>();
                    for (int i = 0; i < vector.size(); i++) {
                        GroupMember groupMember = vector.get(i);
                        if(groupMember.getMemberIdentity()==GroupIdentity.GROUP_LEADER){
                            groupMember_LEADER = groupMember;
                        }else if(groupMember.getMemberIdentity()==GroupIdentity.ADMINISTRATORS){
                            groupMember_ADMINISTRATORS.add(groupMember);
                        }else if(groupMember.getMemberIdentity()==GroupIdentity.ORDINARY_GROUP_MEMBERS){
                            groupMember_ORDINARY.add(groupMember);
                        }
                        if(groupMember.getMemberAccountNumber().equals(AccountNumber)){
                            AllVector.set(0,groupMember);
                        }
                    }
                    //依次加入
                    AllVector.set(1,groupMember_LEADER);
                    AllVector.set(2,groupMember_ADMINISTRATORS);
                    AllVector.set(3,groupMember_ORDINARY);
                    Vector<User> vectorUser= DatabaseOperation_GroupApplication.GetGroupAllUser(group.getGroupNumber());
                    for (int i = 0; i < vectorUser.size(); i++) {
                        User user = vectorUser.get(i);
                        File file = new File(user.getHeadPortraitPath());
                        user.setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
                        user.setHeadPortraitFile(file);
                    }
                    AllVector.set(4,vectorUser);
                    Vector<Group> groups = DatabaseOperation_SearchGroup.SearchGroup(group.getGroupNumber());
                    for (int i = 0; i < groups.size(); i++) {
                        Group group1 = groups.get(i);
                        if(group1.getGroupNumber().equals(group.getGroupNumber())){
                            File file = new File(group1.getGroupHeadPortraitFilePath());
                            group1.setGroupHeadPortraitFile(file);
                            group1.setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
                            AllVector.set(5,group1);
                            break;
                        }
                    }
                    ForwardTransferMessageToAllGroupMember(AllVector,ServerRespond.PULL_MANAGE_GROUP_RESOURCE_SUCCEED,AccountNumber);
                }


                //拉取群通知
                else if(messageType == ClientRequest.PULL_GROUP_NOTICE){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    Vector<GroupNotice> vectorGroupNotice = DatabaseOperation_GroupNotice.PullAllGroupNotice(AccountNumber);
                    ForwardTransferMessageToAllGroupMember(vectorGroupNotice,ServerRespond.PULL_GROUP_NOTICE_SUCCEED,AccountNumber);
                }


                //拉取user
                else if(messageType == ClientRequest.PULL_USER_FROM_ACCOUNT){
                    User user = (User) ReceiveMessage.getJavabean();
                    user = DatabaseOperation_Login.GetUserInformation(user.getAccountNumber(),"...");
                    File file = new File(user.getHeadPortraitPath());
                    user.setHeadPortraitFile(file);
                    user.setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
                    ForwardTransferMessageToAllGroupMember(user,ServerRespond.PULL_USER_FROM_ACCOUNT_SUCCEED,AccountNumber);
                }

                //拉取表情
                else if(messageType == ClientRequest.PULL_EMOJI){
                    Vector<String> vector = DatabaseOperation_Emoji.FindAllEmoji(AccountNumber);
                    ForwardTransferMessageToAllGroupMember(vector,ServerRespond.PULL_EMOJI_SUCCEED,AccountNumber);
                }

                //添加表情
                else if(messageType == ClientRequest.ADD_EMOJI){
                    EmojiPojo emojiPojo = (EmojiPojo)ReceiveMessage.getJavabean();
                    DatabaseOperation_Emoji.CreatEmoji(emojiPojo);
                    ForwardTransferMessageToAllGroupMember(emojiPojo,ServerRespond.ADD_EMOJI_SUCCEED,AccountNumber);
                }

                //删除表情
                else if(messageType == ClientRequest.DELETE_EMOJI){
                    EmojiPojo emojiPojo = (EmojiPojo)ReceiveMessage.getJavabean();
                    DatabaseOperation_Emoji.DeleteEmoji(emojiPojo);
                    ForwardTransferMessageToAllGroupMember(emojiPojo,ServerRespond.DELETE_EMOJI_SUCCEED,AccountNumber);

                }

                else{
                    System.out.println("客户端发给服务端的"+messageType+"暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static User getUserAndHeadByte(String AccountNumber){
        User user = null;
        try {
            user = DatabaseOperation_Login.GetUserInformation(AccountNumber," ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(user.getHeadPortraitPath());
        try {
            user.setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
            user.setHeadPortraitFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setHeadPortraitFile(file);
        return user;
    }

    public static Group getGroupAndHeadByte (String GroupNumber){
        Group group = null;
        try {
            Vector s = new Vector<>();
            s.add(GroupNumber);
            Vector<Group> groups = DatabaseOperation_GroupList.ReturnGroupListInformation(s);
            group = groups.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(group.getGroupHeadPortraitFilePath());
        try {
            group.setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        group.setGroupHeadPortraitFile(file);
        return group;
    }

    public static boolean CheckOneIsOnline(String Account){
        if(ManageClientThreads.getServerConnectClientThread(Account)==null){
            return false;
        }else{
            return true;
        }
    }
    public static boolean CheckIsADMINISTRATORSOrLEADER(String account,String groupNumber){
        try {
            if(DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(account,groupNumber)==GroupIdentity.ADMINISTRATORS||DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(account,groupNumber)==GroupIdentity.GROUP_LEADER){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
