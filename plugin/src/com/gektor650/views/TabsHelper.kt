package com.gektor650.views

import com.fasterxml.jackson.databind.JsonNode
import com.gektor650.views.json.JsonTreeModel
import com.gektor650.views.json.JsonTreeRowModel
import com.intellij.ui.components.JBScrollPane
import com.intellij.uiDesigner.core.GridConstraints
import org.netbeans.swing.outline.DefaultOutlineModel
import org.netbeans.swing.outline.Outline
import java.awt.Component
import javax.swing.JPanel
import javax.swing.JTabbedPane

class TabsHelper(private val tabbedPane: JTabbedPane) {

    fun addJsonTab(parent: JPanel, jsonNode: JsonNode) {
        val mdl = DefaultOutlineModel.createOutlineModel(JsonTreeModel(jsonNode), JsonTreeRowModel())
        val outline = Outline()
        outline.isRootVisible = true
        outline.model = mdl
        val scrollPane = JBScrollPane(outline)
        val constraints = GridConstraints()
        constraints.fill = GridConstraints.FILL_BOTH
        parent.add(scrollPane, constraints)
    }

    fun removeTab() {

    }

    private fun addTab(name: String, component: Component) {
        tabbedPane.addTab(name, component)
        tabbedPane.invalidate()
    }
}