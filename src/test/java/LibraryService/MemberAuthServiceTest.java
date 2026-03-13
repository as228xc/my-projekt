package LibraryService;

import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryRepository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberAuthServiceTest {

    private MemberAuthService memberAuthService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberAuthService = new MemberAuthService(memberRepository);

    }

    @Test
    void logInMember_returnsNull_whenPasswordisBlank() {
        Member result = memberAuthService.loginMember(1001, " ");
        assertNull(result);
        verify(memberRepository, never()).findByIdAndPassword(anyInt(), anyString());

    }

    @Test
    void logInMember_returnsNull_whenMembersNotFound() {
        when(memberRepository.findByIdAndPassword(1001, "secret")).thenReturn(null);
        Member result = memberAuthService.loginMember(1001, "secret");
        assertNull(result);
        verify(memberRepository).findByIdAndPassword(1001, "secret");
    }

    @Test
    void logInMember_returnsNull_whenMemberInactive() {
        Member member = new Member(1001, "elin", "test", "199901012020", MemberType.MASTER, "secret");
        member.setActive(false);
        when(memberRepository.findByIdAndPassword(1001, "secret")).thenReturn(member);
        Member result = memberAuthService.loginMember(1001, "secret");
        assertNull(result);


    }

    @Test
    void loginMember_returnsNull_whenMemberIsBlacklisted() {
        Member member = new Member(1001, "elin", "test", "199901012020", MemberType.MASTER, "secret");
        member.setBlacklisted(true);
        when(memberRepository.findByIdAndPassword(1001, "secret")).thenReturn(member);
        Member result = memberAuthService.loginMember(1001, "secret");

        assertNull(result);
    }

    @Test
    void loginMember_returnsMember_whenLoginIsValid() {

        Member member = new Member(1001,"elin", "test", "199902021010", MemberType.MASTER, "secret");
        when(memberRepository.findByIdAndPassword(1001, "secret")).thenReturn(member);
        Member result = memberAuthService.loginMember(1001, "secret");
        assertNotNull(result);
        assertEquals(member, result);
    }

}