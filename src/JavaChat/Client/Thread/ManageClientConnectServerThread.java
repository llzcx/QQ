package JavaChat.Client.Thread;

import java.util.HashMap;

/**
 *
 * 该类管理客户端连接到服务器端的线程的类
 */
public class ManageClientConnectServerThread {
    //k = 用户账号,value = 线程
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();
    //将某个线程放入集合
    public static void addClientConnectServerThread(String AccountNumber,ClientConnectServerThread clientConnectServerThread){
        hm.put(AccountNumber,clientConnectServerThread);
    }
    //通过AccountNUmber获取线程
    public static ClientConnectServerThread getClientConnectServerThread(String AccountNumber){
        return hm.get(AccountNumber);
    }
    //从集合中移除某个线程
    public static void RemoveClientConnectServerThread(String AccountNumber){
        hm.remove(AccountNumber);
    }
}
