package com.itkacher.views.form;

import com.itkacher.Resources;
import com.itkacher.data.DebugDevice;
import com.itkacher.data.DebugProcess;

import javax.swing.*;
import java.awt.*;

public class MainForm {
    private JPanel panel;
    private JComboBox<DebugDevice> deviceList;
    private JComboBox<DebugProcess> appList;
    private JPanel mainContainer;
    private JEditorPane initialHtml;
    private JPanel buttonContainer;
    private final JButton scrollToBottomButton;
    private final JButton clearButton;

    public MainForm() {
        scrollToBottomButton = new JButton();
        scrollToBottomButton.setIcon(Resources.Companion.getIcon(getClass().getClassLoader(), "scroll.png"));
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;

        clearButton = new JButton();
        clearButton.setIcon(Resources.Companion.getIcon(getClass().getClassLoader(), "delete.png"));
        GridBagConstraints clearConstraints = new GridBagConstraints();
        clearConstraints.gridx = 1;
        clearConstraints.gridy = 0;

        buttonContainer.add(scrollToBottomButton, scrollConstraints);
        buttonContainer.add(clearButton, clearConstraints);

    }

    public JPanel getPanel() {
        return panel;
    }

    public JComboBox<DebugDevice> getDeviceList() {
        return deviceList;
    }

    public JComboBox<DebugProcess> getAppList() {
        return appList;
    }

    public JButton getScrollToBottomButton() {
        return scrollToBottomButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public JEditorPane getInitialHtml() {
        return initialHtml;
    }
}
