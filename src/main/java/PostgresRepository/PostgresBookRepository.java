package PostgresRepository;

import LibraryDomain.BookTitle;
import LibraryRepository.BookRepository;

public class PostgresBookRepository implements BookRepository {

    @Override
    public BookTitle findByIsbn(int isbn) {

        return null;
    }

    @Override
    public void save(BookTitle book) {

    }
}