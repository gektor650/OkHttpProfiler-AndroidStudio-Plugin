package com.gektor650.forms.table

import com.gektor650.forms.DebuggerForm
import com.gektor650.models.DebugRequest
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import org.netbeans.swing.outline.DefaultOutlineModel
import org.netbeans.swing.outline.Outline
import org.netbeans.swing.outline.OutlineModel
import javax.swing.JScrollPane





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
//        form.responseJsonTree.model = null
//        form.responseJsonTree.cellRenderer = JsonJTreeRenderer()

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
                val mdl = DefaultOutlineModel.createOutlineModel(JsonTreeObject(responseJson), JsonTreeRowModel())
                val outline = Outline()
                outline.isRootVisible = true
                outline.model = mdl
                val scroll = JBScrollPane(outline)
                form.responseJson.add(scroll)
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