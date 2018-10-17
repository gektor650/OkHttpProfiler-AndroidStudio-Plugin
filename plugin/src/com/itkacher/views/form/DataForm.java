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
    private JButton deleteButton;
    private JButton scrollButton;

    public DataForm() {
        deleteButton.setBorderPainted(false);
        deleteButton.setBorder(null);
        deleteButton.setMargin(JBUI.emptyInsets());
        deleteButton.setContentAreaFilled(false);

        scrollButton.setBorderPainted(false);
        scrollButton.setBorder(null);
        scrollButton.setMargin(JBUI.emptyInsets());
        scrollButton.setContentAreaFilled(false);
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getScrollButton() {
        return scrollButton;
    }

}
