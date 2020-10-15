package com.itkacher.views.form

import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.table.JBTable
import com.itkacher.views.FrameScrollPanel
import com.itkacher.views.SimpleJBPanel
import java.awt.BorderLayout

class DataForm {
    val dataPanel = SimpleJBPanel()
    val requestTable = JBTable()
    val tabsPane = JBTabbedPane()


    init {
        val panel1 = FrameScrollPanel(requestTable)
        val panel2 = SimpleJBPanel()
        panel2.add(tabsPane, BorderLayout.CENTER)
        val splitter = JBSplitter(true)
        splitter.proportion = 0.5f
        splitter.firstComponent = panel1
        splitter.secondComponent = panel2
        dataPanel.add(splitter)
    }

}
