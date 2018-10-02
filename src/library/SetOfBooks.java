/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.util.ArrayList;

/**
 * A set of books in the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class SetOfBooks extends ArrayList<Book> {

    public SetOfBooks() {
        super();
    }

    public void addBook(Book aBook) {
        super.add(aBook);
    }

    public SetOfBooks findBookByAuthor(String author) {
        return null;
    }

    public SetOfBooks findBookFromTitle(String title) {
        return null;
    }

    public Book findBookFromAccNumber(int accNumber) {
        return null;
    }

    public SetOfBooks findBookFromISBN(int isbnNumber) {
        return null;
    }

}
