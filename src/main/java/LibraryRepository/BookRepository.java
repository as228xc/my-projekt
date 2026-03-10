package LibraryRepository;

import LibraryDomain.BookTitle;

public interface BookRepository {

    BookTitle findByIsbn(int isbn);

    void save(BookTitle book);

}