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
    private JList logList;

    public JPanel getPanel() {
        return panel;
    }

    public JComboBox<DebugDevice> getDeviceList() {
        return deviceList;
    }

    public JList getLogList() {
        return logList;
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
