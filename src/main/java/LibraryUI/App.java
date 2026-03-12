package LibraryUI;

import LibraryRepository.AdminRepository;
import LibraryRepository.BookCopyRepository;
import LibraryRepository.BookRepository;
import LibraryRepository.LoanRepository;
import LibraryRepository.MemberRepository;
import LibraryService.AuthService;
import LibraryService.LibrarySystem;
import LibraryService.LibrarySystemAPI;
import LibraryService.MemberAuthService;
import PostgresRepository.PostgresAdminRepository;
import PostgresRepository.PostgresBookCopyRepository;
import PostgresRepository.PostgresBookRepository;
import PostgresRepository.PostgresLoanRepository;
import PostgresRepository.PostgresMemberRepository;

public class App {

    public static LibrarySystemAPI createSystem() {
        MemberRepository memberRepo = new PostgresMemberRepository();
        BookRepository bookRepo = new PostgresBookRepository();
        BookCopyRepository bookCopyRepo = new PostgresBookCopyRepository();
        LoanRepository loanRepo = new PostgresLoanRepository();

        return new LibrarySystem(memberRepo, bookRepo, bookCopyRepo, loanRepo);
    }

    public static AuthService createAuthService() {
        AdminRepository adminRepository = new PostgresAdminRepository();
        return new AuthService(adminRepository);
    }

    public static MemberAuthService createMemberAuthService() {
        MemberRepository memberRepository = new PostgresMemberRepository();
        return new MemberAuthService(memberRepository);
    }
}