package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupApplication;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseOperation_AlterGroup {
    /**
     * 更新群资料
     * @param group
     * @throws Exception
     */
    public static void UpdateGroupInformation(Group group) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        if(group.getGroupHeadPortraitFilePath()!=null){
            String sql = "UPDATE `group` SET GroupName = ?,GroupAnnouncement = ?,GroupHeadPortraitPath = ? WHERE GroupNumber = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1,group.getGroupName());
            ps.setObject(2,group.getGroupAnnouncement());
            ps.setObject(3,group.getGroupHeadPortraitFilePath());
            ps.setObject(4,group.getGroupNumber());
            ps.execute();
            JDBCUtilsByDruid.close(ps,con);
        }else{
            String sql = "UPDATE `group` SET GroupName = ?,GroupAnnouncement = ? WHERE GroupNumber = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1,group.getGroupName());
            ps.setObject(2,group.getGroupAnnouncement());
            ps.setObject(3,group.getGroupNumber());
            ps.execute();
            JDBCUtilsByDruid.close(ps,con);
            if(!con.isClosed()){
                con.close();
            }
        }
    }

    public static void main(String[] args) {
        Group group = new Group();
        group.setGroupName("23");
        group.setGroupNumber("123");
        try {
            UpdateGroupInformation(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
