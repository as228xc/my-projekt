package LibraryDomain;

import java.time.LocalDate;

public class Loan {
    private final int memberId;
    private final int isbn;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(int memberId, int isbn, LocalDate loanDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.isbn = isbn;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public void markReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isLate(LocalDate today) {
        LocalDate comparisonDate = isReturned() ? returnDate : today;
        return comparisonDate.isAfter(dueDate);
    }

    public int getMemberId() {
        return memberId;
    }

    public int getIsbn() {
        return isbn;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}