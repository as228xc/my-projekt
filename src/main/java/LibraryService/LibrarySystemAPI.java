package LibraryService;

import LibraryDomain.BookTitle;
import LibraryDomain.Member;

import java.time.LocalDate;

public interface LibrarySystemAPI {

    void registerMember(Member member);
    void addBook(BookTitle book);
    void deleteBook(int isbn);
    void lendBook(int memberId, int isbn, LocalDate date);
    void returnBook(int memberId, int isbn, LocalDate date);
    void findMemberById(int memberId);
    void getAllMembers();
    void getAllBooks();
    void removeMember(int memberId);
    void banMember(int memberId);
    void searchBooks(String query);
}