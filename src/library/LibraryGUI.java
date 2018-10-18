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
import java.util.regex.Matcher;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
    private boolean buttonsEnabled = false;

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
            throw new RuntimeException("Cannot load library system as there was an issue when accessing the serialized data file.", ex);
        }

        memberList.setListData(theMembers.toArray());
        showCurrentLoans();

        memberList.addListSelectionListener((ListSelectionEvent event) -> {
            selectMember(event);
            showCurrentLoans();
            if (!buttonsEnabled) {
                bookList.setEnabled(true);
                loanedBookList.setEnabled(true);
                loanButton.setEnabled(true);
                returnButton.setEnabled(true);
                buttonsEnabled = true;
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

    private void showCurrentLoans() {
        SetOfBooks availableBooks = new SetOfBooks(holdings);
        availableBooks.removeIf(b -> b.isOnLoan());
        bookList.setListData(availableBooks.toArray());
        if (selectedMember == null) {
            loanedBookList.setListData(new SetOfBooks().toArray());
        } else {
            loanedBookList.setListData(selectedMember.getBooksOnLoan().toArray());
        }
    }

    public void selectBook(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            JList source = (JList) event.getSource();
            Object obj = source.getSelectedValue();
            String selected = obj == null ? "" : obj.toString();
            if (!selected.isEmpty()) {
                selectedBook = holdings.findBookFromAccNumber(Integer.valueOf(selected.substring(1, selected.indexOf(")"))));
            }
        }
    }

    public void selectMember(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            JList source = (JList) event.getSource();
            Object obj = source.getSelectedValue();
            String selected = obj == null ? "" : obj.toString();
            if (!selected.isEmpty()) {
                selectedMember = theMembers.getMemberFromNumber(Integer.valueOf(selected.substring(1, selected.indexOf(")"))));
            }
            selectedBook = null;
        }
    }

    private void loadLibrarySystem() throws IOException {
        ObjectInputStream objectIn = null;
        try (FileInputStream fileIn = new FileInputStream(LIBRARY_FILE)) {
            objectIn = new ObjectInputStream(fileIn);
            Object o;
            try {
                while (true) {
                    o = objectIn.readObject();
                    if (o instanceof Book) {
                        Book newBook = (Book) o;
                        holdings.addBook(newBook);
                        newBook.setBookCount(newBook.getAccNumber() + 1);
                    } else if (o instanceof Member) {
                        Member newMember = (Member) o;
                        theMembers.addMember(newMember);
                        newMember.setMemberCount(newMember.getNumber() + 1);
                    } else {
                        String className = o == null ? null : o.getClass().getCanonicalName();
                        throw new UnsupportedOperationException("Cannot deserialize class " + className);
                    }
                }
            } catch (EOFException ex) {
                // noop
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Cannot load library system as saved data is not the same version as the current program version.", ex);
            }
        } finally {
            if (objectIn != null) {
                objectIn.close();
            }
        }

    }

    public void saveLibrarySystem() throws IOException {
        ObjectOutputStream objectOut = null;
        try (FileOutputStream fileOut = new FileOutputStream(LIBRARY_FILE)) {
            objectOut = new ObjectOutputStream(fileOut);
            for (Member m : theMembers) {
                objectOut.writeObject(m);
                objectOut.flush();
            }
            for (Book b : holdings) {
                objectOut.writeObject(b);
                objectOut.flush();
            }
        } finally {
            if (objectOut != null) {
                objectOut.close();
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
        memberTextField = new javax.swing.JTextField();
        bookTextField = new javax.swing.JTextField();
        loanedBookTextField = new javax.swing.JTextField();
        memberLabel = new javax.swing.JLabel();
        bookLabel = new javax.swing.JLabel();
        loanedBookLabel = new javax.swing.JLabel();

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

        memberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberTextFieldActionPerformed(evt);
            }
        });

        bookTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookTextFieldActionPerformed(evt);
            }
        });

        loanedBookTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loanedBookTextFieldActionPerformed(evt);
            }
        });

        memberLabel.setText("Members");

        bookLabel.setText("Available Books");

        loanedBookLabel.setText("Loaned Books");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(memberTextField)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(bookTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                    .addComponent(loanedBookTextField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(229, 229, 229))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(addNewBook, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(loanButton, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(returnButton)))
                                .addGap(235, 235, 235))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(memberLabel)
                                .addGap(55, 55, 55)
                                .addComponent(bookLabel)
                                .addGap(18, 18, 18)
                                .addComponent(loanedBookLabel))
                            .addComponent(addNewMember))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memberLabel)
                    .addComponent(bookLabel)
                    .addComponent(loanedBookLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                            .addComponent(memberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bookTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loanedBookTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(loanButton)
                            .addComponent(returnButton))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addNewMember)
                            .addComponent(addNewBook))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loanButtonActionPerformed
        if (selectedMember != null && selectedBook != null) {
            loanBook();
        }
    }//GEN-LAST:event_loanButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        if (selectedMember != null && selectedBook != null) {
            acceptReturn();
        }
    }//GEN-LAST:event_returnButtonActionPerformed

    private void addNewBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBookActionPerformed
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnNumberField = new JTextField();
        Object[] dialogContents = {"Title:", titleField, "Author:", authorField, "ISBN:", isbnNumberField};
        String title = "";
        String author = "";
        String isbnNumber = "";
        int selectedOption;
        boolean titleEmpty = false;
        boolean authorEmpty = false;
        boolean isbnEmpty = false;
        boolean isbnMatches = false;
        do {
            boolean showDialog = false;
            String errorMessage = "";
            if (titleEmpty) {
                errorMessage += "Book title cannot be empty" + System.lineSeparator();
                showDialog = true;
            }
            if (authorEmpty) {
                errorMessage += "Book author cannot be empty" + System.lineSeparator();
                showDialog = true;
            }
            if (isbnEmpty || isbnMatches) {
                errorMessage += "ISBN number ";
                if (isbnEmpty) {
                    errorMessage += "cannot be empty";
                }
                if (isbnEmpty && isbnMatches) {
                    errorMessage += " and ";
                }
                if (isbnMatches) {
                    errorMessage += "can only contain numbers, hyphens and spaces";
                }
                showDialog = true;
            }
            if (showDialog) {
                JOptionPane.showMessageDialog(libraryGui, errorMessage);
            }

            selectedOption = JOptionPane.showConfirmDialog(libraryGui, dialogContents, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
            if (selectedOption == JOptionPane.OK_OPTION) {
                title = titleField.getText();
                author = authorField.getText();
                isbnNumber = isbnNumberField.getText().replace("-", "").replace(" ", "");
            } else {
                break;
            }

            titleEmpty = title.isEmpty();
            authorEmpty = author.isEmpty();
            isbnEmpty = isbnNumber.isEmpty();
            isbnMatches = isbnNumber.chars().anyMatch(Character::isLetter);
        } while (titleEmpty || authorEmpty || isbnEmpty || isbnMatches);

        if (selectedOption == JOptionPane.OK_OPTION) {
            Book newBook = new Book(title.trim(), author.trim(), Long.valueOf(isbnNumber.trim()));
            holdings.addBook(newBook);
            bookList.setListData(holdings.toArray());
        }
    }//GEN-LAST:event_addNewBookActionPerformed

    private void addNewMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewMemberActionPerformed
        String memberName = "";
        boolean nameEmpty = false;
        do {
            if (nameEmpty) {
                JOptionPane.showMessageDialog(libraryGui, "Member name cannot be empty");
            }
            memberName = (String) JOptionPane.showInputDialog(
                    libraryGui, "Name:", "Add New Member",
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (memberName == null) { // user has clicked cancel
                break;
            }
            nameEmpty = memberName.isEmpty();
        } while (nameEmpty);
        if (memberName != null) {
            Member newMember = new Member(memberName.trim());
            theMembers.addMember(newMember);
            memberList.setListData(theMembers.toArray());
        }
    }//GEN-LAST:event_addNewMemberActionPerformed

    private void memberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memberTextFieldActionPerformed

    private void bookTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bookTextFieldActionPerformed

    private void loanedBookTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loanedBookTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loanedBookTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            libraryGui = new LibraryGUI();
            libraryGui.setVisible(true);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    libraryGui.saveLibrarySystem();
                } catch (IOException ex) {
                    // noop
                }
            }));

        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBook;
    private javax.swing.JButton addNewMember;
    private javax.swing.JLabel bookLabel;
    private javax.swing.JList bookList;
    private javax.swing.JTextField bookTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton loanButton;
    private javax.swing.JLabel loanedBookLabel;
    private javax.swing.JList loanedBookList;
    private javax.swing.JTextField loanedBookTextField;
    private javax.swing.JLabel memberLabel;
    private javax.swing.JList memberList;
    private javax.swing.JTextField memberTextField;
    private javax.swing.JButton returnButton;
    // End of variables declaration//GEN-END:variables

}
