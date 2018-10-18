package com.itkacher.views.form;

import com.intellij.util.ui.JBUI;

import javax.swing.*;

public class DataForm {
    private JPanel dataPanel;
    private JTable requestTable;

    public JPanel getDataPanel() {
        return dataPanel;
    }

    public JTable getRequestTable() {
        return requestTable;
    }

    public JTabbedPane getTabsPane() {
        return tabsPane;
    }

    private JTabbedPane tabsPane;

}
