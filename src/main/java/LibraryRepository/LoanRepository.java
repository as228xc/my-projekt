package LibraryRepository;

import LibraryDomain.Loan;

public interface LoanRepository {
    void save(Loan loan);
    Loan findActiveLoanByMemberIdAndIsbn(int memberId, int isbn);
    void markLoanAsReturned(int copyId);
    int countActiveLoansByMemberId(int memberId);
}