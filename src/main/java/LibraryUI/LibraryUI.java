package LibraryUI;

import LibraryDomain.*;
import LibraryService.LibrarySystem;
import LibraryRepository.*;
import PostgresRepository.*;

import java.time.LocalDate;
import java.util.Scanner;

public class LibraryUI {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        MemberRepository memberRepo = new PostgresMemberRepository();
        BookRepository bookRepo = new PostgresBookRepository();

        LibrarySystem library = new LibrarySystem(memberRepo, bookRepo);

        while (true) {

            System.out.println("\n===== LIBRARY SYSTEM =====");
            System.out.println("1. Register member");
            System.out.println("2. Add book");
            System.out.println("3. Lend book");
            System.out.println("4. Return book");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> registerMember(scanner, library);

                case 2 -> addBook(scanner, library);

                case 3 -> lendBook(scanner, library);

                case 4 -> returnBook(scanner, library);

                case 5 -> {
                    System.out.println("Goodbye");
                    return;
                }

                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void registerMember(Scanner scanner, LibrarySystem library) {

        System.out.print("Member ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("First name: ");
        String first = scanner.nextLine();

        System.out.print("Last name: ");
        String last = scanner.nextLine();

        System.out.print("Personal number: ");
        String pnr = scanner.nextLine();

        System.out.println("Type (UNDERGRADUATE / MASTER / PHD / TEACHER): ");
        MemberType type = MemberType.valueOf(scanner.nextLine().toUpperCase());

        Member member = new Member(id, first, last, pnr, type);

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

        BookTitle book = new BookTitle(isbn, title, author, copies);

        library.addBook(book);
    }

    private static void lendBook(Scanner scanner, LibrarySystem library) {

        System.out.print("Member ID: ");
        int memberId = scanner.nextInt();

        System.out.print("ISBN: ");
        int isbn = scanner.nextInt();

        library.lendBook(memberId, isbn, LocalDate.now());
    }

    private static void returnBook(Scanner scanner, LibrarySystem library) {

        System.out.print("Member ID: ");
        int memberId = scanner.nextInt();

        System.out.print("ISBN: ");
        int isbn = scanner.nextInt();

        library.returnBook(memberId, isbn, LocalDate.now());
    }
}