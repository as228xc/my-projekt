package LibraryRepository;

import LibraryDomain.Member;

public interface MemberRepository {

    Member findById(int memberId);

    void save(Member member);

    void delete(int memberId);

}