package PostgresRepository;

import LibraryRepository.BookCopyRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class PostgresBookCopyRepository implements BookCopyRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/1IK173";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alma";

    @Override
    public void createCopies(int isbn, int numberOfCopies) {
        String sql = "INSERT INTO book_copies(isbn, available) VALUES (?, true)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < numberOfCopies; i++) {
                stmt.setInt(1, isbn);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}