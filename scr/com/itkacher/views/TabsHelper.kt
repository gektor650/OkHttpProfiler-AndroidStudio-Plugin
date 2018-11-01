package com.itkacher.views

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.itkacher.PluginPreferences
import com.itkacher.Resources
import com.itkacher.views.form.HeaderForm
import com.itkacher.views.form.JsonPlainTextForm
import com.itkacher.views.form.JsonTreeForm
import com.itkacher.views.form.RawForm
import com.itkacher.views.json.JTreeItemMenuListener
import com.itkacher.views.json.JsonTreeModel
import com.itkacher.views.json.JTreeMenuMouseAdapter
import com.jgoodies.common.collect.ArrayListModel
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.*
import javax.swing.JTabbedPane
import javax.swing.ListModel
import javax.swing.event.ChangeListener
import kotlin.collections.HashMap
import kotlin.collections.set


@Suppress("UNCHECKED_CAST")
class TabsHelper(private val tabbedPane: JTabbedPane,
                 private val settings: PluginPreferences,
                 private val menuListener: JTreeItemMenuListener) {

    private val executor = Executors.newFixedThreadPool(3)

    private val tabListener = ChangeListener {
        if (tabbedPane.selectedIndex != -1) {
            val selectedTabName = tabbedPane.getTitleAt(tabbedPane.selectedIndex)
            settings.setSelectedTabName(selectedTabName)
        }
    }

    fun addListener() {
        tabbedPane.addChangeListener(tabListener)
    }

    fun removeListener() {
        tabbedPane.removeChangeListener(tabListener)
    }

    fun addJsonTab(titleKey: String, json: String) {
        val jsonNode = parseModel(json)
        if (jsonNode != null) {
            val model = JsonTreeModel(jsonNode)
            val jsonTreeForm = JsonTreeForm()
            jsonTreeForm.tree.model = model
            jsonTreeForm.tree.addMouseListener(JTreeMenuMouseAdapter(menuListener))
            tabbedPane.addTab(Resources.getString(titleKey), jsonTreeForm.treePanel)
        }
    }

    fun removeAllTabs() {
        tabbedPane.removeAll()
    }

    fun addFormattedTab(titleKey: String, json: String) {
        val jsonNode = parseModel(json)
        if (jsonNode != null) {
            val prettyJson = prettifyNode(jsonNode)
            val plain = JsonPlainTextForm()
            plain.editorPane.text = prettyJson
            plain.editorPane.caretPosition = 0
            tabbedPane.addTab(Resources.getString(titleKey), plain.panel)
        }
    }

    fun addRawTab(titleKey: String, data: String?) {
        val form = RawForm()
        form.editor.text = data
        form.editor.caretPosition = 0
        tabbedPane.addTab(Resources.getString(titleKey), form.panel)
    }

    private fun parseModel(jsonString: String): JsonNode? {
        val objectMapper = ObjectMapper()
        return try {
            objectMapper.readTree(jsonString)
        } catch (e: IOException) {
            null
        }
    }

    private fun prettifyNode(jsonNode: JsonNode): String? {
        return try {
            val mapper = ObjectMapper()
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun addHeaderTab(resName: String, requestHeaders: List<String>) {
        val form = HeaderForm()
        val listModel = ArrayListModel<String>(requestHeaders)
        form.headersList.model = listModel as ListModel<String>
        tabbedPane.addTab(Resources.getString(resName), form.panel)
    }

    fun selectByPreference() {
        val selectedTab = settings.getSelectedTabName()
        if (selectedTab != null) {
            for (i in 0 until tabbedPane.tabCount) {
                if (tabbedPane.getTitleAt(i) == selectedTab) {
                    tabbedPane.selectedIndex = i
                    break
                }
            }
        }
    }

    fun addJsonTabs(treeTabName: String, formattedTabName: String, requestJson: String) {
        executor.execute((Runnable {
            val jsonNode = parseModel(requestJson)
            jsonNode?.let {
                val prettyJson = prettifyNode(it)
                val jsonTreeForm = JsonTreeForm()
                val model = JsonTreeModel(jsonNode)
                jsonTreeForm.tree.model = model
                jsonTreeForm.tree.addMouseListener(JTreeMenuMouseAdapter(menuListener))
                tabbedPane.addTab(Resources.getString(treeTabName), jsonTreeForm.treePanel)

                val plain = JsonPlainTextForm()
                plain.editorPane.text = prettyJson
                plain.editorPane.caretPosition = 0
                tabbedPane.addTab(Resources.getString(formattedTabName), plain.panel)
            }
        }))
    }

}