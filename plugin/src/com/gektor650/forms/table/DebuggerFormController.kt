package com.gektor650.forms.table

import com.gektor650.forms.DebuggerForm
import com.gektor650.models.ContentType
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
        form.requestEditorPane.contentType = "text/html"

    }

    private fun fillRequestInfo(debugRequest: DebugRequest) {
        form.rawResponse.text = debugRequest.getRawResponse()
        form.rawRequest.text = debugRequest.getRawRequest()
        if(debugRequest.isClosed) {
            form.responseJson.isVisible = debugRequest.responseContentType == ContentType.JSON
            form.requestJson.isVisible = debugRequest.requestContentType == ContentType.JSON
            val json = debugRequest.getResponseJsonNode()
            if(json != null) {
                form.tree1.model = JsonTreeObject(json)
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