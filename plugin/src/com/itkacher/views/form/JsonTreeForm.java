package com.itkacher.views.form;

import javax.swing.*;
import java.awt.*;

public class JsonTreeForm {
    private JTree jtree;
    private JPanel jtreePanel;

    public JsonTreeForm() {
        jtree.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    public JTree getJtree() {
        return jtree;
    }

    public JPanel getJtreePanel() {
        return jtreePanel;
    }
}
