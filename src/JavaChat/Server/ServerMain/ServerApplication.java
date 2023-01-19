package JavaChat.Server.ServerMain;

import JavaChat.Server.Service.ServerMonitoring;

public class ServerApplication {
    public static void main(String[] args) throws Exception{
        while(true) {
            ServerMonitoring serverMonitoring = new ServerMonitoring();
        }
    }
}
