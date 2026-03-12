package PostgresRepository;

import LibraryDomain.Loan;
import LibraryRepository.LoanRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresLoanRepository implements LoanRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/1IK173";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alma";

    @Override
    public void save(Loan loan) {
        String sql = "INSERT INTO loans(member_id, copy_id, loan_date, due_date, return_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getMemberId());
            stmt.setInt(2, loan.getCopyId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setNull(5, java.sql.Types.DATE);

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to save loan", e);
        }
    }

    @Override
    public Loan findActiveLoanByMemberIdAndIsbn(int memberId, int isbn) {
        String sql = """
                SELECT l.member_id, l.copy_id, l.loan_date, l.due_date, l.return_date
                FROM loans l
                JOIN book_copies bc ON l.copy_id = bc.copy_id
                WHERE l.member_id = ? AND bc.isbn = ? AND l.return_date IS NULL
                ORDER BY l.loan_date
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.setInt(2, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("member_id"),
                        rs.getInt("copy_id"),
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate()
                );

                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) {
                    loan.markReturned(returnDate.toLocalDate());
                }

                return loan;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to find active loan", e);
        }

        return null;
    }

    @Override
    public void markLoanAsReturned(int copyId) {
        String sql = "UPDATE loans SET return_date = CURRENT_DATE WHERE copy_id = ? AND return_date IS NULL";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copyId);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to mark loan as returned", e);
        }
    }

    @Override
    public int countActiveLoansByMemberId(int memberId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND return_date IS NULL";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to count active loans", e);
        }

        return 0;
    }

    @Override
    public boolean hasActiveLoansByIsbn(int isbn) {
        String sql = """
                SELECT COUNT(*)
                FROM loans l
                JOIN book_copies bc ON l.copy_id = bc.copy_id
                WHERE bc.isbn = ? AND l.return_date IS NULL
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to check active loans for book", e);
        }

        return false;
    }
}