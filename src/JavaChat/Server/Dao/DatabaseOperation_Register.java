package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class DatabaseOperation_Register {

    public static boolean EmailIsEmpty(User user) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select Email from user where Email=?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, user.getEmail()); // 注意：索引从1开始
        String email = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                email = rs.getString("Email");
            }
        }
        ps.close();
        con.close();
        if(email==null){
            return true;
        }else{
            return false;
        }
    }

    public  static boolean CreatNewUser(User user) throws Exception {
        //将头像放入指定的文件夹当中
        String path = FileUtils.saveFile(user.getHeadPortrait_byte(), ServerFileAddress.HeadPortrait);
        user.setHeadPortraitPath(path);
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO user(AccountNumber,AccountPassword,Email,Name,Age,Gender,HeadPortrait)value (?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, user.getAccountNumber());
        //对密码进行加密
        user.setAccountPassword(PasswordUtil.Creat_MD5Slat(user.getAccountNumber(),user.getAccountPassword()));
        ps.setObject(2, user.getAccountPassword());
        ps.setObject(3, user.getEmail());
        ps.setObject(4, user.getName());
        ps.setObject(5, user.getAge());
        ps.setObject(6, user.getGender());
        ps.setObject(7,user.getHeadPortraitPath());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
        return true;
    }

    public static void main(String[] args) throws Exception{
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(sdf.format(System.currentTimeMillis()));

    }
}
