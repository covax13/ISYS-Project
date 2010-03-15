/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SalesTabView.java
 *
 * Created on Feb 16, 2010, 5:00:37 PM
 */

package edu.byu.isys413.cbrammer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class SalesTabView extends javax.swing.JPanel {
    private TransListModel transListModel; // The model for the data grid list
    private Trans trans; // Keep track of our trans
    
    public double total = 0.0;  // The total for the trans
    private double taxTotal = 0.0; // The total for the trans
    private double subtotal = 0.0; // the subtotal for hte trans
    private double taxrate = .07; // the tax rate for the trans
    private double cogs = 0; // the total COGS

    DefaultListModel searchListModel = new DefaultListModel();

    /**
     * Create an accounting line item
     * @param je - the JE to add it to
     * @param desc - a desc of the item
     * @param Type - what type
     * @param amount - the amount of it
     */
    public void makeAccounting(JournalEntry je, String desc, String Type, Double amount){
        try{
            // create the obj and assign the values
            Accounting a = AccountingDAO.getInstance().create(GUID.generate());
            a.setAmount(amount);
            a.setType(Type);
            a.setDescription(desc);
            a.setJournalEntry(je);
            je.getAccountingList().add(a);
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }//end makeAccounting()

    /**
     * Init the data for the view
     */
    public void initData() {
        startNewTrans();
        populateCustomers();
    }

    /**
     * Reset the transaction data and add in all new objects
     */
    public void startNewTrans() {
        try {
            // creat all new objects for the trans
            trans = TransDAO.getInstance().create(GUID.generate());
            trans.setPayment(PaymentDAO.getInstance().create(GUID.generate()));
            trans.setJournal(JournalEntryDAO.getInstance().create(GUID.generate()));
            trans.setCommission(CommissionDAO.getInstance().create(GUID.generate()));
            trans.setEmployee(Model.getInstance().getEmployee());
            trans.setStore(Model.getInstance().getEmployee().getStore());
            transListModel = new TransListModel(trans.getTransList());
        } catch (DataException ex) {
            JOptionPane.showMessageDialog(this, "Error initializing data.");
        }
        orderTable.setModel(transListModel);
        calcTotals();
    }

    /**
     * create the customer lsit
     */
    public void populateCustomers() {
        List<Customer> custList = null;
        try {
            custList = CustomerDAO.getInstance().getAll();
        } catch (DataException ex) {
            JOptionPane.showMessageDialog(this, "Error initializing data.");
        }
        for (Customer cust:custList) {
           custCombo.addItem(cust);
        } //end for
    }


    /** Creates new form SalesTabView */
    public SalesTabView() {
        initComponents();
        searchList.setModel(searchListModel);
    }

    /**
     * Calculate all the totals for hte application
     * Populate all of the view fields
     */
    private void calcTotals() {
        subtotal = 0.0;
        cogs = 0.0;
        for (TransLine transLine:trans.getTransList()) {
            subtotal += (transLine.getRs().getPrice() * transLine.getRs().getQuantity());
            cogs += (transLine.getRs().getCost() * transLine.getRs().getQuantity());
        }
        taxTotal = subtotal* taxrate;
        total = subtotal + taxTotal;

        cogs = Math.round(cogs*100)/100;
        taxTotal = Math.round(taxTotal*100)/100;
        subtotal = Math.round(subtotal*100)/100;
        total = Math.round(total*100)/100;


        subTotalLabel.setText(String.valueOf(subtotal));
        taxLabel.setText(String.valueOf(taxTotal));
        totalLabel.setText(String.valueOf(total));

    }
    //date method
    public String Date() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        String d = dateFormat.format(calendar.getTime());

        return d;

    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        barcodeText = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        lookupBtn = new javax.swing.JButton();
        tableCombo = new javax.swing.JComboBox();
        jTextField4 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        addToOrderBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        custCombo = new javax.swing.JComboBox();
        jLabel35 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        taxLabel = new javax.swing.JLabel();
        subTotalLabel = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        clearTransactionBtn = new javax.swing.JButton();
        finalizeBtn = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Product Lookup"));

        jLabel28.setText("Barcode:");

        jLabel29.setText("Table Name:");

        lookupBtn.setText("Lookup");
        lookupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lookupBtnMouseClicked(evt);
            }
        });
        lookupBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lookupBtnActionPerformed(evt);
            }
        });

        tableCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Service", "Conceptual Product", "Physical Product", "Rental" }));

        jTextField4.setText("1");

        jLabel30.setText("Quantity:");

        addToOrderBtn.setText("Add To Order");
        addToOrderBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addToOrderBtnMouseClicked(evt);
            }
        });
        addToOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToOrderBtnActionPerformed(evt);
            }
        });

        searchList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(searchList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(barcodeText, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tableCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addComponent(lookupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addComponent(addToOrderBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(barcodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(lookupBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addToOrderBtn))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Order"));

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(orderTable);

        custCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                custComboMouseClicked(evt);
            }
        });

        jLabel35.setText("Subtotal:");

        jLabel31.setText("Tax:");

        jLabel36.setText("Total:");

        totalLabel.setText(" ");

        taxLabel.setText(" ");

        subTotalLabel.setText(" ");

        jLabel27.setText("Customer:");

        clearTransactionBtn.setText("Clear Transaction");
        clearTransactionBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearTransactionBtnMouseClicked(evt);
            }
        });
        clearTransactionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTransactionBtnActionPerformed(evt);
            }
        });

        finalizeBtn.setText("Finalize Btn");
        finalizeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                finalizeBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel27)
                        .addGap(18, 18, 18)
                        .addComponent(custCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearTransactionBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel31)
                            .addComponent(jLabel35))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(subTotalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(taxLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                        .addComponent(finalizeBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(custCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(clearTransactionBtn))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(subTotalLabel))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(taxLabel))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(totalLabel)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(finalizeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lookupBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lookupBtnMouseClicked

        //check if barcode field is emtpy
        if(barcodeText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "You must enter in a product ID.");
            return;
        }//end if

        // Update the list model
        searchListModel = new DefaultListModel();
        

        // Service
        if(tableCombo.getSelectedIndex()==0){
            try{
                List<Service> services =  ServiceDAO.getInstance().readByCode(barcodeText.getText());
                for(Service s: services) {
                    searchListModel.addElement(s);
                }
                searchList.setModel(searchListModel);
                
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Error loading services");
            }//end try/catch
        }

        // Conceptual Product
        else if(tableCombo.getSelectedIndex()==1){
            try{
                List<ConceptualProduct> conceptualProducts =  ConceptualProductDAO.getInstance().readByCode(barcodeText.getText());
                for(ConceptualProduct c: conceptualProducts) {
                    // Get a new SP and assign the CP to it
                    SaleProduct sp = SaleProductDAO.getInstance().create(GUID.generate());
                    sp.setProduct(c);
                    sp.setType("sale product");
                    sp.setQuantity(Integer.parseInt(jTextField4.getText()));
                    searchListModel.addElement(sp);
                }
                searchList.setModel(searchListModel);

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Error loading conceptual products");
            }//end try/catch
        }

        // Physical Product
        else if(tableCombo.getSelectedIndex()==2){

            try{
                List<PhysicalProduct> physicalProducts =  PhysicalProductDAO.getInstance().readByCode(barcodeText.getText());
                for(PhysicalProduct p: physicalProducts) {
                    // Get a new SP and add the PP to it
                    SaleProduct sp = SaleProductDAO.getInstance().create(GUID.generate());
                    sp.setProduct(p);
                    sp.setType("sale product");
                    sp.setQuantity(Integer.parseInt(jTextField4.getText()));
                    searchListModel.addElement(sp);
                }
                searchList.setModel(searchListModel);

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Error loading phys products");
            }//end try/catch
        }

        // Rental
         else if(tableCombo.getSelectedIndex()==3){

            try{
                List<ForRent> forRents =  ForRentDAO.getInstance().readByCode(barcodeText.getText());
                for(ForRent fr: forRents) {
                    // Get a new Rental and add the forRent to it
                    Rental r = RentalDAO.getInstance().create(GUID.generate());
                    r.setForRent(fr);   
                    r.setType("rental");
                    r.setDateOut(null);
                    r.setDateIn(null);
                    r.setDateDue(null);
                    r.setAmount(total);
                    
                    searchListModel.addElement(r);
                }
                searchList.setModel(searchListModel);

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Error loading phys products");
            }//end try/catch
         }

    }//GEN-LAST:event_lookupBtnMouseClicked

    private void lookupBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lookupBtnActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_lookupBtnActionPerformed

    private void addToOrderBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addToOrderBtnMouseClicked

        //  Create a new trans line and add it to the revenue source
        TransLine transLine = null;
        try{
            transLine = new TransLine(GUID.generate());
        }catch(Exception ex){
            ex.printStackTrace();
        }

        // Set the vals the RS
        RevenueSource rs = (RevenueSource) searchList.getSelectedValue();
        Commission comm = null;
        try {
            comm = CommissionDAO.getInstance().create(GUID.generate());
        } catch (DataException ex) {
            Logger.getLogger(SalesTabView.class.getName()).log(Level.SEVERE, null, ex);
        }
        comm.setAmount(100.00);//subtotal * (trans.getEmployee().getCommissionRate()/100));
        comm.setPaid("0");
        rs.setComm(comm);

        rs.setTransLine(transLine);
        transLine.setRs(rs);
        transLine.setTrans(trans);
        //Set the trans line in the trans
        trans.getTransList().add(transLine);

        // Update the data grid
        orderTable.updateUI();

        // Calc our totals
        calcTotals();
    }//GEN-LAST:event_addToOrderBtnMouseClicked

    private void addToOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToOrderBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addToOrderBtnActionPerformed

    private void clearTransactionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTransactionBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearTransactionBtnActionPerformed

    private void clearTransactionBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearTransactionBtnMouseClicked
        startNewTrans();
    }//GEN-LAST:event_clearTransactionBtnMouseClicked

    private void finalizeBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_finalizeBtnMouseClicked
        // Set the customer
        trans.setCustomer((Customer) custCombo.getSelectedItem());
        trans.setDate(Date());

        // shortcuts
        Payment p = trans.getPayment();
        
        JournalEntry je = trans.getJournal();

        // Set the payment details
        p.setAmount(total);
        p.setChange(0);
        p.setType("Cash");

       

        // Set the accounting items
        makeAccounting(je, "Cash", "Debit", total);
        makeAccounting(je, "Sales Revenue", "Credit", subtotal);
        makeAccounting(je, "Sales Tax", "Credit", taxTotal);
        makeAccounting(je, "Cost of Goods Sold", "Debit", cogs);
        makeAccounting(je, "Inventory", "Credit", cogs);
        makeAccounting(je, "Commission Payable", "Credit", 100.00);//comm.getAmount());
        makeAccounting(je, "Commission Expense", "Debit", 100.00);//comm.getAmount());

        // Save it out
        try{
            trans.save();
        }catch(Exception ex){
            System.out.println("Problem saving t, je, p, or comm in the sales tab window");
            ex.printStackTrace();
        }

        startNewTrans();
    }//GEN-LAST:event_finalizeBtnMouseClicked

    private void custComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_custComboMouseClicked

        populateCustomers();
        // TODO add your handling code here:
    }//GEN-LAST:event_custComboMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToOrderBtn;
    private javax.swing.JTextField barcodeText;
    private javax.swing.JButton clearTransactionBtn;
    private javax.swing.JComboBox custCombo;
    private javax.swing.JButton finalizeBtn;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton lookupBtn;
    private javax.swing.JTable orderTable;
    private javax.swing.JList searchList;
    private javax.swing.JLabel subTotalLabel;
    private javax.swing.JComboBox tableCombo;
    private javax.swing.JLabel taxLabel;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables

}
