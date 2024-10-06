package kadai_010;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class Scores_Chapter10 {
    public static void main(String[] args) {
        Connection con = null;
        Statement statement = null;

        try {
            InputStream is = Objects.requireNonNull(Scores_Chapter10.class.getResource("/database.properties")).openStream();
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

            // レコード更新
            String updateSql = "UPDATE scores SET score_math = 95, score_english = 80 WHERE id = 5;";
            System.out.println("レコード更新を実行します");
            int rowCnt = statement.executeUpdate(updateSql);
            System.out.println(rowCnt + "件のレコードが更新されました");

            // レコード並べ替え
            String selectSql = "SELECT * FROM scores ORDER BY score_math DESC, score_english DESC;";
            ResultSet result = statement.executeQuery(selectSql);
            System.out.println("数学・英語の点数が高い順に並べ替えました");
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int scoreMath = result.getInt("score_math");
                int scoreEnglish = result.getInt("score_english");
                System.out.println(result.getRow() + "件目：生徒ID=" + id + "／氏名=" + name + "／数学=" + scoreMath + "／英語=" + scoreEnglish);
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
