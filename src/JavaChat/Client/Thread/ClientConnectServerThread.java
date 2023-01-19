package JavaChat.Client.Thread;

import java.io.File;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import JavaChat.Client.Service.*;
import JavaChat.Client.View.*;
import JavaChat.Common.Pojo.*;
import JavaChat.Common.Transfer.*;
import JavaChat.Common.Utils.*;
import com.mysql.cj.xdevapi.JsonValue;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

/**
 * 该类负责开启与客户端通信的线程,接受一些登录以后的操作,比如退出登录成功,删除好友等等...
 */
public class ClientConnectServerThread extends Thread {
    //持有socket
    private Socket socket;
    //构造器,让这个对象持有一个socket
    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }
    //重写run方法
    @Override
    public void run() {
        while(true){
            try {
                //拿到socket的输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //读取流,服务器没有发送message则会卡在这
                TransferMessage ReceiveMessage = (TransferMessage)ois.readObject();
                MessageType messageType = ReceiveMessage.getMessageType();
                String time = GetTimeUtils.GetNowTime();
                if(messageType!=null) System.out.println(time+"客户端接收到内部消息:"+messageType);
                if(false){}//判断ReceiveMessage类型,做出相应的业务处理


                /**
                 * 退出登录是否成功
                 */
                else if(messageType == ServerRespond.EXIT_LOGIN_SUCCEED) {
                    User user = (User)ReceiveMessage.getJavabean();
                    ManageClientConnectServerThread.RemoveClientConnectServerThread(user.getAccountNumber());
                    //安全关闭这个对象持有的socket流
                    socket.close();
                    System.out.println("退出成功!");
                    break;
                }else if(ReceiveMessage.getMessageType() == ServerRespond.EXIT_LOGIN_FAIL){
                    System.out.println("退出失败");
                }


                /**
                 * 发送文本消息
                 */
                else if(messageType==ServerRespond.SEND_TEXT_MESSAGE_SUCCEED_TO_ACTIVE){
                    Message message = (Message)ReceiveMessage.getJavabean();
                    ChatListItem.ItemHashMap.put(message.getId(),new ChatListItem(message.getId()));
                    JavaFxUtils.AddToObservableList(MainCoreController.GetMessageObservableList(message.getReceive_User_Id()),message);
                    JavaFxUtils.scrollListViewToBottom((ListView) MainCoreView.$("ChatList_ListView"));
                }
                else if(messageType==ServerRespond.SEND_TEXT_MESSAGE_SUCCEED_TO_PASSIVE){
                    Message message = (Message)ReceiveMessage.getJavabean();
                    ChatListItem.ItemHashMap.put(message.getId(),new ChatListItem(message.getId()));
                    JavaFxUtils.AddToObservableList(MainCoreController.GetMessageObservableList(message.getSend_User_Id()),message);
                    //JavaFxUtils.scrollListViewToBottom((ListView) MainCoreView.$("ChatList_ListView"));
                    MusicPlayer.PlayAMessageMedia();
                }


                /**
                 *发送图片消息
                 */
                else if(messageType == ServerRespond.SEND_IMAGE_MESSAGE_SUCCEED_ACTIVE){
                    Message message = (Message)ReceiveMessage.getJavabean();
                    ChatListItem.ItemHashMap.put(message.getId(),new ChatListItem(message.getId()));
                    JavaFxUtils.AddToObservableList(MainCoreController.GetMessageObservableList(message.getReceive_User_Id()),message);
                    JavaFxUtils.scrollListViewToBottom((ListView) MainCoreView.$("ChatList_ListView"));
                }
                else  if(messageType == ServerRespond.SEND_IMAGE_MESSAGE_SUCCEED_PASSIVE){
                    Message message = (Message)ReceiveMessage.getJavabean();
                    ChatListItem.ItemHashMap.put(message.getId(),new ChatListItem(message.getId()));
                    JavaFxUtils.AddToObservableList(MainCoreController.GetMessageObservableList(message.getSend_User_Id()),message);
                    JavaFxUtils.scrollListViewToBottom((ListView) MainCoreView.$("ChatList_ListView"));
                    MusicPlayer.PlayAMessageMedia();
                }


                /**
                 * 文件消息
                 */
                else if(messageType==ServerRespond.SEND_FILE_MESSAGE_TO_ACTIVE){
                   FragmentFile fragmentFile =(FragmentFile) ReceiveMessage.getJavabean();
                   if(fragmentFile.getCurrent()==1){
                       Message message = new Message();
                       message.setSend_User_Id(MainCoreController.getAccountNumber());
                       message.setId(fragmentFile.getId());
                       message.setReceive_User_Id(fragmentFile.getReceiver());
                       message.setSend_Time(fragmentFile.getTime());
                       message.setMesType("文件消息");
                       message.setFileMessageState(FileMessageState.UNTREATED);
                       message.setFileName(fragmentFile.getName());
                       ChatListItem.ItemHashMap.put(message.getId(),new ChatListItem(message.getId()));
                       JavaFxUtils.AddToObservableList(MainCoreController.FriendChatRecord.get(fragmentFile.getReceiver()),message);
                       ChatListItem.hm.put(fragmentFile.getId(),new ProgressBar(0));
                   }
                    System.out.println("发送进度:"+fragmentFile.getCurrent()*1.0/fragmentFile.getTotal()+"  "+fragmentFile.getCurrent());
                    if(ChatListItem.hm.get(fragmentFile.getId())!=null)
                    JavaFxUtils.ChangeProgressBar(ChatListItem.hm.get(fragmentFile.getId()),fragmentFile.getTotal(),fragmentFile.getCurrent(),true);
                    if(fragmentFile.getCurrent()*1.0/fragmentFile.getTotal()==1){
                        JavaFxUtils.SetImageViewVisible(ChatListItem.ItemHashMap.get(fragmentFile.getId()).getFileFinished(),true);
                        JavaFxUtils.SetProgressVisible(ChatListItem.hm.get(fragmentFile.getId()),false);
                    }
                }
                else if(messageType==ServerRespond.SEND_FILE_MESSAGE_TO_PASSIVE){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    //更新Listview
                    ChatListItem.ItemHashMap.put(message.getId(),new ChatListItem(message.getId()));
                    JavaFxUtils.AddToObservableList(MainCoreController.FriendChatRecord.get(message.getSend_User_Id()),message);
                    ChatListItem.hm.put(message.getId(),new ProgressBar(0));
                    MusicPlayer.PlayAMessageMedia();
                }


                /**
                 * 同意接收
                 */
                else if(messageType == ServerRespond.AGREE_FILE_MESSAGE_TO_ACTIVE){
                    FragmentFile fragmentFile = (FragmentFile) ReceiveMessage.getJavabean();
                    if(fragmentFile.getCurrent()==1){
                        JavaFxUtils.SetImageViewVisible(ChatListItem.ItemHashMap.get(fragmentFile.getId()).getFileFinished(),false);
                    }
                    JavaFxUtils.ChangeProgressBar(ChatListItem.hm.get(fragmentFile.getId()),fragmentFile.getTotal(),fragmentFile.getCurrent(),true);
                    //创建一个文件夹地址
                    String path = ClientFileAddress.File + "\\" + CreatString.RemoveOther(fragmentFile.getSender() + fragmentFile.getTime() + fragmentFile.getName());
                    FileUtils.saveFile(fragmentFile.getBytes(),path,((Integer)fragmentFile.getCurrent()).toString());
                    //是最后一个
                   if(fragmentFile.getCurrent()==fragmentFile.getTotal()){
                       //将整合的字节数组转化为文件
                       FileUtils.loadFiles(path,path,fragmentFile.getName());
                       JavaFxUtils.SetProgressVisible(ChatListItem.hm.get(fragmentFile.getId()),false);
                       JavaFxUtils.SetImageViewVisible(ChatListItem.ItemHashMap.get(fragmentFile.getId()).getFileFinished(),true);

                   }
                }
                else if(messageType == ServerRespond.AGREE_FILE_MESSAGE_TO_PASSIVE){
                    //对方已经成功接收了
                    Message message = (Message) ReceiveMessage.getJavabean();
                    message.setFileMessageState(FileMessageState.AGREE);
                    //你已经拒绝接受了
                    int index = -1;
                    for (int i = 0; i < MainCoreController.GetMessageObservableList(message.getReceive_User_Id()).size() ; i++) {
                        Message message1 = MainCoreController.GetMessageObservableList(message.getReceive_User_Id()).get(i);
                        if(message.getId().equals(message1.getId())){
                            index = i;
                            break;
                        }
                    }
                    JavaFxUtils.ChangeObservableList(MainCoreController.GetMessageObservableList(message.getReceive_User_Id()),index,message);
                }


                /**
                 * 拒绝接收
                 */
                else if(messageType == ServerRespond.REJECT_FILE_MESSAGE_TO_ACTIVE){
                    Message message = (Message) ReceiveMessage.getJavabean();
                    message.setFileMessageState(FileMessageState.REJECT);
                    //你已经拒绝接受了
                    int index = -1;
                    for (int i = 0; i < MainCoreController.GetMessageObservableList(message.getReceive_User_Id()).size() ; i++) {
                        Message message1 = MainCoreController.GetMessageObservableList(message.getReceive_User_Id()).get(i);
                        if(message.getId().equals(message1.getId())){
                            index = i;
                            break;
                        }
                    }
                    JavaFxUtils.ChangeObservableList(MainCoreController.GetMessageObservableList(message.getReceive_User_Id()),index,message);
                }
                else if(messageType == ServerRespond.REJECT_FILE_MESSAGE_TO_PASSIVE){
                    //对方已经拒绝接受
                    Message message = (Message) ReceiveMessage.getJavabean();
                    message.setFileMessageState(FileMessageState.REJECT);
                    //你已经拒绝接受了
                    int index = -1;
                    for (int i = 0; i < MainCoreController.GetMessageObservableList(message.getReceive_User_Id()).size() ; i++) {
                        Message message1 = MainCoreController.GetMessageObservableList(message.getReceive_User_Id()).get(i);
                        if(message.getId().equals(message1.getId())){
                            index = i;
                            break;
                        }
                    }
                    JavaFxUtils.ChangeObservableList(MainCoreController.GetMessageObservableList(message.getReceive_User_Id()),index,message);
                }


                /**
                 * 拉取好友列表成功
                 */
                else if(messageType==ServerRespond.PULL_FRIEND_LIST_SUCCEED){
                    Vector<User> vec = (Vector<User>) ReceiveMessage.getJavabean();
                    Vector<Object> vecObject = (Vector<Object>) ReceiveMessage.getJavabean();
                    //好友头像
                    for (int i = 0; i < vec.size() ; i++) {
                        User user = vec.get(i);
                        MainCoreController.FriendHeadPortrait.put(user.getAccountNumber(),FileUtils.ByteToFile(user.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName()));
                    }
                    JavaFxUtils.AddToObservableList(MainCoreController.ChatFriend_ObservableList,vecObject);
                    for (int i = 0; i < vec.size(); i++) {
                        User user = vec.get(i);
                        MainCoreController.FriendChatRecord.put(user.getAccountNumber(),FXCollections.observableArrayList());
                        MessageService.PullMessage(MainCoreController.getAccountNumber(),vec.get(i).getAccountNumber());
                    }
                }

                /**
                 * 拉取消息记录
                 */
                else if(messageType==ServerRespond.PULL_MESSAGE_LIST_SUCCEED){
                    Vector<Object> AllVector = (Vector<Object>) ReceiveMessage.getJavabean();
                    Vector<Message> vec = (Vector<Message>) AllVector.get(0);
                    Vector<Object> Objectvec = (Vector<Object>) AllVector.get(0);
                    Message message1 = (Message) AllVector.get(1);
                    String AccountNumber = "";
                    if(message1.getSend_User_Id().equals(MainCoreController.getAccountNumber())){
                        AccountNumber = message1.getReceive_User_Id();
                    }else{
                        AccountNumber = message1.getSend_User_Id();
                    }
                    if (vec.size()!=0) {
                        for (int i = 0; i < vec.size(); i++) {
                            Message message = vec.get(i);
                            ChatListItem.ItemHashMap.put(vec.get(i).getId(),new ChatListItem(vec.get(i).getId()));
                        }
                        JavaFxUtils.AddToObservableList(MainCoreController.GetMessageObservableList(AccountNumber),Objectvec);
                    }else{

                    }
                }


                /**
                 *添加好友申请
                 */
                else if(messageType == ServerRespond.ADD_FRIEND_APPLICATION_SEND_FAIL_MYSELF){
                    JavaFxUtils.ShowStringOnLabel(SearchFriendView.$("AddFriendErrorTip_Label"),"你在添加谁看清了没??");
                }
                else if(messageType == ServerRespond.ADD_FRIEND_APPLICATION_SEND_FAIL_REPEAT){
                    JavaFxUtils.ShowStringOnLabel(SearchFriendView.$("AddFriendErrorTip_Label"),"添加失败,重复添加");
                }
                else if(messageType == ServerRespond.ADD_FRIEND_APPLICATION_SEND_FAIL_HAVE_BE_FRIEND){
                    JavaFxUtils.ShowStringOnLabel(SearchFriendView.$("AddFriendErrorTip_Label"),"已经是好友了~");
                }
                else if(messageType==ServerRespond.TRANSMIT_APPLICATION_TO_ACTIVE_SUCCEED){
                    FriendApplication friendApplication = (FriendApplication) ReceiveMessage.getJavabean();
                    System.out.println("好友申请已经发送");

                }
                else if(messageType==ServerRespond.TRANSMIT_APPLICATION_TO_PASSIVE_SUCCEED){
                    System.out.println("您收到了一封好友申请");
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("FriendRed_Circle"),true);
                }


                /**
                 * 拉取好友申请application列表
                 */
                else if(messageType==ServerRespond.PULL_ADD_FRIEND_APPLICATION_LIST_SUCCEED){
                   // HashMap<String,FriendApplication> hp = (HashMap<String, FriendApplication>)ReceiveMessage.getJavabean();
                    Vector<Object> AllVector = (Vector<Object>)ReceiveMessage.getJavabean();
                    Vector<Object> vec = (Vector<Object>) AllVector.get(0);
                    Vector<FriendApplication> vector = (Vector<FriendApplication>)AllVector.get(0);
                    Vector<User> users = (Vector<User>)AllVector.get(1);
                    for (int i = 0; i < users.size(); i++) {
                        User user = users.get(i);
                        MainCoreController.FriendHeadPortrait.put(user.getAccountNumber(),FileUtils.ByteToFile(user.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName()));
                    }
                   //FriendApplicationController.FriendApplication_Vector.addAll(vector);
                    JavaFxUtils.AddToObservableList(FriendApplicationController.FriendApplication_ObservableList, vec);

                }
                else if(messageType==ServerRespond.PULL_ADD_FRIEND_APPLICATION_LIST_FAIL){
                    System.out.println("拉取好友申请列表失败");
                }

                /**
                 * 删除好友
                 */
                else if(messageType == ServerRespond.DELETE_FRIEND_SUCCEED_TO_ACTIVE){
                    Friend friend = (Friend) ReceiveMessage.getJavabean();
                    for (int i = 0; i < MainCoreController.ChatFriend_ObservableList.size(); i++) {
                        int index = -1;
                        //拿到friend对象中删除你的的人
                        if (MainCoreController.ChatFriend_ObservableList.get(i).getAccountNumber().equals(friend.getFriend_AccountNumber())){
                            index = i;
                        }else{
                            continue;
                        }
                        User user = MainCoreController.ChatFriend_ObservableList.get(i);
                        JavaFxUtils.DeleteObservableList(MainCoreController.ChatFriend_ObservableList,index);
                    }
                    System.out.println("您已经成功删除好友:"+friend.getFriend_AccountNumber());

                }
                else if(messageType==ServerRespond.DELETE_FRIEND_SUCCEED_TO_PASSIVE){
                    Friend friend = (Friend) ReceiveMessage.getJavabean();
                    for (int i = 0; i < MainCoreController.ChatFriend_ObservableList.size(); i++) {
                        int index = -1;
                        //拿到friend对象中删除你的的人
                        if (MainCoreController.ChatFriend_ObservableList.get(i).getAccountNumber().equals(friend.getUser_AccountNumber())){
                            index = i;
                        }else{
                            continue;
                        }
                        User user = MainCoreController.ChatFriend_ObservableList.get(i);
                        JavaFxUtils.DeleteObservableList(MainCoreController.ChatFriend_ObservableList,index);
                    }
                }


                /**
                 * 同意好友申请
                 */
                else if(messageType == ServerRespond.AGREE_FRIEND_APPLICATION_TO_ACTIVE_SUCCEED){
                    Vector<Object> vector = (Vector<Object>)ReceiveMessage.getJavabean();
                    FriendApplication friendApplication = (FriendApplication) vector.get(0);
                    User user = (User)vector.get(1);
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("FriendRed_Circle"),true);
                    MainCoreController.FriendHeadPortrait.put(user.getAccountNumber(),FileUtils.ByteToFile(user.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName()));
                    JavaFxUtils.AddToObservableList(MainCoreController.ChatFriend_ObservableList,user);
                    MainCoreController.FriendChatRecord.put(user.getAccountNumber(), FXCollections.observableArrayList());
                    MessageService.PullMessage(MainCoreController.getAccountNumber(),user.getAccountNumber());
                }
                else if(messageType == ServerRespond.AGREE_FRIEND_APPLICATION_TO_PASSIVE_SUCCEED){
                    Vector<Object> vector = (Vector<Object>)ReceiveMessage.getJavabean();
                    FriendApplication friendApplication = (FriendApplication) vector.get(0);
                    User user = (User)vector.get(1);
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("FriendRed_Circle"),true);
                    JavaFxUtils.AddToObservableList(MainCoreController.ChatFriend_ObservableList,user);
                    MainCoreController.FriendChatRecord.put(user.getAccountNumber(), FXCollections.observableArrayList());
                    MessageService.PullMessage(MainCoreController.getAccountNumber(),user.getAccountNumber());
                }


                /**
                 * 好友上线
                 */
                else if(messageType == ServerRespond.ONLINE_NOTIFICATION){
                    String acc = (String) ReceiveMessage.getJavabean();
                    for (int i = 0; i < MainCoreController.ChatFriend_ObservableList.size(); i++) {
                        int index = -1;
                        if (MainCoreController.ChatFriend_ObservableList.get(i).getAccountNumber().equals(acc)){
                            index = i;
                        }else{
                            continue;
                        }
                        User user = MainCoreController.ChatFriend_ObservableList.get(i);
                        user.setOnline_Offline_State("在线");
                        JavaFxUtils.ChangeObservableList(MainCoreController.ChatFriend_ObservableList,index,user);
                    }
                }


                /**
                 *好友下线
                 */
                else if(messageType == ServerRespond.OFFLINE_NOTIFICATION){
                    String acc = (String) ReceiveMessage.getJavabean();
                        for (int i = 0; i < MainCoreController.ChatFriend_ObservableList.size(); i++) {
                            int index = -1;
                            if (MainCoreController.ChatFriend_ObservableList.get(i).getAccountNumber().equals(acc)){
                                index = i;
                            }else{
                                continue;
                            }
                            User user = MainCoreController.ChatFriend_ObservableList.get(i);
                            user.setOnline_Offline_State("离线");
                            JavaFxUtils.ChangeObservableList(MainCoreController.ChatFriend_ObservableList,index,user);
                        }
                }


                /**
                 * 拒绝好友申请
                 */
                else if(messageType == ServerRespond.REJECT_FRIEND_APPLICATION_TO_ACTIVE_SUCCEED){
                    FriendApplication friendApplication = (FriendApplication) ReceiveMessage.getJavabean();
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("FriendRed_Circle"),true);
                    System.out.println("您已拒绝"+friendApplication.getFriendApplicationSender()+"的好友申请");
                }
                else if(messageType == ServerRespond.REJECT_FRIEND_APPLICATION_TO_PASSIVE_SUCCEED){
                    FriendApplication friendApplication = (FriendApplication) ReceiveMessage.getJavabean();
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("FriendRed_Circle"),true);
                    System.out.println("您的好友申请被"+friendApplication.getFriendApplicationReceiver()+"拒绝了");
                }


                /**
                 * 搜索好友
                 */
                else if(messageType == ServerRespond.SEARCH_USER_SUCCEED){
                    Vector<User> v = (Vector<User>) ReceiveMessage.getJavabean();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            SearchFriendController.SearchFriend_ObservableList.addAll(v);
                        }
                    });
                }


                /**
                 * 修改个人资料
                 */
                else if(messageType == ServerRespond.ALTER_PERSON_INFORMATION_SUCCEED){
                    User user = (User) ReceiveMessage.getJavabean();
                    //将文件保存到本地,并得到路径
                    System.out.println("zh"+user.getAccountNumber());
                    if(user.getHeadPortrait_byte()!=null){
                        String path = null;
                        path = FileUtils.saveFile(user.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName());
                        //更新用户头像map
                        MainCoreController.FriendHeadPortrait.put(MainCoreController.getAccountNumber(),new File(path));
                        //更新主界面图片
                        JavaFxUtils.SetImageOnImageView(MainCoreView.$("Head_ImageView"),new File(path));
                        //FriendListService.PullFriendList(MainCoreController.getAccountNumber());

                    }
                    JavaFxUtils.ShowStringOnLabel(AlterPersonView.$("AlterPersonTip_Label"),"资料修改成功!");
                }else if(messageType == ServerRespond.ALTER_PERSON_INFORMATION_FAIL){
                    User user = (User) ReceiveMessage.getJavabean();
                    JavaFxUtils.ShowStringOnLabel(AlterPersonView.$("AlterPersonTip_Label"),"资料修改失败!");
                }

                /**
                 * 修改密码
                 */
                else if(messageType == ServerRespond.ALTER_PASSWORD_SUCCEED){
                    Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                JavaFxUtils.ShowTipDialogStage("重置密码","您的密码已经重置!");
                                ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("重置密码成功");
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //防止登录页面被关闭了
                                            if ((AlterPasswordView.$("AlterPasswordErrorTip_Label")!=null)) {
                                                ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("");
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            t.start();
                        }
                    });
                }
                else if(messageType == ServerRespond.ALTER_PASSWORD_EMAIL_IS_NOT_RIGHT){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("账号或邮箱错误");
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //防止登录页面被关闭了
                                            if ((AlterPasswordView.$("AlterPasswordErrorTip_Label")!=null)) {
                                                ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("");
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            t.start();
                        }
                    });
                }
                else if(messageType == ServerRespond.ALTER_PASSWORD_VERIFICATION_CODE_ERROR){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("验证码错误");
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //防止登录页面被关闭了
                                            if ((AlterPasswordView.$("AlterPasswordErrorTip_Label")!=null)) {
                                                ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("");
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            t.start();
                        }
                    });
                }
                else if(messageType == ServerRespond.ALTER_PASSWORD_VERIFICATION_CODE_NOT_SEND){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("验证码未发送");
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //防止登录页面被关闭了
                                            if ((AlterPasswordView.$("AlterPasswordErrorTip_Label")!=null)) {
                                                ((Label)(AlterPasswordView.$("AlterPasswordErrorTip_Label"))).setText("");
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            t.start();
                        }
                    });
                }


                /**
                 * 修改密码验证码
                 */
                else if (messageType == ServerRespond.SEND_ALTER_PASSWORD_EMAIL_REPEAT){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label)(AlterPasswordView.$("SendVerificationCodeErrorTip_Label"))).setText("您的验证码依然有效,请勿重复发送验证码");
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //防止登录页面被关闭了
                                            if ((AlterPasswordView.$("SendVerificationCodeErrorTip_Label")!=null)) {
                                                ((Label)(AlterPasswordView.$("SendVerificationCodeErrorTip_Label"))).setText("");
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            t.start();
                        }
                    });
                }
                else if(messageType == ServerRespond.SEND_ALTER_PASSWORD_EMAIL_SUCCEED){
                    User user = (User) ReceiveMessage.getJavabean();
                    //让改变javafx控件的代码在主线程中运行
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //先将view层的按钮设置一下
                            ((Button) (AlterPasswordView.$("SendVerificationCode_Button"))).setStyle(
                                    "-fx-background-color: #847d7d"
                            );
                            //按钮不可用
                            ((Button)AlterPasswordView.$("SendVerificationCode_Button")).setDisable(true);
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
                                        ((Button)AlterPasswordView.$("SendVerificationCode_Button")).setText("重新发送("+i[0].toString()+")");
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
                                ((Button)AlterPasswordView.$("SendVerificationCode_Button")).setStyle(
                                        "-fx-background-color: #17e7f2"
                                );
                                //按钮还原
                                ((Button)AlterPasswordView.$("SendVerificationCode_Button")).setDisable(false);
                                ((Button)AlterPasswordView.$("SendVerificationCode_Button")).setText("重新发送验证码");
                            }
                        });
                        TransferMessage SendMessage = new TransferMessage();
                        //让服务器删除这个验证码
                        SendMessage.setJavabean(user);
                        try {
                            TransferUtil.ClientInsideTransfer(SendMessage, ClientRequest.Alter_PASSWORD_OBSOLETE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    t.start(); // 启动新线程
                }else if(messageType == ServerRespond.ALTER_PASSWORD_EMAIL_IS_NOT_RIGHT){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((Label)(AlterPasswordView.$("SendVerificationCodeErrorTip_Label"))).setText("请检查邮箱是否正确");
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //防止登录页面被关闭了
                                            if ((AlterPasswordView.$("SendVerificationCodeErrorTip_Label")!=null)) {
                                                ((Label)(AlterPasswordView.$("SendVerificationCodeErrorTip_Label"))).setText("");
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            t.start();
                        }
                    });
                }


                /**
                 * 创建群
                 */
                else if(messageType == ServerRespond.CREAT_GROUP_SUCCEED){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    MainCoreController.GroupHeadPortrait.put(group.getGroupNumber(),FileUtils.ByteToFile(group.getGroupHeadPortraitFileBytes(),ClientFileAddress.GroupHeadPortrait,group.getGroupHeadPortraitFile().getName()));
                    GroupListService.PullGroupList(MainCoreController.getAccountNumber());
                    JavaFxUtils.ShowStringOnLabel(CreatGroupView.$("CreatGroupErrorTip_Label"),"创建成功!");
                }
                else if(messageType == ServerRespond.CREAT_GROUP_FAIL){
                    JavaFxUtils.ShowStringOnLabel(CreatGroupView.$("CreatGroupErrorTip_Label"),"创建失败!");
                }


                /**
                 * 搜索群
                 */
                else if(messageType == ServerRespond.SEARCH_GROUP_SUCCEED){
                    Vector<Group> vector = (Vector<Group>) ReceiveMessage.getJavabean();
                    SearchGroupController.SearchGroup_Vector.addAll(vector);
                    JavaFxUtils.ShowStringOnLabel(SearchGroupView.$("SearchErrorTip_Label"),"为您找到相关群:");
                }
                else if(messageType == ServerRespond.SEARCH_GROUP_FAIL){
                    JavaFxUtils.ShowStringOnLabel(SearchGroupView.$("SearchErrorTip_Label"),"");
                }


                /**
                 * 申请入群
                 */
                else if(messageType == ServerRespond.ENTER_GROUP_SUCCEED_TO_PASSIVE_ADMINISTRATOR){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                }
                else if(messageType == ServerRespond.ENTER_GROUP_SUCCEED_TO_ACTIVE){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                }
                else if(messageType == ServerRespond.ENTER_GROUP_FAIL_HAVE_IN_GROUP){
                    JavaFxUtils.ShowStringOnLabel(SearchGroupView.$("SearchErrorTip_Label"),"你已经在群里了");
                }
                else if(messageType == ServerRespond.ENTER_GROUP_FAIL_REPEAT){
                    JavaFxUtils.ShowStringOnLabel(SearchGroupView.$("SearchErrorTip_Label"),"已经发送过申请了,请等待群管理处理");
                }

                /**
                 * 修改群资料
                 */
                else if(messageType == ServerRespond.ALTER_GROUP_DATA_SUCCEED){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"修改成功!");
                    int index = -1;
                    for (int i = 0; i < MainCoreController.Group_ObservableList.size(); i++) {
                        Group group1 = MainCoreController.Group_ObservableList.get(i);
                        if(group1.getGroupNumber().equals(group.getGroupNumber())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        if (group.getGroupHeadPortraitFileBytes()!=null) {
                            MainCoreController.GroupHeadPortrait.put(group.getGroupNumber(),FileUtils.ByteToFile(group.getGroupHeadPortraitFileBytes(),ClientFileAddress.GroupHeadPortrait,group.getGroupHeadPortraitFile().getName()));
                        }
                        JavaFxUtils.ChangeObservableList(MainCoreController.Group_ObservableList,index,group);
                    }else{
                        System.out.println("你设置群:"+group.getGroupNumber()+"的群资料,但列表中不能查到此群->"+group.getGroupNumber());
                    }
                }
                else if(messageType ==  ServerRespond.ALTER_GROUP_DATA_FAIL){
                    JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"你的权限不够");
                    JavaFxUtils.HideOrShowButton(GroupManageView.$("Alter_Button"),false);
                }


                /**
                 * 拉取群列表
                 */
                else if(messageType == ServerRespond.PULL_GROUP_LIST_SUCCEED){
                    Vector<Object> AllSet_vector =  (Vector<Object>) ReceiveMessage.getJavabean();
                    Vector<Object> Group_vector = (Vector<Object>) AllSet_vector.get(0);
                    Vector<Group> Groupv = (Vector<Group>) AllSet_vector.get(0);
                    Vector<User> User_vector = (Vector<User>) AllSet_vector.get(1);
                    Vector<GroupMember> GroupMemberVector = (Vector<GroupMember>) AllSet_vector.get(2);
                    //群关系对照表
                    for (int i = 0; i < GroupMemberVector.size(); i++) {
                        GroupMember groupMember =GroupMemberVector.get(i);
                        MainCoreController.PutNewIdentity(groupMember.getMemberAccountNumber(),groupMember.getGroupNumber(),groupMember.getMemberIdentity().toChinese());
                        MainCoreController.PutNewIdentity(groupMember.getMemberAccountNumber(),groupMember.getGroupNumber(),groupMember.getMemberIdentity().toChinese());
                    }
                    //群头像
                    for (Group temp:Groupv) {
                        MainCoreController.GroupHeadPortrait.put(temp.getGroupNumber(),
                                new File(FileUtils.saveFile(temp.getGroupHeadPortraitFileBytes(), ClientFileAddress.GroupHeadPortrait,temp.getGroupHeadPortraitFile().getName())));
                    }
                    //将群里的人的头像添加至头像map
                    for (int i = 0; i < User_vector.size(); i++) {
                        User user = User_vector.get(i);
                        MainCoreController.FriendHeadPortrait.put(user.getAccountNumber(),new File(FileUtils.saveFile(user.getHeadPortrait_byte(), ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName())));
                        MainCoreController.GroupUser.put(user.getAccountNumber(),user);
                    }
                    //将group添加至数据源
                    JavaFxUtils.AddToObservableList(MainCoreController.Group_ObservableList,Group_vector);
                    //群消息的map
                    for (int i = 0; i < Groupv.size(); i++) {
                        Group  group = Groupv.get(i);
                        MainCoreController.GroupChatRecord.put(group.getGroupNumber(),FXCollections.observableArrayList());
                    }
                    //拉取一下群聊消息
                    for (int i = 0; i < Groupv.size(); i++) {
                        Group group = Groupv.get(i);
                        GroupMessageService.PullGroupMessage(group.getGroupNumber());
                    }
                }
                else if(messageType == ServerRespond.PULL_GROUP_LIST_FAIL){

                }


                /**
                 * 发送群文本消息
                 */
                else if(messageType == ServerRespond.SEND_GROUP_TEXT_MESSAGE_SUCCEED_TO_ACTIVE){
                    GroupMessage groupMessage = (GroupMessage) ReceiveMessage.getJavabean();
                    JavaFxUtils.AddToObservableList(MainCoreController.GetGroupMessageObservableList(groupMessage.getGroupNumber()),groupMessage);
                    JavaFxUtils.scrollListViewToBottom((ListView) MainCoreView.$("GroupChatList_ListView"));
                }
                else if(messageType == ServerRespond.SEND_GROUP_TEXT_MESSAGE_SUCCEED_TO_PASSIVE){
                    GroupMessage groupMessage = (GroupMessage) ReceiveMessage.getJavabean();
                    JavaFxUtils.AddToObservableList(MainCoreController.GetGroupMessageObservableList(groupMessage.getGroupNumber()),groupMessage);
                    MusicPlayer.PlayAMessageMedia();
                }


                /**
                 * 拉取群消息
                 */
                else if(messageType == ServerRespond.PULL_GROUP_MESSAGE_SUCCEED){
                    Vector<Object> AllVector = (Vector<Object>) ReceiveMessage.getJavabean();
                    Vector<GroupMessage> vectorMsg = (Vector<GroupMessage>)AllVector.get(0);
                    Vector<Object> vector = (Vector<Object>)AllVector.get(0);
                    Vector<User> vectorUser = (Vector<User>) AllVector.get(1);
                    Group group = (Group) AllVector.get(2);
                    for (int i = 0; i < vectorUser.size(); i++) {
                        //插入这些不是这个人但是有聊天记录的人的头像
                        User user = vectorUser.get(i);
                        MainCoreController.FriendHeadPortrait.put(user.getAccountNumber(),new File(FileUtils.saveFile(user.getHeadPortrait_byte(),ClientFileAddress.HeadPortrait,user.getHeadPortraitFile().getName())));
                        MainCoreController.PutNewIdentity(user.getAccountNumber(),vectorMsg.get(0).getGroupNumber(),"不是群成员");
                    }
                    if (vector.size()!=0){
                        MainCoreController.GroupChatRecord.putIfAbsent(group.getGroupNumber(),FXCollections.observableArrayList());
                        JavaFxUtils.AddToObservableList(MainCoreController.GroupChatRecord.get(group.getGroupNumber()),vector);
                    }else{
                    }
                }
                else if(messageType == ServerRespond.PULL_GROUP_MESSAGE_FAIL){
                }


                /**
                 * 拉取群申请列表
                 */
                else if(messageType == ServerRespond.PULL_GROUP_APPLICATION_LIST_SUCCEED){
                    Vector<Object> AllVector = (Vector<Object>) ReceiveMessage.getJavabean();
                    Vector<Object> vectorGroupApplication =(Vector<Object>) AllVector.get(0);
                    Vector<Group> vectorGroup =(Vector<Group>) AllVector.get(1);
                    for (int i = 0; i < vectorGroup.size(); i++) {
                        Group group = vectorGroup.get(i);
                        MainCoreController.GroupHeadPortrait.put(group.getGroupNumber(),FileUtils.ByteToFile(group.getGroupHeadPortraitFileBytes(),ClientFileAddress.GroupHeadPortrait,group.getGroupHeadPortraitFile().getName()));
                    }
                    JavaFxUtils.AddToObservableList(GroupApplicationController.GroupApplication_ObservableList,vectorGroupApplication);
                }
                else if(messageType == ServerRespond.PULL_GROUP_APPLICATION_LIST_FAIL){

                }


                /**
                 * 同意群申请
                 */
                else if(messageType == ServerRespond.AGREE_GROUP_APPLICATION_TO_ACTIVE){
                    GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                    GroupApplication groupApplication = (GroupApplication) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupApplication.getGroupApplicationSender(),groupApplication.getGroupNumber(),GroupIdentity.ORDINARY_GROUP_MEMBERS.toChinese());
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                    GroupMessageService.PullGroupMessage(groupApplication.getGroupNumber());
                }
                else if(messageType == ServerRespond.AGREE_GROUP_APPLICATION_TO_PASSIVE_ADMINISTRATORS){
                    GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                    GroupApplication groupApplication = (GroupApplication) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupApplication.getGroupApplicationSender(),groupApplication.getGroupNumber(),GroupIdentity.ORDINARY_GROUP_MEMBERS.toChinese());
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                    GroupMessageService.PullGroupMessage(groupApplication.getGroupNumber());
                }else if(messageType == ServerRespond.AGREE_GROUP_APPLICATION_TO_PASSIVE_SENDER){
                    //刷新群列表
                    GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                    Vector<Object> AllVector = (Vector<Object>) ReceiveMessage.getJavabean();
                    GroupApplication groupApplication =(GroupApplication) AllVector.get(0);
                    Group group = (Group) AllVector.get(1);
                    //MainCoreController.PutNewIdentity(groupApplication.getGroupApplicationSender(),groupApplication.getGroupNumber(),GroupIdentity.ORDINARY_GROUP_MEMBERS.toChinese());
                    MainCoreController.GroupHeadPortrait.put(groupApplication.getGroupNumber(),FileUtils.ByteToFile(group.getGroupHeadPortraitFileBytes(),ClientFileAddress.GroupHeadPortrait,group.getGroupHeadPortraitFile().getName()));
                    //JavaFxUtils.AddToObservableList(MainCoreController.Group_ObservableList,group);
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                    GroupMessageService.PullGroupMessage(group.getGroupNumber());
                }


                /**
                 * 拒绝群申请
                 */
                else if(messageType == ServerRespond.REJECT_GROUP_APPLICATION_TO_ACTIVE){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                }
                else if(messageType == ServerRespond.REJECT_GROUP_APPLICATION_TO_PASSIVE_ADMINISTRATORS){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                }
                else if(messageType == ServerRespond.REJECT_GROUP_APPLICATION_TO_PASSIVE_SENDER){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupRed_Circle"),true);
                }


                //以上2个操作权限不足
                else if(messageType == ServerRespond.GROUP_APPLICATION_INSUFFICIENT_PERMISSIONS){
                    JavaFxUtils.ShowStringOnLabel(GroupApplicationView.$("ErrorTip_Label"),"你的权限不足");
                }
                //以上2个操作已经被管理员或者群主处理过了
                else if(messageType == ServerRespond.GROUP_APPLICATION_HAD_BEEN_PROCESSED){
                    JavaFxUtils.ShowStringOnLabel(GroupApplicationView.$("ErrorTip_Label"),"此申请已经被处理过了");
                }
                /**
                 *拉取管理群需要的资源
                 */
                else if(messageType == ServerRespond.PULL_MANAGE_GROUP_RESOURCE_SUCCEED){
                    Vector<Object> AllVector = (Vector<Object>) ReceiveMessage.getJavabean();
                    GroupManageController.GroupMember_Myself = (GroupMember) AllVector.get(0);
                    Object groupMember_LEADER = AllVector.get(1);
                    Vector<Object> groupMember_ADMINSTRATORS = (Vector<Object>) AllVector.get(2);
                    Vector<Object> groupMember_ORDINARY = (Vector<Object>) AllVector.get(3);
                    Vector<Object> vectorUser = (Vector<Object>) AllVector.get(4);
                    Vector<User> vectorUser2 = (Vector<User>) AllVector.get(4);
                    GroupManageController.group = (Group) AllVector.get(5);
                    JavaFxUtils.AddToObservableList(GroupManageController.GroupMemberLIst_ObservableList,groupMember_LEADER);
                    if(groupMember_ADMINSTRATORS.size()!=0)
                    JavaFxUtils.AddToObservableList(GroupManageController.GroupMemberLIst_ObservableList,groupMember_ADMINSTRATORS);
                    if(groupMember_ORDINARY.size()!=0)
                    JavaFxUtils.AddToObservableList(GroupManageController.GroupMemberLIst_ObservableList,groupMember_ORDINARY);
                    for (int i = 0; i < vectorUser2.size(); i++) {
                        User user = vectorUser2.get(i);
                        GroupManageController.UserHp.put(user.getAccountNumber(), user);
                    }
                    JavaFxUtils.ShowStringOnLabelForever(GroupManageView.$("Numbers_Label"),((Integer)(1+groupMember_ORDINARY.size()+groupMember_ADMINSTRATORS.size())).toString());
                    JavaFxUtils.ShowStringOnTextFieldForever(GroupManageView.$("GroupName_TextField"),GroupManageController.group.getGroupName());
                    JavaFxUtils.ShowStringOnTextAreaForever(GroupManageView.$("GroupAnnouncement_TextArea"),GroupManageController.group.getGroupAnnouncement());
                    JavaFxUtils.ShowStringOnLabelForever(GroupManageView.$("GroupNumber_Label"),GroupManageController.group.getGroupNumber());
                    File file = FileUtils.ByteToFile(GroupManageController.group.getGroupHeadPortraitFileBytes(),ClientFileAddress.GroupHeadPortrait,GroupManageController.group.getGroupHeadPortraitFile().getName());
                    JavaFxUtils.SetImageOnImageView(GroupManageView.$("HeadPortrait_ImageView"),file);
                    if(GroupManageController.GroupMember_Myself.getMemberIdentity() == GroupIdentity.ORDINARY_GROUP_MEMBERS){
                        JavaFxUtils.HideOrShowButton(GroupManageView.$("AlterHead_Button"),false);
                    }
                }

                //拉取群通知
                else if(messageType == ServerRespond.PULL_GROUP_NOTICE_SUCCEED){
                    Vector<Object> vector = (Vector<Object>)ReceiveMessage.getJavabean();
                    JavaFxUtils.AddToObservableList(GroupNoticeController.GroupNotice_ObservableList,vector);

                }else if(messageType == ServerRespond.PULL_GROUP_NOTICE_FAIL){

                }


                //设置管理员
                else if(messageType == ServerRespond.SET_GROUP_ADMINISTRATORS_SUCCEED_ACTIVE){
                    GroupNotice groupNotice = (GroupNotice) ReceiveMessage.getJavabean();
                    GroupMember groupMember = new GroupMember();
                    groupMember.setMemberAccountNumber(groupNotice.getPassiveUser());
                    groupMember.setMemberIdentity(GroupIdentity.ADMINISTRATORS);
                    groupMember.setLink("1");
                    groupMember.setGroupNumber(groupNotice.getGroupNumber());
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    int index = -1;
                    for (int i = 0; i < GroupManageController.GroupMemberLIst_ObservableList.size(); i++) {
                        GroupMember groupMember1 = GroupManageController.GroupMemberLIst_ObservableList.get(i);
                        if(groupMember1.getMemberAccountNumber().equals(groupNotice.getPassiveUser())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        JavaFxUtils.ChangeObservableList(GroupManageController.GroupMemberLIst_ObservableList,index,groupMember);
                    }else{
                        System.out.println("你设置了"+groupNotice.getPassiveUser()+"管理员,但列表中不能查到此人->"+groupNotice.getPassiveUser());
                    }
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),GroupIdentity.ADMINISTRATORS.toChinese());
                    //GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                }
                else if(messageType == ServerRespond.SET_GROUP_ADMINISTRATORS_SUCCEED_PASSIVE){
                    GroupNotice groupNotice = (GroupNotice) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),GroupIdentity.ADMINISTRATORS.toChinese());
                    GroupMember groupMember = new GroupMember();
                    groupMember.setMemberAccountNumber(groupNotice.getPassiveUser());
                    groupMember.setMemberIdentity(GroupIdentity.ADMINISTRATORS);
                    groupMember.setLink("1");
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                }
                else if(messageType == ServerRespond.SET_GROUP_ADMINISTRATORS_FAIL){
                    JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"你的权限不够");
                }


                //撤销管理
                else if(messageType == ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_SUCCEED_ACTIVE){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    GroupNotice groupNotice = (GroupNotice) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),GroupIdentity.ORDINARY_GROUP_MEMBERS.toChinese());
                    GroupMember groupMember = new GroupMember();
                    groupMember.setMemberAccountNumber(groupNotice.getPassiveUser());
                    groupMember.setMemberIdentity(GroupIdentity.ORDINARY_GROUP_MEMBERS);
                    groupMember.setLink("1");
                    groupMember.setGroupNumber(groupNotice.getGroupNumber());
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    int index = -1;
                    for (int i = 0; i < GroupManageController.GroupMemberLIst_ObservableList.size(); i++) {
                        GroupMember groupMember1 = GroupManageController.GroupMemberLIst_ObservableList.get(i);
                        if(groupMember1.getMemberAccountNumber().equals(groupNotice.getPassiveUser())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        JavaFxUtils.ChangeObservableList(GroupManageController.GroupMemberLIst_ObservableList,index,groupMember);
                    }else{
                        System.out.println("你撤销了"+groupNotice.getPassiveUser()+"管理员,但不能在列表中查到此人->"+groupNotice.getPassiveUser());
                    }
                    //GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                }
                else if(messageType == ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_SUCCEED_PASSIVE){
                    GroupNotice groupNotice = (GroupNotice) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),GroupIdentity.ADMINISTRATORS.toChinese());
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    //GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                }
                else if(messageType == ServerRespond.DISMISSAL_GROUP_ADMINISTRATORS_FAIL){
                    JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"你的权限不够");
                }


                /**
                 * 删除群成员,踢人
                 */
                else if(messageType == ServerRespond.DELETE_GROUP_MEMBER_SUCCEED_ACTIVE){
                    GroupNotice groupNotice = (GroupNotice) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),"不是群成员");
                    int index = -1;
                    for (int i = 0; i < GroupManageController.GroupMemberLIst_ObservableList.size(); i++) {
                        GroupMember groupMember = GroupManageController.GroupMemberLIst_ObservableList.get(i);
                        if(groupMember.getMemberAccountNumber().equals(groupNotice.getPassiveUser())){
                            index = i;
                            break;
                        }
                    }

                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    //GroupApplicationService.PullGroupList(MainCoreController.getAccountNumber());
                    GroupManageService.getGroupMangeInformation(groupNotice.getgroupNumber());
                }
                else if(messageType == ServerRespond.DELETE_GROUP_MEMBER_SUCCEED_PASSIVE){
                    GroupNotice groupNotice = (GroupNotice) ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),"不是群成员");
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                    int index = -1;
                    for (int i = 0; i < MainCoreController.Group_ObservableList.size(); i++) {
                        Group group1 = MainCoreController.Group_ObservableList.get(i);
                        if(group1.getGroupNumber().equals(groupNotice.getGroupNumber())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        JavaFxUtils.DeleteObservableList(MainCoreController.Group_ObservableList, index);
                    }else{
                        System.out.println("某人已经将你从群中踢出,但你的列表里面没有此群->"+groupNotice.getGroupNumber());
                    }
                    //如果这个时候被踢的人正好打开群聊天框
                    if(groupNotice.getGroupNumber().equals(MainCoreController.GroupChatTarget)){
                        //此时关闭被踢者的聊天框
                        JavaFxUtils.HideOrShowRectangle(MainCoreView.$("HideOrShowRectangle"),true);
                        MainCoreController.GroupChatTarget = null;
                        MainCoreController.GroupChatRecord.remove(groupNotice.getGroupNumber());
                    }
                }
                else if(messageType == ServerRespond.DELETE_GROUP_MEMBER_FAIL){
                    JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"你的权限不够");
                }

                //解散群
                else if(messageType == ServerRespond.DISSOLUTION_GROUP_SUCCEED_ACTIVE){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    int index = -1;
                    for (int i = 0; i < MainCoreController.Group_ObservableList.size(); i++) {
                        Group group1 = MainCoreController.Group_ObservableList.get(i);
                        if(group1.getGroupNumber().equals(group.getGroupNumber())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        JavaFxUtils.DeleteObservableList(MainCoreController.Group_ObservableList,index);
                    }else{

                    }
                    //如果这个时候被踢的人正好打开群聊天框
                    if(group.getGroupNumber().equals(MainCoreController.GroupChatTarget)){
                        //此时关闭被踢者的聊天框
                        JavaFxUtils.HideOrShowRectangle(MainCoreView.$("HideOrShowRectangle"),true);
                        MainCoreController.GroupChatTarget = null;
                    }
                }
                else if(messageType == ServerRespond.DISSOLUTION_GROUP_SUCCEED_PASSIVE){
                    Group group = (Group) ReceiveMessage.getJavabean();
                    int index = -1;
                    for (int i = 0; i < MainCoreController.Group_ObservableList.size(); i++) {
                        Group group1 = MainCoreController.Group_ObservableList.get(i);
                        if(group1.getGroupNumber().equals(group.getGroupNumber())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        JavaFxUtils.DeleteObservableList(MainCoreController.Group_ObservableList,index);
                    }else{
                        System.out.println("某群已经被解散,但列表中查无此群");
                    }
                    //如果这个时候被踢的人正好打开群聊天框
                    if(group.getGroupNumber().equals(MainCoreController.GroupChatTarget)){
                        //此时关闭被踢者的聊天框
                        JavaFxUtils.HideOrShowRectangle(MainCoreView.$("HideOrShowRectangle"),true);
                        MainCoreController.GroupChatTarget = null;
                    }
                }
                else if(messageType == ServerRespond.DISSOLUTION_GROUP_FAIL){
                    JavaFxUtils.ShowStringOnLabel(GroupManageView.$("ErrorTip_Label"),"你的权限不够");
                }


                //退出群
                else if(messageType == ServerRespond.EXIT_GROUP_SUCCEED_TO_ACTIVE){
                    GroupNotice groupNotice = (GroupNotice)ReceiveMessage.getJavabean();
                    MainCoreController.PutNewIdentity(groupNotice.getPassiveUser(),groupNotice.getGroupNumber(),"不是群成员");
                    int index = -1;
                    for (int i = 0; i < MainCoreController.Group_ObservableList.size(); i++) {
                        Group group1 = MainCoreController.Group_ObservableList.get(i);
                        if(group1.getGroupNumber().equals(groupNotice.getGroupNumber())){
                            index = i;
                            break;
                        }
                    }
                    if(index!=-1){
                        JavaFxUtils.DeleteObservableList(MainCoreController.Group_ObservableList,index);
                       JavaFxUtils.CloseStage(GroupManageView.loginStage);
                        GroupManageView.root = null;
                    }else{
                        System.out.println("你已经退群,但你的列表里面没有此群");
                    }
                    //如果这个时候被踢的人正好打开群聊天框
                    if(groupNotice.getGroupNumber().equals(MainCoreController.GroupChatTarget)){
                        //此时关闭被踢者的聊天框
                        JavaFxUtils.HideOrShowRectangle(MainCoreView.$("HideOrShowRectangle"),true);
                        MainCoreController.GroupChatTarget = null;
                    }
                }
                else if(messageType == ServerRespond.EXIT_GROUP_SUCCEED_TO_PASSIVE){
                    JavaFxUtils.RedCircle_Visible(MainCoreView.$("GroupNotice_Circle"),true);
                }
                else if(messageType == ServerRespond.EXIT_GROUP_FAIL){

                }


                //拉取user
                else if(messageType == ServerRespond.PULL_USER_FROM_ACCOUNT_SUCCEED){
                    User user = (User) ReceiveMessage.getJavabean();
                    UserInformationController.user = user;
                    UserInformationController userInformationController = new UserInformationController(user);
                    userInformationController.OpenStage();
                }else if(messageType == ServerRespond.PULL_USER_FROM_ACCOUNT_FAIL){

                }

                //拉取表情
                else if(messageType == ServerRespond.PULL_EMOJI_SUCCEED){
                    Vector<String> vector = (Vector<String>) ReceiveMessage.getJavabean();
                    MainCoreController.Emoji_Vector.addAll(vector);
                }
                else if(messageType == ServerRespond.PULL_EMOJI_FAIL){

                }


                //添加表情
                else if(messageType == ServerRespond.ADD_EMOJI_SUCCEED){
//                    EmojiPojo emojiPojo = (EmojiPojo) ReceiveMessage.getJavabean();
//                    MainCoreController.Emoji_Vector.add(emojiPojo.getAccountNumber());
                }
                else if(messageType == ServerRespond.ADD_EMOJI_FAIL){

                }

                //删除表情
                else if(messageType == ServerRespond.DELETE_EMOJI_SUCCEED){
//                    EmojiPojo emojiPojo = (EmojiPojo) ReceiveMessage.getJavabean();
//                    for (int i = 0; i < MainCoreController.Emoji_Vector.size(); i++) {
//                        if(emojiPojo.getAccountNumber().equals(MainCoreController.Emoji_Vector.get(i))){
//                            MainCoreController.Emoji_Vector.remove(i);
//                            break;
//                        }
//                    }
                }
                else if(messageType == ServerRespond.DELETE_EMOJI_FAIL){

                }


                else {
                    System.out.println(time+"客户端从服务端接受的消息:"+messageType+"暂不处理:"+messageType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
