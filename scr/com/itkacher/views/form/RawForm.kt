package com.itkacher.views.form

import com.itkacher.views.FrameScrollPanel
import javax.swing.*

class RawForm {
    val editor = JTextPane()
    val panel = FrameScrollPanel(editor)

    init {
        editor.isEditable = false
    }
}
