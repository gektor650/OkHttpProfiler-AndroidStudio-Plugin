package com.itkacher.views.form;

import com.itkacher.data.DebugDevice;
import com.itkacher.data.DebugProcess;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DebuggerForm {
    private JPanel panel;
    private JComboBox<DebugDevice>  deviceList;
    private JComboBox<DebugProcess>  appList;
    private JButton scrollToBottomButton;
    private JPanel mainContainer;
    private JEditorPane initialHtml;

    public DebuggerForm() {
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

    public JButton getScrollToBottomButton() {
        return scrollToBottomButton;
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public JEditorPane getInitialHtml() {
        return initialHtml;
    }
}
