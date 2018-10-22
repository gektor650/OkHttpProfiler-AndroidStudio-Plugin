package com.itkacher.views

import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.Component

class FrameScrollPanel(comp: Component) : JBPanel<FrameScrollPanel>(BorderLayout()) {
    init {
        val scrollPane = JBScrollPane(comp)
        super.add(scrollPane, BorderLayout.CENTER)
    }

}
