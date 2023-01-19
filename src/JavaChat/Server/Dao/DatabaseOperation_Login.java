package JavaChat.Server.Dao;


import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.JDBCUtilsByDruid;
import JavaChat.Common.Utils.PasswordUtil;

import java.sql.*;

public class DatabaseOperation_Login {
    public static boolean CheckAccountNumberIsRight(String AccountNumber, String AccountPassword) throws Exception {
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select AccountPassword, AccountNumber from user where AccountNumber=? and AccountPassword=?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, AccountNumber); // 注意：索引从1开始
        //优先进行密码转换
        AccountPassword = PasswordUtil.Creat_MD5Slat(AccountNumber,AccountPassword);
        ps.setObject(2, AccountPassword);
        String AccountNumber_ = "";
        String AccountPassword_ = "";
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AccountNumber_ = rs.getString("AccountNumber");
                AccountPassword_ = rs.getString("AccountPassword");
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        if(AccountNumber_.equals("") && AccountPassword_.equals("")){
            return false;
        }else {
            return true;
        }
    }
    public static boolean CheckAccountNumberIsExist(String AccountNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select AccountNumber from user where AccountNumber=?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, AccountNumber); // 注意：索引从1开始
        //优先进行密码转换
        String AccountNumber_ = "";
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AccountNumber_ = rs.getString("AccountNumber");
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        if(AccountNumber_.equals("")){
            return false;
        }else {
            return true;
        }
    }


    /**
     * 通过账号密码蝴蝶一整个user账号信息
     * @param AccountNumber
     * @param AccountPassword
     * @return
     * @throws Exception
     */
    public static User GetUserInformation(String AccountNumber, String AccountPassword) throws Exception {
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from user where AccountNumber=?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, AccountNumber); // 注意：索引从1开始
        User user = new User();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                user.setId(rs.getInt("Id"));
              user.setAccountNumber(rs.getString("AccountNumber"));
              user.setName(rs.getString("Name"));
              user.setBirthday(rs.getString("Birthday"));
              user.setAge(rs.getString("Age"));
              user.setGender(rs.getString("Gender"));
              user.setHeadPortraitPath(rs.getString("HeadPortrait"));
              user.setPersonalSignature(rs.getString("PersonalSignature"));
              user.setEmail(rs.getString("Email"));
              user.setSchool(rs.getString("School"));
              user.setFileSavePath(rs.getString("FileSavePath"));
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return user;
    }

    public static void main(String[] args) {
        try {
            System.out.println(CheckAccountNumberIsExist(""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
