package com.itkacher.views

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.itkacher.PluginPreferences
import com.itkacher.Resources
import com.itkacher.views.form.HeaderForm
import com.itkacher.views.form.JsonPlainTextForm
import com.itkacher.views.form.JsonTreeForm
import com.itkacher.views.form.RawForm
import com.itkacher.views.json.JsonTreeModel
import com.jgoodies.common.collect.ArrayListModel
import java.io.IOException
import java.util.ArrayList
import javax.swing.JTabbedPane
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class TabsHelper(private val tabbedPane: JTabbedPane,val settings: PluginPreferences) {

    private val nodeHash = HashMap<String, JsonNode>()

    private val tabListener = object : ChangeListener {
        override fun stateChanged(e: ChangeEvent?) {
            if(tabbedPane.selectedIndex != -1) {
                val selectedTabName = tabbedPane.getTitleAt(tabbedPane.selectedIndex)
                settings.setSelectedTabName(selectedTabName)
            }
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
            jsonTreeForm.jtree.model = model
            tabbedPane.addTab(Resources.getString(titleKey), jsonTreeForm.jtreePanel)
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
        if (nodeHash.containsKey(jsonString)) {
            return nodeHash[jsonString]
        }
        val objectMapper = ObjectMapper()
        return try {
            val node: JsonNode? = objectMapper.readTree(jsonString)
            if (node != null) {
                nodeHash[jsonString] = node
            }
            node
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

    fun addHeaderTab(resName: String, requestHeaders: ArrayList<String>) {
        val form = HeaderForm()
        form.headersList.model = ArrayListModel<String>(requestHeaders)
        tabbedPane.addTab(Resources.getString(resName), form.panel)
    }

    fun selectByPreference() {
        val selectedTab = settings.getSelectedTabName()
        if(selectedTab != null) {
            for(i in 0 until tabbedPane.tabCount) {
                if(tabbedPane.getTitleAt(i) == selectedTab) {
                    tabbedPane.selectedIndex = i
                    break
                }
            }
        }
    }

}