package LibraryDomain;

import java.time.LocalDate;

public class Member {
    private int memberId;
    private String firstName;
    private String lastName;
    private int personalNumber;
    private MemberType memberType;

    private boolean AccountStatus = true;
    private int borrowedCount = 0;
    private int lateReturnsCount = 0;
    private int suspensionsCount = 0;
    private LocalDate suspendedUntil;

    public Member(int memberId, String firstName, String lastName, int personalNumber, MemberType memberType) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.memberType = memberType;
    }

    public int getMaxLoans() {
        return switch (memberType) {
            case UNDERGRADUATE -> 3;
            case MASTER -> 5;
            case PHD -> 7;
            case TEACHER -> 10;
        };
    }

    public boolean isSuspended(LocalDate today) {
        if (suspendedUntil == null) {
            return false;
        }
        return today.isBefore(suspendedUntil);
    }

    //public boolean isSuspended() {
    //    return LocalDate.now().isBefore(suspendedUntil);
    //}

    public boolean canBorrow(LocalDate today) {
        return AccountStatus &&
                !isSuspended(today) &&
                borrowedCount < getMaxLoans();
    }

    public void incrementBorrowedCount() {
        borrowedCount++;
    }

    public void decrementBorrowedCount() {
        if (borrowedCount > 0) {
            borrowedCount--;
        }
    }

    public void registerLateReturn(LocalDate today) {
        lateReturnsCount++;

        if (lateReturnsCount > 2) {
            suspend(today.plusDays(15));
        }
    }

    public void suspend(LocalDate until) {
        suspensionsCount++;
        suspendedUntil = until;

        if (suspensionsCount > 2) {
            AccountStatus = false;
        }
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public boolean isActive() {
        return AccountStatus;
    }

    public int getLateReturnsCount() {
        return lateReturnsCount;
    }

    public int getSuspensionsCount() {
        return suspensionsCount;
    }
}

