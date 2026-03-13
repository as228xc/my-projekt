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
        Member member = new Member(1, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "test");

        when(memberRepository.findByPersonalNumber("199901011234")).thenReturn(null);
        when(memberRepository.findById(1)).thenReturn(null);

        librarySystem.registerMember(member);

        verify(memberRepository).save(member);
    }

    @Test
    void registerMemberShouldNotSaveWhenPersonalNumberAlreadyExists() {
        Member existing = new Member(1, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "test");
        Member newMember = new Member(2, "Anna", "Karlsson", "199901011234", MemberType.UNDERGRADUATE, "test");

        when(memberRepository.findByPersonalNumber("199901011234")).thenReturn(existing);

        librarySystem.registerMember(newMember);

        verify(memberRepository, never()).save(any());
    }

    @Test
    void addBookShouldSaveTitleAndCreateCopies() {
        BookTitle book = new BookTitle(123, "Java Basics", "Smith", 2);

        when(bookRepository.findByIsbn(123)).thenReturn(null);

        librarySystem.addBook(book);

        verify(bookRepository).save(book);
        verify(bookCopyRepository).createCopies(123, 2);
    }

    @Test
    void lendBookShouldBorrowAvailableCopyAndCreateLoan() {
        Member member = new Member(10, "Olle", "Persson", "199501011234", MemberType.MASTER, "test");
        BookTitle book = new BookTitle(123, "Java Basics", "Smith", 2);
        LocalDate today = LocalDate.of(2026, 3, 10);

        when(memberRepository.findById(10)).thenReturn(member);
        when(bookRepository.findByIsbn(123)).thenReturn(book);
        when(bookCopyRepository.findAvailableCopyIdByIsbn(123)).thenReturn(55);
        when(loanRepository.countActiveLoansByMemberId(10)).thenReturn(1);

        librarySystem.lendBook(10, 123, today);

        verify(bookCopyRepository).markCopyAsBorrowed(55);
        verify(loanRepository).save(any(Loan.class));
        verify(memberRepository).update(member);

        assertEquals(1, member.getBorrowedCount());
    }

    @Test
    void lendBookShouldDoNothingWhenNoCopyAvailable() {
        Member member = new Member(10, "Olle", "Persson", "199501011234", MemberType.MASTER, "test");
        BookTitle book = new BookTitle(123, "Java Basics", "Smith", 2);
        LocalDate today = LocalDate.of(2026, 3, 10);

        when(memberRepository.findById(10)).thenReturn(member);
        when(bookRepository.findByIsbn(123)).thenReturn(book);
        when(bookCopyRepository.findAvailableCopyIdByIsbn(123)).thenReturn(null);

        librarySystem.lendBook(10, 123, today);

        verify(bookCopyRepository, never()).markCopyAsBorrowed(anyInt());
        verify(loanRepository, never()).save(any());
        verify(memberRepository, never()).update(any());
    }

    @Test
    void returnBookShouldMarkCopyReturnedAndUpdateMember() {
        Member member = new Member(10, "Olle", "Persson", "199501011234", MemberType.MASTER, "test");
        LocalDate today = LocalDate.of(2026, 3, 10);

        Loan loan = new Loan(10, 55, today.minusDays(3), today.plusDays(11));

        when(memberRepository.findById(10)).thenReturn(member);
        when(loanRepository.findActiveLoanByMemberIdAndIsbn(10, 123)).thenReturn(loan);
        when(loanRepository.countActiveLoansByMemberId(10)).thenReturn(0);

        librarySystem.returnBook(10, 123, today);

        verify(bookCopyRepository).markCopyAsReturned(55);
        verify(loanRepository).markLoanAsReturned(55);
        verify(memberRepository).update(member);

        assertEquals(0, member.getBorrowedCount());
    }

    @Test
    void returnBookShouldRegisterLateReturnWhenReturnedLate() {
        Member member = new Member(10, "Olle", "Persson", "199501011234", MemberType.MASTER, "test");
        LocalDate today = LocalDate.of(2026, 3, 10);

        Loan loan = new Loan(10, 55, today.minusDays(20), today.minusDays(5));

        when(memberRepository.findById(10)).thenReturn(member);
        when(loanRepository.findActiveLoanByMemberIdAndIsbn(10, 123)).thenReturn(loan);
        when(loanRepository.countActiveLoansByMemberId(10)).thenReturn(0);

        librarySystem.returnBook(10, 123, today);

        assertEquals(1, member.getLateReturnsCount());
        verify(memberRepository).update(member);
    }
}