package com.gektor650.forms.table

import com.gektor650.models.DebugRequest
import javax.swing.table.DefaultTableModel


class RequestTableModel : DefaultTableModel() {
    private val requestMap = HashMap<DebugRequest, Int>()

    init {
        columnCount = TableColumn.values().size
    }

    override fun getColumnName(column: Int): String {
        return TableColumn.values()[column].text
    }

    fun addOrUpdate(request: DebugRequest) {
        if (!requestMap.keys.contains(request)) {
            requestMap[request] = rowCount
            super.addRow(getRowData(request))
        } else {
            val rowIndex = requestMap[request]
            if (rowIndex != null) {
                for ((i, any) in getRowData(request).withIndex()) {
                    setValueAt(any, rowIndex, i)
                }
            }
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    private fun getRowData(request: DebugRequest): Array<Any?> {
        return arrayOf(request.id, request.method, request.url, request.duration, request.getStartTimeString(), request.status ?: "Loading...")
    }

    fun clear() {
//        for (i in 0 until rowCount) {
//            removeRow(i)
//        }
    }

    fun getRequest(selectedRow: Int): DebugRequest? {
        for (mutableEntry in requestMap) {
            if (mutableEntry.value == selectedRow) {
                return mutableEntry.key
            }
        }
        return null
    }
}
