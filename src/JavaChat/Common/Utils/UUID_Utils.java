package JavaChat.Common.Utils;

import java.util.UUID;

public class UUID_Utils {
    public static String getGroupNumber() {
        UUID uuid = UUID.randomUUID();
        String ans = uuid.toString();
        char temp = 'a';
        for (Integer i = 0; i < 26; i++) {
             String s= ((char) (temp + i))+"";
            ans = ans.replace(s,i.toString());
        }
        ans = ans.replaceAll("-","");
        ans = ans.substring(0,10);
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(getGroupNumber());
    }
}
