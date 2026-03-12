package LibraryService;

import LibraryDomain.Member;
import LibraryRepository.MemberRepository;

public class MemberAuthService {

    private final MemberRepository memberRepository;

    public MemberAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member loginMember(int memberId, String password) {
        if (password == null || password.isBlank()) {
            return null;
        }

        Member member = memberRepository.findByIdAndPassword(memberId, password);

        if (member == null) {
            return null;
        }

        if (!member.isActive() || member.isBlacklisted()) {
            return null;
        }

        return member;
    }
}