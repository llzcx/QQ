package JavaChat.Common.Utils;
public class PasswordUtil {
   public static String Salt(String AccountNumber){
       return AccountNumber;
   }
   public static String Creat_MD5Slat(String AccountNumber,String AccountPassword){
       return MD5Util.code(AccountPassword + Salt(AccountNumber));
   }

    public static void main(String[] args) {
        System.out.println(Creat_MD5Slat("53719574", "123456789"));
    }
}
