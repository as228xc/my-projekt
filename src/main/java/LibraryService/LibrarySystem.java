package LibraryService;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;
import LibraryRepository.BookRepository;
import LibraryRepository.MemberRepository;
import java.time.LocalDate;

public class LibrarySystem {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public LibrarySystem(MemberRepository memberRepository,
                          BookRepository bookRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    public void registerMember(Member member) {

        Member existing = memberRepository.findById(member.getMemberId());

        if (existing != null) {
            System.out.println("Member already registered.");
            return;
        }

        memberRepository.save(member);
        System.out.println("Member registered successfully.");
    }

    public void addBook(BookTitle book) {

        BookTitle existing = bookRepository.findByIsbn(book.getIsbn());

        if (existing != null) {
            System.out.println("Book already exists in library.");
            return;
        }

        bookRepository.save(book);
        System.out.println("Book added successfully.");
    }

    public void lendBook(int memberId, int isbn, LocalDate today) {

        Member member = memberRepository.findById(memberId);
        BookTitle book = bookRepository.findByIsbn(isbn);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (!member.canBorrow(today)) {
            System.out.println("Member cannot borrow more books.");
            return;
        }

        if (!book.hasAvailableCopy()) {
            System.out.println("No copies available.");
            return;
        }

        book.borrowOne();
        member.incrementBorrowedCount();

        bookRepository.save(book);
        memberRepository.save(member);

        System.out.println("Book lent successfully.");
    }

    public void returnBook(int memberId, int isbn, LocalDate today) {

        Member member = memberRepository.findById(memberId);
        BookTitle book = bookRepository.findByIsbn(isbn);

        if (member == null || book == null) {
            System.out.println("Invalid member or book.");
            return;
        }

        book.returnOne();
        member.decrementBorrowedCount();

        bookRepository.save(book);
        memberRepository.save(member);

        System.out.println("Book returned successfully.");
    }

}