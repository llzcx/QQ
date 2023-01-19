package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class DatabaseOperation_SearchGroup {
    public static Vector<Group> SearchGroup(String KeyWord) throws Exception {
        Vector<Group> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "SELECT * FROM `group` WHERE GroupName LIKE ? OR GroupNumber LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + KeyWord + "%");
        ps.setString(2, "%" + KeyWord + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("Id"));
                group.setGroupName(rs.getString("GroupName"));
                group.setGroupNumber(rs.getString("GroupNumber"));
                group.setGroupHeadPortraitFilePath(rs.getString("GroupHeadPortraitPath"));
                group.setGroupMemberNum(rs.getInt("GroupMemberNum"));
                group.setGroupAnnouncement(rs.getString("GroupAnnouncement"));
                vec.add(group);
                //我们只为用户准备了10个与关键字相关的群
                if(vec.size()>=10){
                    break;
                }
            }
        }
        JDBCUtilsByDruid.close(ps, con);
        return vec;
    }
}
