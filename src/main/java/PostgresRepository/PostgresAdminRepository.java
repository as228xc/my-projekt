package PostgresRepository;

import LibraryRepository.AdminRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresAdminRepository implements AdminRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/1IK173";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alma";

    @Override
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT 1 FROM admin_accounts WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException("Failed to validate admin login", e);
        }
    }
}