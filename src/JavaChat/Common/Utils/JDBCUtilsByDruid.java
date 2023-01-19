package JavaChat.Common.Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtilsByDruid {

    private static DataSource ds;

    //在静态代码块完成 ds初始化功能(加载类的时候只会执行一次)
    static {
        try {
            //1.加入jar包
            //2.加入配置文件
            //3.创建对象,读取配置文件
            Properties properties = new Properties();
            properties.load(new FileInputStream("src\\druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(properties);
            Connection connection = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拿到连接数据库的连接的方法
    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }

    //关闭连接,并不是真正的关闭连接.
    public static void close(ResultSet resultSet, Statement statement,Connection connection) {
        try {
            if(resultSet!=null){
                resultSet.close();
            }

            if(statement!=null){
                statement.close();
            }

            if(connection!=null){
                /**
                 * 这个close方法会根据connection的类型不同而是有所不同
                 * 使用了连接池技术(我使用的是德鲁伊连接池)以后获取到的connection
                 * 是由阿里巴巴实现了connection接口而得到的一个类.所以这个close会有所不同
                 * 实际上并不是真正的关闭,而是放入了线程池当中
                 */
                connection.close();
            }

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

    }

    //方法重载,修改删除数据库内容可以用此方法(少了ResultSet来保存数据)
    public static void close(Statement statement,Connection connection){
        try {
            if(statement!=null){
                statement.close();
            }


            if(connection!=null){
                connection.close();
            }

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

    }
}
