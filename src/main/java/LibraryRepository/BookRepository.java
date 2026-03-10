package LibraryRepository;

import LibraryDomain.BookTitle;

import java.util.List;

public interface BookRepository {

    BookTitle findByIsbn(int isbn);

    void save(BookTitle book);

    List<BookTitle> findAll();
}