package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.JDBCUtilsByDruid;
import JavaChat.Common.Utils.PasswordUtil;

import javax.xml.crypto.Data;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseOperation_AlterPerson {
   public static void UpdatePersonInformation(User user) throws Exception{
       Connection con = JDBCUtilsByDruid.getConnection();
       String sql = "UPDATE `user` SET `Name` = ? , Birthday = ? , PersonalSignature = ? , Gender = ? , Age = ?  ,School = ?, HeadPortrait = ?,FileSavePath = ? WHERE AccountNumber = ?";
       PreparedStatement ps = con.prepareStatement(sql);
       ps.setObject(1,user.getName());
       ps.setObject(2,user.getBirthday());
       ps.setObject(3,user.getPersonalSignature());
       ps.setObject(4,user.getGender());
       ps.setObject(5,user.getAge());
       ps.setObject(6,user.getSchool());
       ps.setObject(7,user.getHeadPortraitPath());
       ps.setObject(8,user.getFileSavePath());
       ps.setObject(9,user.getAccountNumber());
       ps.execute();
       JDBCUtilsByDruid.close(ps,con);
   }

    public static void main(String[] args) {
        User user = new User();
        user.setAccountNumber("666666");
        user.setName("1");
        user.setBirthday("1");
        user.setPersonalSignature("2");
        user.setGender("男");
        user.setAge("10");
        user.setHeadPortraitPath("2");
        try {
            DatabaseOperation_AlterPerson.UpdatePersonInformation(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
