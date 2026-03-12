package LibraryRepository;

import LibraryDomain.BookTitle;
import java.util.List;

public interface BookRepository {
    BookTitle findByIsbn(int isbn);
    List<BookTitle> search(String query);
    void save(BookTitle book);
    void update(BookTitle book);
    void deleteByIsbn(int isbn);
    List<BookTitle> findAll();
}