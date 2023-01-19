package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Friend;
import JavaChat.Common.Pojo.FriendApplication;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.FriendApplicationState;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

/**
 * 此类为好友关系类,添加好友等相关数据库操作
 */
public class DatabaseOperation_FriendApplication {
    /**
     * 根据关键字在User表中插叙含有关键字的User对象
     * @param KeyWord 关键字
     * @return
     * @throws Exception
     */
    public static Vector<User> SearchFriend(String KeyWord) throws Exception {
        Vector<User> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "SELECT * FROM User WHERE Name LIKE ? OR AccountNumber LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + KeyWord + "%");
        ps.setString(2, "%" + KeyWord + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
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
                vec.add(user);
                if(vec.size()>=10){
                    break;
                }
            }
        }
        JDBCUtilsByDruid.close(ps, con);
        return vec;
    }

    public static Vector<User> SearchFriendTwo(String KeyWord) throws Exception {
        Vector<User> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "SELECT * FROM User WHERE AccountNumber = ? ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, KeyWord);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
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
                vec.add(user);
                if(vec.size()>=10){
                    break;
                }
            }
        }
        JDBCUtilsByDruid.close(ps, con);
        return vec;
    }

    /**
     * 在好友申请表中创建一个好友申请行
     * @param friendApplication
     * @throws Exception
     */
    public  static void CreatFriendApplication(FriendApplication friendApplication) throws Exception {
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO friend_application(FriendApplicationSender,FriendApplicationReceiver,FriendApplicationState)value (?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,friendApplication.getFriendApplicationSender());
        ps.setObject(2,friendApplication.getFriendApplicationReceiver());
        ps.setObject(3,friendApplication.getFriendApplicationState().toString());
        ps.execute();
       JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 好友关系
     * @param friendApplication
     * @throws Exception
     */
    public  static void CreatFriend(FriendApplication friendApplication) throws Exception {
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO friend(User_AccountNumber,Friend_AccountNumber)value (?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,friendApplication.getFriendApplicationSender());
        ps.setObject(2,friendApplication.getFriendApplicationReceiver());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 获取最新的好友申请状态
     * @param friendApplication
     */
    public static FriendApplicationState CheckFriendApplication(FriendApplication friendApplication) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "SELECT * FROM friend_application WHERE FriendApplicationSender = ? && FriendApplicationReceiver = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, friendApplication.getFriendApplicationSender());
        ps.setString(2, friendApplication.getFriendApplicationReceiver());
        FriendApplicationState friendApplicationState = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String temp = rs.getString("FriendApplicationState");
              if(FriendApplicationState.UNTREATED.toString().equals(temp)){
                  friendApplicationState = FriendApplicationState.UNTREATED;
              }else if(FriendApplicationState.AGREE.toString().equals(temp)){
                  friendApplicationState = FriendApplicationState.AGREE;
              }else if(FriendApplicationState.REJECT.toString().equals(temp)){
                  friendApplicationState =FriendApplicationState.REJECT;
              }
            }
        }
        JDBCUtilsByDruid.close(ps, con);
        return friendApplicationState;
    }


    /**
     * 更新数据库中某一行好友申请的状态
     * @param friendApplication
     * @throws Exception
     */
    public static void UpdateFriendApplicationState(FriendApplication friendApplication) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "UPDATE friend_application SET FriendApplicationState = ? WHERE FriendApplicationSender = ? && FriendApplicationReceiver = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,friendApplication.getFriendApplicationState().toString());
        ps.setObject(2,friendApplication.getFriendApplicationSender());
        ps.setObject(3,friendApplication.getFriendApplicationReceiver());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    /**
     * 删除好友关系
     * @param friend
     * @throws Exception
     */
    public static void DeleteFriendShip(Friend friend) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM friend WHERE (User_AccountNumber = ? && Friend_AccountNumber = ?) || (User_AccountNumber = ? && Friend_AccountNumber = ?) ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,friend.getUser_AccountNumber());
        ps.setObject(2,friend.getFriend_AccountNumber());
        ps.setObject(3,friend.getFriend_AccountNumber());
        ps.setObject(4,friend.getUser_AccountNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 根据账号获得一个好友申请表
     * @param AccountNumber
     * @return
     * @throws Exception
     */
    public static HashMap<String,FriendApplication> GetFriendApplicationList(String AccountNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from friend_application where FriendApplicationSender=? OR FriendApplicationReceiver=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,AccountNumber); // 注意：索引从1开始
        ps.setObject(2,AccountNumber);
        HashMap<String,FriendApplication> hp = new HashMap();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FriendApplication friendApplication = new FriendApplication();
                friendApplication.setFriendApplicationSender(rs.getString("FriendApplicationSender"));
                friendApplication.setFriendApplicationReceiver(rs.getString("FriendApplicationReceiver"));
                friendApplication.setFriendApplicationState(rs.getString("FriendApplicationState"));
                hp.put(friendApplication.getFriendApplicationSender()+friendApplication.getFriendApplicationReceiver(),friendApplication);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return hp;
    }


    /**
     * 判断2个人是否为好友关系
     * @param AccountNumber01
     * @param AccountNumber02
     * @return
     * @throws Exception
     */
    public static boolean CheckTwoIsFriend(String AccountNumber01,String AccountNumber02) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from friend where (User_AccountNumber = ? && Friend_AccountNumber = ?) || (Friend_AccountNumber = ? && User_AccountNumber = ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,AccountNumber01);
        ps.setObject(2,AccountNumber02);
        ps.setObject(3,AccountNumber02);
        ps.setObject(4,AccountNumber01);
        String Account = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String s1 = rs.getString("User_AccountNumber");
                System.out.println(s1);
                String s2 = rs.getString("Friend_AccountNumber");
                System.out.println(s2);
                Account = rs.getString("User_AccountNumber");
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        //不为空的话说明这2个人为朋友关系
        if(Account==null) return false;
        return (Account.equals(AccountNumber01)||Account.equals(AccountNumber02));
    }




    /**
     * 测试main
     * @param args
     */
    public static void main(String[] args) {
//        FriendApplication friendApplication = new FriendApplication();
//        friendApplication.setFriendApplicationSender("1");
//        friendApplication.setFriendApplicationReceiver("2");
//        friendApplication.setFriendApplicationState(FriendApplicationState.AGREE);
//        Friend friend = new Friend();
//        friend.setFriend_AccountNumber("2");
//        friend.setUser_AccountNumber("1");
//
//            try {
//               Vector<FriendApplication> v = DatabaseOperation_FriendApplication.GetFriendApplicationList("1");
//               for(FriendApplication temp : v){
//                   System.out.println(temp.getFriendApplicationSender() + " " + temp.getFriendApplicationReceiver());
//               }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        Vector<User> v = null;
        try {
            v = SearchFriend("537");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(User temp : v){
            System.out.println(temp.getAccountNumber()+" "+temp.getName());
        }

    }


}
