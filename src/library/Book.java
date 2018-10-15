/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.io.Serializable;

/**
 * A book in the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String title;
    private Member borrower = null;
    private static int bookCount = 0;
    private final int accessionNumber;
    private final long isbnNumber;
    private final String author;

    public Book(String name, String author, long isbnNumber) {
        title = name;
        this.author = author;
        this.isbnNumber = isbnNumber;
        accessionNumber = bookCount++;
    }

    public void setCurrentBorrower(Member theBorrower) {
        borrower = theBorrower;
    }

    public Member getBorrower() {
        return borrower;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(accessionNumber) + ") " + title + " - " + author + " [" + isbnNumber + "]";
    }

    public boolean isOnLoan() {
        return borrower != null;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getAccNumber() {
        return accessionNumber;
    }

    public long getIsbnNumber() {
        return isbnNumber;
    }

    public void setBookCount(int bookCount) {
        Book.bookCount = bookCount;
    }

}
