package com.gektor650.forms;

import com.gektor650.models.DebugDevice;
import com.gektor650.models.DebugProcess;

import javax.swing.*;

public class DebuggerForm {
    private JPanel panel;
    private JComboBox<DebugDevice>  deviceList;
    private JComboBox<DebugProcess>  appList;
    private JList requestList;
    private JButton scrollToBottomButton;
    private JTabbedPane tabs;
    private JTextPane rawRequest;
    private JTextPane rawResponse;

    public JPanel getPanel() {
        return panel;
    }

    public JComboBox<DebugDevice> getDeviceList() {
        return deviceList;
    }

    public JComboBox<DebugProcess>  getAppList() {
        return appList;
    }

    public JList getRequestList() {
        return requestList;
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
}
