package com.gektor650.forms.table

import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer


class JsonJTreeRenderer : DefaultTreeCellRenderer() {

    @Override
    override fun getTreeCellRendererComponent(tree: JTree?, value: Any?, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Component {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus)
//        val nodo = value as DefaultMutableTreeNode
//        if (tree.getModel().root == nodo) {
//            setIcon(root)
//        } else if (nodo.childCount > 0) {
//            setIcon(parent)
//        } else {
//            setIcon(leaf)
//        }
        return this
    }

}