package LibraryDomain;

public enum MemberType {
    UNDERGRADUATE(3),
    MASTER(5),
    PHD(7),
    TEACHER(10);

    private final int maxLoans;

    MemberType(int maxLoans) {
        this.maxLoans = maxLoans;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

}
