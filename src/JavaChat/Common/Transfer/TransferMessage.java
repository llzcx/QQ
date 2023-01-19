package JavaChat.Common.Transfer;

import java.io.Serializable;

/**
 * 服务端与客户端的交互类
 * @param <T>
 */
public class TransferMessage<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //这个用来传输实体类/实体类的数组
    private T Javabean;
    //这个字段用来判断处理不同的消息请求
    private MessageType MessageType;

    //构造方法
    public TransferMessage(){}

   public TransferMessage(T javabean){
        this.Javabean = javabean;
   }

    public T getJavabean() {
        return Javabean;
    }

    public void setJavabean(T javabean) {
        Javabean = javabean;
    }

    public MessageType getMessageType() {
        return MessageType;
    }

    public void setMessageType(MessageType messageType) {
        MessageType = messageType;
    }

}