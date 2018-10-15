package com.gektor650

import com.gektor650.data.DebugRequest
import com.gektor650.views.Tabs
import com.gektor650.views.TabsHelper
import com.gektor650.views.form.DataForm
import com.gektor650.views.form.DebuggerForm
import com.gektor650.views.list.ForcedListSelectionModel
import com.gektor650.views.list.RequestTableCellRenderer
import com.gektor650.views.list.RequestTableModel
import java.awt.GridLayout
import javax.swing.JTable


class FormViewController(private val form: DebuggerForm) {

    private val dataForm = DataForm()
    private val requestTable = dataForm.requestTable
    private val requestListModel = RequestTableModel()
    private val tabsHelper = TabsHelper(dataForm.tabsPane)
    private val genericTabTitles = listOf(
            Tabs.TAB_RAW_REQUEST,
            Tabs.TAB_RAW_RESPONSE,
            Tabs.TAB_JSON_RESPONSE,
            Tabs.TAB_JSON_REQUEST,
            Tabs.TAB_REQUEST_FORMATTED,
            Tabs.TAB_RESPONSE_FORMATTED
    )

    private var firstLaunch = true

    init {
        requestTable.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        requestTable.model = requestListModel
        requestTable.setDefaultRenderer(Any::class.java, RequestTableCellRenderer())
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
        tabsHelper.removeAllTabs()
        tabsHelper.addRawTab(Tabs.TAB_RAW_REQUEST.resName, debugRequest.getRawRequest())
        tabsHelper.addHeaderTab(Tabs.TAB_REQUEST_HEADERS.resName, debugRequest.requestHeaders)
        if (debugRequest.isClosed) {
            tabsHelper.addRawTab(Tabs.TAB_RAW_RESPONSE.resName, debugRequest.getRawResponse())
            val requestJson = debugRequest.getRequestBodyString()
            tabsHelper.addJsonTab(Tabs.TAB_JSON_REQUEST.resName, requestJson)
            tabsHelper.addFormattedTab(Tabs.TAB_REQUEST_FORMATTED.resName, requestJson)

            val responseBody = debugRequest.getResponseBodyString()
            tabsHelper.addHeaderTab(Tabs.TAB_RESPONSE_HEADERS.resName, debugRequest.responseHeaders)
            tabsHelper.addJsonTab(Tabs.TAB_JSON_RESPONSE.resName, responseBody)
            tabsHelper.addFormattedTab(Tabs.TAB_RESPONSE_FORMATTED.resName, responseBody)
        }
    }

    fun clearSelection() {
        requestTable.clearSelection()
    }

    fun insertOrUpdate(debugRequest: DebugRequest) {
        if (firstLaunch) {
            form.mainContainer.removeAll()
            val panel = dataForm.dataPanel
            panel.layout = GridLayout(1, 1)
            form.mainContainer.add(dataForm.dataPanel)
            firstLaunch = false
        }
        requestListModel.addOrUpdate(debugRequest)
    }

    fun clear() {
        requestListModel.clear()
    }

}