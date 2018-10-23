package com.itkacher.views.list

import com.itkacher.Resources
import com.itkacher.data.DebugRequest
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTable


class TableMouseAdapter : MouseAdapter() {

    private fun popupEvent(e: MouseEvent) {
        val x = e.x
        val y = e.y
        val source = e.source as JTable
        val row = source.rowAtPoint(e.point)
        val model = (source.model as RequestTableModel).getRequest(row)
        val popup = JPopupMenu()
        val javaClassItem = JMenuItem(Resources.getString("jtable_popup_copy_response"))
        javaClassItem.addActionListener {
            if(model != null) {
                val stringSelection = StringSelection(model.url)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
            }
        }
        popup.add(javaClassItem)
        popup.show(source, x, y)
    }

    override fun mousePressed(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupEvent(e)
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupEvent(e)
        }
    }
}