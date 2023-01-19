package JavaChat.Common.Transfer;

/**
 * 文件相关常量
 */
public interface Size {
    //单位比特
    int KB = 1024;
    int MB = 1024*1024;
    int GB = 1024*2024*1024;
    long TB = (1024L *1024*1024*1024);

    int ASmallFile = KB;//单片文件大小,单位比特

    int SendInterval = 10;//发送间隔,单位毫秒
}
