package kadai_004;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

public class Employees_Chapter04 {
    public static void main(String[] args) {
        Connection con = null;
        Statement statement = null;

        try {
            InputStream is = Objects.requireNonNull(Employees_Chapter04.class.getResource("/database.properties")).openStream();
            if (is == null) {
                throw new IOException("プロパティファイルが見つかりません: /database.properties");
            }

            Properties props = new Properties();
            props.load(is);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            con = DriverManager.getConnection(
                    url,
                    user,
                    password
            );

            System.out.println("データベース接続成功：" + con);

            statement = con.createStatement();
            String sql = """
                    CREATE TABLE IF NOT EXISTS employees (
                      id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(60) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      age INT(11),
                      address VARCHAR(255)
                    );
                    """;

            int rowCnt = statement.executeUpdate(sql);
            System.out.println("社員テーブルを作成しました:更新レコード数=" + rowCnt);
        } catch (IOException | SQLException e) {
            System.out.println("データベース接続失敗：" + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
}
