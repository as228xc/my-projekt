package LibraryUI;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryService.AuthService;
import LibraryService.LibrarySystemAPI;

import java.time.LocalDate;
import java.util.Scanner;

public class LibraryUI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LibrarySystemAPI library = App.createSystem();
        AuthService authService = App.createAuthService();

        while (true) {
            System.out.println("\nLIBRARY SYSTEM");
            System.out.println("1. Login as admin");
            System.out.println("2. Continue without admin login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = readInt(scanner);

            switch (choice) {
                case 1 -> adminLogin(scanner, authService, library);
                case 2 -> userMenu(scanner, library);
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void adminLogin(Scanner scanner, AuthService authService, LibrarySystemAPI library) {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = authService.loginAdmin(username, password);

        if (success) {
            System.out.println("Admin login successful.");
            adminMenu(scanner, library);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void adminMenu(Scanner scanner, LibrarySystemAPI library) {
        while (true) {
            System.out.println("\nADMIN MENU");
            System.out.println("1. Add member");
            System.out.println("2. Add book");
            System.out.println("3. Search book (ISBN/title/author)");
            System.out.println("4. Lend book");
            System.out.println("5. Return book");
            System.out.println("6. Search member by ID");
            System.out.println("7. Show all members");
            System.out.println("8. Show all books");
            System.out.println("9. Remove member");
            System.out.println("10. Ban member");
            System.out.println("11. Delete book");
            System.out.println("12. Logout");
            System.out.print("Choose option: ");

            int choice = readInt(scanner);

            switch (choice) {
                case 1 -> addMember(scanner, library);
                case 2 -> addBook(scanner, library);
                case 3 -> searchBooks(scanner, library);
                case 4 -> lendBook(scanner, library);
                case 5 -> returnBook(scanner, library);
                case 6 -> searchMember(scanner, library);
                case 7 -> library.getAllMembers();
                case 8 -> library.getAllBooks();
                case 9 -> removeMember(scanner, library);
                case 10 -> banMember(scanner, library);
                case 11 -> deleteBook(scanner, library);
                case 12 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void userMenu(Scanner scanner, LibrarySystemAPI library) {
        while (true) {
            System.out.println("\nUSER MENU");
            System.out.println("1. Search book (ISBN/title/author)");
            System.out.println("2. Lend book");
            System.out.println("3. Return book");
            System.out.println("4. Show all books");
            System.out.println("5. Back");
            System.out.print("Choose option: ");

            int choice = readInt(scanner);

            switch (choice) {
                case 1 -> searchBooks(scanner, library);
                case 2 -> lendBook(scanner, library);
                case 3 -> returnBook(scanner, library);
                case 4 -> library.getAllBooks();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static void addMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Member ID: ");
        int id = readInt(scanner);

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

    private static void addBook(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("ISBN: ");
        int isbn = readInt(scanner);

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Author: ");
        String author = scanner.nextLine();

        System.out.print("Number of copies: ");
        int copies = readInt(scanner);

        BookTitle book = new BookTitle(isbn, title, author, copies);
        library.addBook(book);
    }

    private static void searchBooks(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter ISBN, title or author: ");
        String query = scanner.nextLine();
        library.searchBooks(query);
    }

    private static void searchMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter member ID: ");
        int memberId = readInt(scanner);
        library.findMemberById(memberId);
    }

    private static void lendBook(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Member ID: ");
        int memberId = readInt(scanner);

        System.out.print("ISBN: ");
        int isbn = readInt(scanner);

        library.lendBook(memberId, isbn, LocalDate.now());
    }

    private static void returnBook(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Member ID: ");
        int memberId = readInt(scanner);

        System.out.print("ISBN: ");
        int isbn = readInt(scanner);

        library.returnBook(memberId, isbn, LocalDate.now());
    }

    private static void removeMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter member ID: ");
        int memberId = readInt(scanner);
        library.removeMember(memberId);
    }

    private static void banMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter member ID: ");
        int memberId = readInt(scanner);
        library.banMember(memberId);
    }

    private static void deleteBook(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter ISBN of the book to delete: ");
        int isbn = readInt(scanner);
        library.deleteBook(isbn);
    }
}