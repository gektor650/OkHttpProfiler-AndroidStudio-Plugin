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
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBTabbedPane
import com.itkacher.Resources
import com.itkacher.data.DebugRequest
import com.itkacher.views.form.HeaderForm
import com.itkacher.views.form.JsonPlainTextForm
import com.itkacher.views.form.JsonTreeForm
import com.itkacher.views.form.RawForm
import com.itkacher.views.json.JTreeItemMenuListener
import com.itkacher.views.json.JsonTreeModel
import com.itkacher.views.json.JTreeMenuMouseAdapter
import java.awt.Color
import java.awt.Dimension
import java.io.IOException
import java.util.*
import java.util.concurrent.*
import javax.swing.JLabel
import javax.swing.SwingUtilities
import kotlin.collections.HashMap


class TabsHelper(private val tabbedPane: JBTabbedPane,
                 private val menuListener: JTreeItemMenuListener) {

    private val executor = Executors.newFixedThreadPool(3)

    @Volatile
    private var currentRequest: DebugRequest? = null

    private val tabTitles = HashMap<Int, String>()

    private val requestHeadersFrom = HeaderForm()
    private val requestRawForm = RawForm()
    private val requestJsonTreeForm = JsonTreeForm()
    private val requestJsonFormattedForm = JsonPlainTextForm()


    private val responseHeadersFrom = HeaderForm()
    private val responseRawForm = RawForm()
    private val responseJsonTreeForm = JsonTreeForm()
    private val responseJsonFormattedForm = JsonPlainTextForm()

    private val errorRawForm = RawForm()

    private var indexRequestRawTab = 0
    private var indexRequestHeaderTab = 0
    private var indexRequestTreeTab = 0
    private var indexRequestFormattedTab = 0

    private var indexResponseRawTab = 0
    private var indexResponseHeaderTab = 0
    private var indexResponseTreeTab = 0
    private var indexResponseFormattedTab = 0

    private var indexErrorTab = 0

    private var defaultBackground = Color.GRAY

    fun initialize() {
        tabbedPane.minimumSize = Dimension(100, 50)

        indexRequestRawTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQUEST_RAW.resName), requestRawForm.panel)
        indexRequestHeaderTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQUEST_HEADERS.resName), requestHeadersFrom.panel)
        indexRequestTreeTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQUEST_JSON.resName), requestJsonTreeForm.treePanel)
        indexRequestFormattedTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_REQUEST_FORMATTED.resName), requestJsonFormattedForm.panel)
        requestJsonTreeForm.tree.addMouseListener(JTreeMenuMouseAdapter(menuListener))

        indexResponseRawTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESPONSE_RAW.resName), responseRawForm.panel)
        indexResponseHeaderTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESPONSE_HEADERS.resName), responseHeadersFrom.panel)
        indexResponseTreeTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESPONSE_JSON.resName), responseJsonTreeForm.treePanel)
        indexResponseFormattedTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_RESPONSE_FORMATTED.resName), responseJsonFormattedForm.panel)

        indexErrorTab = tabbedPane.tabCount
        tabbedPane.addTab(Resources.getString(Tabs.TAB_ERROR_MESSAGE.resName), errorRawForm.panel)

        responseJsonTreeForm.tree.addMouseListener(JTreeMenuMouseAdapter(menuListener))
        defaultBackground = tabbedPane.getForegroundAt(0)

        clearTabs()
    }

    private fun updateRequestHeaderTab(headers: List<String>) {
        val listModel = CollectionListModel<String>(headers)
        requestHeadersFrom.headersList.model = listModel
    }

    private fun updateResponseHeaderTab(headers: List<String>) {
        val listModel = CollectionListModel<String>(headers)
        responseHeadersFrom.headersList.model = listModel
    }

    private val acceptedContentTypes = listOf(
            "text/plain",
            "text/html",
            "application/json"
    )

    private val contentTypeHeader = "content-type:"

    private fun isAcceptedHeaders(headers: ArrayList<String>): Boolean {
        return headers.isEmpty() || headers.find {
            var res = false
            for (type in acceptedContentTypes) {
                val text = it.toLowerCase()
                if (text.startsWith(contentTypeHeader) && text.contains(type)) {
                    res = true
                }
            }
            res
        } != null
    }

    fun fill(debugRequest: DebugRequest) {
        currentRequest = debugRequest
        clearTabs(Resources.getString("processing"))
        executor.execute {
            //Skip non text response/requests
            val isRequestIsText = isAcceptedHeaders(debugRequest.requestHeaders)
            val isResponseIsText = isAcceptedHeaders(debugRequest.responseHeaders)

            val requestJson = if (isRequestIsText) {
                debugRequest.getRequestBodyString()
            } else {
                null
            }
            val requestJsonPair = getTreeModelPrettifyPair(requestJson)
            val responseBody = if (isResponseIsText) {
                debugRequest.getResponseBodyString()
            } else {
                null
            }

            val responseJsonPair = getTreeModelPrettifyPair(responseBody)

            SwingUtilities.invokeLater {
                if (currentRequest != debugRequest) return@invokeLater

                requestRawForm.setText(debugRequest.getRawRequest())
                enableTab(indexRequestRawTab)

                if (debugRequest.requestHeaders.isNotEmpty()) {
                    updateRequestHeaderTab(debugRequest.requestHeaders)
                    enableTab(indexRequestHeaderTab)
                } else {
                    disableTab(indexRequestHeaderTab)
                }
                if (debugRequest.isClosed) {
                    responseRawForm.setText(debugRequest.getRawResponse())
                    enableTab(indexResponseRawTab)

                    if (debugRequest.responseHeaders.isNotEmpty()) {
                        updateResponseHeaderTab(debugRequest.responseHeaders)
                        enableTab(indexResponseHeaderTab)
                    } else {
                        disableTab(indexResponseHeaderTab)
                    }

                    if (requestJsonPair?.first != null) {
                        requestJsonTreeForm.tree.model = requestJsonPair.first
                        enableTab(indexRequestTreeTab)
                    } else {
                        disableTab(indexRequestTreeTab)
                    }

                    if (requestJsonPair?.second != null && requestJsonPair.second != "") {
                        requestJsonFormattedForm.setText(requestJsonPair.second)
                        enableTab(indexRequestFormattedTab)
                    } else {
                        disableTab(indexRequestFormattedTab)
                    }

                    if (responseJsonPair?.first != null) {
                        responseJsonTreeForm.tree.model = responseJsonPair.first
                        enableTab(indexResponseTreeTab)
                    } else {
                        disableTab(indexResponseTreeTab)
                    }

                    if (responseJsonPair?.second != null && responseJsonPair.second != "") {
                        responseJsonFormattedForm.setText(responseJsonPair.second)
                        enableTab(indexResponseFormattedTab)
                    } else {
                        disableTab(indexResponseFormattedTab)
                    }

                    if (debugRequest.errorMessage.isNullOrBlank()) {
                        disableTab(indexErrorTab)
                    } else {
                        enableTab(indexErrorTab)
                        errorRawForm.setText(debugRequest.errorMessage)
                    }
                }
            }
        }
    }

    private fun enableTab(tabIndex: Int) {
        val title = tabTitles[tabIndex]
        if (title != null) {
            val label = tabbedPane.getTabComponentAt(tabIndex)
            if (label is JLabel) {
                label.text = title
            }
        }
        tabbedPane.setEnabledAt(tabIndex, true)
        tabbedPane.setForegroundAt(tabIndex, defaultBackground)
        tabbedPane.setToolTipTextAt(tabIndex, null)
    }

    private fun disableTab(tabIndex: Int) {
        val label = tabbedPane.getTabComponentAt(tabIndex)
        if (label is JLabel) {
            if (tabTitles[tabIndex] == null) {
                tabTitles[tabIndex] = label.text
            }
            label.text = ""
        }

        tabbedPane.setEnabledAt(tabIndex, false)
        tabbedPane.setForegroundAt(tabIndex, Color.GRAY)
        tabbedPane.setToolTipTextAt(tabIndex, Resources.getString("empty"))
        if (tabbedPane.selectedIndex == tabIndex) {
            tabbedPane.selectedIndex = 0
        }
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

    private fun getTreeModelPrettifyPair(requestJson: String?): Pair<JsonTreeModel, String?>? {
        if(requestJson != null) {
            val jsonNode = parseModel(requestJson)
            jsonNode?.let {
                val prettyJson = prettifyNode(it)
                val model = JsonTreeModel(jsonNode)
                return Pair(model, prettyJson)
            }
        }
        return null
    }

    fun clearTabs(processingText: String = "") {
        disableTab(indexRequestRawTab)
        disableTab(indexRequestHeaderTab)
        disableTab(indexRequestTreeTab)
        disableTab(indexRequestFormattedTab)
        disableTab(indexResponseRawTab)
        disableTab(indexResponseHeaderTab)
        disableTab(indexResponseTreeTab)
        disableTab(indexResponseFormattedTab)
        disableTab(indexErrorTab)

        responseRawForm.setText(processingText)
        updateResponseHeaderTab(Collections.singletonList(processingText))
        requestJsonFormattedForm.setText(processingText)
        requestJsonTreeForm.tree.model = null

        requestRawForm.setText(processingText)
        updateResponseHeaderTab(Collections.singletonList(processingText))
        responseJsonFormattedForm.setText(processingText)
        responseJsonTreeForm.tree.model = null

        errorRawForm.setText(processingText)
    }

}