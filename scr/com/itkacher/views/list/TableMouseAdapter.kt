package com.itkacher.views.list

import com.itkacher.Resources
import com.itkacher.util.SystemUtil
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTable


class TableMouseAdapter(val listener: TableClickListener) : MouseAdapter() {

    private fun popupEvent(e: MouseEvent) {
        val x = e.x
        val y = e.y
        val source = e.source as JTable
        val row = source.rowAtPoint(e.point)
        val model = (source.model as RequestTableModel).getRequest(row)
        val popup = JPopupMenu()
        val copyUrl = JMenuItem(Resources.getString("jtable_popup_copy_url"))
        copyUrl.addActionListener {
            if(model != null) {
                SystemUtil.copyToClipBoard(model.url)
            }
        }
        val openUrl = JMenuItem(Resources.getString("jtable_popup_open_url_in_browser"))
        openUrl.addActionListener {
            if(model != null) {
                SystemUtil.openUrlInBrowser(model.url)
            }
        }
        val copyResponse = JMenuItem(Resources.getString("jtable_popup_copy_response"))
        copyResponse.addActionListener {
            if(model != null) {
                SystemUtil.copyToClipBoard(model.getResponseBodyString())
            }
        }
        popup.add(copyUrl)
        popup.add(openUrl)
        popup.add(copyResponse)
        popup.show(source, x, y)
    }

    override fun mousePressed(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupEvent(e)
        } else {
            mouseEvent(e)
        }
    }

    private fun mouseEvent(e: MouseEvent) {
        val source = e.source as JTable
        val row = source.rowAtPoint(e.point)
        (source.model as RequestTableModel).getRequest(row)?.let {
            listener.leftButtonClick(it)
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupEvent(e)
        }
    }
}