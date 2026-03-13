package LibraryDomain;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void undergraduateShouldHaveMaxThreeLoans() {
        Member member = new Member(1000, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "test123");

        assertEquals(3, member.getMaxLoans());
    }

    @Test
    void teacherShouldHaveMaxTenLoans() {
        Member member = new Member(2000, "Ophelia", "Huovila", "198001011234", MemberType.TEACHER, "test");

        assertEquals(10, member.getMaxLoans());
    }

    @Test
    void memberShouldBeAbleToBorrowWhenActiveAndUnderLimit() {
        Member member = new Member(3000, "Alma", "Svensson", "199501011234", MemberType.MASTER, "test");

        assertTrue(member.canBorrow(LocalDate.now()));
    }

    @Test
    void memberShouldNotBeAbleToBorrowWhenAtLimit() {
        Member member = new Member(4000, "Elin", "Tonning", "199701011234", MemberType.UNDERGRADUATE, "test");

        member.incrementBorrowedCount();
        member.incrementBorrowedCount();
        member.incrementBorrowedCount();

        assertFalse(member.canBorrow(LocalDate.now()));
    }

    @Test
    void memberShouldBeSuspendedAfterMoreThanTwoLateReturns() {
        Member member = new Member(5000, "Sara", "Holm", "199301011234", MemberType.PHD, "test");
        LocalDate today = LocalDate.of(2026, 3, 10);

        member.registerLateReturn(today);
        member.registerLateReturn(today);
        member.registerLateReturn(today);

        assertTrue(member.isSuspended(today.plusDays(1)));
    }

    @Test
    void memberShouldBecomeInactiveAfterMoreThanTwoSuspensions() {
        Member member = new Member(6000, "Erik", "Berg", "199201011234", MemberType.MASTER, "test");
        LocalDate today = LocalDate.of(2026, 3, 10);

        member.suspend(today.plusDays(15));
        member.suspend(today.plusDays(15));
        member.suspend(today.plusDays(15));

        assertFalse(member.isActive());
    }
}
