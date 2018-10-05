package com.gektor650.views

import com.fasterxml.jackson.databind.JsonNode
import com.gektor650.views.json.JsonTreeModel
import javax.swing.JTabbedPane


class TabsHelper(private val tabbedPane: JTabbedPane) {

    fun addJsonTab(title: String, jsonNode: JsonNode) {
        val model = JsonTreeModel(jsonNode)
        val jsonTreeForm = JsonTreeForm()
        jsonTreeForm.jtree.model = model
        tabbedPane.addTab(title, jsonTreeForm.jtreePanel)
    }

    fun removeTab(title: String) {
        val index = tabbedPane.indexOfTab(title)
        if (index >= 0) {
            tabbedPane.removeTabAt(index)
        }
    }

    fun addFormattedTab(text: String, value: String) {
        val plain = JsonPlainTextForm()
        plain.editorPane.text = value
        plain.editorPane.caretPosition = 0
        tabbedPane.addTab(text, plain.panel)
    }
}