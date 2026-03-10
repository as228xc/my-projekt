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
import java.sql.*;

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

                int id = rs.getInt("member_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String personalNumber = rs.getString("personal_number");
                MemberType type = MemberType.valueOf(rs.getString("member_type"));

                Member member = new Member(id, firstName, lastName, personalNumber, type);

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

        } catch (Exception e) {
            e.printStackTrace();
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

                int id = rs.getInt("member_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String pnr = rs.getString("personal_number");
                MemberType type = MemberType.valueOf(rs.getString("member_type"));

                Member member = new Member(id, firstName, lastName, pnr, type);

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO members(member_id, first_name, last_name, personal_number, member_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, member.getMemberId());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getPersonalNumber());
            stmt.setString(5, member.getMemberType().name());

            stmt.executeUpdate();
            System.out.println("Member saved to database");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deactivateMember(int memberId) {
        String sql = "UPDATE members SET active = FALSE WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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

                int id = rs.getInt("member_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String personalNumber = rs.getString("personal_number");
                MemberType type = MemberType.valueOf(rs.getString("member_type"));

                Member member = new Member(id, firstName, lastName, personalNumber, type);

                member.setActive(rs.getBoolean("active"));
                member.setBorrowedCount(rs.getInt("borrowed_count"));
                member.setLateReturnsCount(rs.getInt("late_returns_count"));
                member.setSuspensionsCount(rs.getInt("suspensions_count"));

                Date suspendedDate = rs.getDate("suspended_until");
                if (suspendedDate != null) {
                    member.setSuspendedUntil(suspendedDate.toLocalDate());
                }

                member.setBlacklisted(rs.getBoolean("blacklisted"));

                members.add(member);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return members;
    }

    @Override
    public void update(Member member) {
        String sql = "UPDATE members SET first_name = ?, last_name = ?, personal_number = ?, member_type = ?, active = ?, borrowed_count = ?, late_returns_count = ?, suspensions_count = ?, suspended_until = ?, blacklisted = ? WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getPersonalNumber());
            stmt.setString(4, member.getMemberType().name());
            stmt.setBoolean(5, member.isActive());
            stmt.setInt(6, member.getBorrowedCount());
            stmt.setInt(7, member.getLateReturnsCount());
            stmt.setInt(8, member.getSuspensionsCount());

            if (member.getSuspendedUntil() != null) {
                stmt.setDate(9, java.sql.Date.valueOf(member.getSuspendedUntil()));
            } else {
                stmt.setDate(9, null);
            }

            stmt.setBoolean(10, member.isBlacklisted());
            stmt.setInt(11, member.getMemberId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}