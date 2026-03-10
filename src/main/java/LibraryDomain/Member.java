package LibraryDomain;

import java.time.LocalDate;

public class Member {
    private final int memberId;
    private final String firstName;
    private final String lastName;
    private final String personalNumber;
    private final MemberType memberType;

    private boolean active;
    private int borrowedCount;
    private int lateReturnsCount;
    private int suspensionsCount;
    private LocalDate suspendedUntil;

    public Member(int memberId, String firstName, String lastName,
                  String personalNumber, MemberType memberType) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.memberType = memberType;
        this.active = true;
        this.borrowedCount = 0;
        this.lateReturnsCount = 0;
        this.suspensionsCount = 0;
        this.suspendedUntil = null;
    }

    public int getMaxLoans() {
        return memberType.getMaxLoans();
    }

    public boolean isSuspended(LocalDate today) {
        return suspendedUntil != null && today.isBefore(suspendedUntil);
    }

    public boolean canBorrow(LocalDate today) {
        return active && !isSuspended(today) && borrowedCount < getMaxLoans();
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
            active = false;
        }
    }

    public int getMemberId() {
        return memberId;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public int getLateReturnsCount() {
        return lateReturnsCount;
    }

    public int getSuspensionsCount() {
        return suspensionsCount;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getSuspendedUntil() {
        return suspendedUntil;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

