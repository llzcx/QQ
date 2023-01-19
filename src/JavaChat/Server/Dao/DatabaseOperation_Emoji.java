package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.EmojiPojo;
import JavaChat.Common.Transfer.FriendApplicationState;
import JavaChat.Common.Utils.JDBCUtilsByDruid;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class DatabaseOperation_Emoji implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 为某个账号添加一个常用表情
     * @param emojiPojo
     * @throws Exception
     */
    public  static void CreatEmoji(EmojiPojo emojiPojo) throws Exception {
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "INSERT INTO emoji(AccountNumber,Emoji)value (?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,emojiPojo.getAccountNumber());
        ps.setObject(2,emojiPojo.getEmoji());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 将某个账号的表情删掉
     * @param emojiPojo
     * @throws Exception
     */
    public static void DeleteEmoji(EmojiPojo emojiPojo) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "DELETE FROM `emoji` WHERE AccountNumber = ? && Emoji = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,emojiPojo.getAccountNumber());
        ps.setObject(2,emojiPojo.getEmoji());
        ps.execute();
        JDBCUtilsByDruid.close(ps,con);
    }


    /**
     * 查找到一个账号所有的常用表情
     * @param AccountNumber
     * @return
     * @throws Exception
     */
    public static Vector<String> FindAllEmoji(String AccountNumber) throws Exception{
        Connection con = JDBCUtilsByDruid.getConnection();
        String sql = "SELECT * FROM emoji WHERE AccountNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,AccountNumber);
        Vector<String> vector = new Vector<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               String emo = rs.getString("Emoji");
               vector.add(emo);
            }
        }
        JDBCUtilsByDruid.close(ps, con);
        return vector;
    }




}
