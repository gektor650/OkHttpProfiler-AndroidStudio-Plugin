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
package com.itkacher.views.form

import com.intellij.openapi.ui.ComboBox
import com.intellij.uiDesigner.core.GridConstraints
import com.itkacher.JBKPanel
import com.itkacher.Resources
import com.itkacher.data.DebugDevice
import com.itkacher.data.DebugProcess
import com.itkacher.views.FrameScrollPanel
import java.awt.*
import java.io.IOException
import java.net.URISyntaxException
import javax.swing.JButton
import javax.swing.JEditorPane
import javax.swing.event.HyperlinkEvent

class KForm {
    val panel = JBKPanel(GridLayout(2, 1))

    val deviceList: ComboBox<DebugDevice> = ComboBox()
    val appList: ComboBox<DebugProcess> = ComboBox()
    val scrollToBottomButton = JButton()
    val clearButton = JButton()

    private val initialHtml = JEditorPane()
    val mainContainer = FrameScrollPanel(initialHtml)

    private val topPanel = JBKPanel(GridLayout(1, 4))


    init {
        scrollToBottomButton.icon = Resources.getIcon("scroll.png")
        clearButton.icon = Resources.getIcon("delete.png")

        deviceList.preferredSize = Dimension(200, 30)
        appList.preferredSize = Dimension(200, 30)
        scrollToBottomButton.preferredSize = Dimension(200, 30)
        clearButton.preferredSize = Dimension(200, 30)

        val con2 = GridConstraints()
        con2.column = 0
        con2.row = 0
        topPanel.add(deviceList, con2)

        val con1 = GridConstraints()
        con1.column = 1
        con1.row = 0
        topPanel.add(appList, con1)

        val scrollConstraints = GridConstraints()
        scrollConstraints.column = 2
        scrollConstraints.row = 0
        topPanel.add(scrollToBottomButton, scrollConstraints)

        val clearConstraints = GridConstraints()
        clearConstraints.column = 3
        clearConstraints.row = 0
        topPanel.add(clearButton, clearConstraints)

        val con3 = GridConstraints()
        con3.row = 0
        con3.column = 0
        panel.add(topPanel, con3)

        val con4 = GridConstraints()
        con4.row = 1
        con4.column = 0
        panel.add(mainContainer, con4)

        initialHtml.editorKit = JEditorPane.createEditorKitForContentType("text/html")
        initialHtml.isEditable = false

        initialHtml.addHyperlinkListener { e ->
            if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.url.toURI())
                } catch (e1: IOException) {
                    e1.printStackTrace()
                } catch (e1: URISyntaxException) {
                    e1.printStackTrace()
                }

            }
        }

        try {
            val initialFile = javaClass.classLoader.getResource("initial.html")
            if (initialFile != null) {
                initialHtml.page = initialFile
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
