/**
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itkacher.views

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.CollectionListModel
import com.itkacher.PluginPreferences
import com.itkacher.Resources
import com.itkacher.data.DebugRequest
import com.itkacher.views.form.HeaderForm
import com.itkacher.views.form.JsonPlainTextForm
import com.itkacher.views.form.JsonTreeForm
import com.itkacher.views.form.RawForm
import com.itkacher.views.json.JTreeItemMenuListener
import com.itkacher.views.json.JsonTreeModel
import com.itkacher.views.json.JTreeMenuMouseAdapter
import java.io.IOException
import java.util.concurrent.*
import javax.swing.JTabbedPane
import javax.swing.ListModel
import javax.swing.event.ChangeListener


class TabsHelper(private val tabbedPane: JTabbedPane,
                 private val settings: PluginPreferences,
                 private val menuListener: JTreeItemMenuListener) {

    private val executor = Executors.newFixedThreadPool(3)

    @Volatile
    private var currentRequest: DebugRequest? = null

    private val tabListener = ChangeListener {
        if (tabbedPane.selectedIndex != -1) {
            val selectedTabName = tabbedPane.getTitleAt(tabbedPane.selectedIndex)
            settings.setSelectedTabName(selectedTabName)
        }
    }


    fun fill(debugRequest: DebugRequest) {
        currentRequest = debugRequest
        removeListener()
        removeAllTabs()
        executor.execute {
            val requestJson = debugRequest.getRequestBodyString()
            val responseBody = debugRequest.getResponseBodyString()

            val requestJsonPair = getTreeModelPrettifyPair(requestJson)
            val responseJsonPair = getTreeModelPrettifyPair(responseBody)

            ApplicationManager.getApplication().invokeLater {
                if(currentRequest != debugRequest) return@invokeLater
                addRawTab(Tabs.TAB_RAW_REQUEST.resName, debugRequest.getRawRequest())
                if (debugRequest.requestHeaders.isNotEmpty()) {
                    addHeaderTab(Tabs.TAB_REQUEST_HEADERS.resName, debugRequest.requestHeaders)
                }
                if (debugRequest.isClosed) {
                    if (requestJsonPair != null) {
                        addTreeTab(Tabs.TAB_JSON_REQUEST.resName, requestJsonPair.first)
                        requestJsonPair.second?.let {
                            addFormattedTab(Tabs.TAB_REQUEST_FORMATTED.resName, it)
                        }
                    }

                    addRawTab(Tabs.TAB_RAW_RESPONSE.resName, debugRequest.getRawResponse())

                    if (debugRequest.responseHeaders.isNotEmpty()) {
                        addHeaderTab(Tabs.TAB_RESPONSE_HEADERS.resName, debugRequest.responseHeaders)
                    }

                    if (responseJsonPair != null) {
                        addTreeTab(Tabs.TAB_JSON_RESPONSE.resName, responseJsonPair.first)
                        responseJsonPair.second?.let {
                            addFormattedTab(Tabs.TAB_RESPONSE_FORMATTED.resName, it)
                        }
                    }

                    if (debugRequest.errorMessage?.isNotEmpty() == true) {
                        addRawTab(Tabs.TAB_ERROR_MESSAGE.resName, debugRequest.errorMessage)
                    }
                }
                selectByPreference()
                addListener()
            }
        }
    }

    private fun addListener() {
        tabbedPane.addChangeListener(tabListener)
    }

    private fun removeListener() {
        tabbedPane.removeChangeListener(tabListener)
    }

    fun removeAllTabs() {
        tabbedPane.removeAll()
    }

    private fun addFormattedTab(titleKey: String, json: String) {
        val plain = JsonPlainTextForm()
        plain.editorPane.text = json
        plain.editorPane.caretPosition = 0
        tabbedPane.addTab(Resources.getString(titleKey), plain.panel)
    }

    private fun addRawTab(titleKey: String, data: String?) {
        val form = RawForm(data)
        tabbedPane.addTab(Resources.getString(titleKey), form.panel)
    }

    private fun parseModel(jsonString: String): JsonNode? {
        val trimmed = jsonString.trim()
        return if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            val objectMapper = ObjectMapper()
            try {
                objectMapper.readTree(trimmed)
            } catch (e: IOException) {
                null
            }
        } else {
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

    private fun addHeaderTab(resName: String, requestHeaders: List<String>) {
        val form = HeaderForm()
        val listModel = CollectionListModel<String>(requestHeaders)
        form.headersList.model = listModel as ListModel<String>
        tabbedPane.addTab(Resources.getString(resName), form.panel)
    }

    private fun selectByPreference() {
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

    private fun addTreeTab(name: String, model: JsonTreeModel) {
        val jsonTreeForm = JsonTreeForm()
        jsonTreeForm.tree.model = model
        jsonTreeForm.tree.addMouseListener(JTreeMenuMouseAdapter(menuListener))
        tabbedPane.addTab(Resources.getString(name), jsonTreeForm.treePanel)
    }

    private fun getTreeModelPrettifyPair(requestJson: String): Pair<JsonTreeModel, String?>? {
        val jsonNode = parseModel(requestJson)
        jsonNode?.let {
            val prettyJson = prettifyNode(it)
            val model = JsonTreeModel(jsonNode)
            return Pair(model, prettyJson)
        }
        return null
    }

}