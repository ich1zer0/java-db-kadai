package kadai_007;

import kadai_004.Employees_Chapter04;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class Posts_Chapter07 {
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

            // レコード追加
            String insertSql = """
                    INSERT INTO posts (user_id, posted_at, post_content, likes)
                    VALUES
                    (1003, '2023-02-08', '昨日の夜は徹夜でした・・', 13),
                    (1002, '2023-02-08', 'お疲れ様です！', 12),
                    (1003, '2023-02-09', '今日も頑張ります！', 18),
                    (1001, '2023-02-09', '無理は禁物ですよ！', 17),
                    (1002, '2023-02-10', '明日から連休ですね！', 20);
                    """;
            System.out.println("レコード追加を実行します");
            int rowCnt = statement.executeUpdate(insertSql);
            System.out.println(rowCnt + "件のレコードが追加されました");

            // レコード検索
            int targetUserId = 1002;
            String whereSql = "SELECT * FROM posts WHERE user_id =" + targetUserId + ";";
            ResultSet result = statement.executeQuery(whereSql);
            System.out.println("ユーザーIDが" + targetUserId + "のレコードを検索しました");
            while (result.next()) {
                Date postedAt = result.getDate("posted_at");
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(result.getRow() + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
            }
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
