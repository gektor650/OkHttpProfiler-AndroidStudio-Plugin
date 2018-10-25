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
        val treeNode = rightClickedNode as JsonMutableTreeNode
        val popup = JPopupMenu()
        val copyItem = JMenuItem(Resources.getString("jtree_popup_copy_to_clipboard"))
        copyItem.addActionListener {
            listener.copyToClipboard(treeNode)
        }
        val openItem = JMenuItem(Resources.getString("jtree_popup_open_in_editor"))
        openItem.addActionListener {
            listener.openInEditor(treeNode)
        }
//        val javaClassItem = JMenuItem(Resources.getString("jtree_popup_create_java_class"))
//        javaClassItem.addActionListener {
//            listener.createJavaModel(treeNode)
//        }
//        popup.add(javaClassItem)
//        val kotlinClassItem = JMenuItem(Resources.getString("jtree_popup_create_kotlin_class"))
//        kotlinClassItem.addActionListener {
//            listener.createKotlinModel(treeNode)
//        }
//        popup.add(kotlinClassItem)
        popup.add(copyItem)
        popup.add(openItem)
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