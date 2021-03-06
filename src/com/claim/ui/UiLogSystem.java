/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.ui;

import com.claim.component.HeaderRenderer;
import com.claim.controller.LogAppController;
import com.claim.dialog.DialogViewDataParameter;
import com.claim.object.ObjLogApplication;
import com.claim.support.CssStyle;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Poolsawat.a
 */
public class UiLogSystem extends javax.swing.JInternalFrame {

    DefaultTableModel tableModel = null;
    static int LIMIT_SHOW_LOG = 1000;
    /**
     * Creates new form UiLogSystem
     */
    public UiLogSystem() {
        initComponents();
        tableModel = (DefaultTableModel) tableLog.getModel();
        this.loadTable();
    }

    private void loadTable() {
        ((DefaultTableModel) tableLog.getModel()).setRowCount(0);
        List<ObjLogApplication> listLog = new LogAppController().getListLogLimit(LIMIT_SHOW_LOG);
        for (int i = 0; i < listLog.size(); i++) {
            ObjLogApplication objLog = listLog.get(i);
            Vector data = new Vector();
            data.add(objLog.getLog_name());
            data.add(objLog.getLog_desc());
            data.add(objLog.getLog_ip());
            data.add(objLog.getLog_createdate());
            data.add(new String("<html><div style=\""+CssStyle.BTN_PRIMARY+"\"><b>กดดู</b></div></html>"));
            tableModel.addRow(data);
            tableLog.setRowHeight(i, 30);            
        }
        tableLog.setModel(tableModel);

        new HeaderRenderer().setWidthAsPercentages(tableLog, 0.20, 0.40, 0.10, 0.15, 0.10);
        ((DefaultTableCellRenderer) tableLog.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tableLog = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("หน้าจอข้อมูลการใช้งานโปรแกรม OPBKKCLAIM 2015");
        setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        setPreferredSize(new java.awt.Dimension(1300, 550));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ข้อมูลการเข้าใช้งานโปรแกรมทั้งหมด โดยเรียงจาก ณ วันที่ปัจจุบัน - อดีต", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("TH SarabunPSK", 1, 24))); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableLog.setAutoCreateRowSorter(true);
        tableLog.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        tableLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ชื่อการทำงาน", "อธิบาย", "ip เครื่องใช้งาน", "วันที่เข้าใช้งาน", "กดเพื่อดูพารามิเตอร์"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableLogMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableLog);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLogMouseClicked
        if (evt.getClickCount() == 1) {
            System.out.println("evt.getButton() ::==" + evt.getButton());
            System.out.println("evt.getComponent ::=="+evt.getComponent());
            int indexRow = tableLog.getSelectedRow();
            int columnRow = tableLog.getSelectedColumn();
            System.out.println("columnRow ::=="+columnRow);
            if(columnRow==4){
                String text = (String)tableLog.getModel().getValueAt(indexRow, 1);
                DialogViewDataParameter viewParam = new DialogViewDataParameter(true, text);
                viewParam.setVisible(true);
                viewParam.setLocationRelativeTo(this);
            }                        
        }
    }//GEN-LAST:event_tableLogMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableLog;
    // End of variables declaration//GEN-END:variables
}
