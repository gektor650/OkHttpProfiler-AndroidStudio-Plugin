package com.itkacher.views.list

import com.itkacher.Resources
import com.itkacher.data.DebugRequest
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
        if(! request.isValid()) return
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
        val code: String = when {
            request.errorMessage?.isNotEmpty() == true -> Resources.getString("request_list_fallen")
            request.responseCode == null -> Resources.getString("request_list_loading")
            else -> request.responseCode.toString()
        }
        return arrayOf(code, request.method, request.url, request.duration, request.requestTime)
    }

    fun clear() {
        requestMap.clear()
        requestList.clear()
        while (super.getRowCount().compareTo(0) == 1) {
            super.removeRow(0)
        }
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
        return requestList.getOrNull(row)?.isFallenDown() ?: true
    }

    fun addAll(requestList: List<DebugRequest>) {
        for (request in requestList) {
            addOrUpdate(request)
        }
    }
}
