package JavaChat.Server.Dao;


import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.JDBCUtilsByDruid;
import JavaChat.Common.Utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseOperation_Forget {

    public static boolean UserAccountNumberAndEmailIsRight(User user) throws Exception{
        //从连接池中拿到连接
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select AccountNumber, Email from user where AccountNumber=? and Email=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, user.getAccountNumber()); // 注意：索引从1开始
        ps.setObject(2, user.getEmail());
        String AccountNumber_ = null;
        String Email_ = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AccountNumber_ = rs.getString("AccountNumber");
                Email_ = rs.getString("Email");
            }
        }
       JDBCUtilsByDruid.close(ps,con);
        if(AccountNumber_==null && Email_==null){//数据库中并无此账号(与其绑定在一起的邮箱)
            return false;
        }else{
            return true;
        }
    }

    public static void ResetAccountPassword(User user) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "UPDATE user SET AccountPassword = ? WHERE AccountNumber = ? && Email = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        //加密以后再进行存储
        user.setAccountPassword(PasswordUtil.Creat_MD5Slat(user.getAccountNumber(),user.getAccountPassword()));
        ps.setObject(1,user.getAccountPassword());
        ps.setObject(2,user.getAccountNumber());
        ps.setObject(3,user.getEmail());
        ps.execute();
            JDBCUtilsByDruid.close(ps,con);
    }

    public static void main(String[] args) {
        User user = new User();
        user.setAccountNumber("666666");
        user.setAccountPassword("123456789");
        user.setEmail("123@qq.com");
        try {
            DatabaseOperation_Forget.ResetAccountPassword(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
