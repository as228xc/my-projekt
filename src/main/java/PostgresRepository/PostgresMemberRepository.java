package PostgresRepository;

import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryRepository.MemberRepository;
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

                return new Member(id, firstName, lastName, personalNumber, type);
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
                    members.add(member);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return members;
        }
    }