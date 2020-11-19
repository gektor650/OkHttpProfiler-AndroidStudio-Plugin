/*
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itkacher.views.form;

import com.itkacher.Resources;
import com.itkacher.data.DebugDevice;
import com.itkacher.data.DebugProcess;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
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
    private final JButton localizeButton;

    public MainForm() {
        localizeButton = new JButton();
        localizeButton.setPreferredSize(new Dimension(200,30));
        localizeButton.setIcon(Resources.Companion.getIcon("localebro.png"));
//        localizeButton.setOpaque(true);
//        localizeButton.setBorder(null);
//        localizeButton.setBackground(JBColor.WHITE);
//        localizeButton.setForeground(JBColor.DARK_GRAY);
        localizeButton.setText(Resources.Companion.getString("localize"));
        GridBagConstraints localeBroConstraints = new GridBagConstraints();
        localeBroConstraints.gridx = 0;
        localeBroConstraints.gridy = 0;

        scrollToBottomButton = new JButton();
        scrollToBottomButton.setIcon(Resources.Companion.getIcon("scroll.png"));
        scrollToBottomButton.setPreferredSize(new Dimension(30,30));
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 1;
        scrollConstraints.gridy = 0;

        clearButton = new JButton();
        clearButton.setPreferredSize(new Dimension(30,30));
        clearButton.setIcon(Resources.Companion.getIcon("delete.png"));
        GridBagConstraints clearConstraints = new GridBagConstraints();
        clearConstraints.gridx = 2;
        clearConstraints.gridy = 0;

        buttonContainer.add(localizeButton, localeBroConstraints);
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

    public JButton getLocalizeButton() {
        return localizeButton;
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public JEditorPane getInitialHtml() {
        return initialHtml;
    }
}
