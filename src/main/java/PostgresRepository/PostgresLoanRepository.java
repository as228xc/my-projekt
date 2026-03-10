package PostgresRepository;

import LibraryDomain.Loan;
import LibraryRepository.LoanRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class PostgresLoanRepository implements LoanRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/1IK173";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alma";

    @Override
    public void save(Loan loan) {
        String sql = "INSERT INTO loans(member_id, copy_id, loan_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getMemberId());
            stmt.setInt(2, loan.getCopyId());
            stmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, java.sql.Date.valueOf(loan.getDueDate()));

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}