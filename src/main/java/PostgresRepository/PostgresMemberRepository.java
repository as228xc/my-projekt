package PostgresRepository;

import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryRepository.MemberRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class PostgresMemberRepository implements MemberRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/1IK173";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alma";

    @Override
    public Member findById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapMember(rs);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Member findByPersonalNumber(String personalNumber) {
        String sql = "SELECT * FROM members WHERE personal_number = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, personalNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapMember(rs);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Member findByIdAndPassword(int memberId, String password) {
        String sql = "SELECT * FROM members WHERE member_id = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapMember(rs);
            }

        } catch (Exception e) {
           throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO members(member_id, first_name, last_name, personal_number, member_type, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, member.getMemberId());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getPersonalNumber());
            stmt.setString(5, member.getMemberType().name());
            stmt.setString(6, member.getPassword());

            stmt.executeUpdate();
            System.out.println("Member saved to database");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void blacklistMember(int memberId) {
        String sql = "UPDATE members SET active = FALSE, blacklisted = TRUE WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapMember(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return members;
    }

    @Override
    public void update(Member member) {
        String sql = """
            UPDATE members
            SET first_name = ?, last_name = ?, personal_number = ?, member_type = ?,
                password = ?, active = ?, borrowed_count = ?, late_returns_count = ?,
                suspensions_count = ?, suspended_until = ?, blacklisted = ?
            WHERE member_id = ?
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getPersonalNumber());
            stmt.setString(4, member.getMemberType().name());
            stmt.setString(5, member.getPassword());
            stmt.setBoolean(6, member.isActive());
            stmt.setInt(7, member.getBorrowedCount());
            stmt.setInt(8, member.getLateReturnsCount());
            stmt.setInt(9, member.getSuspensionsCount());

            if (member.getSuspendedUntil() != null) {
                stmt.setDate(10, java.sql.Date.valueOf(member.getSuspendedUntil()));
            } else {
                stmt.setNull(10, java.sql.Types.DATE);
            }

            stmt.setBoolean(11, member.isBlacklisted());
            stmt.setInt(12, member.getMemberId());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Member mapMember(ResultSet rs) throws Exception {
        int id = rs.getInt("member_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String personalNumber = rs.getString("personal_number");
        MemberType type = MemberType.valueOf(rs.getString("member_type"));
        String password = rs.getString("password");

        Member member = new Member(id, firstName, lastName, personalNumber, type, password);

        member.setActive(rs.getBoolean("active"));
        member.setBorrowedCount(rs.getInt("borrowed_count"));
        member.setLateReturnsCount(rs.getInt("late_returns_count"));
        member.setSuspensionsCount(rs.getInt("suspensions_count"));

        Date suspendedDate = rs.getDate("suspended_until");
        if (suspendedDate != null) {
            member.setSuspendedUntil(suspendedDate.toLocalDate());
        }

        member.setBlacklisted(rs.getBoolean("blacklisted"));

        return member;
    }
}