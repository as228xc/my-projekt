package PostgresRepository;

import LibraryDomain.Member;
import LibraryRepository.MemberRepository;

public class PostgresMemberRepository implements MemberRepository {

    @Override
    public Member findById(int memberId) {
        // här ska din PostgreSQL-kod ligga
        return null;
    }

    @Override
    public void save(Member member) {
        // insert/update i databasen
    }

    @Override
    public void delete(int memberId) {
        // delete i databasen
    }
}