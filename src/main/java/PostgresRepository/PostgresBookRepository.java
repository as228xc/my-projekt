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
        String sql = """
                SELECT bt.isbn, bt.title, bt.author,
                       COUNT(bc.copy_id) AS total_copies,
                       COUNT(CASE WHEN bc.available = true THEN 1 END) AS available_copies
                FROM book_titles bt
                LEFT JOIN book_copies bc ON bt.isbn = bc.isbn
                WHERE bt.isbn = ?
                GROUP BY bt.isbn, bt.title, bt.author
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int foundIsbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int totalCopies = rs.getInt("total_copies");
                int availableCopies = rs.getInt("available_copies");

                BookTitle book = new BookTitle(foundIsbn, title, author, totalCopies);
                book.setAvailableCopies(availableCopies);
                return book;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to find book by ISBN", e);
        }

        return null;
    }

    @Override
    public void save(BookTitle book) {
        String sql = "INSERT INTO book_titles(isbn, title, author) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to save book title", e);
        }
    }

    @Override
    public void deleteByIsbn(int isbn) {
        String sql = "DELETE FROM book_titles WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, isbn);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete book title", e);
        }
    }

    @Override
    public List<BookTitle> findAll() {
        List<BookTitle> books = new ArrayList<>();

        String sql = """
                SELECT bt.isbn, bt.title, bt.author,
                       COUNT(bc.copy_id) AS total_copies,
                       COUNT(CASE WHEN bc.available = true THEN 1 END) AS available_copies
                FROM book_titles bt
                LEFT JOIN book_copies bc ON bt.isbn = bc.isbn
                GROUP BY bt.isbn, bt.title, bt.author
                ORDER BY bt.title
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int totalCopies = rs.getInt("total_copies");
                int availableCopies = rs.getInt("available_copies");

                BookTitle book = new BookTitle(isbn, title, author, totalCopies);
                book.setAvailableCopies(availableCopies);
                books.add(book);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all books", e);
        }

        return books;
    }

    @Override
    public List<BookTitle> search(String query) {
        List<BookTitle> books = new ArrayList<>();
        String sql = """
                SELECT bt.isbn, bt.title, bt.author,
                       COUNT(bc.copy_id) AS total_copies,
                       COUNT(CASE WHEN bc.available = true THEN 1 END) AS available_copies
                FROM book_titles bt
                LEFT JOIN book_copies bc ON bt.isbn = bc.isbn
                WHERE
                    CAST(bt.isbn AS TEXT) LIKE ?
                    OR LOWER(bt.title) LIKE LOWER(?)
                    OR LOWER(bt.author) LIKE LOWER(?)
                GROUP BY bt.isbn, bt.title, bt.author
                ORDER BY bt.title
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + query + "%";

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BookTitle book = new BookTitle(
                        rs.getInt("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("total_copies")
                );

                book.setAvailableCopies(rs.getInt("available_copies"));
                books.add(book);
            }

        } catch (Exception e) {
            throw new RuntimeException("Search failed", e);
        }

        return books;
    }
}