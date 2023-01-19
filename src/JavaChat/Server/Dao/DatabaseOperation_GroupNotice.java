package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupNotice;
import JavaChat.Common.Transfer.GroupIdentity;
import JavaChat.Common.Transfer.GroupNoticeType;
import JavaChat.Common.Utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * 关于群通知的数据库操作
 */
public class DatabaseOperation_GroupNotice {
    /**
     * 拉取一个账号的所有的通知
     * @return
     * @throws Exception
     */
    public static Vector<GroupNotice> PullAllGroupNotice(String Account) throws Exception{
            Vector<GroupNotice> vec = new Vector<>();
            Connection con = JDBCUtilsByDruid.getConnection();
            Vector<String> vectorGroup = DatabaseOperation_GroupList.PullAllGroup(Account);
            String sql = "SELECT * FROM `group_notice` WHERE GroupNumber = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < vectorGroup.size(); i++) {
                String GroupNumber = vectorGroup.get(i);
                ps.setString(1, GroupNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GroupNotice groupNotice = new GroupNotice();
                        if (DatabaseOperation_GroupApplication.CheckGroupMemberIdentity(Account, GroupNumber) == GroupIdentity.ORDINARY_GROUP_MEMBERS) {
                            continue;
                        }
                        if(!DatabaseOperation_GroupApplication.CheckUserIsInGroup(Account,GroupNumber)){
                            continue;
                        }
                        groupNotice.setTime(rs.getString("Time"));
                        groupNotice.setId(rs.getInt("Id"));
                        groupNotice.setGroupNumber(rs.getString("GroupNumber"));
                        groupNotice.setActiveUser(rs.getString("ActiveUser"));
                        groupNotice.setPassiveUser(rs.getString("PassiveUser"));
                        GroupNoticeType groupNoticeType = null;
                        String temp = rs.getString("Type");
                        if ("0".equals(temp)) {
                            groupNoticeType = GroupNoticeType.XX_ENTER;
                        } else if ("1".equals(temp)) {
                            groupNoticeType = GroupNoticeType.XX_EXIT;
                        } else if ("2".equals(temp)) {
                            groupNoticeType = GroupNoticeType.XX_REMOVE_XX;
                        } else if ("3".equals(temp)) {
                            groupNoticeType = GroupNoticeType.XX_BE_XX_SET_ADMINISTRATOR;
                        } else if ("4".equals(temp)) {
                            groupNoticeType = GroupNoticeType.XX_BE_XX_DEMOTION;
                        } else if ("5".equals(temp)) {
                            groupNoticeType = GroupNoticeType.XX_GROUP_ADMINISTRATOR_BE_DISSOLUTION;
                        }
                        groupNotice.setGroupNoticeType(groupNoticeType);
                        vec.add(groupNotice);
                    }
                }
            }
            JDBCUtilsByDruid.close(ps, con);
            return vec;
    }

    /**
     * 删除群通知
     * @param group
     * @throws Exception
     */
    public static void DeleteAllGroupNotice(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `group_notice` WHERE GroupNumer = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,group.getGroupNumber());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    public static void CreatGroupNotice(GroupNotice groupNotice) throws Exception{
        //将头像放入指定的文件夹当中
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO group_notice(GroupNumber,ActiveUser,PassiveUser,`Time`,`Type`)value (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,groupNotice.getgroupNumber());
        ps.setObject(2,groupNotice.getActiveUser());
        ps.setObject(3,groupNotice.getPassiveUser());
        ps.setObject(4,groupNotice.getTime());
        ps.setObject(5,groupNotice.getGroupNoticeType().toString());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }





}
