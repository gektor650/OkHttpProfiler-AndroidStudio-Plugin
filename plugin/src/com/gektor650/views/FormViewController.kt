package com.gektor650.views

import com.gektor650.models.DebugRequest
import com.gektor650.views.json.JsonTreeModel
import com.gektor650.views.list.ForcedListSelectionModel
import com.gektor650.views.list.RequestTableModel


class FormViewController(private val form: DebuggerForm) {

    private val requestListModel = RequestTableModel()
    private val requestTable = form.requestTable
    private val tabsHelper = TabsHelper(form.tabs)

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
    }

    private fun fillRequestInfo(debugRequest: DebugRequest) {
        form.rawResponse.text = debugRequest.getRawResponse()
        form.rawRequest.text = debugRequest.getRawRequest()
        if (debugRequest.isClosed) {
            val responseJson = debugRequest.getResponseJsonNode()
            if (responseJson != null) {
                form.responseJson.isVisible = true
                tabsHelper.addJsonTab(form.responseJson, responseJson)
            } else {
                form.responseJson.isVisible = false
            }
            val requestJson = debugRequest.getRequestJsonNode()
            if (requestJson != null) {
                form.requestJson.isVisible = true
                tabsHelper.addJsonTab(form.requestJson, requestJson)
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