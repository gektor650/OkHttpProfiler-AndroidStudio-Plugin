package com.itkacher.views.form

import com.intellij.ui.treeStructure.Tree
import com.itkacher.views.FrameScrollPanel
import java.awt.Font

class JsonTreeForm {
    val tree = Tree()
    val treePanel = FrameScrollPanel(tree)

    init {
        tree.font = Font(Font.MONOSPACED, Font.PLAIN, 12)
    }
}
