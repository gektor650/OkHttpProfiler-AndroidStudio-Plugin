package com.gektor650.views;

import com.gektor650.models.DebugDevice;
import com.gektor650.models.DebugProcess;

import javax.swing.*;

public class DebuggerForm {
    private JPanel panel;
    private JComboBox<DebugDevice>  deviceList;
    private JComboBox<DebugProcess>  appList;
    private JButton scrollToBottomButton;
    private JTabbedPane tabs;
    private JTable requestTable;
    private JEditorPane rawRequest;
    private JEditorPane rawResponse;

    public DebuggerForm() {
        rawRequest.setContentType("text/plain");
        rawResponse.setContentType("text/plain");
    }

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

    public JEditorPane getRawRequest() {
        return rawRequest;
    }

    public JEditorPane getRawResponse() {
        return rawResponse;
    }
}
