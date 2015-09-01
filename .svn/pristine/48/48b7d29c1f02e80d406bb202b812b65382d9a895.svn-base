/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author
 * poon__000
 */
public class Picker {
    private JTextField txtDateOPD;
    private JButton btnChoose;
    private JPanel panel;
    private JFrame frame;
    private JInternalFrame internalFrame;
    private String formatDate;

    public Picker() {
    }

    public void PickerJFrame(java.awt.Frame parent, Map map) {
        try {           
            btnChoose = (JButton) map.get("btn");
            txtDateOPD = (JTextField) map.get("txt");
            panel = (JPanel) map.get("panel");
            formatDate = map.get("formatDate").toString();
            frame = (JFrame) parent;
            btnChoose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    txtDateOPD.setText(new DatePicker(frame).setPickedDate(formatDate));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void PickerJInternal(JInternalFrame parent, Map map) {
        try {           
            btnChoose = (JButton) map.get("btn");
            txtDateOPD = (JTextField) map.get("txt");
            panel = (JPanel) map.get("panel");
            formatDate = map.get("formatDate").toString();
            internalFrame = (JInternalFrame) parent;
            btnChoose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    txtDateOPD.setText(new DatePicker(frame).setPickedDate(formatDate));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
