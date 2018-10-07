package com.gektor650.views.list

import java.awt.Component
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.JTable



class RequestTableCellRenderer: DefaultTableCellRenderer() {

    override fun getTableCellRendererComponent(table: JTable?, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component? {
        val c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        val model = table?.model
        if(model is RequestTableModel) {
            val color = model.getBackgroundColor(row)
            if(color != null) {
                c.background = color
            }
        }
        return c
    }
}