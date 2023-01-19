package JavaChat.Client.Service;

import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.FragmentFile;
import JavaChat.Common.Pojo.Message;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Utils.FileUtils;
import JavaChat.Common.Utils.GetTimeUtils;
import JavaChat.Common.Utils.TransferUtil;

import java.io.File;
import java.util.Date;

import static JavaChat.Common.Transfer.Size.ASmallFile;
import static JavaChat.Common.Transfer.Size.SendInterval;

/**
 * 此类为客户端发消息的业务类,客户端内部消息传输
 */
public class MessageService {
    /**
     *发送文本消息给某人
     * @param send_User_Id
     * @param receive_User_Id
     * @param content
     * @param send_Time
     */
    public static void SendTextMessageToSomeone(String send_User_Id, String receive_User_Id, String content, String send_Time){
        Message message = new Message();
        message.setSend_User_Id(send_User_Id);
        message.setReceive_User_Id(receive_User_Id);
        message.setContent(content);
        message.setSend_Time(send_Time);
        message.setMesType("文本消息");
        try {
            TransferUtil.ClientInsideTransfer(message, ClientRequest.SEND_TEXT_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *发送图片
     * @param send_User_Id
     * @param receive_User_Id
     * @param send_Time
     */
    public static void SendImageMessageToSomeone(String send_User_Id, String receive_User_Id, String send_Time,byte[] FileByteFile,File file){
        Message message = new Message();
        message.setSend_User_Id(send_User_Id);
        message.setReceive_User_Id(receive_User_Id);
        message.setSend_Time(send_Time);
        message.setFile(file);
        message.setBytes(FileByteFile);
        message.setMesType("图片消息");
        try {
            TransferUtil.ClientInsideTransfer(message, ClientRequest.SEND_IMAGE_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉取聊天消息
     * @param AccountNumber
     * @param ChatTarget
     */
    public static void PullMessage(String AccountNumber, String ChatTarget){
        Message message = new Message();
        message.setSend_User_Id(AccountNumber);
        message.setReceive_User_Id(ChatTarget);
        message.setSend_Time(new Date().toString());
        try {
            TransferUtil.ClientInsideTransfer(message,ClientRequest.PULL_MESSAGE_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件类型消息
     * @param send_User_Id
     * @param receive_User_Id
     * @param FileByte
     * @param send_Time
     */
    public static void SendFileMessageToSomeone(String send_User_Id, String receive_User_Id, byte[] FileByte, String send_Time, File file){
        Thread t = new Thread(() -> {
            int lastLen = FileByte.length%ASmallFile;
            String time = GetTimeUtils.GetNowTime();
            //获取到
            Object[] bs = FileUtils.splitByteArr(FileByte,ASmallFile);
            byte[] b = (byte[]) bs[bs.length-1];
            bs[bs.length-1] = new byte[lastLen];
            for (int i = 0; i < lastLen ; i++) {
                ((byte[])bs[bs.length-1])[i] = b[i];
            }
            //将文件将一片一片发给服务器
            for (int i = 0; i < bs.length; i++) {
                FragmentFile fragmentFile = new FragmentFile((byte[])bs[i],i+1,bs.length,lastLen,file.getName(),time,receive_User_Id);
                try {
                    TransferUtil.ClientInsideTransfer(fragmentFile,ClientRequest.SEND_FILE_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //设置间隔
                    try {
                        Thread.sleep(SendInterval);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
            }
        });
        t.start();
    }

    /**
     * 同意接受文件
     * @param message
     */
    public static void AgreeFileMessage(Message message){
        try {
            TransferUtil.ClientInsideTransfer(message, ClientRequest.AGREE_FILE_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void  RejectFileMessage(Message message){
        //这里服务器需要主键才能找到消息在数据库中的位置
        try {
            TransferUtil.ClientInsideTransfer(message, ClientRequest.REJECT_FILE_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
