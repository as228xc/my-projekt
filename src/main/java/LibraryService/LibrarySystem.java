package LibraryService;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;
import LibraryRepository.BookRepository;
import LibraryRepository.MemberRepository;
import java.time.LocalDate;
import java.util.*;

public class LibrarySystem {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public LibrarySystem(MemberRepository memberRepository, BookRepository bookRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    public void showAllMembers() {
        List<Member> members = memberRepository.findAll();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

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
            return;
        }

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
        Member existing = memberRepository.findById(member.getMemberId());

        if (existing != null) {
            System.out.println("Member already exists.");
            return;
        }
        String personalNumber = member.getPersonalNumber();

        if (!personalNumber.matches("\\d+")) {
            System.out.println("Personal number must contain only digits.");
            return;
        }
        memberRepository.save(member);

        System.out.println("Member registered successfully.");
    }

    public void searchMemberById(int memberId) {
        Member member = memberRepository.findById(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

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
            System.out.println("Book already exists.");
            return;
        }

        bookRepository.save(book);
        System.out.println("Book added successfully.");
    }

    public void searchBookByIsbn(int isbn) {
        BookTitle book = bookRepository.findByIsbn(isbn);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Available copies: " + book.getAvailableCopies());

        if (book.hasAvailableCopy()) {
            System.out.println("This book can be borrowed.");
        } else {
            System.out.println("No copies available right now.");
        }
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

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        book.returnOne();
        member.decrementBorrowedCount();

        bookRepository.save(book);
        memberRepository.save(member);

        System.out.println("Book returned successfully.");
    }
}