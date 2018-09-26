package com.gektor650.forms;

import com.gektor650.models.DebugDevice;
import com.gektor650.models.DebugProcess;

import javax.swing.*;

public class DebuggerForm {
    private JPanel panel;
    private JTextArea area;
    private JComboBox<DebugDevice>  deviceList;
    private JComboBox<DebugProcess>  appList;
    private JList requestList;
    private JButton scrollToBottomButton;

    public JPanel getPanel() {
        return panel;
    }

    public JTextArea getArea() {
        return area;
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
}
