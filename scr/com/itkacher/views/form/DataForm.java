package com.itkacher.views.form;

import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.*;
import java.awt.*;

public class DataForm {
    private JBPanel dataPanel;
    private JBTable requestTable1;
    private JBTabbedPane tabsPane1;


    public JPanel getDataPanel() {
        return dataPanel;
    }

    public JTable getRequestTable() {
        return requestTable1;
    }

    public JTabbedPane getTabsPane() {
        return tabsPane1;
    }


    public DataForm() {
        dataPanel = new JBPanel(new BorderLayout());
        JBPanel panel1 = new JBPanel(new BorderLayout());
        JBPanel panel2 = new JBPanel(new BorderLayout());

        requestTable1 = new JBTable();
        tabsPane1 = new JBTabbedPane();

        JBScrollPane scroll1 = new JBScrollPane(requestTable1);

        panel1.add(scroll1, BorderLayout.CENTER);
        panel2.add(tabsPane1, BorderLayout.CENTER);

        JBSplitter splitter = new JBSplitter(true);
        splitter.setProportion(0.5f);
        splitter.setFirstComponent(panel1);
        splitter.setSecondComponent(panel2);

        dataPanel.removeAll();
        dataPanel.add(splitter, BorderLayout.CENTER);
    }

}
