package com.itkacher.views.json

import com.itkacher.Resources
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode


class JTreeMenuMouseAdapter(private val listener: JTreeItemMenuListener) : MouseAdapter() {

    private fun popupEvent(e: MouseEvent) {
        val x = e.x
        val y = e.y
        val tree = e.source as JTree
        val path = tree.getPathForLocation(x, y) ?: return

        val rightClickedNode = path
                .lastPathComponent as DefaultMutableTreeNode

        val selectionPaths = tree.selectionPaths

        var isSelected = false
        if (selectionPaths != null) {
            for (selectionPath in selectionPaths) {
                if (selectionPath == path) {
                    isSelected = true
                }
            }
        }
        if (!isSelected) {
            tree.selectionPath = path
        }
        val popup = JPopupMenu()
        val javaClassItem = JMenuItem(Resources.getString("jtree_popup_create_java_class"))
        javaClassItem.addActionListener {
            listener.createJavaModel(rightClickedNode as JsonMutableTreeNode)
        }
        popup.add(javaClassItem)
        val kotlinClassItem = JMenuItem(Resources.getString("jtree_popup_create_kotlin_class"))
        kotlinClassItem.addActionListener {
            listener.createKotlinModel(rightClickedNode as JsonMutableTreeNode)
        }
        popup.add(kotlinClassItem)
        popup.show(tree, x, y)
    }


    override fun mousePressed(e: MouseEvent) {
        if (e.isPopupTrigger)
            popupEvent(e)
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.isPopupTrigger)
            popupEvent(e)
    }
}