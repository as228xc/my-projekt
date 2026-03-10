package LibraryDomain;

public class BookTitle {
    private final int isbn;
    private final String title;
    private final String author;
    private final int totalCopies;
    private int availableCopies;

    public BookTitle(int isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public boolean hasAvailableCopy() {
        return availableCopies > 0;
    }

    public void borrowOne() {
        if (!hasAvailableCopy()) {
            throw new IllegalStateException("No available copies for ISBN " + isbn);
        }
        availableCopies--;
    }

    public void returnOne() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public int getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}