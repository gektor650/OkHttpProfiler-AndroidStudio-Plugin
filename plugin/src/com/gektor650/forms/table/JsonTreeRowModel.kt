package com.gektor650.forms.table

import org.netbeans.swing.outline.RowModel


class JsonTreeRowModel : RowModel {
    override fun getValueFor(p0: Any?, p1: Int): Any? {
        return null
    }

    override fun setValueFor(p0: Any?, p1: Int, p2: Any?) {

    }

    override fun isCellEditable(p0: Any?, p1: Int): Boolean {
        return false
    }

    override fun getColumnName(p0: Int): String {
        return "name"
    }

    override fun getColumnClass(p0: Int): Class<*> {
        return String::class.java
    }

    override fun getColumnCount(): Int {
        return 2
    }

}