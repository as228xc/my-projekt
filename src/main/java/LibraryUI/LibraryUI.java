package LibraryUI;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryRepository.BookCopyRepository;
import LibraryRepository.BookRepository;
import LibraryRepository.LoanRepository;
import LibraryRepository.MemberRepository;
import LibraryService.LibrarySystem;
import PostgresRepository.PostgresBookCopyRepository;
import PostgresRepository.PostgresBookRepository;
import PostgresRepository.PostgresLoanRepository;
import PostgresRepository.PostgresMemberRepository;

import java.time.LocalDate;
import java.util.Scanner;

public class LibraryUI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MemberRepository memberRepo = new PostgresMemberRepository();
        BookRepository bookRepo = new PostgresBookRepository();
        BookCopyRepository bookCopyRepo = new PostgresBookCopyRepository();
        LoanRepository loanRepo = new PostgresLoanRepository();

        LibrarySystem library = new LibrarySystem(memberRepo, bookRepo, bookCopyRepo, loanRepo);

        while (true) {
            System.out.println("\nLIBRARY SYSTEM");
            System.out.println("1. Add member");
            System.out.println("2. Add book");
            System.out.println("3. Search book");
            System.out.println("4. Lend book");
            System.out.println("5. Return book");
            System.out.println("6. Search member");
            System.out.println("7. Show all members");
            System.out.println("8. Show all books");
            System.out.println("9. Remove member");
            System.out.println("10. Ban member");
            System.out.println("11. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addMember(scanner, library);
                case 2 -> addBook(scanner, library);
                case 3 -> searchBook(scanner, library);
                case 4 -> lendBook(scanner, library);
                case 5 -> returnBook(scanner, library);
                case 6 -> searchMember(scanner, library);
                case 7 -> library.showAllMembers();
                case 8 -> library.showAllBooks();
                case 9 -> removeMember(scanner, library);
                case 10 -> banMember(scanner, library);
                case 11 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void searchMember(Scanner scanner, LibrarySystem library) {
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();

        library.searchMemberById(memberId);
    }

    private static void addMember(Scanner scanner, LibrarySystem library) {
        System.out.print("Member ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Firstname: ");
        String firstName = scanner.nextLine();

        System.out.print("Lastname: ");
        String lastName = scanner.nextLine();

        System.out.print("Personalnumber: ");
        String personalNumber = scanner.nextLine();

        System.out.print("Member type (UNDERGRADUATE, MASTER, PHD, TEACHER): ");
        MemberType memberType = MemberType.valueOf(scanner.nextLine().toUpperCase());

        Member member = new Member(id, firstName, lastName, personalNumber, memberType);
        library.registerMember(member);
    }

    private static void addBook(Scanner scanner, LibrarySystem library) {
        System.out.print("ISBN: ");
        int isbn = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Author: ");
        String author = scanner.nextLine();

        System.out.print("Number of copies: ");
        int copies = scanner.nextInt();
        scanner.nextLine();

        BookTitle book = new BookTitle(isbn, title, author, copies);
        library.addBook(book);
    }

    private static void searchBook(Scanner scanner, LibrarySystem library) {
        System.out.print("Enter ISBN, title or author: ");
        String query = scanner.nextLine();
        library.searchBooks(query);
    }

    private static void lendBook(Scanner scanner, LibrarySystem library) {
        System.out.print("Member ID: ");
        int memberId = scanner.nextInt();

        System.out.print("ISBN: ");
        int isbn = scanner.nextInt();
        scanner.nextLine();

        library.lendBook(memberId, isbn, LocalDate.now());
    }

    private static void returnBook(Scanner scanner, LibrarySystem library) {
        System.out.print("Member ID: ");
        int memberId = scanner.nextInt();

        System.out.print("ISBN: ");
        int isbn = scanner.nextInt();
        scanner.nextLine();

        library.returnBook(memberId, isbn, LocalDate.now());
    }

    private static void removeMember(Scanner scanner, LibrarySystem library) {
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();

        library.removeMember(memberId);
    }

    private static void banMember(Scanner scanner, LibrarySystem library) {
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();

        library.banMember(memberId);
    }
}