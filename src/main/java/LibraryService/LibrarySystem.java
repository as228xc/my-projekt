package LibraryService;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;
import LibraryRepository.BookRepository;
import LibraryRepository.LoanRepository;
import LibraryRepository.MemberRepository;
import LibraryRepository.BookCopyRepository;
import java.time.LocalDate;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LibrarySystem {

    private static final Logger logger = LogManager.getLogger(LibrarySystem.class);
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LoanRepository loanRepository;

    public LibrarySystem(MemberRepository memberRepository, BookRepository bookRepository, BookCopyRepository bookCopyRepository,LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.loanRepository
    }

    public void showAllMembers() {
        List<Member> members = memberRepository.findAll();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            logger.info("Show all members requested, but no members were found.");
            return;
        }

        logger.info("Showing all members. Count={}", members.size());

        System.out.println("\n ALL MEMBERS");
        for (Member member : members) {
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Name: " + member.getFirstName() + " " + member.getLastName());
            System.out.println("Personal number: " + member.getPersonalNumber());
            System.out.println("Member type: " + member.getMemberType());
            System.out.println("Active account: " + member.isActive());
            System.out.println("Borrowed books: " + member.getBorrowedCount());
            System.out.println("Late returns: " + member.getLateReturnsCount());
            System.out.println("Suspensions: " + member.getSuspensionsCount());
            System.out.println("Suspended until: " + member.getSuspendedUntil());
            System.out.println("-------------------------");
        }
    }

    public void showAllBooks() {
        List<BookTitle> books = bookRepository.findAll();

        if (books.isEmpty()) {
            System.out.println("No books found.");
            logger.info("Show all books requested, but no books were found.");
            return;
        }

        logger.info("Showing all books. Count={}", books.size());

        System.out.println("\n ALL BOOKS");
        for (BookTitle book : books) {
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Total copies: " + book.getTotalCopies());
            System.out.println("Available copies: " + book.getAvailableCopies());
            System.out.println(" ");
        }
    }

    public void registerMember(Member member) {

        if (member == null) {
            System.out.println("Member cannot be null.");
            logger.error("Attempted to register a null member.");
            return;
        }

        String personalNumber = member.getPersonalNumber();

        if (personalNumber == null || personalNumber.isBlank()) {
            System.out.println("Personal number is required.");
            logger.warn("Registration failed: missing personal number for memberId={}", member.getMemberId());
            return;
        }

        if (!personalNumber.matches("\\d+")) {
            System.out.println("Personal number must contain only digits.");
            logger.warn("Registration failed: invalid personal number '{}' for memberId={}", personalNumber, member.getMemberId());
            return;
        }

        Member existingByPersonalNumber = memberRepository.findByPersonalNumber(personalNumber);

        if (existingByPersonalNumber != null) {
            if (existingByPersonalNumber.isBlacklisted()) {
                System.out.println("Registration is not allowed. This person is blocked due to violation of library regulations.");
                logger.warn("Registration denied: blacklisted person tried to register again. personalNumber={}", personalNumber);
            } else {
                System.out.println("This person is already registered.");
                logger.info("Registration skipped: person already registered. personalNumber={}", personalNumber);
            }
            return;
        }

        Member existingById = memberRepository.findById(member.getMemberId());

        if (existingById != null) {
            System.out.println("Member ID already exists.");
            logger.warn("Registration failed: memberId {} already exists.", member.getMemberId());
            return;
        }

        memberRepository.save(member);
        System.out.println("Member registered successfully.");
        logger.info("Member registered successfully. memberId={}, personalNumber={}", member.getMemberId(), personalNumber);
    }

    public void searchMemberById(int memberId) {
        Member member = memberRepository.findById(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            logger.warn("Member search failed. memberId={} not found.", memberId);
            return;
        }

        logger.info("Member found. memberId={}", memberId);

        System.out.println("\n MEMBER INFORMATION ");
        System.out.println("Member ID: " + member.getMemberId());
        System.out.println("Firstname: " + member.getFirstName());
        System.out.println("Lastname: " + member.getLastName());
        System.out.println("Personalnumber: " + member.getPersonalNumber());
        System.out.println("Member type: " + member.getMemberType());
        System.out.println("Account active: " + member.isActive());
        System.out.println("Borrowed books: " + member.getBorrowedCount());
        System.out.println("Late returns: " + member.getLateReturnsCount());
        System.out.println("Number of suspensions: " + member.getSuspensionsCount());
        System.out.println("Suspended until: " + member.getSuspendedUntil());
    }

    public void addBook(BookTitle book) {
        BookTitle existing = bookRepository.findByIsbn(book.getIsbn());

        if (existing != null) {
            System.out.println("A book with this ISBN already exists.");
            logger.warn("Add book failed. ISBN {} already exists.", book.getIsbn());
            return;
        }

        bookRepository.save(book);
        bookCopyRepository.createCopies(book.getIsbn(), book.getTotalCopies());
        System.out.println("Book added successfully.");
        logger.info("Book added successfully. ISBN={}, title={}", book.getIsbn(), book.getTitle());
    }

    public void searchBookByIsbn(int isbn) {
        BookTitle book = bookRepository.findByIsbn(isbn);

        if (book == null) {
            System.out.println("Book not found.");
            logger.warn("Book search failed. ISBN {} not found.", isbn);
            return;
        }

        logger.info("Book found. ISBN={}, title={}", isbn, book.getTitle());

        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Available copies: " + book.getAvailableCopies());

        if (book.hasAvailableCopy()) {
            System.out.println("This book can be borrowed.");
        } else {
            System.out.println("No copies available right now.");
            logger.info("Book ISBN {} found, but no copies are currently available.", isbn);
        }
    }

    public void lendBook(int memberId, int isbn, LocalDate today) {
        Member member = memberRepository.findById(memberId);
        BookTitle book = bookRepository.findByIsbn(isbn);

        if (member == null) {
            System.out.println("Member not found.");
            logger.warn("Lend book failed. Member {} not found.", memberId);
            return;
        }

        if (book == null) {
            System.out.println("Book not found.");
            logger.warn("Lend book failed. ISBN {} not found.", isbn);
            return;
        }

        if (!member.canBorrow(today)) {
            System.out.println("Member cannot borrow more books.");
            logger.warn("Lend book denied. Member {} is not allowed to borrow more books.", memberId);
            return;
        }

        Integer copyId = bookCopyRepository.findAvailableCopyIdByIsbn(isbn);

        if (copyId == null) {
            System.out.println("No available copies.");
            logger.warn("Lend book failed. No available copies for ISBN {}.", isbn);
            return;
        }

        bookCopyRepository.markCopyAsBorrowed(copyId);

        member.incrementBorrowedCount();
        memberRepository.update(member);

        Loan loan = new Loan(memberId, copyId, today, today.plusDays(14));
        loanRepository.save(loan);

        System.out.println("Book lent successfully.");
        logger.info("Book lent successfully. memberId={}, isbn={}, copyId={}", memberId, isbn, copyId);
    }

    public void returnBook(int memberId, int isbn, LocalDate today) {
        Member member = memberRepository.findById(memberId);
        BookTitle book = bookRepository.findByIsbn(isbn);

        if (member == null) {
            System.out.println("Member not found.");
            logger.warn("Return book failed. Member {} not found.", memberId);
            return;
        }

        if (book == null) {
            System.out.println("Book not found.");
            logger.warn("Return book failed. ISBN {} not found.", isbn);
            return;
        }

        book.returnOne();
        member.decrementBorrowedCount();

        bookRepository.update(book);
        memberRepository.update(member);

        System.out.println("Book returned successfully.");
        logger.info("Book returned successfully. memberId={}, isbn={}", memberId, isbn);
    }

    public void removeMember(int memberId) {

        Member member = memberRepository.findById(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            logger.warn("Attempted to deactivate non-existing memberId={}", memberId);
            return;
        }

        memberRepository.deactivateMember(memberId);
        System.out.println("Member account deactivated.");
        logger.info("Member account deactivated. memberId={}", memberId);
    }

    public void banMember(int memberId) {

        Member member = memberRepository.findById(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            logger.warn("Attempted to blacklist non-existing memberId={}", memberId);
            return;
        }

        memberRepository.blacklistMember(memberId);
        System.out.println("Member has been blacklisted and cannot register again.");
        logger.warn("Member {} was blacklisted.", memberId);
    }

    public void handleLateReturn(int memberId) {

        Member member = memberRepository.findById(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            logger.warn("Late return handling failed: member {} not found.", memberId);
            return;
        }

        member.registerLateReturn(java.time.LocalDate.now());

        logger.info("Late return registered for memberId={}. Late returns={}",
                memberId, member.getLateReturnsCount());

        if (member.getLateReturnsCount() > 2) {
            logger.warn("Member {} has exceeded allowed late returns and will be suspended.", memberId);
        }

        if (member.getSuspensionsCount() > 2) {
            memberRepository.blacklistMember(memberId);
            System.out.println("Member has been blacklisted due to repeated suspensions.");
            logger.error("Member {} was automatically blacklisted after more than two suspensions.", memberId);
        } else {
            memberRepository.update(member);
        }
    }
}