package LibraryDomain;

import java.time.LocalDate;

public class Loan {
    private final int memberId;
    private final int copyId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;

    public Loan(int memberId, int copyId, LocalDate loanDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.copyId = copyId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getCopyId() {
        return copyId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}