package PostgresRepository;

import LibraryDomain.BookTitle;
import LibraryRepository.BookRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostgresBookRepository implements BookRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/1IK173";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alma";

    @Override
    public BookTitle findByIsbn(int isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int foundIsbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int totalCopies = rs.getInt("total_copies");

                return new BookTitle(foundIsbn, title, author, totalCopies);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(BookTitle book) {
        String sql = "INSERT INTO books(isbn, title, author, total_copies) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getTotalCopies());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BookTitle> findAll() {
        List<BookTitle> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int totalCopies = rs.getInt("total_copies");

                BookTitle book = new BookTitle(isbn, title, author, totalCopies);
                books.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
}