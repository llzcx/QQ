package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.*;
import JavaChat.Common.Transfer.GroupApplicationState;
import JavaChat.Common.Transfer.GroupIdentity;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

/**
 * 该类主要完成一些群相关的业务
 */
public class DatabaseOperation_GroupApplication {
    /**
     * 创建群聊
     * @param group
     * @throws Exception
     */
    public static void CreatGroup(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO `group`(GroupName,GroupNumber,GroupMemberNum,GroupAnnouncement,GroupHeadPortraitPath)value (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,group.getGroupName());
        ps.setObject(2, group.getGroupNumber());
        ps.setObject(3, group.getGroupMemberNum());
        ps.setObject(4, group.getGroupAnnouncement());
        ps.setObject(5,group.getGroupHeadPortraitFilePath());
        ps.execute();
       JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 看某个人是否在某个群内
     * @param Account
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static boolean CheckUserIsInGroup(String Account, String GroupNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_member` where GroupNumber=? && MemberAccountNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,GroupNumber);
        ps.setObject(2,Account);
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String Link = rs.getString("Link");
                if("1".equals(Link)){
                    return true;
                }
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return false;
    }

    /**
     * 看某个人是否过去某个群内
     * @param Account
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static boolean CheckUserPassIsInGroup(String Account, String GroupNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_member` where GroupNumber=? && MemberAccountNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,GroupNumber);
        ps.setObject(2,Account);
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String acc = rs.getString("MemberAccountNumber");
                String Link = rs.getString("Link");
                if("0".equals(Link)){
                    return true;
                }
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return false;
    }


    /**
     * 是否有群申请处于未被处理状态
     * @param Account
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static boolean CheckGroupApplication(String Account,String GroupNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_application` where GroupNumber=? && GroupApplicationSender = ? && GroupApplicationState = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,GroupNumber);
        ps.setObject(2,Account);
        ps.setObject(3,"0");
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String Sender = rs.getString("GroupApplicationSender");
                String number = rs.getString("GroupNumber");
                if(Sender!=null && number!=null){
                    return true;
                }
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return false;
    }

    /**
     * 将一个GroupMember放入到表中
     * @param groupMember
     * @throws Exception
     */
    public static void InsertGroupMember(GroupMember groupMember) throws Exception{
        if(CheckUserPassIsInGroup(groupMember.getMemberAccountNumber(),groupMember.getGroupNumber())){
            Connection con = JDBCUtilsByDruid.getConnection();
            String sql = "UPDATE group_member SET EnterTime = ?,Link = ? WHERE MemberAccountNumber = ? &&  GroupNumber= ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1,groupMember.getEnterTime());
            ps.setObject(2,"1");
            ps.setObject(3,groupMember.getMemberAccountNumber());
            ps.setObject(4,groupMember.getGroupNumber());
            ps.execute();
            JDBCUtilsByDruid.close(ps,con);
        }else {
            //没有进过群那就直接插入一个新行
            Connection con = JDBCUtilsByDruid.getConnection();
            String sql = "INSERT INTO group_member(GroupNumber,GroupName,MemberAccountNumber,MemberIdentity,Link,EnterTime)value (?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, groupMember.getGroupNumber());
            ps.setObject(2, groupMember.getGroupName());
            ps.setObject(3, groupMember.getMemberAccountNumber());
            ps.setObject(4, groupMember.getMemberIdentity().toString());
            ps.setObject(5, groupMember.getLink());
            ps.setObject(6, groupMember.getEnterTime());
            ps.execute();
            JDBCUtilsByDruid.close(ps, con);
        }
    }


    /**
     * 根据群号返回这个群的所有成员的User对象
     * @param GroupNumber
     */
    public static Vector<User> GetGroupAllGroupMemberUser(String GroupNumber) throws Exception{
        Vector<User> vector = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select MemberAccountNumber from `group_member` where GroupNumber=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,GroupNumber);
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String Account = rs.getString("MemberAccountNumber");
                User user = DatabaseOperation_Login.GetUserInformation(Account,"");
                vector.add(user);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return vector;
    }


    /**
     * 拿到所有以前加过这个群的人的Member集合
     * @param GroupNumber
     * @throws Exception
     */
    public Vector<User> GetAllLinkIsFalseMember(GroupMember GroupNumber) throws Exception{
        Vector<User> vector = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_member` where GroupNumber=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,GroupNumber);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GroupMember groupMember = new GroupMember();
                groupMember.setLink(rs.getString("Link"));
                groupMember.setMemberAccountNumber(rs.getString("MemberAccountNumber"));
                if ("1".equals(groupMember.getLink())) {
                    //0说明这个人不在群里
                    continue;
                }
                vector.add(DatabaseOperation_Login.GetUserInformation(groupMember.getMemberAccountNumber(),"..."));
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return vector;
    }
    /**
     * 根据群号获得这个群所有的groupMember
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static Vector<GroupMember> GetGroupAllGroupMember(String GroupNumber) throws Exception{
        Vector<GroupMember> vector = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_member` where GroupNumber=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,GroupNumber);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GroupMember groupMember = new GroupMember();
                groupMember.setLink(rs.getString("Link"));
                if("0".equals(groupMember.getLink())){
                    //0说明这个人不在群里
                    continue;
                }
                groupMember.setId(rs.getInt("Id"));
                groupMember.setGroupName(rs.getString("GroupName"));
                groupMember.setGroupNumber(rs.getString("GroupNumber"));
                String temp = rs.getString("MemberIdentity");
                GroupIdentity groupIdentity = null;
                if("0".equals(temp)){
                    groupIdentity = GroupIdentity.GROUP_LEADER;
                }else if("1".equals(temp)){
                    groupIdentity = GroupIdentity.ADMINISTRATORS;
                }else if("2".equals(temp)){
                    groupIdentity = GroupIdentity.ORDINARY_GROUP_MEMBERS;
                }
                groupMember.setMemberIdentity(groupIdentity);
                groupMember.setEnterTime(rs.getString("EnterTime"));
                groupMember.setMemberAccountNumber(rs.getString("MemberAccountNumber"));
                vector.add(groupMember);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return vector;
    }

    /**
     * 获得一个群里所有的管理员和群主
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static Vector<GroupMember>  GetGroupAllADMINISTRATORS(String GroupNumber) throws Exception{
        Vector<GroupMember> vector = null;
        Vector<GroupMember> newVector = new Vector<>();
        try {
            vector = GetGroupAllGroupMember(GroupNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < vector.size(); i++) {
            GroupMember groupMember = vector.get(i);
            if(groupMember.getMemberIdentity()==GroupIdentity.GROUP_LEADER || groupMember.getMemberIdentity()==GroupIdentity.ADMINISTRATORS){
                newVector.add(groupMember);
            }
        }

        return newVector;
    }

    /**
     * 根据群号获得所有成员的USer对象
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static Vector<User> GetGroupAllUser(String GroupNumber) throws Exception{
        //先得到这个群里的人的所有账号
        Vector<GroupMember> vector = GetGroupAllGroupMember(GroupNumber);
        Vector<User> vector1 = new Vector<>();
        //根据账号获得这个人的user
        for (int i = 0; i < vector.size(); i++) {
            String acc = vector.get(i).getMemberAccountNumber();
            vector1.add(DatabaseOperation_Login.GetUserInformation(acc,"..."));
        }
        return vector1;
    }

    /**
     * 根据账号和群号查验这个人的身份
     * @param AccountNumber
     * @param GroupNumber
     */
    public static GroupIdentity CheckGroupMemberIdentity(String AccountNumber, String GroupNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_member` where MemberAccountNumber=? and GroupNumber=?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, AccountNumber);
        ps.setObject(2,GroupNumber);
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String flag = rs.getString("MemberIdentity");
                if(flag.equals("0")){
                    groupIdentity = GroupIdentity.GROUP_LEADER;
                }else if(flag.equals("1")){
                    groupIdentity = GroupIdentity.ADMINISTRATORS;
                }else if(flag.equals("2")){
                    groupIdentity = GroupIdentity.ORDINARY_GROUP_MEMBERS;
                }
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return groupIdentity;
    }


    /**
     * 删除群
     * @param group
     * @throws Exception
     */
    public static void DeleteGroup(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `group` WHERE GroupNumer = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,group.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    /**
     * 删除关于这个群的所有群成员
     * @param group
     * @throws Exception
     */
    public static void DeleteGroupMember(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `group_member` WHERE GroupNumer = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,group.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    /**
     * 删除关于这个群的所有消息记录
     * @param group
     * @throws Exception
     */
    public static void DeleteGroupChatMessage(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `group_chat_message` WHERE GroupNumer = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,group.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    /**
     * 在数据库中创建一个群申请
     * @param groupApplication
     */
    public static void CreateGroupApplication(GroupApplication groupApplication) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO group_application (GroupApplicationGroupName,GroupNumber,GroupApplicationSender,GroupApplicationState,`Time`,Processor) value (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,groupApplication.getGroupApplicationGroupName());
        ps.setObject(2,groupApplication.getGroupNumber());
        ps.setObject(3,groupApplication.getGroupApplicationSender());
        ps.setObject(4,groupApplication.getGroupApplicationState().toString());
        ps.setObject(5,groupApplication.getTime());
        ps.setObject(6,groupApplication.getProcessor());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 更新数据库中某一行群申请的状态
     * @param groupApplication
     * @throws Exception
     */
    public static void UpdateGroupApplicationState(GroupApplication groupApplication) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "UPDATE group_application SET GroupApplicationState = ?,Processor = ? WHERE GroupApplicationSender = ? &&  GroupNumber= ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,groupApplication.getGroupApplicationState().toString());
        ps.setObject(2,groupApplication.getProcessor());
        ps.setObject(3,groupApplication.getGroupApplicationSender());
        ps.setObject(4,groupApplication.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 更新数据库中某一行群成员信息,设置管理员
     * @param groupMember
     * @throws Exception
     */
    public static void UpdateGroupMemberIdentity(GroupMember groupMember) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "UPDATE group_member SET MemberIdentity = ? WHERE MemberAccountNumber = ? &&  GroupNumber= ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,groupMember.getMemberIdentity().toString());
        ps.setObject(2,groupMember.getMemberAccountNumber());
        ps.setObject(3,groupMember.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    /**
     * 更新数据库中某一行群成员的是否在群内的信息,Link
     * @param groupMember
     * @throws Exception
     */
    public static void UpdateGroupMemberLink(GroupMember groupMember) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "UPDATE group_member SET Link = ? WHERE MemberAccountNumber = ? &&  GroupNumber= ?";
        System.out.println(groupMember.getMemberAccountNumber()+" "+groupMember.getGroupNumber()+" "+groupMember.getLink());
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,groupMember.getLink());
        ps.setObject(2,groupMember.getMemberAccountNumber());
        ps.setObject(3,groupMember.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }



    /**
     * 删除关于这个群所有的申请记录
     * @param group
     * @throws Exception
     */
    public static void DeleteGroupApplication(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `group_application` WHERE GroupNumer = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,group.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    /**
     * 删除某个群里的某个成员
     * @param groupMember
     * @throws Exception
     */
    public static void DeleteGroupMember(GroupMember groupMember) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `group_member` WHERE MemberAccountNumber = ? AND GroupNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,groupMember.getMemberAccountNumber());
        ps.setObject(1,groupMember.getGroupNumber());
        //被踢了以后要将身份重置
        ps.setObject(1,groupMember.getMemberIdentity().toString());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 根据群号找到所有与该群有关的群申请
     * @param GroupNumber
     */
    public static HashMap<String,GroupApplication> GetAllGroupApplicationByGroupNumber(String GroupNumber) throws Exception{
        HashMap<String,GroupApplication> hp = new HashMap<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_application` where GroupNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, GroupNumber);
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               GroupApplication groupApplication = new GroupApplication();
               groupApplication.setId(rs.getInt("Id"));
               groupApplication.setGroupNumber(rs.getString("GroupNumber"));
               groupApplication.setGroupApplicationSender(rs.getString("GroupApplicationSender"));
               String temp = rs.getString("GroupApplicationState");
               GroupApplicationState groupApplicationState = null;
               if("0".equals(temp)){
                   groupApplicationState = GroupApplicationState.UNTREATED;
               }else if("1".equals(temp)){
                   groupApplicationState = GroupApplicationState.AGREE;
               }else if("2".equals(temp)){
                   groupApplicationState = GroupApplicationState.REJECT;
               }
               groupApplication.setGroupApplicationState(groupApplicationState);
               groupApplication.setTime(rs.getString("Time"));
               groupApplication.setGroupApplicationGroupName(rs.getString("GroupApplicationGroupName"));
               groupApplication.setProcessor(rs.getString("Processor"));
               hp.put(groupApplication.getGroupApplicationSender(),groupApplication);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return hp;
    }

    public static HashMap<String,GroupApplication> GetAllGroupApplicationBySender(String Account) throws Exception{
        HashMap<String,GroupApplication> hp = new HashMap<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from `group_application` where GroupApplicationSender  = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, Account);
        GroupIdentity groupIdentity = null;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GroupApplication groupApplication = new GroupApplication();
                groupApplication.setId(rs.getInt("Id"));
                groupApplication.setGroupNumber(rs.getString("GroupNumber"));
                groupApplication.setGroupApplicationSender(rs.getString("GroupApplicationSender"));
                String temp = rs.getString("GroupApplicationState");
                GroupApplicationState groupApplicationState = null;
                if("0".equals(temp)){
                    groupApplicationState = GroupApplicationState.UNTREATED;
                }else if("1".equals(temp)){
                    groupApplicationState = GroupApplicationState.AGREE;
                }else if("2".equals(temp)){
                    groupApplicationState = GroupApplicationState.REJECT;
                }
                groupApplication.setGroupApplicationState(groupApplicationState);
                groupApplication.setTime(rs.getString("Time"));
                groupApplication.setGroupApplicationGroupName(rs.getString("GroupApplicationGroupName"));
                groupApplication.setProcessor(rs.getString("Processor"));
                hp.put(groupApplication.getGroupApplicationSender(),groupApplication);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return hp;
    }


    /**
     * 获取群申请表
     * @param AccountNumber
     * @return
     * @throws Exception
     */
    public static Vector<GroupApplication> GetFriendApplicationList(String AccountNumber) throws Exception{
        Vector<GroupApplication> vector = new Vector<>();
        //先根据账号获得这个账号的所有群的群号
        Vector<String> GroupSet = DatabaseOperation_GroupList.PullAllGroup(AccountNumber);
        for (int i = 0; i < GroupSet.size(); i++) {
            //对于每个人查验这个人的身份是否为管理员
            GroupIdentity groupIdentity = CheckGroupMemberIdentity(AccountNumber,GroupSet.get(i));
            if(groupIdentity==GroupIdentity.GROUP_LEADER || groupIdentity==GroupIdentity.ADMINISTRATORS){
                //如果是管理员,则查找该群所有的 群申请 到集合当中
                vector.addAll(GetAllGroupApplicationByGroupNumber(GroupSet.get(i)).values());
            }
        }
        //或者这个请求的发送者是自己
        vector.addAll(GetAllGroupApplicationBySender(AccountNumber).values());
        return vector;
    }


    /**
     * 创建member
     * @param groupMember
     * @throws Exception
     */
    public static void CreateGroupMember(GroupMember groupMember) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO group_member (GroupNumber,GroupName,MemberAccountNumber,MemberIdentity,Link,EnterTime) value (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,groupMember.getGroupNumber());
        ps.setObject(2,groupMember.getGroupName());
        ps.setObject(3,groupMember.getMemberAccountNumber());
        ps.setObject(4,groupMember.getMemberIdentity().toString());
        ps.setObject(5,groupMember.getLink());
        ps.setObject(6,groupMember.getEnterTime());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }





    public static void main(String[] args) {
        GroupApplication groupApplication = new GroupApplication();
        groupApplication.setGroupApplicationState(GroupApplicationState.AGREE);
        groupApplication.setGroupApplicationSender("48260608");
        groupApplication.setGroupNumber("438464");
        try {
            UpdateGroupApplicationState(groupApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
