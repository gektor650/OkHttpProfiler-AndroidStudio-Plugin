package com.gektor650.views.json

import org.netbeans.swing.outline.RowModel

class JsonTreeRowModel : RowModel {
    override fun getValueFor(node: Any?, column: Int): Any? {
        return when (column) {
            0 -> (node as JsonMutableTreeNode).type.nodeName
            else -> {
                null
            }
        }
    }

    override fun setValueFor(p0: Any?, p1: Int, p2: Any?) {
        p0.toString()
    }

    override fun isCellEditable(p0: Any?, p1: Int): Boolean {
        return false
    }

    override fun getColumnName(p0: Int): String {
        return ColumnHeader.values()[p0].nameVal
    }

    override fun getColumnClass(p0: Int): Class<*> {
        return String::class.java
    }

    override fun getColumnCount(): Int {
        return ColumnHeader.values().size
    }

    enum class ColumnHeader(val nameVal: String) {
        TYPE("Type")
    }

}