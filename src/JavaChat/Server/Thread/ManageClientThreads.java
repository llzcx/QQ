package JavaChat.Server.Thread;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();
    //添加线程对象
    public static void  addServerConnectClientThread(String AccountNumber,ServerConnectClientThread serverConnectClientThread){
        hm.put(AccountNumber,serverConnectClientThread);
    }
    //根据AccountNumber返回线程
    public static ServerConnectClientThread getServerConnectClientThread(String AccountNumber){
        return hm.get(AccountNumber);
    }
    public static void RemoveServerConnectionThread(String AccountNumber){
        hm.remove(AccountNumber);
    }
    public static String getOnlineUser(){
        //遍历集合
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()){
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

}
