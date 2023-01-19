package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupMessage;
import JavaChat.Common.Pojo.Message;
import JavaChat.Common.Transfer.FileMessageState;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class DatabaseOperation_GroupMessage {

    /**
     * 获取一个id的行为第几个
     * @param id
     * @return
     * @throws Exception
     */
    public static int getCount(int id) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        int sum = 0;
        String sql = "select Id from group_chat_message ORDER BY Id DESC"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        try (ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                sum++;
                if(rs.getInt("Id")<=id){
                    break;
                }
            }
        }
        return sum;
    }

    /**
     * 提取一个群里所有的消息记录
     * @param GroupNumber
     * @return
     * @throws Exception
     */
    public static Vector<GroupMessage> ExtractGroupMessage(String GroupNumber) throws Exception{//提取消息
        Vector<GroupMessage> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from group_chat_message where GroupNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, GroupNumber);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GroupMessage groupMessage = new GroupMessage();
                groupMessage.setId(rs.getInt("Id"));
                groupMessage.setGroupNumber(rs.getString("GroupNumber"));
                groupMessage.setGroupName(rs.getString("GroupName"));
                groupMessage.setSenderAccountNumber(rs.getString("SenderAccountNumber"));
                groupMessage.setContent(rs.getString("Content"));
                groupMessage.setSendTime(rs.getString("SendTime"));
                String temp = rs.getString("GroupFileMessageState");
                FileMessageState fileMessageState = null;
                vec.add(groupMessage);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return vec;
    }


    public static int SaveGroupMessage(GroupMessage groupMessage) throws Exception{
        int key = 0;
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO group_chat_message(GroupNumber,GroupName,SendTime,SenderAccountNumber,Content)value (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setObject(1,groupMessage.getGroupNumber());
        ps.setObject(2, groupMessage.getGroupName());
        ps.setObject(3, groupMessage.getSendTime());
        ps.setObject(4, groupMessage.getSenderAccountNumber());
        ps.setObject(5,groupMessage.getContent());
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if(keys.next()) {
            key = keys.getInt(1);
        }
        JDBCUtilsByDruid.close(ps,con);
        return key;
    }

    public static void main(String[] args) {
        try {
            //System.out.println(ExtractGroupMessage("111").get(0).getGroupNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
