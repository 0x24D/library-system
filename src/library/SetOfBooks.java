/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public SetOfBooks(SetOfBooks holdings) {
        super(holdings);
    }

    public void addBook(Book aBook) {
        super.add(aBook);
    }

    public SetOfBooks findBookByAuthor(String author) {
        return (SetOfBooks) super.stream().filter(b -> b.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public SetOfBooks findBookFromTitle(String title) {
        return (SetOfBooks) super.stream().filter(b -> b.getTitle().equals(title)).collect(Collectors.toList());
    }

    public Book findBookFromAccNumber(int accNumber) {
        return super.stream().filter(b -> b.getAccNumber() == accNumber).findFirst().orElse(null);
    }

    public SetOfBooks findBookFromISBN(int isbnNumber) {
        return (SetOfBooks) super.stream().filter(b -> b.getIsbnNumber() == isbnNumber).collect(Collectors.toList());
    }

    public void removeBook(Book book) {
        super.remove(book);
    }

}
