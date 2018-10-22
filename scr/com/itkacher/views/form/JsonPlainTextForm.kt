package com.itkacher.views.form

import com.itkacher.views.FrameScrollPanel
import javax.swing.*

class JsonPlainTextForm {
    val editorPane = JTextPane()
    val panel = FrameScrollPanel(editorPane)

    init {
        editorPane.isEditable = false
    }
}
