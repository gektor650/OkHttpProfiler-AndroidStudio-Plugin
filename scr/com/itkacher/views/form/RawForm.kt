package com.itkacher.views.form

import com.itkacher.views.FrameScrollPanel
import javax.swing.JTextPane

class RawForm(data: String?)  {
    private val editor = JTextPane()
    val panel = FrameScrollPanel(editor)

    init {
        editor.isEditable = false
        editor.text = data
        editor.caretPosition = 0
    }
}
