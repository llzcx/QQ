package JavaChat.Server.Dao;
import JavaChat.Common.Pojo.FriendApplication;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**该类主要完成一些好友列表数据库操作
 *
 */
public class DatabaseOperation_FriendList {
    /**
     * 该方法可以通过一个账号返回这个账号的所有好友
     * @param AccountNumber 账号
     * @return 返回好友账号集合
     * @throws Exception
     */
    public static Vector<String> PullAllFriend(String AccountNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select User_AccountNumber,Friend_AccountNumber from friend where User_AccountNumber=? OR Friend_AccountNumber=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, AccountNumber); // 注意：索引从1开始
        ps.setObject(2, AccountNumber);
        String AccountNumber_ = null;
        Vector<String> Vec = new Vector<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String us = rs.getString("User_AccountNumber");
                String fs = rs.getString("Friend_AccountNumber");
                if(AccountNumber.equals(us)){
                    AccountNumber_ = rs.getString("Friend_AccountNumber");
                }else{
                    AccountNumber_ = rs.getString("User_AccountNumber");
                }
                if(AccountNumber_!=null)
                Vec.add(AccountNumber_);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return Vec;
    }

    /**返回一个账号列表的所有好友的User实例
     * @param AccountNumberSet 账号集合
     * @return  返回user集合
     * @throws Exception
     */
    public static Vector<User> ReturnFriendListInformation(Vector<String> AccountNumberSet) throws Exception {
        Vector<User> Friend_INFSet = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        for (int i = 0; i < AccountNumberSet.size(); i++) {
            String sql = "select * from user where AccountNumber=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, AccountNumberSet.get(i)); // 注意：索引从1开始
            User user = new User();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user.setId(rs.getInt("Id"));
                    user.setAccountNumber(rs.getString("AccountNumber"));
                    user.setEmail(rs.getString("Email"));
                    user.setAccountPassword(rs.getString("AccountPassword"));
                    user.setAge(rs.getString("Age"));
                    user.setName(rs.getString("Name"));
                    user.setBirthday(rs.getString("Birthday"));
                    user.setGender(rs.getString("Gender"));
                    user.setHeadPortraitPath(rs.getString("HeadPortrait"));
                    user.setPersonalSignature(rs.getString("PersonalSignature"));
                    user.setSchool(rs.getString("School"));
                }
            }
            if(user!=null)
            Friend_INFSet.add(user);
            ps.close();
        }
        con.close();
        return Friend_INFSet;
    }



    //测试
    public static void main(String[] args) throws Exception{
        String Acc = "53719571";
        Vector<String> vec;
        vec = PullAllFriend(Acc);
        for (int i = 0; i < vec.size(); i++) {
            System.out.println(vec.get(i)+"\t");
        }
        Vector<User> v;
        v = ReturnFriendListInformation(vec);
        for (int i = 0; i < v.size(); i++) {
            System.out.println(v.get(i).getName()+"\t");
        }

    }

}
