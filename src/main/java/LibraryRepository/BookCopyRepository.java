package LibraryRepository;

public interface BookCopyRepository {
    void createCopies(int isbn, int numberOfCopies);
    Integer findAvailableCopyIdByIsbn(int isbn);
    void markCopyAsBorrowed(int copyId);
    void markCopyAsReturned(int copyId);
    void deleteCopiesByIsbn(int isbn);
}