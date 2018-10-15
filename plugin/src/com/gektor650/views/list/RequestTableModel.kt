package com.gektor650.views.list

import com.gektor650.data.DebugRequest
import java.awt.Color
import javax.swing.table.DefaultTableModel


class RequestTableModel : DefaultTableModel() {
    private val requestMap = HashMap<DebugRequest, Int>()
    private val requestList = ArrayList<DebugRequest>()

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
            requestList.add(request)
        } else {
            val rowIndex = requestMap[request]
            if (rowIndex != null) {
                for ((i, any) in getRowData(request).withIndex()) {
                    setValueAt(any, rowIndex, i)
                    requestList[rowIndex] = request
                }
            }
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    private fun getRowData(request: DebugRequest): Array<Any?> {
        return arrayOf(request.responseCode ?: "Loading...", request.method, request.url, request.duration, request.requestTime)
    }

    fun clear() {
    }

    fun getRequest(selectedRow: Int): DebugRequest? {
        for (mutableEntry in requestMap) {
            if (mutableEntry.value == selectedRow) {
                return mutableEntry.key
            }
        }
        return null
    }

    fun isFallenDown(row: Int): Boolean {
        return requestList[row].isFallenDown()
    }
}
