package com.itkacher

import com.itkacher.data.DebugRequest
import com.itkacher.views.Tabs
import com.itkacher.views.TabsHelper
import com.itkacher.views.form.DataForm
import com.itkacher.views.form.MainForm
import com.itkacher.views.list.ForcedListSelectionModel
import com.itkacher.views.list.RequestTableCellRenderer
import com.itkacher.views.list.RequestTableModel
import java.awt.GridLayout
import javax.swing.JTable


class FormViewController(private val form: MainForm) {

    private val dataForm = DataForm()
    private val requestTable = dataForm.requestTable
    private val requestListModel = RequestTableModel()
    private val tabsHelper = TabsHelper(dataForm.tabsPane)
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
        form.clearButton.addActionListener {
            requestListModel.clear()
            tabsHelper.removeAllTabs()
        }
        form.scrollToBottomButton.addActionListener {
            requestTable.clearSelection()
            requestTable.scrollRectToVisible(requestTable.getCellRect(requestTable.rowCount -1, 0, true))
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
            if(debugRequest.errorMessage?.isNotEmpty() == true) {
                tabsHelper.addRawTab(Tabs.TAB_ERROR_MESSAGE.resName, debugRequest.errorMessage)
            }
        }
    }

    fun insertOrUpdate(debugRequest: DebugRequest) {
        if (firstLaunch) {
            form.mainContainer.removeAll()
            val panel = dataForm.dataPanel
            panel.layout = GridLayout(1, 1)
            form.mainContainer.add(dataForm.dataPanel)
            firstLaunch = false
        }
        if(requestTable.selectedColumn == -1) {
            requestTable.scrollRectToVisible(requestTable.getCellRect(requestTable.rowCount -1, 0, true))
        }
        requestListModel.addOrUpdate(debugRequest)
    }

    fun clear() {
        requestListModel.clear()
    }

}