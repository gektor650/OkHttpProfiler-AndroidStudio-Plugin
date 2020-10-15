package com.itkacher.views.form

import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.itkacher.views.FrameScrollPanel
import com.itkacher.views.list.RequestTableModel

import javax.swing.*
import java.awt.*

class HeaderForm {
    val headersList = JBList<String>()
    val panel = FrameScrollPanel(headersList)
}
