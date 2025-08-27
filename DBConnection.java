package Hotel1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/testhotel"; // ✅ replace hotel_db with your DB name
        String user = "root";
        String password = ""; // default password in XAMPP
        return DriverManager.getConnection(url, user, password);
    }
}
