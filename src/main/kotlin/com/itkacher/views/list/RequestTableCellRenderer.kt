/**
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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