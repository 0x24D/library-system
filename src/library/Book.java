/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

/**
 * A book in the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class Book {

    private String title;
    private Member borrower = null;
    private static int bookCount = 0;
    private int accessionNumber;
    private String isbnNumber;
    private String author;

    public Book(String name) {
        title = name;
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
        return Integer.toString(accessionNumber) + " " + title;
    }

    public boolean isOnLoan() {
        return false;
    }

}
