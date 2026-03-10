package LibraryRepository;

import LibraryDomain.Member;

import java.util.List;

public interface MemberRepository {
    Member findById(int memberId);
    Member findByPersonalNumber(String personalNumber);
    void deactivateMember(int memberId);
    void blacklistMember(int memberId);
    void save(Member member);
    void delete(int memberId);
    void update(Member member);
    List<Member> findAll();
}