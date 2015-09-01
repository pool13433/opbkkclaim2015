/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.ui;

import com.claim.component.HeaderRenderer;
import com.claim.controller.DropReportController;
import com.claim.object.ObjPaymentResult;
import com.claim.support.FunctionUtil;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.constants.ConstantVariable;
import com.claim.dialog.DialogWaitProcess;
import java.awt.Font;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Poolsawat.a
 */
public class UiDropReport extends javax.swing.JInternalFrame {

    DefaultTableModel tableModel = null;

    /**
     * Creates new form UiDropReport
     */
    public UiDropReport() {
        initComponents();
        init();
        tableModel = (DefaultTableModel) tableReportDrop.getModel();
    }

    private void init() {
        comboReportName.removeAllItems();
        String[] arrReport = ConstantVariable.PROCE_TABLE_PAYMENT;
        for (String object : arrReport) {
            if (!object.equals("HC16_&&_chula") && !object.equals("Noni")) {
                comboReportName.addItem(object);
            }
        }
        // jtable header align center
        ((DefaultTableCellRenderer) tableReportDrop.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
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
        comboReportName = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        panelReportDrop = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableReportDrop = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnDropReport = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("หน้าจอการ ล้างข้อมูลรายงาน");
        setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        setPreferredSize(new java.awt.Dimension(1000, 550));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "แสดงชื่อตารางที่สามารถล้างข้อมูลได้", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("TH SarabunPSK", 1, 24))); // NOI18N

        comboReportName.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        comboReportName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        jLabel1.setText("ชื่อรายงาน");

        jButton2.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/claim/util/images/Search-icon.png"))); // NOI18N
        jButton2.setText("ดูข้อมูล");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(comboReportName, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboReportName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        panelReportDrop.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "แสดงงวดที่สามารถล้างข้อมูลได้", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("TH SarabunPSK", 1, 24))); // NOI18N

        tableReportDrop.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        tableReportDrop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "งวด", "สถานะ", "จำนวนข้อมูล"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableReportDrop);

        javax.swing.GroupLayout panelReportDropLayout = new javax.swing.GroupLayout(panelReportDrop);
        panelReportDrop.setLayout(panelReportDropLayout);
        panelReportDropLayout.setHorizontalGroup(
            panelReportDropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportDropLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        panelReportDropLayout.setVerticalGroup(
            panelReportDropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportDropLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnDropReport.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        btnDropReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/claim/util/images/delete.png"))); // NOI18N
        btnDropReport.setText("ล้างข้อมูล");
        btnDropReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(377, 377, 377)
                .addComponent(btnDropReport, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDropReport, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelReportDrop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 326, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelReportDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Thread thread = new Thread(new Runnable() {
            DialogWaitProcess frm = new DialogWaitProcess();

            @Override
            public void run() {
                try {
                    if (comboReportName.getSelectedIndex() != 0 && comboReportName.getSelectedIndex() != 5) {
                        frm.setVisible(true);
                        List<ObjPaymentResult> listData = new DropReportController().getSearchResult(comboReportName.getSelectedIndex());
                        if (listData != null) {
                            ((DefaultTableModel) tableReportDrop.getModel()).setRowCount(0);
                            for (ObjPaymentResult objpayment_result : listData) {
                                Vector vector = new Vector();
                                vector.add(objpayment_result.getStmp_report());
                                vector.add(objpayment_result.getStatus());
                                vector.add(objpayment_result.getTotal_case());
                                tableModel.addRow(vector);
                            }
                            tableReportDrop.setModel(tableModel);
                        }
                        new HeaderRenderer().setWidthAsPercentages(tableReportDrop, 0.20, 0.60, 0.20);
                        panelReportDrop.setBorder(BorderFactory.createTitledBorder("แสดงข้อมูลงวดที่สามารถล้างได้ ของรายงานประเภท " + ConstantVariable.PROCE_TABLE_PAYMENT[comboReportName.getSelectedIndex()]));
                        panelReportDrop.setFont(new Font("TH SarabunPSK", 1, 24));
                        Console.LOG("กำลังค้นหาข้อมูล " + ConstantVariable.PROCE_TABLE_PAYMENT[comboReportName.getSelectedIndex()], 1);
                        frm.setVisible(false);
                    }else{
                        System.out.println(" index combo 0,5");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnDropReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropReportActionPerformed
        // TODO add your handling code here:
        Thread thread = new Thread(new Runnable() {
            DialogWaitProcess frm = new DialogWaitProcess();

            @Override
            public void run() {
                try {

                    int indexRecord = tableReportDrop.getSelectedRow();
                    if (indexRecord >= 0) {
                        String stmp = tableReportDrop.getModel().getValueAt(indexRecord, 0).toString();
                        int selectOption = JOptionPane.showConfirmDialog(null,
                                "ยืนยันการล้างข้อมูล งวด " + stmp + " ใช่ [OK] || ไม่ใช่ [No]",
                                "ข้อความยืนยัน การลบข้อมูล", JOptionPane.YES_NO_OPTION);
                        if (selectOption == JOptionPane.YES_OPTION) {
                            frm.setVisible(true);
                            new DropReportController().deleteRptReport(comboReportName.getSelectedIndex(), stmp);
                            System.out.println(" not individual ");
                        }
                        frm.setVisible(false);                        
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "กรุณาเลือกงวด ที่จะล้างข้อมูล จากตารางก่อนการลบ",
                                "ข้อความแจ้งเตือนจากระบบ", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }//GEN-LAST:event_btnDropReportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDropReport;
    private javax.swing.JComboBox comboReportName;
    public javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelReportDrop;
    private javax.swing.JTable tableReportDrop;
    // End of variables declaration//GEN-END:variables
}