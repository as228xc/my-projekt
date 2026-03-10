package LibraryDomain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void undergraduateShouldHaveMaxThreeLoans() {
        Member member = new Member(1, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE);

        assertEquals(3, member.getMaxLoans());
    }

    @Test
    void teacherShouldHaveMaxTenLoans() {
        Member member = new Member(2, "Ophelia", "Huovila", "198001011234", MemberType.TEACHER);

        assertEquals(10, member.getMaxLoans());
    }

    @Test
    void memberShouldBeAbleToBorrowWhenActiveAndUnderLimit() {
        Member member = new Member(3, "Alma", "Svensson", "199501011234", MemberType.MASTER);

        assertTrue(member.canBorrow(LocalDate.now()));
    }

    @Test
    void memberShouldNotBeAbleToBorrowWhenAtLimit() {
        Member member = new Member(4, "Elin", "Tonning", "199701011234", MemberType.UNDERGRADUATE);

        member.incrementBorrowedCount();
        member.incrementBorrowedCount();
        member.incrementBorrowedCount();

        assertFalse(member.canBorrow(LocalDate.now()));
    }

    @Test
    void memberShouldBeSuspendedAfterMoreThanTwoLateReturns() {
        Member member = new Member(5, "Sara", "Holm", "199301011234", MemberType.PHD);
        LocalDate today = LocalDate.of(2026, 3, 10);

        member.registerLateReturn(today);
        member.registerLateReturn(today);
        member.registerLateReturn(today);

        assertTrue(member.isSuspended(today.plusDays(1)));
    }

    @Test
    void memberShouldBecomeInactiveAfterMoreThanTwoSuspensions() {
        Member member = new Member(6, "Erik", "Berg", "199201011234", MemberType.MASTER);
        LocalDate today = LocalDate.of(2026, 3, 10);

        member.suspend(today.plusDays(15));
        member.suspend(today.plusDays(15));
        member.suspend(today.plusDays(15));

        assertFalse(member.isActive());
    }
}
