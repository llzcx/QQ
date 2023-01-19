package JavaChat.Client.Service;

import JavaChat.Client.View.MainCoreController;
import JavaChat.Common.Pojo.EmojiPojo;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.ClientRequest;
import JavaChat.Common.Transfer.ServerRespond;
import JavaChat.Common.Utils.TransferUtil;

public class EmojiService {
    /**
     * 拉取表情
     */
    public static void PullEmoji(){
        User user = new User();
        user.setAccountNumber(MainCoreController.getAccountNumber());
        try {
            TransferUtil.ClientInsideTransfer(user, ClientRequest.PULL_EMOJI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加表情
     * @param emoji
     */
    public static void AddEmoji(String emoji){
        EmojiPojo emojiPojo = new EmojiPojo();
        emojiPojo.setAccountNumber(MainCoreController.getAccountNumber());
        emojiPojo.setEmoji(emoji);
        try {
            TransferUtil.ClientInsideTransfer(emojiPojo, ClientRequest.ADD_EMOJI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除表情
     * @param emoji
     */
    public static void DeleteEmoji(String emoji){
        EmojiPojo emojiPojo = new EmojiPojo();
        emojiPojo.setAccountNumber(MainCoreController.getAccountNumber());
        emojiPojo.setEmoji(emoji);
        try {
            TransferUtil.ClientInsideTransfer(emojiPojo, ClientRequest.DELETE_EMOJI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
