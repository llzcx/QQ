package JavaChat.Common.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetTimeUtils {
    public static final long Ynum = 31104000L;
    public static final long Mnum = 2592000L;
    public static final long Dnum = 86400L;
    public static final long Wnum = 604800L;
    public static final long Hnum = 3600L;
    public static final long mnum = 60L;

    public static String GetNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(System.currentTimeMillis());
    }

    public static long GetStringTimeToNum(String time){
        long yyyy = Long.parseLong(time.substring(0,3));
        long MM = Long.parseLong(time.substring(5,6));
        long dd = Long.parseLong(time.substring(8,9));
        long HH = Long.parseLong(time.substring(11,12));
        long mm = Long.parseLong(time.substring(14,15));
        long ss = Long.parseLong(time.substring(17,18));
        return yyyy*Ynum + MM*Mnum + dd*Dnum + HH*Hnum + mm*mnum + ss;
    }

    public static Date StringToDate(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newTime = null;
        try {
            newTime = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }
    public static int getAge(String bir) throws Exception {
        int age = 0;
        try {
            Date birthDay = GetTimeUtils.StringToDate(bir);
            Calendar cal = Calendar.getInstance();
            if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算

                throw new IllegalArgumentException(

                        "The birthDay is before Now.It's unbelievable!");

            }

            int yearNow = cal.get(Calendar.YEAR); //当前年份

            int monthNow = cal.get(Calendar.MONTH); //当前月份

            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期

            cal.setTime(birthDay);

            int yearBirth = cal.get(Calendar.YEAR);

            int monthBirth = cal.get(Calendar.MONTH);

            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            age = yearNow - yearBirth; //计算整岁数

            if (monthNow <= monthBirth) {

                if (monthNow == monthBirth) {

                    if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一

                }else{

                    age--;//当前月份在生日之前，年龄减一

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }
    public static void main(String[] args) {
        try {
            System.out.println(getAge("2000-1-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
