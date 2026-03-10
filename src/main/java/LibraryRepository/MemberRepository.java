package LibraryRepository;

import LibraryDomain.Member;

import java.util.List;

public interface MemberRepository {

    Member findById(int memberId);

    void save(Member member);

    void delete(int memberId);

    List<Member> findAll();

}