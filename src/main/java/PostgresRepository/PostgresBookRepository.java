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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public void update(BookTitle book) {
        String sql = "UPDATE book_titles SET title = ?, author = ? WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getIsbn());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}