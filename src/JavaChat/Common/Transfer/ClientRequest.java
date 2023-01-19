package JavaChat.Common.Transfer;

import java.io.Serializable;

/**
 * 此类为枚举类,用来保存客户端的业务需求
 */
public enum ClientRequest implements MessageType {
    //登录
    LOGIN,

    //注册
    REGISTER,

    //忘记密码
    FORGET,

    //退出登录
    EXIT_LOGIN,

    //发送文本消息
    SEND_TEXT_MESSAGE,

    //发送图片消息
    SEND_IMAGE_MESSAGE,

    //发送文件消息
    SEND_FILE_MESSAGE,

    //拒绝接受文件消息
    REJECT_FILE_MESSAGE,

    //同意接受该文件消息
    AGREE_FILE_MESSAGE,

    //发送注册邮件
    SEND_REGISTER_EMAIL,

    //注册邮件过时
    REGISTER_VERIFICATION_OBSOLETE,


    //发送忘记密码邮件
    SEND_FORGET_EMAIL,

    //忘记密码邮件过时
    FORGET_VERIFICATION_OBSOLETE,

    //拉取好友列表
    PULL_FRIEND_LIST,

    //拉取消息列表
    PULL_MESSAGE_LIST,

    //添加好友
    ADD_FRIEND_APPLICATION,

    //同意好友申请
    AGREE_FRIEND_APPLICATION,

    //拒绝好友申请
    REJECT_FRIEND_APPLICATION,

    //拉取好友申请列表
    PULL_ADD_FRIEND_APPLICATION_LIST,

    //删除好友
    DELETE_FRIEND,

    //搜素好友
    SEARCH_USER,

    //修改个人资料
    ALTER_PERSON_INFORMATION,

    //发送修改密码验证码
    SEND_ALTER_PASSWORD_EMAIL,

    //修改密码
    Alter_PASSWORD,

    //修改密码的验证码过时
    Alter_PASSWORD_OBSOLETE,

    //创建群聊
    CREAT_GROUP,

    //添加群聊
    ENTER_GROUP,

    //搜索群
    SEARCH_GROUP,

    //退出群聊
    EXIT_GROUP,

    //解散群聊
    DISSOLUTION_GROUP,


    //修改群资料
    ALTER_GROUP_DATA,

    //群聊发文本消息
    SEND_GROUP_TEXT_MESSAGE,

    //拉取某个账号加入的所有群
    PULL_GROUP_LIST,

    //拉取某个群聊的消息
    PULL_GROUP_MESSAGE_LIST,

    //拉取某个群的申请
    PULL_GROUP_APPLICATION_LIST,

    //同意某人进群
    AGREE_GROUP_APPLICATION,

    //拒绝某人进群
    REJECT_GROUP_APPLICATION,

    //设置管理员
    SET_GROUP_ADMINISTRATORS,

    //撤职管理员
    DISMISSAL_GROUP_ADMINISTRATORS,

    //踢掉群成员
    DELETE_GROUP_MEMBER,

    //拉取管理群所需要的资源
    PULL_MANAGE_GROUP_RESOURCE,

    //拉取群通知
    PULL_GROUP_NOTICE,

    //添加表情
    ADD_EMOJI,

    //删除表情
    DELETE_EMOJI,

    //拉取表情
    PULL_EMOJI,

    //拉取一个账号的user信息
    PULL_USER_FROM_ACCOUNT,





}
