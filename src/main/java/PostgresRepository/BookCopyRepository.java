package PostgresRepository;

public interface BookCopyRepository {
    void createCopies(int isbn, int numberOfCopies);
}
