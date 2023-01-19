package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * 该类完成群列表操作
 */
public class DatabaseOperation_GroupList {
    /**
     * 该方法返回一个账号的所有群的群号
     * @param AccountNumber
     * @return
     * @throws Exception
     */
    public static Vector<String> PullAllGroup(String AccountNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from group_member where MemberAccountNumber=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, AccountNumber);
        Vector<String> Vec = new Vector<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String link = rs.getString("Link");
                if("0".equals(link)){
                    //不是本群的群员直接continue
                    continue;
                }
                String GroupNumber = rs.getString("GroupNumber");
             Vec.add(GroupNumber);
            }
        }
       JDBCUtilsByDruid.close(ps,con);
        return Vec;
    }

    /**
     * 该方法可以通过一个群号的集合返回一个Group数组
     * @param GroupNumberSet
     * @return
     * @throws Exception
     */
    public static Vector<Group> ReturnGroupListInformation(Vector<String> GroupNumberSet) throws Exception {
        Vector<Group> Group_INFSet = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        for (int i = 0; i < GroupNumberSet.size(); i++) {
            //这个group一定要加上反单引号
            String sql = "select * from `group` where GroupNumber=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, GroupNumberSet.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getInt("Id"));
                   group.setGroupName(rs.getString("GroupName"));
                   group.setGroupMemberNum(rs.getInt("GroupMemberNum"));
                   group.setGroupNumber(rs.getString("GroupNumber"));
                   group.setGroupHeadPortraitFilePath(rs.getString("GroupHeadPortraitPath"));
                   group.setGroupAnnouncement(rs.getString("GroupAnnouncement"));
                    Group_INFSet.add(group);
                }
            }
            ps.close();
        }
        con.close();
        return Group_INFSet;
    }


    public static void main(String[] args) {
        Vector<String> v;
        try {
            v = PullAllGroup("666666");
            for (String num:
                    v) {
                System.out.println(num);
            }
            Vector<Group>  vec = ReturnGroupListInformation(v);
            for (Group group:
                 vec) {
                System.out.println(group.getGroupMemberNum());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
