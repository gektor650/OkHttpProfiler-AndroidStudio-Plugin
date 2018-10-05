package com.gektor650.views

import com.gektor650.StringValues
import com.gektor650.models.DebugRequest
import com.gektor650.views.list.ForcedListSelectionModel
import com.gektor650.views.list.RequestTableModel
import com.gektor650.views.list.TableColumn
import javax.swing.JTable


class FormViewController(private val form: DebuggerForm) {

    private val requestTable = form.requestTable
    private val requestListModel = RequestTableModel()
    private val tabsHelper = TabsHelper(form.tabs)
    private val genericTabTitles = listOf(
            StringValues.TAB_JSON_RESPONSE.text,
            StringValues.TAB_JSON_REQUEST.text,
            StringValues.TAB_REQUEST_FORMATTED.text,
            StringValues.TAB_RESPONSE_FORMATTED.text
    )

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

        for(i in 0 until requestListModel.columnCount) {
            requestTable.autoResizeMode = JTable.AUTO_RESIZE_OFF
            requestTable.columnModel.getColumn(i).preferredWidth = TableColumn.values()[i].width
        }
    }

    private fun fillRequestInfo(debugRequest: DebugRequest) {
        form.rawResponse.text = debugRequest.getRawResponse()
        form.rawRequest.text = debugRequest.getRawRequest()
        form.rawResponse.caretPosition = 0
        form.rawRequest.caretPosition = 0
        removeGenericTabs()
        if (debugRequest.isClosed) {
            val requestJson = debugRequest.getRequestJsonNode()
            if (requestJson != null) {
                tabsHelper.addJsonTab(StringValues.TAB_JSON_REQUEST.text, requestJson)
                debugRequest.prettifyNode(requestJson)?.let {
                    tabsHelper.addFormattedTab(StringValues.TAB_REQUEST_FORMATTED.text, it)
                }
            }

            val responseJson = debugRequest.getResponseJsonNode()
            if (responseJson != null) {
                tabsHelper.addJsonTab(StringValues.TAB_JSON_RESPONSE.text, responseJson)
                debugRequest.prettifyNode(responseJson)?.let {
                    tabsHelper.addFormattedTab(StringValues.TAB_REQUEST_FORMATTED.text, it)
                }
            }
        }
    }

    private fun removeGenericTabs() {
        for (genericTab in genericTabTitles) {
            tabsHelper.removeTab(genericTab)
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