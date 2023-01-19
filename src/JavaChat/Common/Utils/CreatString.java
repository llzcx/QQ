package JavaChat.Common.Utils;

import java.util.Random;

public class CreatString {
    //生成一个长度为lenth的数字串
    public static String getNum(int length) {

        Random random = new Random();

        StringBuffer valSb = new StringBuffer();

        String charStr = "0123456789";

        int charLength = charStr.length();



        for (int i = 0; i < length; i++) {

            int index = random.nextInt(charLength);

            valSb.append(charStr.charAt(index));

        }

        return valSb.toString();
    }
    //生成一个长度为lenth的字符串(有数字和英文)
    public static String getCharAndNum(int length) {

        Random random = new Random();

        StringBuffer valSb = new StringBuffer();

        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";

        int charLength = charStr.length();



        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));

        }

        return valSb.toString();

    }
    public static String RemoveOther(String s){
        return s = s.replaceAll("[^a-zA-Z0-9\u4E00-\u9FA5]", "");  //去除数字，英文，汉字  之外的内容
    }

}
