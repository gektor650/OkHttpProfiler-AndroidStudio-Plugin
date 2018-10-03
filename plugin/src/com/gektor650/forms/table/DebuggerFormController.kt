package com.gektor650.forms.table

import com.gektor650.forms.DebuggerForm
import com.gektor650.models.DebugRequest

class DebuggerFormController(private val form: DebuggerForm) {

    private val requestListModel = RequestTableModel()
    private val requestTable = form.requestTable

    init {
        requestTable.model = requestListModel
        requestTable.selectionModel = ForcedListSelectionModel()
        requestTable.selectionModel.addListSelectionListener { it ->
            if (!it.valueIsAdjusting) {
                requestListModel.getRequest(requestTable.selectedRow)?.let {
                    fillRequestInfo(it)
                }
            }
        }
        form.responseJsonTree.model = null
        form.responseJsonTree.cellRenderer = JsonJTreeRenderer()

        form.requestJsonTree.model = null
        form.requestJsonTree.cellRenderer = JsonJTreeRenderer()
    }

    private fun fillRequestInfo(debugRequest: DebugRequest) {
        form.rawResponse.text = debugRequest.getRawResponse()
        form.rawRequest.text = debugRequest.getRawRequest()
        if (debugRequest.isClosed) {
            val responseJson = debugRequest.getResponseJsonNode()
            if (responseJson != null) {
                form.responseJson.isVisible = true
                form.responseJsonTree.model = JsonTreeObject(responseJson)
            } else {
                form.responseJson.isVisible = false
            }
            val requestJson = debugRequest.getRequestJsonNode()
            if (requestJson != null) {
                form.requestJson.isVisible = true
                form.requestJsonTree.model = JsonTreeObject(requestJson)
            } else {
                form.requestJson.isVisible = false
            }
        }
    }

    fun clearSelection() {
        requestTable.clearSelection()
    }

    fun insertOrUpdate(debugRequest: DebugRequest) {
        requestListModel.addOrUpdate(debugRequest)
    }

    fun clear() {
        requestListModel.clear()
    }


}