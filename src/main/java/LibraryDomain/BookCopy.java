package LibraryDomain;

public class BookCopy {

    public int BookCopyID;
    public boolean StatusOfCopy;
    public String BookTitle;

    public BookCopy(int bookCopyID, boolean statusOfCopy, String bookTitle) {
        this.BookCopyID = bookCopyID;
        this.StatusOfCopy = statusOfCopy;
        this.BookTitle = bookTitle;

    }

    public boolean isStatusOfCopy() {
        return StatusOfCopy;
    }

    public void lend(){
        StatusOfCopy = false;
    }

    public void returnCopy(){
        StatusOfCopy = true;
    }
}
