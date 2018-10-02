package com.gektor650.forms;

import com.gektor650.models.DebugDevice;
import com.gektor650.models.DebugProcess;

import javax.swing.*;

public class DebuggerForm {
    private JPanel panel;
    private JComboBox<DebugDevice>  deviceList;
    private JComboBox<DebugProcess>  appList;
    private JButton scrollToBottomButton;
    private JTabbedPane tabs;
    private JTextPane rawRequest;
    private JTextPane rawResponse;
    private JTable requestTable;
    private JPanel requestJson;
    private JPanel responseJson;
    private JEditorPane requestEditorPane;
    private JTree tree1;

    public JPanel getPanel() {
        return panel;
    }

    public JComboBox<DebugDevice> getDeviceList() {
        return deviceList;
    }

    public JComboBox<DebugProcess>  getAppList() {
        return appList;
    }

    public JTable getRequestTable() {
        return requestTable;
    }

    public JButton getScrollToBottomButton() {
        return scrollToBottomButton;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    public JTextPane getRawRequest() {
        return rawRequest;
    }

    public JTextPane getRawResponse() {
        return rawResponse;
    }

    public JPanel getRequestJson() {
        return requestJson;
    }

    public JPanel getResponseJson() {
        return responseJson;
    }

    public JEditorPane getRequestEditorPane() {
        return requestEditorPane;
    }

    public JTree getTree1() {
        return tree1;
    }
}
