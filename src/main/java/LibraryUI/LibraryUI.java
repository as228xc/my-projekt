package LibraryUI;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;
import LibraryDomain.MemberType;
import LibraryService.AuthService;
import LibraryService.LibrarySystemAPI;
import LibraryService.MemberAuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Scanner;

public class LibraryUI {

    private static final Logger logger = LogManager.getLogger(LibraryUI.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        logger.info("Starting Library System UI");

        LibrarySystemAPI library = App.createSystem();
        AuthService authService = App.createAuthService();
        MemberAuthService memberAuthService = App.createMemberAuthService();

        while (true) {
            System.out.println("\nLIBRARY SYSTEM");
            System.out.println("1. Login as admin");
            System.out.println("2. Login as member");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = readInt(scanner);
            logger.debug("Main menu choice selected: {}", choice);

            switch (choice) {
                case 1 -> adminLogin(scanner, authService, library);
                case 2 -> memberLogin(scanner, memberAuthService, library);
                case 3 -> {
                    logger.info("System closed by user");
                    System.out.println("Goodbye!");
                    return;
                }
                default -> {
                    logger.warn("Invalid main menu choice: {}", choice);
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void adminLogin(Scanner scanner, AuthService authService, LibrarySystemAPI library) {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        logger.info("Admin login attempt for username={}", username);

        boolean success = authService.loginAdmin(username, password);

        if (success) {
            logger.info("Admin login successful for username={}", username);
            System.out.println("Admin login successful.");
            adminMenu(scanner, library);
        } else {
            logger.warn("Admin login failed for username={}", username);
            System.out.println("Invalid username or password.");
        }
    }

    private static void memberLogin(Scanner scanner, MemberAuthService memberAuthService, LibrarySystemAPI library) {
        System.out.print("Member ID (4 digits): ");
        int memberId = readMemberId(scanner);

        System.out.print("Password: ");
        String password = scanner.nextLine();

        logger.info("Member login attempt for memberId={}", memberId);

        Member member = memberAuthService.loginMember(memberId, password);

        if (member != null) {
            logger.info("Member login successful for memberId={}", memberId);
            System.out.println("Member login successful. Welcome " + member.getFirstName() + "!");
            memberMenu(scanner, library, member);
        } else {
            logger.warn("Member login failed for memberId={}", memberId);
            System.out.println("Invalid member ID or password.");
        }
    }

    private static void adminMenu(Scanner scanner, LibrarySystemAPI library) {
        logger.debug("Entered admin menu");

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
            System.out.println("9. Delete member");
            System.out.println("10. Ban member");
            System.out.println("11. Delete book");
            System.out.println("12. Logout");
            System.out.print("Choose option: ");

            int choice = readInt(scanner);
            logger.debug("Admin menu choice selected: {}", choice);

            switch (choice) {
                case 1 -> addMember(scanner, library);
                case 2 -> addBook(scanner, library);
                case 3 -> searchBooks(scanner, library);
                case 4 -> lendBook(scanner, library);
                case 5 -> returnBook(scanner, library);
                case 6 -> searchMember(scanner, library);
                case 7 -> {
                    logger.info("Admin requested all members");
                    library.getAllMembers();
                }
                case 8 -> {
                    logger.info("Admin requested all books");
                    library.getAllBooks();
                }
                case 9 -> removeMember(scanner, library);
                case 10 -> banMember(scanner, library);
                case 11 -> deleteBook(scanner, library);
                case 12 -> {
                    logger.info("Admin logged out");
                    System.out.println("Logged out.");
                    return;
                }
                default -> {
                    logger.warn("Invalid admin menu choice: {}", choice);
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void memberMenu(Scanner scanner, LibrarySystemAPI library, Member member) {
        logger.debug("Entered member menu for memberId={}", member.getMemberId());

        while (true) {
            System.out.println("\nMEMBER MENU");
            System.out.println("1. Search book (ISBN/title/author)");
            System.out.println("2. Borrow book");
            System.out.println("3. Return book");
            System.out.println("4. Show all books");
            System.out.println("5. My information");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");

            int choice = readInt(scanner);
            logger.debug("Member menu choice selected: {} for memberId={}", choice, member.getMemberId());

            switch (choice) {
                case 1 -> searchBooks(scanner, library);
                case 2 -> lendBookAsLoggedInMember(scanner, library, member);
                case 3 -> returnBookAsLoggedInMember(scanner, library, member);
                case 4 -> {
                    logger.info("Member {} requested all books", member.getMemberId());
                    library.getAllBooks();
                }
                case 5 -> {
                    logger.info("Member {} requested own information", member.getMemberId());
                    library.findMemberById(member.getMemberId());
                }
                case 6 -> {
                    logger.info("Member {} logged out", member.getMemberId());
                    System.out.println("Logged out.");
                    return;
                }
                default -> {
                    logger.warn("Invalid member menu choice: {} for memberId={}", choice, member.getMemberId());
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            logger.warn("Invalid numeric input entered");
            System.out.print("Please enter a number: ");
            scanner.nextLine();
        }

        int value = scanner.nextInt();
        scanner.nextLine();
        logger.debug("Numeric input read: {}", value);
        return value;
    }

    private static int readMemberId(Scanner scanner) {
        while (true) {
            int id = readInt(scanner);

            if (id >= 1000 && id <= 9999) {
                logger.debug("Valid member ID entered: {}", id);
                return id;
            }

            logger.warn("Invalid member ID entered: {}", id);
            System.out.print("Member ID must be exactly 4 digits (1000-9999). Try again: ");
        }
    }

    private static int readIsbn(Scanner scanner) {
        while (true) {
            int isbn = readInt(scanner);

            if (isbn >= 100000 && isbn <= 999999) {
                logger.debug("Valid ISBN entered: {}", isbn);
                return isbn;
            }

            logger.warn("Invalid ISBN entered: {}", isbn);
            System.out.print("ISBN must be exactly 6 digits (100000-999999). Try again: ");
        }
    }

    private static int readPositiveInt(Scanner scanner, String errorMessage) {
        while (true) {
            int value = readInt(scanner);

            if (value > 0) {
                logger.debug("Valid positive number entered: {}", value);
                return value;
            }

            logger.warn("Non-positive number entered: {}", value);
            System.out.print(errorMessage);
        }
    }

    private static void addMember(Scanner scanner, LibrarySystemAPI library) {
        logger.info("Admin started add member process");

        System.out.print("Member ID (4 digits): ");
        int id = readMemberId(scanner);

        System.out.print("Firstname: ");
        String firstName = scanner.nextLine();

        System.out.print("Lastname: ");
        String lastName = scanner.nextLine();

        System.out.print("Personalnumber: ");
        String personalNumber = scanner.nextLine();

        MemberType memberType;
        while (true) {
            System.out.print("Member type (UNDERGRADUATE, MASTER, PHD, TEACHER): ");
            try {
                memberType = MemberType.valueOf(scanner.nextLine().toUpperCase());
                logger.debug("Valid member type entered: {}", memberType);
                break;
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid member type entered");
                System.out.println("Invalid member type. Please try again.");
            }
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        logger.info("Attempting to register member with memberId={} and personalNumber={}", id, personalNumber);

        Member member = new Member(id, firstName, lastName, personalNumber, memberType, password);
        library.registerMember(member);
    }

    private static void addBook(Scanner scanner, LibrarySystemAPI library) {
        logger.info("Admin started add book process");

        System.out.print("ISBN (6 digits): ");
        int isbn = readIsbn(scanner);

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Author: ");
        String author = scanner.nextLine();

        System.out.print("Number of copies: ");
        int copies = readPositiveInt(scanner, "Number of copies must be greater than 0. Try again: ");

        logger.info("Attempting to add book with isbn={}, title={}, author={}, copies={}", isbn, title, author, copies);

        BookTitle book = new BookTitle(isbn, title, author, copies);
        library.addBook(book);
    }

    private static void searchBooks(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter ISBN, title or author: ");
        String query = scanner.nextLine();

        logger.info("Book search performed with query='{}'", query);
        library.searchBooks(query);
    }

    private static void searchMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter member ID (4 digits): ");
        int memberId = readMemberId(scanner);

        logger.info("Searching for member with memberId={}", memberId);
        library.findMemberById(memberId);
    }

    private static void lendBook(Scanner scanner, LibrarySystemAPI library) {
        logger.info("Admin started lend book process");

        System.out.print("Member ID (4 digits): ");
        int memberId = readMemberId(scanner);

        System.out.print("Search book by ISBN, title or author: ");
        String query = scanner.nextLine();

        logger.info("Admin searched books before lend with query='{}'", query);
        library.searchBooks(query);

        System.out.print("Enter ISBN (6 digits) of the book you want to borrow: ");
        int isbn = readIsbn(scanner);

        logger.info("Attempting to lend book isbn={} to memberId={}", isbn, memberId);
        library.lendBook(memberId, isbn, LocalDate.now());
    }

    private static void returnBook(Scanner scanner, LibrarySystemAPI library) {
        logger.info("Admin started return book process");

        System.out.print("Member ID (4 digits): ");
        int memberId = readMemberId(scanner);

        System.out.print("Search book by ISBN, title or author: ");
        String query = scanner.nextLine();

        logger.info("Admin searched books before return with query='{}'", query);
        library.searchBooks(query);

        System.out.print("Enter ISBN (6 digits) of the book you want to return: ");
        int isbn = readIsbn(scanner);

        logger.info("Attempting to return book isbn={} for memberId={}", isbn, memberId);
        library.returnBook(memberId, isbn, LocalDate.now());
    }

    private static void lendBookAsLoggedInMember(Scanner scanner, LibrarySystemAPI library, Member member) {
        logger.info("Member {} started borrow process", member.getMemberId());

        System.out.print("Search book by ISBN, title or author: ");
        String query = scanner.nextLine();

        logger.info("Member {} searched books before borrow with query='{}'", member.getMemberId(), query);
        library.searchBooks(query);

        System.out.print("Enter ISBN (6 digits) of the book you want to borrow: ");
        int isbn = readIsbn(scanner);

        logger.info("Member {} attempting to borrow isbn={}", member.getMemberId(), isbn);
        library.lendBook(member.getMemberId(), isbn, LocalDate.now());
    }

    private static void returnBookAsLoggedInMember(Scanner scanner, LibrarySystemAPI library, Member member) {
        logger.info("Member {} started return process", member.getMemberId());

        System.out.print("Search book by ISBN, title or author: ");
        String query = scanner.nextLine();

        logger.info("Member {} searched books before return with query='{}'", member.getMemberId(), query);
        library.searchBooks(query);

        System.out.print("Enter ISBN (6 digits) of the book you want to return: ");
        int isbn = readIsbn(scanner);

        logger.info("Member {} attempting to return isbn={}", member.getMemberId(), isbn);
        library.returnBook(member.getMemberId(), isbn, LocalDate.now());
    }

    private static void removeMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter member ID (4 digits): ");
        int memberId = readMemberId(scanner);

        logger.warn("Admin attempting to remove memberId={}", memberId);
        library.removeMember(memberId);
    }

    private static void banMember(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter member ID (4 digits): ");
        int memberId = readMemberId(scanner);

        logger.warn("Admin attempting to ban memberId={}", memberId);
        library.banMember(memberId);
    }

    private static void deleteBook(Scanner scanner, LibrarySystemAPI library) {
        System.out.print("Enter ISBN (6 digits) of the book to delete: ");
        int isbn = readIsbn(scanner);

        logger.warn("Admin attempting to delete book with isbn={}", isbn);
        library.deleteBook(isbn);
    }
}