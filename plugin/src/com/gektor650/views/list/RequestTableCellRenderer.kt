package com.gektor650.views.list

import java.awt.Color
import java.awt.Component
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.JTable



class RequestTableCellRenderer: DefaultTableCellRenderer() {
    lateinit var foregroundColor: Color

    override fun getTableCellRendererComponent(table: JTable?, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component? {
        val c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        if(! ::foregroundColor.isInitialized) {
            foregroundColor = c.foreground
        }
        val model = table?.model
        if(model is RequestTableModel) {
            val fallen = model.isFallenDown(row)
            if(fallen) {
                c.foreground = Color.RED
            } else {
                c.foreground = foregroundColor
            }
        }
        return c
    }
}