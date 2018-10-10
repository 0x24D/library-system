/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * LibraryGUI.java
 *
 * Created on 28-Sep-2009, 11:55:39
 */
package library;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

/**
 * The GUI used to control the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class LibraryGUI extends javax.swing.JFrame {

    private static LibraryGUI libraryGui;
    private final SetOfMembers theMembers = new SetOfMembers();
    private final SetOfBooks holdings = new SetOfBooks();
    private Book selectedBook;
    private Member selectedMember;

    /**
     * The name of the file that is used for storing the contents of the library
     * system.
     */
    public final static String LIBRARY_FILE = "library_system.ser";

    /**
     * Creates new form LibraryGUI.
     */
    public LibraryGUI() {

        initComponents();

        try {
            loadLibrarySystem();
        } catch (IOException ex) {
            // noop
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LibraryGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

//        Member member1 = new Member("Jane");
//        Member member2 = new Member("Amir");
//        Member member3 = new Member("Astrid");
//        Member member4 = new Member("Andy");
//
//        theMembers.addMember(member1);
//        theMembers.addMember(member2);
//        theMembers.addMember(member3);
//        theMembers.addMember(member4);
//
//        Book book1 = new Book("book1");
//        Book book2 = new Book("book2");
//        Book book3 = new Book("book3");
//        Book book4 = new Book("book4");
//
//        holdings.addBook(book1);
//        holdings.addBook(book2);
//        holdings.addBook(book3);
//        holdings.addBook(book4);
        memberList.setListData(theMembers.toArray());

        holdings.removeIf(b -> b.isOnLoan());
        bookList.setListData(holdings.toArray());

        SetOfBooks loanedBooks = new SetOfBooks(holdings);
        loanedBooks.removeIf(b -> !b.isOnLoan());
        loanedBookList.setListData(loanedBooks.toArray());

        memberList.addListSelectionListener((ListSelectionEvent event) -> {
            selectMember(event);

            /* Where to put these? */
            if (selectedMember != null) {
                showCurrentLoans();
                selectedBook = null;

                bookList.setEnabled(true);
                loanedBookList.setEnabled(true);
                loanButton.setEnabled(true);
                returnButton.setEnabled(true);
            }
        });

        bookList.addListSelectionListener((ListSelectionEvent event) -> {
            selectBook(event);
        });

        loanedBookList.addListSelectionListener((ListSelectionEvent event) -> {
            selectBook(event);
        });

    }

    public void loanBook() {
        if (loanedBookList.getModel().getSize() < 3 && selectedBook != null) {
            selectedMember.borrowBook(selectedBook);
            showCurrentLoans();
            selectedBook = null;
        }
    }

    public void acceptReturn() {
        if (selectedBook != null) {
            selectedMember.returnBook(selectedBook);
            showCurrentLoans();
            selectedBook = null;
        }
    }

    public void showCurrentLoans() {
        SetOfBooks availableBooks = new SetOfBooks(holdings);
        availableBooks.removeIf(b -> b.isOnLoan());
        bookList.setListData(availableBooks.toArray());
        loanedBookList.setListData(selectedMember.getBooksOnLoan().toArray());
    }

    public void selectBook(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            JList source = (JList) event.getSource();
            Object obj = source.getSelectedValue();
            String selected = obj == null ? "" : obj.toString();

            if (!selected.isEmpty()) {
                selectedBook = holdings.findBookFromAccNumber(Integer.valueOf(selected.substring(0, selected.indexOf(" "))));
            }
        }

    }

    public void selectMember(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            JList source = (JList) event.getSource();
            Object obj = source.getSelectedValue();
            String selected = obj == null ? "" : obj.toString();

            if (!selected.isEmpty()) {
                selectedMember = theMembers.getMemberFromNumber(Integer.valueOf(selected.substring(0, selected.indexOf(" "))));
            }
        }
    }

    private void loadLibrarySystem() throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream objectIn = null;
        try (FileInputStream fileIn = new FileInputStream(LIBRARY_FILE)) {
            objectIn = new ObjectInputStream(fileIn);
            Object o = null;
            try {
                while (true) {
                    o = objectIn.readObject();

                    if (o instanceof Book) {
                        holdings.addBook((Book) o);
                    } else if (o instanceof Member) {
                        theMembers.addMember((Member) o);
                    } else {
                        String className = o == null ? null : o.getClass().getCanonicalName();
                        throw new UnsupportedOperationException("Cannot deserialize class " + className);
                    }
                }
            } catch (EOFException e) {
                return;
            }
        } finally {
            if (objectIn != null) {
                objectIn.close();
            }
        }

    }

    public void saveLibrarySystem() throws FileNotFoundException,
            IOException {
        ObjectOutputStream objectOut
                = null;

        try (FileOutputStream fileOut
                = new FileOutputStream(LIBRARY_FILE
                )) {
            objectOut
                    = new ObjectOutputStream(fileOut
                    );

            for (Member m
                    : theMembers) {
                objectOut
                        .writeObject(m
                        );
                objectOut
                        .flush();

            }
            for (Book b
                    : holdings) {
                objectOut
                        .writeObject(b
                        );
                objectOut
                        .flush();

            }
        } finally {
            if (objectOut
                    != null) {
                objectOut
                        .close();

            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        loanButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        memberList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        bookList = new javax.swing.JList();
        returnButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        loanedBookList = new javax.swing.JList();
        addNewBook = new javax.swing.JButton();
        addNewMember = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );

        loanButton.setText("Loan Book");
        loanButton.setEnabled(false);
        loanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loanButtonActionPerformed(evt);
            }
        });

        memberList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(memberList);

        bookList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        bookList.setEnabled(false);
        jScrollPane2.setViewportView(bookList);

        returnButton.setText("Return Book");
        returnButton.setEnabled(false);
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        loanedBookList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        loanedBookList.setEnabled(false);
        jScrollPane3.setViewportView(loanedBookList);

        addNewBook.setText("Add New Book");
        addNewBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewBookActionPerformed(evt);
            }
        });

        addNewMember.setText("Add New Member");
        addNewMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewMemberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loanButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(229, 229, 229))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(returnButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addNewMember)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(addNewBook, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(235, 235, 235))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(129, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(loanButton)
                            .addComponent(returnButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addNewMember)
                            .addComponent(addNewBook))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loanButtonActionPerformed
        if (selectedMember
                != null && selectedBook
                != null) {
            loanBook();

        }
    }//GEN-LAST:event_loanButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        if (selectedMember
                != null && selectedBook
                != null) {
            acceptReturn();

        }
    }//GEN-LAST:event_returnButtonActionPerformed

    private void addNewBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBookActionPerformed
        String bookTitle
                = (String) JOptionPane
                        .showInputDialog(
                                libraryGui,
                                "What is the new book's title?",
                                "Add New Book",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null, null);

        if ((bookTitle
                != null) && (bookTitle
                        .length() > 0)) {
            Book newBook
                    = new Book(bookTitle
                    );
            holdings
                    .addBook(newBook
                    );
            bookList
                    .setListData(holdings
                            .toArray());

        }
    }//GEN-LAST:event_addNewBookActionPerformed

    private void addNewMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewMemberActionPerformed
        String memberName
                = (String) JOptionPane
                        .showInputDialog(
                                libraryGui,
                                "What is the new member's name?",
                                "Add New Member",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null, null);

        if ((memberName
                != null) && (memberName
                        .length() > 0)) {
            Member newMember
                    = new Member(memberName
                    );
            theMembers
                    .addMember(newMember
                    );
            memberList
                    .setListData(theMembers
                            .toArray());

        }
    }//GEN-LAST:event_addNewMemberActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue
                .invokeLater(() -> {
                    libraryGui
                            = new LibraryGUI();
                    libraryGui
                            .setVisible(true);
                    Runtime
                            .getRuntime().addShutdownHook(new Thread(() -> {
                                try {
                                    libraryGui
                                            .saveLibrarySystem();

                                } catch (IOException ex) {
                                    // noop for now
                                }
                            }));

                });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBook;
    private javax.swing.JButton addNewMember;
    private javax.swing.JList bookList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton loanButton;
    private javax.swing.JList loanedBookList;
    private javax.swing.JList memberList;
    private javax.swing.JButton returnButton;
    // End of variables declaration//GEN-END:variables

}
