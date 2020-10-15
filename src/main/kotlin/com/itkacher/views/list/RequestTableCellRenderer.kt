package com.itkacher.views.list

import java.awt.Color
import java.awt.Component
import javax.swing.JTable
import javax.swing.UIManager
import javax.swing.table.DefaultTableCellRenderer


class RequestTableCellRenderer : DefaultTableCellRenderer() {
    //Hard way to find(
    private var foregroundColor =  UIManager.getLookAndFeelDefaults().getColor("EditorPane.foreground")

    override fun getTableCellRendererComponent(table: JTable?, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component? {
        val c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        val model = table?.model
        if (model is RequestTableModel) {
            val fallen = model.isFallenDown(row)
            when {
                fallen -> c.foreground = Color.RED
                isSelected -> c.foreground = Color.WHITE
                else -> c.foreground = foregroundColor
            }
        }
        return c
    }
}