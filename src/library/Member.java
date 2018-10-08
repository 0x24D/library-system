/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

/**
 * A member of the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class Member {

    private final String name;
    private final int memberNumber;
    private SetOfBooks currentLoans = new SetOfBooks();
    private static int memberCount = 0;

    public Member(String aName) {
        name = aName;
        memberNumber = memberCount++;
        currentLoans = new SetOfBooks();
    }

    @Override
    public String toString() {
        return Integer.toString(memberNumber) + " " + name;

    }

    public void borrowBook(Book aBook) {
        currentLoans.addBook(aBook);
        aBook.setCurrentBorrower(this);
    }

    public void returnBook(Book book) {
        currentLoans.removeBook(book);
        book.setCurrentBorrower(null);
    }

    public SetOfBooks getBooksOnLoan() {
        return currentLoans;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return memberNumber;
    }

}
