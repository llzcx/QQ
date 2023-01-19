package JavaChat.Server.Dao;


import JavaChat.Common.Pojo.Message;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Transfer.FileMessageState;
import JavaChat.Common.Utils.JDBCUtilsByDruid;
import jdk.nashorn.internal.runtime.ECMAException;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class DatabaseOperation_Message {
    /**
     * 将message对象保存至数据库当中
     * @param message
     * @throws Exception
     */
    public static int SaveMessage(Message message) throws Exception{//保存消息
        int key = 0;
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO chat_message(Send_User_Id,Receive_User_Id,Content,Send_Time,FilePathInServer,MsgType,FileMessageState,FileName)value (?,?,?,?,?,?,?,?)";//存的时候Id是不需要存的
        PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setObject(1, message.getSend_User_Id());
        ps.setObject(2, message.getReceive_User_Id());
        ps.setObject(3, message.getContent());
        ps.setObject(4, message.getSend_Time());
        ps.setObject(5, message.getFilePathInServer());
        ps.setObject(6,message.getMesType());
        String state = null;
        if(message.getFileMessageState()!=null){
            state = message.getFileMessageState().toString();
        }
        ps.setObject(7, state);
        ps.setObject(8,message.getFileName());
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if(keys.next()){
            key = keys.getInt(1);
        }
        JDBCUtilsByDruid.close(ps,con);
        return key;
    }

    /**
     * 获取一个id的行为第几个
     * @param id
     * @return
     * @throws Exception
     */
    public static int getCount(int id) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        int sum = 0;
        String sql = "select Id from chat_message ORDER BY Id DESC"; //userlist为表名字
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
     * 将某2个人的消息记录从数据库当中提取出来
     * @param Sender
     * @param Receiver
     * @return
     * @throws Exception
     */
    public static Vector<Message> ExtractMessage(String Sender, String Receiver) throws Exception{//提取消息
        Vector<Message> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select * from chat_message  where (Send_User_Id=? AND Receive_User_Id=?) OR (Send_User_Id=? AND Receive_User_Id=?)"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, Sender);
        ps.setObject(2, Receiver);
        ps.setObject(3, Receiver);
        ps.setObject(4, Sender);
        String email = null;
        try (ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                Message message = new Message();
                message.setId(rs.getInt("Id"));
                message.setSend_User_Id(rs.getString("Send_User_Id"));
                message.setReceive_User_Id(rs.getString("Receive_User_Id"));
                message.setContent(rs.getString("Content"));
                message.setSend_Time(rs.getString("Send_Time"));
                message.setFilePathInServer(rs.getString("FilePathInServer"));
                message.setMesType(rs.getString("MsgType"));
                message.setFileName(rs.getString("FileName"));
                String temp = rs.getString("FileMessageState");
                FileMessageState fileMessageState = null;
                if("0".equals(temp)){
                    fileMessageState = FileMessageState.UNTREATED;
                }else if("1".equals(temp)){
                    fileMessageState = FileMessageState.AGREE;
                }else if("2".equals(temp)){
                    fileMessageState = FileMessageState.REJECT;
                }
                message.setFileMessageState(fileMessageState);
                message.setFilePathInServer(rs.getString("FilePathInServer"));
                vec.add(message);
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return vec;
    }

    /**
     * 改变文件状态
     * @param message
     * @throws Exception
     */
    public static void UpdateFileMessageState(Message message) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "UPDATE chat_message SET FileMessageState=? WHERE Id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,message.getFileMessageState().toString());
        ps.setObject(2, message.getId());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }

    public static String getFileNameFromId(int Id) throws Exception {
        Vector<Message> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select FileName,Id from chat_message where Id = ?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, Id);
        String ans = "";
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ans = rs.getString("FileName");
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return ans;
    }

    public static int getIdFromPath(String path) throws Exception{
        Vector<Message> vec = new Vector<>();
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "select Id,FileMessageState from chat_message where FilePathInServer = ?"; //userlist为表名字
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, path);
        int ans = 0;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ans = rs.getInt("Id");
            }
        }
        JDBCUtilsByDruid.close(ps,con);
        return ans;
    }
    public static void main(String[] args) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
