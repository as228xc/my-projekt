package LibraryUI;


import LibraryRepository.BookCopyRepository;
import LibraryRepository.BookRepository;
import LibraryRepository.LoanRepository;
import LibraryRepository.MemberRepository;
import LibraryService.LibrarySystem;
import LibraryService.LibrarySystemAPI;
import PostgresRepository.PostgresBookCopyRepository;
import PostgresRepository.PostgresBookRepository;
import PostgresRepository.PostgresLoanRepository;
import PostgresRepository.PostgresMemberRepository;

public class App {

    public static LibrarySystemAPI createSystem(){
        MemberRepository memberRepo = new PostgresMemberRepository();
        BookRepository bookRepo = new PostgresBookRepository();
        BookCopyRepository bookCopyRepo = new PostgresBookCopyRepository();
        LoanRepository loanRepo = new PostgresLoanRepository();

        return new LibrarySystem(memberRepo, bookRepo, bookCopyRepo, loanRepo);

    }


}
