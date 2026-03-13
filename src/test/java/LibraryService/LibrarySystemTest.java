package LibraryService;

import LibraryDomain.BookTitle;
import LibraryDomain.Loan;
import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryRepository.BookCopyRepository;
import LibraryRepository.BookRepository;
import LibraryRepository.LoanRepository;
import LibraryRepository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibrarySystemTest {
    private MemberRepository memberRepository;
    private BookRepository bookRepository;
    private BookCopyRepository bookCopyRepository;
    private LoanRepository loanRepository;
    private LibrarySystem librarySystem;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        bookRepository = mock(BookRepository.class);
        bookCopyRepository = mock(BookCopyRepository.class);
        loanRepository = mock(LoanRepository.class);

        librarySystem = new LibrarySystem(
                memberRepository,
                bookRepository,
                bookCopyRepository,
                loanRepository
        );
    }

    @Test
    void registerMemberShouldSaveWhenMemberIsValid() {
        Member member = new Member(3231, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "hej123");

        when(memberRepository.findByPersonalNumber("199901011234")).thenReturn(null);
        when(memberRepository.findById(3231)).thenReturn(null);

        librarySystem.registerMember(member);

        verify(memberRepository).save(member);
    }

    @Test
    void registerMemberShouldNotSaveWhenPersonalNumberAlreadyExists() {
        Member existing = new Member(3231, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "hej");
        Member newMember = new Member(2434, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "hej");

        when(memberRepository.findByPersonalNumber("199901011234")).thenReturn(existing);

        librarySystem.registerMember(newMember);

        verify(memberRepository, never()).save(any());
    }

    @Test
    void addBookShouldSaveTitleAndCreateCopies() {
        BookTitle book = new BookTitle(123098, "Java Basics", "Smith", 2);

        when(bookRepository.findByIsbn(123098)).thenReturn(null);

        librarySystem.addBook(book);

        verify(bookRepository).save(book);
        verify(bookCopyRepository).createCopies(123098, 2);
    }

    @Test
    void lendBookShouldBorrowAvailableCopyAndCreateLoan() {
        Member member = new Member(1650, "Olle", "Persson", "199501011234", MemberType.MASTER, "halloj");
        BookTitle book = new BookTitle(123000, "Java Basics", "Smith", 2);
        LocalDate today = LocalDate.of(2026, 3, 10);

        when(memberRepository.findById(1650)).thenReturn(member);
        when(bookRepository.findByIsbn(123000)).thenReturn(book);
        when(bookCopyRepository.findAvailableCopyIdByIsbn(123000)).thenReturn(55);
        when(loanRepository.countActiveLoansByMemberId(1650)).thenReturn(1);

        librarySystem.lendBook(1650, 123000, today);

        verify(bookCopyRepository).markCopyAsBorrowed(55);
        verify(loanRepository).save(any(Loan.class));
        verify(memberRepository).update(member);

        assertEquals(1, member.getBorrowedCount());
    }

    @Test
    void lendBookShouldDoNothingWhenNoCopyAvailable() {
        Member member = new Member(1012, "Olle", "Persson", "199501011234", MemberType.MASTER, "test1");
        BookTitle book = new BookTitle(123000, "Java Basics", "Smith", 2);
        LocalDate today = LocalDate.of(2026, 3, 10);

        when(memberRepository.findById(1012)).thenReturn(member);
        when(bookRepository.findByIsbn(123000)).thenReturn(book);
        when(bookCopyRepository.findAvailableCopyIdByIsbn(123000)).thenReturn(null);

        librarySystem.lendBook(1012, 123000, today);

        verify(bookCopyRepository, never()).markCopyAsBorrowed(anyInt());
        verify(loanRepository, never()).save(any());
        verify(memberRepository, never()).update(any());
    }

    @Test
    void returnBookShouldMarkCopyReturnedAndUpdateMember() {
        Member member = new Member(1000, "Olle", "Persson", "199501011234", MemberType.MASTER, "test");
        LocalDate today = LocalDate.of(2026, 3, 10);

        Loan loan = new Loan(1000, 55, today.minusDays(3), today.plusDays(11));

        when(memberRepository.findById(1000)).thenReturn(member);
        when(loanRepository.findActiveLoanByMemberIdAndIsbn(1000, 123000)).thenReturn(loan);
        when(loanRepository.countActiveLoansByMemberId(10)).thenReturn(0);

        librarySystem.returnBook(1000, 123000, today);

        verify(bookCopyRepository).markCopyAsReturned(55);
        verify(loanRepository).markLoanAsReturned(55);
        verify(memberRepository).update(member);

        assertEquals(0, member.getBorrowedCount());
    }

    @Test
    void returnBookShouldRegisterLateReturnWhenReturnedLate() {
        Member member = new Member(1010, "Olle", "Persson", "199501011234", MemberType.MASTER, "hej");
        LocalDate today = LocalDate.of(2026, 3, 10);

        Loan loan = new Loan(1010, 55, today.minusDays(20), today.minusDays(5));

        when(memberRepository.findById(1010)).thenReturn(member);
        when(loanRepository.findActiveLoanByMemberIdAndIsbn(1010, 123000)).thenReturn(loan);
        when(loanRepository.countActiveLoansByMemberId(1010)).thenReturn(0);

        librarySystem.returnBook(1010, 123000, today);

        assertEquals(1, member.getLateReturnsCount());
        verify(memberRepository).update(member);
    }

    @Test
    void returnBookShouldSuspendMemberAfterThirdLateReturn() {
        Member member = new Member(1010, "Ophelia", "Persson", "199501011234", MemberType.MASTER, "hej");
        LocalDate today = LocalDate.of(2026, 3, 10);

        member.registerLateReturn(today.minusDays(20));
        member.registerLateReturn(today.minusDays(10));

        Loan loan = new Loan(1010, 55, today.minusDays(20), today.minusDays(5));

        when(memberRepository.findById(1010)).thenReturn(member);
        when(loanRepository.findActiveLoanByMemberIdAndIsbn(1010, 123000)).thenReturn(loan);
        when(loanRepository.countActiveLoansByMemberId(1010)).thenReturn(0);

        librarySystem.returnBook(1010, 123000, today);

        assertEquals(3, member.getLateReturnsCount());
        assertTrue(member.isSuspended(today));
        verify(memberRepository).update(member);
    }
}