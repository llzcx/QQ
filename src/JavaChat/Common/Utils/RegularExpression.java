package JavaChat.Common.Utils;

import java.util.regex.Pattern;

/**
 * 该工具类实现正则判断
 */
public class RegularExpression {

    /**
     * 判断账号是否为一个8位数字的字符串
     * @param Account 账号
     * @return
     */
    public static boolean CheckAccountNumber(String Account){
        //必须为8个数字
        String Matcher = "[0-9]{8}";
        return Pattern.matches(Matcher,Account);
    }

    /**
     * 判断账号是否为一个8位数字的字符串
     * @param GroupNumber 账号
     * @return
     */
    public static boolean CheckGroupNumber(String GroupNumber){
        //必须为8个数字
        String Matcher = "[0-9]{6}";
        return Pattern.matches(Matcher,GroupNumber);
    }



    /**
     * 判断密码是否符合格式
     * @param Psw
     * @return
     */
    public static boolean CheckAccountPassword(String Psw){
        //密码为英文,数字,下划线的组合一共6-20个
        String Matcher =  "^[\\w_]{6,20}$";
        return Pattern.matches(Matcher,Psw);
    }

    /**
     * 判断邮箱格式是否正确
     * @param Email
     * @return
     */
    public static boolean CheckEmail(String Email){
        String matcher = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+";
        return Pattern.matches(matcher,Email);
    }

    /**
     * 检查匿名格式
     * @param name
     * @return
     */
    public static boolean CheckName(String name){
        String Matcher=".{1,10}";
        return Pattern.matches(Matcher,name);
    }

    public static boolean CheckStringIsEmpty(String name){
        if(name==null){
            return false;
        }
        return "".equals(name);
    }

    public static void main(String[] args) {
        System.out.println(CheckAccountPassword("32072213@qq.,com"));
        System.out.println(CheckAccountPassword("32072213#qq.,com"));
        System.out.println(CheckName("1@asd,/.90"));
    }

}
