package JavaChat.Common.Utils;

import JavaChat.Client.View.emojione.Emoji;
import JavaChat.Client.View.emojione.EmojiOne;

import java.util.Queue;

/**
 * 该类为气泡获得自适应的工具
 */
public class ChatBubbleTools {
    /**
     * 自动获取宽度
     * @param Msg
     * @return
     */
    public static double getWidth(String Msg){
        if(Msg==null) return 0;
        int len = 0;
        double width=20;
        Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(Msg);
        while(!obs.isEmpty()) {
            Object ob = obs.poll();
            if(ob instanceof Emoji) {
                width += 32;
            }else{
                len = ((String)(ob)).length();
                for(int i=0;i<len;i++)
                {
                    if(isChinese(((String)(ob)).charAt(i))){
                        width+=17;
                    }
                    else
                    {
                        width+=9;
                    }
                }
            }
        }
        //29中 17=15px/64英 8=7px
        if(width<=480)
        {
            return width;
        }
        else
        {
            return 480;
        }
    }

    /**
     * 自动获取高度
     * @param Msg
     * @return
     */
    public static double getHight(String Msg){
        if(Msg==null) return 0;
        int len = Msg.length();
        double width = 20;
        double height = 40;
        Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(Msg);
        while(!obs.isEmpty()) {
            Object ob = obs.poll();
            if(ob instanceof Emoji) {
                width += 32;
            }else{
                len = ((String)(ob)).length();
                for(int i=0;i<len;i++)
                {
                    if(isChinese(((String)(ob)).charAt(i))){
                        width+=17;
                    }
                    else
                    {
                        width+=9;
                    }
                }
            }
            if(width>=480)
            {
                height+=17.4+4;
                width=20;
            }
        }
        return height;

    }

    /**
     * 判断字符是否为中文
     * @param c
     * @return
     */
    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
