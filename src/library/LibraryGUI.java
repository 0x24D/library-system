package library;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private boolean controlsEnabled = false;

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
        } catch (FileNotFoundException ex) {
            // noop
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load library system as there was an issue when accessing the serialized data file.", ex);
        }

        memberList.setListData(theMembers.toArray());
        showCurrentLoans();

        memberList.addListSelectionListener((ListSelectionEvent event) -> {
            selectMember(event);
            showCurrentLoans();
            if (!controlsEnabled) {
                bookList.setEnabled(true);
                loanedBookList.setEnabled(true);
                loanButton.setEnabled(true);
                returnButton.setEnabled(true);
                bookTextField.setEnabled(true);
                loanedBookTextField.setEnabled(true);
                controlsEnabled = true;
            }
        });

        bookList.addListSelectionListener((ListSelectionEvent event) -> {
            selectBook(event);
        });

        loanedBookList.addListSelectionListener((ListSelectionEvent event) -> {
            selectBook(event);
        });

        memberTextField.getDocument().addDocumentListener(new DocumentListenerImpl());

        bookTextField.getDocument().addDocumentListener(new DocumentListenerImpl());

        loanedBookTextField.getDocument().addDocumentListener(new DocumentListenerImpl());
    }

    public void loanBook() {
        if (loanedBookList.getModel().getSize() < 3 && selectedBook != null && !selectedBook.isOnLoan()) {
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

    private void loadLibrarySystem() throws FileNotFoundException, IOException {
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

    private void filterJList() {
        String memberFilter = memberTextField.getText();
        String bookFilter = bookTextField.getText();
        String loanedBookFilter = loanedBookTextField.getText();

        SetOfMembers members = new SetOfMembers(theMembers);
        if (memberFilter != null && !memberFilter.isEmpty()) {
            members.removeIf(m -> !m.toString().toLowerCase().contains(memberFilter) && !m.toString().contains(memberFilter));
        }
        memberList.setListData(members.toArray());

        SetOfBooks availableBooks = new SetOfBooks(holdings);
        availableBooks.removeIf(b -> b.isOnLoan());
        if (bookFilter != null && !bookFilter.isEmpty()) {
            availableBooks.removeIf(b -> !b.toString().toLowerCase().contains(bookFilter) && !b.toString().contains(bookFilter));
        }
        bookList.setListData(availableBooks.toArray());

        if (selectedMember == null) {
            loanedBookList.setListData(new SetOfBooks().toArray());
        } else {
            SetOfBooks loanedBooks = selectedMember.getBooksOnLoan();
            if (loanedBookFilter != null && !loanedBookFilter.isEmpty()) {
                loanedBooks.removeIf(b -> !b.toString().toLowerCase().contains(loanedBookFilter) && !b.toString().contains(loanedBookFilter));
            }
            loanedBookList.setListData(loanedBooks.toArray());
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

        loanButton.setText("Loan Book");
        loanButton.setEnabled(false);
        loanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loanButtonActionPerformed(evt);
            }
        });

        memberList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(memberList);

        bookList.setModel(new DefaultListModel());
        bookList.setEnabled(false);
        bookList.setVisibleRowCount(4);
        jScrollPane2.setViewportView(bookList);

        returnButton.setText("Return Book");
        returnButton.setEnabled(false);
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        loanedBookList.setModel(new DefaultListModel());
        loanedBookList.setEnabled(false);
        loanedBookList.setVisibleRowCount(4);
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

        bookTextField.setEnabled(false);

        loanedBookTextField.setEnabled(false);

        memberLabel.setText("Members");

        bookLabel.setText("Available Books");

        loanedBookLabel.setText("Loaned Books");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addNewBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addNewMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(returnButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(loanButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(memberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(loanedBookTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(memberLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(bookLabel)
                                    .addComponent(jScrollPane2)
                                    .addComponent(bookTextField)
                                    .addComponent(loanedBookLabel)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bookLabel)
                    .addComponent(memberLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bookTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loanedBookLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loanedBookTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(memberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loanButton)
                    .addComponent(addNewMember))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnButton)
                    .addComponent(addNewBook))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loanButtonActionPerformed
        if (selectedMember != null && selectedBook != null && !selectedBook.isOnLoan()) {
            loanBook();
        }
    }//GEN-LAST:event_loanButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        if (selectedMember != null && selectedBook != null && selectedBook.isOnLoan()) {
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

    public class DocumentListenerImpl implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent de) {
            filterJList();
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            filterJList();
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            filterJList();
        }

    }
}
