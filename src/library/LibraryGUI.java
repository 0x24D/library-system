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

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;

/**
 * The GUI used to control the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class LibraryGUI extends javax.swing.JFrame {

    private SetOfMembers theMembers = new SetOfMembers();
    private SetOfBooks holdings = new SetOfBooks();
    private Book selectedBook;
    private Member selectedMember;

    /**
     * Creates new form LibraryGUI
     */
    public LibraryGUI() {

        initComponents();

        Member member1 = new Member("Jane");
        Member member2 = new Member("Amir");
        Member member3 = new Member("Astrid");
        Member member4 = new Member("Andy");

        theMembers.addMember(member1);
        theMembers.addMember(member2);
        theMembers.addMember(member3);
        theMembers.addMember(member4);

        Book book1 = new Book("book1");
        Book book2 = new Book("book2");
        Book book3 = new Book("book3");
        Book book4 = new Book("book4");

        holdings.addBook(book1);
        holdings.addBook(book2);
        holdings.addBook(book3);
        holdings.addBook(book4);

        memberList.setListData(theMembers.toArray());
        bookList.setListData(holdings.toArray());
        loanedBookList.setListData(new SetOfBooks().toArray());

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(212, 212, 212))
            .addGroup(layout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(loanButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(returnButton)
                            .addComponent(loanButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new LibraryGUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
