package JavaChat.Common.Pojo;

import java.io.Serializable;

public class EmojiPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String AccountNumber;
    private String Emoji;

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getEmoji() {
        return Emoji;
    }

    public void setEmoji(String emoji) {
        Emoji = emoji;
    }
}
