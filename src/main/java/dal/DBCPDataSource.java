package dal;
import org.apache.commons.dbcp2.*;

public class DBCPDataSource {
    private static BasicDataSource dataSource;

    // NEVER store sensitive information below in plain text!
    private static final String HOST_NAME = "rds-mysql-hw3.c5ranqgqojwa.us-east-1.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DATABASE = "SkierApplication";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    static {
        // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
        dataSource = new BasicDataSource();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
        dataSource.setUrl(url);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(60);
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}
