package com.claim.ui;

import com.claim.dao.UtilDao;
import com.claim.object.HospitalService;
import com.claim.constants.ConstantMessage;
import com.claim.dialog.DialogWaitProcess;
import java.awt.Frame;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DialogService extends javax.swing.JDialog {

    private DefaultTableModel tableModel;
    public DialogWaitProcess waitIng = null;
    JTextField txtCode, txtName;
    JTextArea txtAreaLIstHospital;
    private List<HospitalService> listSelectHospital = null;
    int countitem = 0;

    public DialogService() {
    }

    public void DialogServiceJFrame(Frame parent, boolean modal, Map map) {
        //super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(this);
        tableModel = (DefaultTableModel) tableService.getModel();
        waitIng = new DialogWaitProcess();
        listSelectHospital = new ArrayList<HospitalService>();
        txtCode = (JTextField) map.get("code");
        txtAreaLIstHospital = (JTextArea) map.get("name");
        listSelectHospital = (List<HospitalService>) map.get("listHospital");

        txtCountItem.setText(String.valueOf(countitem));
    }

    public void DialogServiceJInternalFrame(JInternalFrame parent, boolean modal, Map map) {
        //super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(this);
        tableModel = (DefaultTableModel) tableService.getModel();
        waitIng = new DialogWaitProcess();
        listSelectHospital = new ArrayList<HospitalService>();
        txtCode = (JTextField) map.get("code");
        txtAreaLIstHospital = (JTextArea) map.get("name");
        listSelectHospital = (List<HospitalService>) map.get("listHospital");

        txtCountItem.setText(String.valueOf(countitem));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMain = new javax.swing.JPanel();
        jPanelSearch = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tbServiceCode = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanelResult = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableService = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtCountItem = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/claim/util/images/Excel-icon.png")));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ค้นหา", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        jLabel1.setText("รหัส/ชื่อ หน่วยให้บริการ");

        tbServiceCode.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N

        jButton1.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/claim/util/images/Search-icon.png"))); // NOI18N
        jButton1.setText("ค้นหา");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSearchLayout = new javax.swing.GroupLayout(jPanelSearch);
        jPanelSearch.setLayout(jPanelSearchLayout);
        jPanelSearchLayout.setHorizontalGroup(
            jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(tbServiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        jPanelSearchLayout.setVerticalGroup(
            jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tbServiceCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelMain.add(jPanelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 910, 80));

        jPanelResult.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ผลการค้นหา", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        tableService.setAutoCreateRowSorter(true);
        tableService.setBackground(new java.awt.Color(204, 255, 204));
        tableService.setFont(new java.awt.Font("TH SarabunPSK", 1, 20)); // NOI18N
        tableService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "เลือก", "รหัสหน่วยบริการ", "ชื่อหน่วยบริการ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableService.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableService.setDragEnabled(true);
        tableService.setGridColor(new java.awt.Color(51, 51, 255));
        tableService.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableServiceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableService);

        javax.swing.GroupLayout jPanelResultLayout = new javax.swing.GroupLayout(jPanelResult);
        jPanelResult.setLayout(jPanelResultLayout);
        jPanelResultLayout.setHorizontalGroup(
            jPanelResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelResultLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelResultLayout.setVerticalGroup(
            jPanelResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelResultLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelMain.add(jPanelResult, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 910, 280));

        jLabel2.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        jLabel2.setText("จำนวนหน่วยบริการที่เลือก");
        jPanelMain.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 410, -1, -1));

        txtCountItem.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        txtCountItem.setText("0");
        txtCountItem.setEnabled(false);
        jPanelMain.add(txtCountItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 410, 70, -1));

        btnOK.setBackground(new java.awt.Color(148, 235, 148));
        btnOK.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        btnOK.setForeground(new java.awt.Color(51, 51, 255));
        btnOK.setText("ตกลง");
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });
        jPanelMain.add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 410, -1, -1));

        btnCancle.setBackground(java.awt.Color.red);
        btnCancle.setFont(new java.awt.Font("TH SarabunPSK", 1, 24)); // NOI18N
        btnCancle.setForeground(new java.awt.Color(255, 255, 255));
        btnCancle.setText("ปิด");
        btnCancle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancleMouseClicked(evt);
            }
        });
        jPanelMain.add(btnCancle, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 410, -1, -1));

        getContentPane().add(jPanelMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 920, 460));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            countitem = 0;
            txtCountItem.setText(String.valueOf(countitem));
            if (tbServiceCode.getText().length() != 0 && !tbServiceCode.getText().equals("") && !tbServiceCode.getText().trim().isEmpty()) {
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "กรุณากรอกข้อมูลรหัส/ชื่อ หน่วยบริการก่อน",
                        "กรุณากรอกข้อมูล",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tableServiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableServiceMouseClicked
//        if (evt.getClickCount() == 2) {
//            int index = tableService.getSelectedRow();
//            String value = tableService.getValueAt(index, 1).toString();
//
//            txtCode.setText(tableService.getValueAt(index, 0).toString());
//            txtName.setText(tableService.getValueAt(index, 1).toString());
//
//            System.out.println("values===>>>" + value);
//            this.dispose();
//        }
        if (evt.getClickCount() == 1) {
            int index = tableService.getSelectedRow();
            Boolean checked = (Boolean) tableModel.getValueAt(index, 0);
            if (checked) {
                countitem++;
            } else {
                countitem--;
            }
            txtCountItem.setText(String.valueOf(countitem));
        }
    }//GEN-LAST:event_tableServiceMouseClicked

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        if (listSelectHospital.size() > 0) {
            listSelectHospital.removeAll(listSelectHospital);
        }
        if (txtAreaLIstHospital.getText().length() > 0) {
            txtAreaLIstHospital.removeAll();
        }

        for (int i = 0; i < tableService.getModel().getRowCount(); i++) {
            if (tableService.getModel().getValueAt(i, 0).equals(true)) {
                listSelectHospital.add(new HospitalService(
                        tableService.getModel().getValueAt(i, 1).toString(),
                        tableService.getModel().getValueAt(i, 2).toString()));
            }
        }

        if (listSelectHospital.size() == 0) {
            JOptionPane.showMessageDialog(this,
                    ConstantMessage.MSG_LIST_NULL_WARNING,
                    ConstantMessage.MSG_LIST_SELECT,
                    JOptionPane.ERROR_MESSAGE);
        } else {
            txtAreaLIstHospital.append("จำนวน หน่วยให้บริการ ที่เลือก ออกรายงาน ได้แก่ : \n");
            for (HospitalService hospitalService : listSelectHospital) {
                txtAreaLIstHospital.append("รหัสหน่วย: " + hospitalService.getHosCode()
                        + "    ชื่อหน่วย: " + hospitalService.getHosCodeName() + "\n");
            }
            txtAreaLIstHospital.append("ถ้า เลือก ผิด หรือ ไม่ครบกรุณาเลือกใหม่อีกครั้ง");
            this.dispose();
        }
    }//GEN-LAST:event_btnOKMouseClicked

    private void btnCancleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancleMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancleMouseClicked

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogService dialog = new DialogService();
                dialog.DialogServiceJInternalFrame(new JInternalFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private void loadTable() {
        waitIng.setTitle("กรุณารอ.....");
        waitIng.setVisible(true);
        waitIng.setLable("รอ.....");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = null;
                try {
                    tableService.setModel(tableModel);
                    int width = tableService.getParent().getSize().width;
                    // System.err.println("table Width: " + width);

                    ((DefaultTableModel) tableService.getModel()).setNumRows(0);
                    tableService.setAutoResizeMode(tableService.AUTO_RESIZE_OFF);

                    List<HospitalService> listServices = new UtilDao().getServiceAll(tbServiceCode.getText());
                    if (listServices.size() > 0) {
                        for (HospitalService hospitalService : listServices) {
                            Vector v = new Vector();
                            v.add(false);
                            v.add(hospitalService.getHosCode());
                            v.add(hospitalService.getHosCodeName());
                            tableModel.addRow(v);
                        }
                    } else {
                        ((DefaultTableModel) tableService.getModel()).setNumRows(0);
                        JOptionPane.showMessageDialog(DialogService.this,
                                "ไม่พบข้อมูล",
                                "ไม่พบข้อมูลที่ค้นหา",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    tableService.getColumnModel()
                            .getColumn(0).setPreferredWidth(60);
                    tableService.getColumnModel()
                            .getColumn(1).setPreferredWidth((width / 4) - 30);
                    tableService.getColumnModel()
                            .getColumn(2).setPreferredWidth((width) - 30);  //Math.round((tableSize.width) * 0.80f)
                    waitIng.setVisible(
                            false);
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        });
        thread.start();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelResult;
    private javax.swing.JPanel jPanelSearch;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableService;
    private javax.swing.JTextField tbServiceCode;
    private javax.swing.JTextField txtCountItem;
    // End of variables declaration//GEN-END:variables
}
