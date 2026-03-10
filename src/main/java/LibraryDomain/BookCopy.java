package LibraryDomain;

public class BookCopy {
    private final int bookCopyId;
    private final int isbn;
    private boolean available;

    public BookCopy(int bookCopyId, int isbn) {
        this.bookCopyId = bookCopyId;
        this.isbn = isbn;
        this.available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void lend() {
        if (!available) {
            throw new IllegalStateException("Copy is already lent");
        }
        available = false;
    }

    public void returnCopy() {
        available = true;
    }

    public int getBookCopyId() {
        return bookCopyId;
    }

    public int getIsbn() {
        return isbn;
    }
}