package com.itkacher.views.form;

import com.intellij.util.ui.HtmlPanel;
import com.itkacher.Resources;
import com.itkacher.data.DebugDevice;
import com.itkacher.data.DebugProcess;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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
        scrollToBottomButton.setIcon(Resources.Companion.getIcon("scroll.png"));
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;

        clearButton = new JButton();
        clearButton.setIcon(Resources.Companion.getIcon("delete.png"));
        GridBagConstraints clearConstraints = new GridBagConstraints();
        clearConstraints.gridx = 1;
        clearConstraints.gridy = 0;

        buttonContainer.add(scrollToBottomButton, scrollConstraints);
        buttonContainer.add(clearButton, clearConstraints);

        initialHtml.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        initialHtml.setEditable(false);

        initialHtml.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        try {
            URL url = new URL("https://itkacher.github.io/OkHttpProfiler/plugin_initial.html");
            initialHtml.setPage(url);
        } catch (IOException e) {
            e.printStackTrace();
            URL initialFile = getClass().getClassLoader().getResource("initial.html");
            if (initialFile != null) {
                try {
                    initialHtml.setPage(initialFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

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
